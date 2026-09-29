package com.example.app_academia

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ===== CORES DO APP (as mesmas do site) =====
// As cores moram SÓ aqui. Nenhuma tela escreve Color(...) direto.
private val VerdeUlbra = Color(0xFF035B53)
private val Bege = Color(0xFFF3CC94)
private val FundoEscuro = Color(0xFF141414)
private val CampoEscuro = Color(0xFF1E1E1E)

// Cada cor ganha um "papel" no app
private val CoresUlbra = darkColorScheme(
    primary = VerdeUlbra,          // barra do topo e botões
    onPrimary = Color.White,       // texto em cima do verde
    secondary = Bege,              // destaques (logo, campo selecionado)
    onSecondary = VerdeUlbra,      // texto em cima do bege
    background = FundoEscuro,      // fundo das telas
    onBackground = Color.White,    // texto em cima do fundo
    surface = FundoEscuro,
    onSurface = Color.White,
    surfaceVariant = CampoEscuro,  // fundo dos campos
    onSurfaceVariant = Color.LightGray
)

// ===== ESPAÇAMENTOS =====
// Só três valores no app inteiro
object Espaco {
    val pequeno = 8.dp
    val medio = 16.dp
    val grande = 24.dp
}

// ===== O TEMA =====
// Tudo que estiver dentro de TemaUlbra { } usa estas cores e cantos
@Composable
fun TemaUlbra(conteudo: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoresUlbra,
        shapes = Shapes(
            small = RoundedCornerShape(4.dp),   // cantos levemente arredondados
            medium = RoundedCornerShape(4.dp)
        ),
        content = conteudo
    )
}