package com.example.app_academia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

// O MainActivity agora só abre o app e "veste" o tema.
// As telas ficam cada uma no seu arquivo:
//   theme.kt          -> cores, cantos e espaçamentos
//   navegation.kt     -> o mapa de rotas (quem vai para onde)
//   TelaInicial.kt    -> tela verde de 2 segundos
//   TelaLogin.kt      -> login com validação
//   TelaBoasVindas.kt -> segunda tela, que recebe o e-mail
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()      // app ocupa a tela inteira
        setContent {
            TemaUlbra {         // aplica o tema do theme.kt em tudo
                Navegacao()     // começa pelo mapa de rotas
            }
        }
    }
}