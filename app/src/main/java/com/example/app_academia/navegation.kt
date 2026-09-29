package com.example.app_academia

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// NavHost é o MAPA, navController é o MOTORISTA,
// cada composable("rota") é um DESTINO.
@Composable
fun Navegacao() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicial") {

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
                // VOLTA: tira a tela do topo da pilha
                aoSair = { navController.popBackStack() }
            )
        }
    }
}