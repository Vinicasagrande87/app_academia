package com.example.app_academia

// ===== IMPORTS =====
import androidx.compose.foundation.layout.*   // Column, padding, fillMaxSize...
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// ===== TELA SOBRE =====
// Tela simples com a versão e os créditos do app.
// Não recebe nada: só mostra texto fixo.
@Composable
fun TelaSobre() {
    Column(
        modifier = Modifier
            .fillMaxSize()                 // ocupa a tela inteira
            .padding(Espaco.medio),        // margem de 16dp em volta
        verticalArrangement = Arrangement.spacedBy(Espaco.pequeno)  // 8dp entre as linhas
    ) {
        // Nome do app em destaque
        Text("Academia Ulbra", style = MaterialTheme.typography.headlineMedium)
        // Versão e créditos, com o estilo de texto comum do tema
        Text("Versão 1.0", style = MaterialTheme.typography.bodyLarge)
        Text("Programação para Dispositivos Móveis · ULBRA", style = MaterialTheme.typography.bodyLarge)
    }
}
