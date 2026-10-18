package com.example.app_academia

// ===== IMPORTS =====
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Handler
import android.os.Looper
// Launcher: abre outra "janela" (pedido de permissão, app de câmera) e recebe a resposta
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
// ContextCompat: confere se a permissão já foi dada
import androidx.core.content.ContextCompat
// Cliente de localização do Google Play Services
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.Locale

// ===== RECURSOS DO APARELHO =====
// O que só o celular tem: câmera e localização.
// Os dois são OPCIONAIS: o app funciona igual sem foto e sem local.
//   Câmera       -> foto de perfil, para o instrutor reconhecer o aluno (TelaPerfil.kt)
//   Localização  -> check-in do treino: mostra em que cidade a pessoa treinou (TelaBoasVindas.kt)
// O que foi capturado mora no navegation.kt e aparece também no Perfil.

// ===== O CHECK-IN =====
// Onde a pessoa treinou. A cidade é opcional: sem internet ficam só os números.
// Serializable: deixa o rememberSaveable guardar o check-in ao girar a tela
data class CheckIn(
    val latitude: Double,
    val longitude: Double,
    val cidade: String? = null
) : java.io.Serializable

// ===== CÂMERA: FOTO DE PERFIL =====
// Usa o app de câmera do sistema: quem usa a câmera é ELE,
// por isso este app NÃO precisa de permissão de câmera.
// A foto mora no navegation.kt (para não sumir ao trocar de aba):
// aqui a tela só mostra e AVISA quando chegou foto nova.
@Composable
fun FotoDePerfil(foto: Bitmap?, aoTirarFoto: (Bitmap) -> Unit) {

    // Prepara o pedido ao app de câmera e diz o que fazer com a resposta
    val tirarFoto = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { imagem ->
        // null quando a pessoa desiste sem fotografar: não muda nada
        if (imagem != null) aoTirarFoto(imagem)
    }

    // Miniatura e botão lado a lado
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Espaco.medio)
    ) {
        // Só desenha a miniatura se já existe foto
        if (foto != null) {
            Image(
                bitmap = foto.asImageBitmap(),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,   // corta para caber no quadrado
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.medium)  // cantos do tema
            )
        }
        OutlinedButton(
            onClick = { tirarFoto.launch(null) },   // abre a câmera do sistema
            shape = MaterialTheme.shapes.small
        ) {
            Text(if (foto == null) "Tirar foto de perfil" else "Trocar foto")
        }
    }
}

// ===== LOCALIZAÇÃO: CHECK-IN DO TREINO =====
// Pede só a localização APROXIMADA: para saber a cidade do treino, ela basta.
// O pedido acontece no toque do botão, quando a pessoa entende para que serve.
// Igual à foto: o check-in mora no navegation.kt, a tela só mostra e AVISA.
@Composable
fun CheckInDoTreino(checkIn: CheckIn?, aoFazerCheckIn: (CheckIn) -> Unit) {
    val contexto = LocalContext.current

    // Mensagem embaixo do botão (procurando, negado, erro)
    var aviso by rememberSaveable { mutableStateOf<String?>(null) }

    // O cliente que pergunta ao Android onde o aparelho está
    val cliente = remember { LocationServices.getFusedLocationProviderClient(contexto) }

    // Busca a posição. Só é chamada depois de conferir a permissão,
    // por isso o @SuppressLint (o Android Studio não enxerga essa conferência)
    @SuppressLint("MissingPermission")
    fun buscar() {
        aviso = "Procurando onde você está..."
        cliente.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
            .addOnSuccessListener { local ->
                if (local != null) {
                    // Com os números, pergunta o nome da cidade
                    buscarCidade(contexto, local.latitude, local.longitude) { nome ->
                        aoFazerCheckIn(CheckIn(local.latitude, local.longitude, nome))
                        aviso = null
                    }
                } else {
                    aviso = "Não deu para achar o local agora. Tente de novo."
                }
            }
            .addOnFailureListener {
                aviso = "Não deu para achar o local agora. Tente de novo."
            }
    }

    // Prepara o pedido de permissão e diz o que fazer com a resposta
    val pedirPermissao = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { permitiu ->
        if (permitiu) buscar()
        // Negado não é bug: avisa sem drama e o app segue normal
        else aviso = "Sem problema: o treino funciona sem o check-in."
    }

    Column(verticalArrangement = Arrangement.spacedBy(Espaco.pequeno)) {
        OutlinedButton(
            onClick = {
                val jaPermitiu = ContextCompat.checkSelfPermission(
                    contexto, Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                if (jaPermitiu) buscar()   // não incomoda quem já deixou
                else pedirPermissao.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fazer check-in do treino")
        }

        // O resultado aparece NA TELA, não só num log
        if (checkIn != null) {
            Text(
                text = "Treinando perto de ${descreverLocal(checkIn)}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        // O aviso aparece embaixo do botão
        aviso?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

// ===== DOS NÚMEROS PARA A CIDADE =====
// O Geocoder faz o caminho de volta: latitude e longitude viram endereço.
// Sem internet ele falha, e aí fica só com os números (aoAchar(null)).
private fun buscarCidade(
    contexto: Context, latitude: Double, longitude: Double,
    aoAchar: (String?) -> Unit
) {
    val geocoder = Geocoder(contexto, Locale.forLanguageTag("pt-BR"))
    // A resposta pode chegar em outra thread: devolve na thread da tela
    val telaPrincipal = Handler(Looper.getMainLooper())

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        // Android 13 ou mais novo: o Geocoder avisa quando termina
        geocoder.getFromLocation(latitude, longitude, 1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(enderecos: MutableList<Address>) {
                    val nome = enderecos.firstOrNull()?.locality   // a cidade
                    telaPrincipal.post { aoAchar(nome) }
                }
                override fun onError(mensagem: String?) {
                    telaPrincipal.post { aoAchar(null) }
                }
            })
    } else {
        // Android antigo: o jeito velho trava enquanto busca, então roda numa thread separada
        Thread {
            val nome = try {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(latitude, longitude, 1)?.firstOrNull()?.locality
            } catch (e: Exception) {
                null
            }
            telaPrincipal.post { aoAchar(nome) }
        }.start()
    }
}

// ===== TEXTO DO LOCAL =====
// "Canoas (-29.92, -51.18)" ou, se não achou a cidade, só os números.
// Sem "private": o Perfil também usa.
fun descreverLocal(checkIn: CheckIn): String {
    val numeros = String.format(Locale.US, "%.2f, %.2f", checkIn.latitude, checkIn.longitude)
    return if (checkIn.cidade != null) "${checkIn.cidade} ($numeros)" else numeros
}
