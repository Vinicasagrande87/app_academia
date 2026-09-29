package com.example.app_academia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.delay

// A tela não conhece o navController: ela só AVISA que terminou
@Composable
fun TelaInicial(aoTerminar: () -> Unit) {

    // Roda uma vez quando a tela aparece
    LaunchedEffect(Unit) {
        delay(2000)    // espera 2 segundos
        aoTerminar()   // avisa que acabou
    }

    // Caixa verde do tamanho da tela, com tudo centralizado
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        // "ulbra" e "GYM" lado a lado, com espaço pequeno entre eles
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Espaco.pequeno)
        ) {
            Text(
                text = "ulbra",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "GYM",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    // background ANTES do padding = fundo bege com respiro interno
                    .background(MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.small)
                    .padding(horizontal = Espaco.pequeno)
            )
        }
    }
}