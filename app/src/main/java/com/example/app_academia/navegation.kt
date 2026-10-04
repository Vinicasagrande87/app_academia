package com.example.app_academia

// ===== IMPORTS =====
import android.net.Uri                                  // Uri.encode protege o e-mail na rota
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
// Ícones da barra de baixo (biblioteca material-icons-core)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*                     // Scaffold, TopAppBar, NavigationBar...
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
// Navigation Compose: o mapa (NavHost), o motorista (navController) e os destinos
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// ===== NAVEGAÇÃO DO APP =====
// NavHost é o MAPA, navController é o MOTORISTA,
// cada composable("rota") é um DESTINO.
// Navegar é uma PILHA DE PRATOS: navigate põe uma tela em cima,
// o botão voltar tira a de cima.
// Este é o ÚNICO arquivo que conhece o navController:
// as telas só recebem funções e avisam o que aconteceu.
@OptIn(ExperimentalMaterial3Api::class)   // a TopAppBar ainda é "experimental" no Material 3
@Composable
fun Navegacao() {
    // Cria o motorista (e lembra dele entre os redesenhos da tela)
    val navController = rememberNavController()

    // E-mail de quem entrou: a barra de baixo precisa dele para montar as rotas
    // rememberSaveable: continua guardado se girar a tela
    var emailLogado by rememberSaveable { mutableStateOf("") }

    // Qual prato está no topo da pilha agora.
    // currentBackStackEntryAsState: a tela se redesenha sozinha quando a rota muda
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route   // ex.: "login", "perfil/{email}"

    // A tela verde não tem barra; a barra de baixo só aparece depois do login
    val mostrarBarraCima = rotaAtual != null && rotaAtual != "inicial"
    val mostrarBarraBaixo = rotaAtual in listOf("boasvindas/{email}", "perfil/{email}", "sobre")

    // ===== O ESQUELETO DO APP: UM SCAFFOLD SÓ =====
    // O Scaffold tem lugares fixos (topBar, bottomBar) e põe o conteúdo no meio.
    Scaffold(
        // ----- BARRA DE CIMA -----
        topBar = {
            if (mostrarBarraCima) {
                TopAppBar(
                    title = { Text(tituloDa(rotaAtual)) },   // o título muda com a tela
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,    // fundo verde
                        titleContentColor = MaterialTheme.colorScheme.onPrimary // texto branco
                    )
                )
            }
        },
        // ----- BARRA DE BAIXO (3 destinos) -----
        bottomBar = {
            if (mostrarBarraBaixo) {
                NavigationBar {
                    // Cada item: selected acende o ícone da aba atual,
                    // onClick troca de aba, icon e label são o desenho
                    NavigationBarItem(
                        selected = rotaAtual == "boasvindas/{email}",
                        onClick = { irPelaBarra(navController, "boasvindas/${Uri.encode(emailLogado)}") },
                        icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
                        label = { Text("Início") }
                    )
                    NavigationBarItem(
                        selected = rotaAtual == "perfil/{email}",
                        onClick = { irPelaBarra(navController, "perfil/${Uri.encode(emailLogado)}") },
                        icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                    NavigationBarItem(
                        selected = rotaAtual == "sobre",
                        onClick = { irPelaBarra(navController, "sobre") },
                        icon = { Icon(Icons.Default.Info, contentDescription = "Sobre") },
                        label = { Text("Sobre") }
                    )
                }
            }
        },
        // Na tela verde o conteúdo vai até a borda (por baixo da barra de status)
        contentWindowInsets = if (mostrarBarraCima) ScaffoldDefaults.contentWindowInsets else WindowInsets(0)
    ) { innerPadding ->
        // innerPadding: o espaço ocupado pelas barras de cima e de baixo

        // ===== O MAPA DE ROTAS =====
        NavHost(
            navController = navController,
            startDestination = "inicial",   // a primeira tela que aparece
            // O innerPadding NÃO é opcional: sem ele o conteúdo some atrás das barras
            modifier = Modifier.padding(innerPadding)
        ) {

            // ----- TELA INICIAL -----
            composable("inicial") {
                TelaInicial(aoTerminar = {
                    navController.navigate("login") {
                        // Tira a tela inicial da pilha:
                        // o botão voltar nunca volta para ela
                        popUpTo("inicial") { inclusive = true }
                    }
                })
            }

            // ----- LOGIN -----
            composable("login") {
                TelaLogin(aoEntrar = { email ->
                    emailLogado = email   // guarda para a barra de baixo usar
                    // IDA: empilha a segunda tela, levando o e-mail na rota
                    // Uri.encode protege caracteres especiais do e-mail
                    navController.navigate("boasvindas/${Uri.encode(email)}")
                })
            }

            // ----- SEGUNDA TELA (recebe o e-mail) -----
            // {email} na rota é um ARGUMENTO: o pedaço que muda
            composable("boasvindas/{email}") { backStackEntry ->
                // Pega o dado que veio na rota. O ?: "" trata o caso de vir null
                val email = backStackEntry.arguments?.getString("email") ?: ""
                TelaBoasVindas(
                    email = email,
                    // VOLTA: desempilha tudo até o login
                    // (inclusive = false: o login fica na pilha)
                    aoSair = { navController.popBackStack("login", inclusive = false) }
                )
            }

            // ----- PERFIL (também recebe o e-mail pela rota) -----
            composable("perfil/{email}") { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                TelaPerfil(email = email)
            }

            // ----- SOBRE -----
            composable("sobre") {
                TelaSobre()
            }
        }
    }
}

// ===== TÍTULO DA BARRA DE CIMA =====
// Recebe a rota atual e devolve o texto do título.
// "when" é como um if com vários casos; "else" é o caso que sobra.
private fun tituloDa(rota: String?): String = when (rota) {
    "login" -> "Login"
    "boasvindas/{email}" -> "Início"
    "perfil/{email}" -> "Perfil"
    "sobre" -> "Sobre"
    else -> "Academia Ulbra"
}

// ===== TROCA DE ABA PELA BARRA DE BAIXO =====
// Sem isso, a pilha cresceria a cada toque (Início, Perfil, Início, Perfil...)
// e o botão voltar passaria por todas elas.
// Assim, o Início fica sempre na base e só o destino escolhido vai por cima.
private fun irPelaBarra(navController: NavHostController, rota: String) {
    navController.navigate(rota) {
        popUpTo("boasvindas/{email}")   // tira tudo que está acima do Início
        launchSingleTop = true          // não empilha a mesma tela duas vezes
    }
}
