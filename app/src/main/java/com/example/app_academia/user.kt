package com.example.app_academia

// ===== O MODELO =====
// Um usuário tem e-mail e senha.
// "data class": classe feita só para guardar dados
// (o Kotlin já cria comparação, cópia e toString sozinho)
data class Usuario(val email: String, val senha: String)

// ===== "BANCO" DE MENTIRA =====
// Uma lista fixa, guardada só na memória (sem tela nenhuma aqui).
// Banco de verdade (SQLite) vem no Módulo 5.
// Para cadastrar mais alguém, é só adicionar outra linha na lista.
// listOf: lista que não muda depois de criada
val usuarios = listOf(
    Usuario(email = "academiaulbra@ulbra.br", senha = "admin123"),
    Usuario(email = "academiaubra@ulbra.br", senha = "admin123"),
    Usuario(email = "vinicius@ulbra.br", senha = "123456")
)

// ===== A REGRA DO LOGIN =====
// Confere se o e-mail e a senha batem com alguém da lista.
// O "any" passa pela lista inteira e responde sim ou não.
// O "it" é cada usuário, um por vez.
// O trim() tira espaços que o teclado coloca sem querer.
// ignoreCase = true: "Vinicius@ulbra.br" e "vinicius@ulbra.br" valem igual.
fun loginValido(email: String, senha: String): Boolean {
    return usuarios.any {
        it.email.equals(email.trim(), ignoreCase = true) && it.senha == senha.trim()
    }
}
