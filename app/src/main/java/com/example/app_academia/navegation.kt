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

// Os nomes das rotas moram num lugar só:
// erro de digitação em rota é o erro mais chato de achar.
object Rotas {
    const val INICIAL = "inicial"
    const val LOGIN = "login"
    const val BOAS_VINDAS = "boasvindas/{email}"
    const val PERFIL = "perfil/{email}"
    const val SOBRE = "sobre"

    // Montam a rota com o e-mail de verdade.
    // Uri.encode protege caracteres especiais do e-mail
    fun boasVindas(email: String) = "boasvindas/${Uri.encode(email)}"
    fun perfil(email: String) = "perfil/${Uri.encode(email)}"
}

// NavHost é o MAPA, navController é o MOTORISTA,
// cada composable("rota") é um DESTINO.
// Este é o único arquivo que conhece o navController.
@OptIn(ExperimentalMaterial3Api::class) // a TopAppBar ainda é experimental no Material 3
@Composable
fun Navegacao() {
    val navController = rememberNavController()

    // E-mail de quem entrou: a barra de baixo precisa dele para montar as rotas
    var emailLogado by rememberSaveable { mutableStateOf("") }

    // Qual tela está no topo da pilha agora
    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    // A tela verde não tem barra nenhuma; a barra de baixo só aparece depois do login
    val mostrarBarraCima = rotaAtual != null && rotaAtual != Rotas.INICIAL
    val mostrarBarraBaixo = rotaAtual in listOf(Rotas.BOAS_VINDAS, Rotas.PERFIL, Rotas.SOBRE)

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
                        selected = rotaAtual == Rotas.BOAS_VINDAS,
                        onClick = { irPelaBarra(navController, Rotas.boasVindas(emailLogado)) },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Início") },
                        label = { Text("Início") }
                    )
                    NavigationBarItem(
                        selected = rotaAtual == Rotas.PERFIL,
                        onClick = { irPelaBarra(navController, Rotas.perfil(emailLogado)) },
                        icon = { Icon(Icons.Filled.Person, contentDescription = "Perfil") },
                        label = { Text("Perfil") }
                    )
                    NavigationBarItem(
                        selected = rotaAtual == Rotas.SOBRE,
                        onClick = { irPelaBarra(navController, Rotas.SOBRE) },
                        icon = { Icon(Icons.Filled.Info, contentDescription = "Sobre") },
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
            startDestination = Rotas.INICIAL,
            // O innerPadding NÃO é opcional: sem ele o conteúdo some atrás das barras
            modifier = Modifier.padding(innerPadding)
        ) {

            // ----- TELA INICIAL -----
            composable(Rotas.INICIAL) {
                TelaInicial(aoTerminar = {
                    navController.navigate(Rotas.LOGIN) {
                        // Tira a tela inicial da pilha:
                        // o botão voltar nunca volta para ela
                        popUpTo(Rotas.INICIAL) { inclusive = true }
                    }
                })
            }

            // ----- LOGIN -----
            composable(Rotas.LOGIN) {
                TelaLogin(aoEntrar = { email ->
                    emailLogado = email
                    // IDA: empilha a segunda tela, levando o e-mail na rota
                    navController.navigate(Rotas.boasVindas(email))
                })
            }

            // ----- SEGUNDA TELA (recebe o e-mail) -----
            composable(Rotas.BOAS_VINDAS) { backStackEntry ->
                // Pega o dado que veio na rota. O ?: "" trata o caso de vir null
                val email = backStackEntry.arguments?.getString("email") ?: ""
                TelaBoasVindas(
                    email = email,
                    // VOLTA: desempilha tudo até o login
                    aoSair = { navController.popBackStack(Rotas.LOGIN, inclusive = false) }
                )
            }

            // ----- PERFIL (também recebe o e-mail) -----
            composable(Rotas.PERFIL) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                TelaPerfil(email = email)
            }

            // ----- SOBRE -----
            composable(Rotas.SOBRE) {
                TelaSobre()
            }
        }
    }
}

// Título da barra de cima de acordo com a tela atual
private fun tituloDa(rota: String?): String = when (rota) {
    Rotas.LOGIN -> "Login"
    Rotas.BOAS_VINDAS -> "Início"
    Rotas.PERFIL -> "Perfil"
    Rotas.SOBRE -> "Sobre"
    else -> "Academia Ulbra"
}

// Troca de aba pela barra de baixo sem a pilha crescer a cada toque:
// volta até o Início e empilha só o destino escolhido.
private fun irPelaBarra(navController: NavHostController, rota: String) {
    navController.navigate(rota) {
        popUpTo(Rotas.BOAS_VINDAS)   // o Início fica sempre na base das abas
        launchSingleTop = true       // não empilha a mesma tela duas vezes
    }
}
