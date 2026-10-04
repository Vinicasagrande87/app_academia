package com.example.app_academia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

// Segunda tela: RECEBE o e-mail da tela de login.
// As barras de cima e de baixo vêm do Scaffold do navegation.kt
@Composable
fun TelaBoasVindas(email: String, aoSair: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Espaco.medio),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Espaco.grande)
    ) {
        Text("Bem-vindo(a)!", style = MaterialTheme.typography.headlineMedium)

        // O dado que veio da outra tela aparece num Card (cores do tema)
        Card(
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Espaco.medio),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Espaco.pequeno)
            ) {
                Text("Você entrou como", style = MaterialTheme.typography.labelLarge)
                Text(
                    text = email,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            }
        }

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
