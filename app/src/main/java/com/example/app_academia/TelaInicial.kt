package com.example.app_academia

// ===== IMPORTS =====
import androidx.compose.foundation.background          // pinta o fundo de um componente
import androidx.compose.foundation.layout.*            // Box, Row, padding, fillMaxSize...
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay                        // espera sem travar o app

// ===== TELA INICIAL (a tela verde de abertura) =====
// A tela não conhece o navController: ela só AVISA que terminou,
// chamando a função aoTerminar. Quem decide para onde ir é o navegation.kt.
@Composable
fun TelaInicial(aoTerminar: () -> Unit) {

    // LaunchedEffect(Unit): roda UMA vez quando a tela aparece
    // (Unit nunca muda, então o bloco não roda de novo)
    LaunchedEffect(Unit) {
        delay(2000)    // espera 2 segundos
        aoTerminar()   // avisa que acabou
    }

    // Box: uma caixa que permite centralizar o conteúdo.
    // Aqui ela é verde (primary do tema) e do tamanho da tela.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center   // tudo no meio da tela
    ) {
        // "ulbra" e "GYM" lado a lado, com espaço pequeno (8dp) entre eles
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Espaco.pequeno)
        ) {
            // "ulbra" grande, em negrito, na cor bege
            Text(
                text = "ulbra",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            // "GYM" em verde dentro de uma etiqueta bege
            Text(
                text = "GYM",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    // background ANTES do padding = fundo bege com respiro interno
                    // (a ordem dos modifiers importa!)
                    .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.small)
                    .padding(horizontal = Espaco.pequeno)
            )
        }
    }
}
