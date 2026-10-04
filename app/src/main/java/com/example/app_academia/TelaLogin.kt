package com.example.app_academia

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun TelaLogin(aoEntrar: (String) -> Unit) {

    // rememberSaveable: o que foi digitado SOBREVIVE a girar a tela
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }

    // Fica true quando a pessoa tenta entrar com dados que não estão na lista
    var loginRecusado by rememberSaveable { mutableStateOf(false) }

    // Fica true enquanto o login está sendo conferido
    var carregando by rememberSaveable { mutableStateOf(false) }

    // Quando carregando vira true, espera um pouco (como se fosse um servidor)
    // e só depois confere no "banco" (user.kt)
    LaunchedEffect(carregando) {
        if (carregando) {
            delay(1500)
            carregando = false
            if (loginValido(email, senha)) {
                aoEntrar(email)          // achou: vai para a próxima tela
            } else {
                loginRecusado = true     // não achou: mostra o aviso
            }
        }
    }

    // ===== 1. AS REGRAS (calculadas a cada letra digitada) =====
    val emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val senhaValida = senha.length >= 6
    // Só mostra erro depois que a pessoa começou a digitar
    val erroEmail = email.isNotEmpty() && !emailValido
    val erroSenha = senha.isNotEmpty() && !senhaValida

    // A barra de cima vem do Scaffold do navegation.kt
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())  // funciona com a tela deitada
            .padding(Espaco.medio),
        horizontalAlignment = Alignment.CenterHorizontally,
        // Um espaço igual entre todos os itens (no lugar dos Spacer)
        verticalArrangement = Arrangement.spacedBy(Espaco.medio)
    ) {
        Text("Academia Ulbra", style = MaterialTheme.typography.headlineMedium)

        Text(
            text = "Faça login para acessar o sistema",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        // ----- CAMPO E-MAIL -----
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                loginRecusado = false   // começou a corrigir: some o aviso
            },
            label = { Text("E-mail") },
            singleLine = true,
            enabled = !carregando,          // trava o campo enquanto carrega
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            // ===== 2. O CAMPO SE PINTA DE VERMELHO SOZINHO =====
            isError = erroEmail || loginRecusado,
            supportingText = {
                if (erroEmail) Text("Digite um e-mail válido, como nome@ulbra.br")
            },
            colors = coresDoCampo(),
            modifier = Modifier.fillMaxWidth()
        )

        // ----- CAMPO SENHA -----
        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
                loginRecusado = false
            },
            label = { Text("Senha") },
            singleLine = true,
            enabled = !carregando,          // trava o campo enquanto carrega
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = erroSenha || loginRecusado,
            supportingText = {
                if (erroSenha) {
                    Text("A senha precisa ter pelo menos 6 caracteres")
                } else if (loginRecusado) {
                    Text("E-mail ou senha incorretos")
                }
            },
            colors = coresDoCampo(),
            modifier = Modifier.fillMaxWidth()
        )

        // ===== 3. O BOTÃO SÓ LIGA QUANDO OS DOIS ESTÃO VÁLIDOS =====
        // (e desliga enquanto carrega, para não clicar duas vezes)
        Button(
            onClick = { carregando = true },
            enabled = emailValido && senhaValida && !carregando,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (carregando) {
                // Estado de carregando: rodinha girando + texto
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Espaco.pequeno)
                ) {
                    CircularProgressIndicator(
                        color = LocalContentColor.current,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(Espaco.medio)
                    )
                    Text("ENTRANDO…")
                }
            } else {
                Text("ENTRAR")
            }
        }
    }
}

// Cores dos campos, sempre vindas do tema
@Composable
private fun coresDoCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    cursorColor = MaterialTheme.colorScheme.secondary
)