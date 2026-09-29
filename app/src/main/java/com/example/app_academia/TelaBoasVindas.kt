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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaBoasVindas(email: String, aoSair: () -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Início") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(Espaco.medio),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Espaco.grande)
        ) {
            Text("Bem-vindo(a)!", style = MaterialTheme.typography.headlineMedium)

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
}