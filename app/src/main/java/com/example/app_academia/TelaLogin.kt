package com.example.app_academia

// ===== IMPORTS =====
// Patterns: regras prontas do Android (usamos a de e-mail)
import android.util.Patterns
// Layout: Column, Row, padding, fillMaxSize, Arrangement...
import androidx.compose.foundation.layout.*
// Rolagem vertical (a tela funciona deitada)
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
// Componentes do Material 3: Text, Button, OutlinedTextField...
import androidx.compose.material3.*
// Estado: remember, mutableStateOf, LaunchedEffect, getValue/setValue (o "by")
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
// delay: espera um tempo sem travar o app (coroutine)
import kotlinx.coroutines.delay

// ===== TELA DE LOGIN =====
// A tela NÃO conhece o navController.
// Ela recebe a função aoEntrar e só AVISA: "a pessoa entrou com este e-mail".
// Quem decide para onde ir é o navegation.kt.
@Composable
fun TelaLogin(aoEntrar: (String) -> Unit) {

    // ===== ESTADO DA TELA =====
    // mutableStateOf: quando o valor muda, a tela se redesenha sozinha.
    // rememberSaveable: o valor SOBREVIVE a girar a tela.
    // O "by" deixa usar email direto, sem precisar escrever email.value
    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }

    // Fica true quando a pessoa tenta entrar com dados que não estão na lista
    var loginRecusado by rememberSaveable { mutableStateOf(false) }

    // Fica true enquanto o login está sendo conferido (estado de carregando)
    var carregando by rememberSaveable { mutableStateOf(false) }

    // ===== O CARREGANDO =====
    // LaunchedEffect roda um código "por fora" da tela (uma coroutine).
    // O (carregando) entre parênteses é a CHAVE: toda vez que ela muda,
    // o bloco roda de novo.
    LaunchedEffect(carregando) {
        if (carregando) {
            delay(1500)                  // espera 1,5 s, como se fosse um servidor
            carregando = false           // terminou: o botão volta ao normal
            // Confere no "banco" (user.kt)
            if (loginValido(email, senha)) {
                aoEntrar(email)          // achou: avisa para ir à próxima tela
            } else {
                loginRecusado = true     // não achou: mostra o aviso nos campos
            }
        }
    }

    // ===== 1. AS REGRAS (calculadas a cada letra digitada) =====
    // Como a tela se redesenha a cada letra, estas linhas rodam de novo sozinhas.
    val emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches()  // tem cara de e-mail?
    val senhaValida = senha.length >= 6                                // 6 caracteres ou mais?
    // Só mostra erro depois que a pessoa começou a digitar
    // (campo vazio não fica vermelho logo de cara)
    val erroEmail = email.isNotEmpty() && !emailValido
    val erroSenha = senha.isNotEmpty() && !senhaValida

    // ===== O LAYOUT =====
    // A barra de cima vem do Scaffold do navegation.kt, por isso não tem Scaffold aqui.
    // Column empilha os itens um embaixo do outro.
    Column(
        modifier = Modifier
            .fillMaxSize()                          // ocupa a tela inteira
            .verticalScroll(rememberScrollState())  // rola: funciona com a tela deitada
            .padding(Espaco.medio),                 // margem de 16dp em volta (do theme.kt)
        horizontalAlignment = Alignment.CenterHorizontally,  // tudo centralizado
        // Um espaço igual entre todos os itens (no lugar dos Spacer)
        verticalArrangement = Arrangement.spacedBy(Espaco.medio)
    ) {
        // Título: o tamanho vem do tema (typography), nada de fontSize na mão
        Text("Academia Ulbra", style = MaterialTheme.typography.headlineMedium)

        // Subtítulo
        Text(
            text = "Faça login para acessar o sistema",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        // ----- CAMPO E-MAIL -----
        OutlinedTextField(
            value = email,                  // o campo MOSTRA o que está no estado
            onValueChange = {               // a cada letra, o estado é atualizado
                email = it                  // "it" é o texto novo do campo
                loginRecusado = false       // começou a corrigir: some o aviso
            },
            label = { Text("E-mail") },     // texto que fica em cima do campo
            singleLine = true,              // não deixa quebrar linha
            enabled = !carregando,          // trava o campo enquanto carrega
            // Abre o teclado com @ e .com
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            // ===== 2. O CAMPO SE PINTA DE VERMELHO SOZINHO =====
            isError = erroEmail || loginRecusado,
            // Mensagem embaixo do campo: o aviso fica NO campo que tem o problema
            supportingText = {
                if (erroEmail) Text("Digite um e-mail válido, como nome@ulbra.br")
            },
            colors = coresDoCampo(),        // cores do tema (função lá embaixo)
            modifier = Modifier.fillMaxWidth()
        )

        // ----- CAMPO SENHA -----
        OutlinedTextField(
            value = senha,
            onValueChange = {
                senha = it
                loginRecusado = false       // começou a corrigir: some o aviso
            },
            label = { Text("Senha") },
            singleLine = true,
            enabled = !carregando,          // trava o campo enquanto carrega
            // Mostra bolinhas no lugar das letras
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            isError = erroSenha || loginRecusado,
            supportingText = {
                // Primeiro a regra do tamanho; se ela está ok, o aviso de login errado
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
            onClick = { carregando = true },   // só liga o carregando; o LaunchedEffect faz o resto
            enabled = emailValido && senhaValida && !carregando,
            shape = MaterialTheme.shapes.small, // cantos do tema
            modifier = Modifier.fillMaxWidth()
        ) {
            // O conteúdo do botão muda conforme o estado
            if (carregando) {
                // Estado de carregando: rodinha girando + texto, lado a lado
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Espaco.pequeno)
                ) {
                    CircularProgressIndicator(
                        color = LocalContentColor.current,  // mesma cor do texto do botão
                        strokeWidth = 2.dp,                 // traço fino da rodinha
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

// ===== CORES DOS CAMPOS =====
// Sempre vindas do tema: quando o campo está selecionado,
// a borda, o rótulo e o cursor ficam bege (secondary).
// "private": só este arquivo usa esta função.
@Composable
private fun coresDoCampo() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.secondary,
    focusedLabelColor = MaterialTheme.colorScheme.secondary,
    cursorColor = MaterialTheme.colorScheme.secondary
)
