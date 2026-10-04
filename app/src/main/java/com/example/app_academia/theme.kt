package com.example.app_academia

// ===== IMPORTS =====
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ===== CORES DO APP (as mesmas do site) =====
// As cores moram SÓ aqui. Nenhuma tela escreve Color(...) direto.
// 0xFF035B53: FF = totalmente opaca, 035B53 = a cor em hexadecimal.
// "private": as telas não usam estas cores pelo nome,
// usam pelo PAPEL (MaterialTheme.colorScheme.primary etc.)
private val VerdeUlbra = Color(0xFF035B53)
private val Bege = Color(0xFFF3CC94)
private val FundoEscuro = Color(0xFF141414)
private val CampoEscuro = Color(0xFF1E1E1E)

// Cada cor ganha um "papel" no app.
// darkColorScheme: parte do tema escuro do Material e troca só o que listamos.
private val CoresUlbra = darkColorScheme(
    primary = VerdeUlbra,          // barra do topo e botões
    onPrimary = Color.White,       // texto em cima do verde
    secondary = Bege,              // destaques (logo, campo selecionado)
    onSecondary = VerdeUlbra,      // texto em cima do bege
    background = FundoEscuro,      // fundo das telas
    onBackground = Color.White,    // texto em cima do fundo
    surface = FundoEscuro,         // fundo das barras e superfícies
    onSurface = Color.White,       // texto em cima das superfícies
    surfaceVariant = CampoEscuro,  // fundo dos campos e dos Cards
    onSurfaceVariant = Color.LightGray  // texto secundário (rótulos)
)

// ===== ESPAÇAMENTOS =====
// Só três valores no app inteiro. As telas usam Espaco.pequeno,
// Espaco.medio ou Espaco.grande, nunca um número solto.
// "object": existe um só, e dá para usar de qualquer arquivo.
object Espaco {
    val pequeno = 8.dp
    val medio = 16.dp
    val grande = 24.dp
}

// ===== O TEMA =====
// Tudo que estiver dentro de TemaUlbra { } usa estas cores e cantos.
// conteudo: as telas que vão "vestir" o tema (no MainActivity é a Navegacao)
@Composable
fun TemaUlbra(conteudo: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoresUlbra,
        shapes = Shapes(
            small = RoundedCornerShape(4.dp),   // cantos levemente arredondados (botões)
            medium = RoundedCornerShape(4.dp)   // mesmo canto nos Cards
        ),
        // typography não foi trocada: usamos a tipografia padrão do Material 3
        content = conteudo
    )
}
