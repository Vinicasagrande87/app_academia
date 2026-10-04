package com.example.app_academia

// ===== IMPORTS =====
import androidx.compose.foundation.layout.*           // Column, padding, fillMaxSize...
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*                   // Text, Card, Button...
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

// ===== SEGUNDA TELA (INÍCIO) =====
// RECEBE o e-mail da tela de login (ele veio pela rota "boasvindas/{email}").
// Também recebe a função aoSair: a tela só AVISA que a pessoa quer sair,
// quem decide para onde ir é o navegation.kt.
// As barras de cima e de baixo vêm do Scaffold do navegation.kt
@Composable
fun TelaBoasVindas(email: String, aoSair: () -> Unit) {

    // Column empilha os itens um embaixo do outro
    Column(
        modifier = Modifier
            .fillMaxSize()                          // ocupa a tela inteira
            .verticalScroll(rememberScrollState())  // rola com a tela deitada
            .padding(Espaco.medio),                 // margem de 16dp em volta
        horizontalAlignment = Alignment.CenterHorizontally,
        // Espaço maior (24dp) entre os blocos desta tela
        verticalArrangement = Arrangement.spacedBy(Espaco.grande)
    ) {
        // Título com o tamanho vindo do tema
        Text("Bem-vindo(a)!", style = MaterialTheme.typography.headlineMedium)

        // ----- CARD COM O E-MAIL -----
        // O dado que veio da outra tela aparece num Card (cores do tema)
        Card(
            shape = MaterialTheme.shapes.medium,    // cantos do tema
            colors = CardDefaults.cardColors(
                // Fundo um pouco mais claro que a tela, para o Card destacar
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Dentro do Card, outra Column com respiro interno
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Espaco.medio),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Espaco.pequeno)
            ) {
                // Rótulo pequeno
                Text("Você entrou como", style = MaterialTheme.typography.labelLarge)
                // O e-mail em destaque, na cor bege do tema
                Text(
                    text = email,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // ----- BOTÃO SAIR -----
        // Ao tocar, chama aoSair: o navegation.kt VOLTA para o login
        Button(
            onClick = aoSair,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("SAIR")
        }
    }
}
