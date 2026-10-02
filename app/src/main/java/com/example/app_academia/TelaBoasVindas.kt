package com.example.app_academia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

// Segunda tela: RECEBE o e-mail da tela de login
// e BUSCA o usuário no "banco" (user.kt), em vez de receber o nome pronto.
// O Scaffold agora mora no navegation.kt: aqui fica só o conteúdo.
@Composable
fun TelaBoasVindas(email: String, aoSair: () -> Unit) {

    // Com o e-mail que veio pela rota, procura o resto dos dados
    val usuario = buscarUsuario(email)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Espaco.medio),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espaco.grande)
    ) {
        // Se não achar o usuário, mostra só "Bem-vindo(a)!"
        Text(
            text = if (usuario != null) "Bem-vindo(a), ${usuario.nome}!" else "Bem-vindo(a)!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )

        // O dado que veio da outra tela aparece aqui
        Text(
            text = "Você entrou como:\n$email",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        // Botão que VOLTA para o login
        Button(
            onClick = aoSair,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SAIR")
        }
    }
}
