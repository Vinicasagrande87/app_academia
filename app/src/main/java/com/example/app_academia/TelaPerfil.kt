package com.example.app_academia

// ===== IMPORTS =====
import androidx.compose.foundation.layout.*           // Column, padding, fillMaxSize...
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import android.graphics.Bitmap
import androidx.compose.material3.*                   // Text, Card...
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// ===== TELA DE PERFIL =====
// Recebe o e-mail pela rota "perfil/{email}", igual à tela de boas-vindas.
// Não navega para lugar nenhum. A foto e o check-in vêm do navegation.kt;
// a tela só AVISA quando a pessoa tira uma foto nova.
@Composable
fun TelaPerfil(email: String, foto: Bitmap?, checkIn: CheckIn?, aoTirarFoto: (Bitmap) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()                          // ocupa a tela inteira
            .verticalScroll(rememberScrollState())  // rola com a tela deitada
            .padding(Espaco.medio),                 // margem de 16dp em volta
        verticalArrangement = Arrangement.spacedBy(Espaco.medio)  // 16dp entre os itens
    ) {
        Text("Meu perfil", style = MaterialTheme.typography.headlineMedium)

        // Foto para o instrutor reconhecer o aluno (opcional, RecursosDoAparelho.kt)
        FotoDePerfil(foto = foto, aoTirarFoto = aoTirarFoto)

        // Um cartão para cada informação.
        // O mesmo componente (ItemPerfil) é reaproveitado: muda só o texto.
        ItemPerfil(titulo = "E-mail", valor = email)
        ItemPerfil(titulo = "Unidade", valor = "Academia Ulbra")
        // O check-in feito no Início aparece aqui também (só se foi feito)
        if (checkIn != null) {
            ItemPerfil(titulo = "Último check-in", valor = "Perto de ${descreverLocal(checkIn)}")
        }
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
