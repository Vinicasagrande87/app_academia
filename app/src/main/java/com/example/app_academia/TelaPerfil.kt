package com.example.app_academia

// ===== IMPORTS =====
import androidx.compose.foundation.layout.*           // Column, padding, fillMaxSize...
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*                   // Text, Card...
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// ===== TELA DE PERFIL =====
// Recebe o e-mail pela rota "perfil/{email}", igual à tela de boas-vindas.
// Ela só MOSTRA dados: não navega para lugar nenhum, então não recebe função.
@Composable
fun TelaPerfil(email: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()                          // ocupa a tela inteira
            .verticalScroll(rememberScrollState())  // rola com a tela deitada
            .padding(Espaco.medio),                 // margem de 16dp em volta
        verticalArrangement = Arrangement.spacedBy(Espaco.medio)  // 16dp entre os itens
    ) {
        Text("Meu perfil", style = MaterialTheme.typography.headlineMedium)

        // Um cartão para cada informação.
        // O mesmo componente (ItemPerfil) é reaproveitado: muda só o texto.
        ItemPerfil(titulo = "E-mail", valor = email)
        ItemPerfil(titulo = "Unidade", valor = "Academia Ulbra")
    }
}

// ===== CARTÃO DE UMA INFORMAÇÃO =====
// Título pequeno em bege e o valor embaixo.
// "private": só este arquivo usa este componente.
@Composable
private fun ItemPerfil(titulo: String, valor: String) {
    Card(
        shape = MaterialTheme.shapes.medium,        // cantos do tema
        colors = CardDefaults.cardColors(
            // Fundo um pouco mais claro que a tela, para o Card destacar
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Espaco.medio),               // respiro interno
            verticalArrangement = Arrangement.spacedBy(Espaco.pequeno) // 8dp entre título e valor
        ) {
            // Título do cartão, na cor bege do tema
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
            // Valor, um pouco maior
            Text(text = valor, style = MaterialTheme.typography.titleMedium)
        }
    }
}
