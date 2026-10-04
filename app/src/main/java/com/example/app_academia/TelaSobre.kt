package com.example.app_academia

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Tela simples com a versão e os créditos do app
@Composable
fun TelaSobre() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Espaco.medio),
        verticalArrangement = Arrangement.spacedBy(Espaco.pequeno)
    ) {
        Text("Academia Ulbra", style = MaterialTheme.typography.headlineMedium)
        Text("Versão 1.0", style = MaterialTheme.typography.bodyLarge)
        Text("Programação para Dispositivos Móveis · ULBRA", style = MaterialTheme.typography.bodyLarge)
    }
}
