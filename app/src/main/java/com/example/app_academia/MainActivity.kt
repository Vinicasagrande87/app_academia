package com.example.app_academia

// ===== IMPORTS =====
import android.os.Bundle
import androidx.activity.ComponentActivity     // a "janela" do app no Android
import androidx.activity.compose.setContent    // liga o Compose na Activity
import androidx.activity.enableEdgeToEdge      // desenha até as bordas da tela

// ===== PONTO DE PARTIDA DO APP =====
// O Android abre esta classe primeiro (ela está registrada no AndroidManifest.xml).
// O MainActivity só abre o app e "veste" o tema.
// As telas ficam cada uma no seu arquivo:
//   theme.kt          -> cores, cantos e espaçamentos
//   navegation.kt     -> o Scaffold, a barra de baixo e o mapa de rotas
//   TelaInicial.kt    -> tela verde de 2 segundos
//   TelaLogin.kt      -> login com validação e carregando
//   TelaBoasVindas.kt -> segunda tela, que recebe o e-mail
//   TelaPerfil.kt     -> perfil, também recebe o e-mail
//   TelaSobre.kt      -> versão e créditos
//   user.kt           -> a lista de usuários e a regra do login (sem tela)
class MainActivity : ComponentActivity() {

    // onCreate: roda quando o app abre
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)   // deixa o Android fazer a parte dele primeiro
        enableEdgeToEdge()      // app ocupa a tela inteira
        // setContent: daqui para dentro é Compose
        setContent {
            TemaUlbra {         // aplica o tema do theme.kt em tudo
                Navegacao()     // começa pelo mapa de rotas (navegation.kt)
            }
        }
    }
}
