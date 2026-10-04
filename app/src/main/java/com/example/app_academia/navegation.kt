package com.example.app_academia

import android.net.Uri
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// NavHost é o MAPA, navController é o MOTORISTA,
// cada composable("rota") é um DESTINO.
// Este é o ÚNICO arquivo que conhece o navController:
// as telas só recebem funções e avisam o que aconteceu.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navegacao() {
    val navController = rememberNavController()

    // E-mail de quem entrou: a barra de baixo precisa dele para montar as rotas
    var emailLogado by rememberSaveable { mutableStateOf("") }

    // Qual prato está no topo da pilha agora
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    // A tela verde não tem barra; a barra de baixo só aparece depois do login
    val mostrarBarraCima = rotaAtual != null && rotaAtual != "inicial"
    val mostrarBarraBaixo = rotaAtual in listOf("boasvindas/{email}", "perfil/{email}", "sobre")

    // ===== O ESQUELETO DO APP: UM SCAFFOLD SÓ =====
    Scaffold(
        topBar = {
            if (mostrarBarraCima) {
                TopAppBar(
                    title = { Text(tituloDa(rotaAtual)) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (mostrarBarraBaixo) {
                NavigationBar {
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

        NavHost(
            navController = navController,
            startDestination = "inicial",
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
                    emailLogado = email
                    // IDA: empilha a segunda tela, levando o e-mail na rota
                    // Uri.encode protege caracteres especiais do e-mail
                    navController.navigate("boasvindas/${Uri.encode(email)}")
                })
            }

            // ----- SEGUNDA TELA (recebe o e-mail) -----
            composable("boasvindas/{email}") { backStackEntry ->
                // Pega o dado que veio na rota. O ?: "" trata o caso de vir null
                val email = backStackEntry.arguments?.getString("email") ?: ""
                TelaBoasVindas(
                    email = email,
                    // VOLTA: desempilha tudo até o login
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

// Título da barra de cima de acordo com a tela atual
private fun tituloDa(rota: String?): String = when (rota) {
    "login" -> "Login"
    "boasvindas/{email}" -> "Início"
    "perfil/{email}" -> "Perfil"
    "sobre" -> "Sobre"
    else -> "Academia Ulbra"
}

// Troca de aba pela barra de baixo sem a pilha crescer a cada toque:
// o Início fica sempre na base e só o destino escolhido vai por cima.
private fun irPelaBarra(navController: NavHostController, rota: String) {
    navController.navigate(rota) {
        popUpTo("boasvindas/{email}")
        launchSingleTop = true   // não empilha a mesma tela duas vezes
    }
}
