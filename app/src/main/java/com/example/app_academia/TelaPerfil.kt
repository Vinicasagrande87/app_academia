package com.example.app_academia

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Perfil: recebe o e-mail pela rota, igual à tela de boas-vindas
@Composable
fun TelaPerfil(email: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(Espaco.medio),
        verticalArrangement = Arrangement.spacedBy(Espaco.medio)
    ) {
        Text("Meu perfil", style = MaterialTheme.typography.headlineMedium)

        // Um cartão para cada informação
        ItemPerfil(titulo = "E-mail", valor = email)
        ItemPerfil(titulo = "Unidade", valor = "Academia Ulbra")
    }
}

// Cartão com um título pequeno em bege e o valor embaixo
@Composable
private fun ItemPerfil(titulo: String, valor: String) {
    Card(
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Espaco.medio),
            verticalArrangement = Arrangement.spacedBy(Espaco.pequeno)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(text = valor, style = MaterialTheme.typography.titleMedium)
        }
    }
}
