package com.agrodiario.livraria

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.agrodiario.livraria.api.LivroApiEntity
import com.agrodiario.livraria.api.LivroApiResponse
import com.agrodiario.livraria.api.LivroApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscaOnlineTela(
    livroService: LivroApiService,
    onVoltar: () -> Unit
) {
    var termo by remember { mutableStateOf("") }
    var livrosApi by remember { mutableStateOf<List<LivroApiEntity>>(emptyList()) }
    var mensagem by remember { mutableStateOf("Digite o nome de um livro para buscar.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        Text(
            text = "🌐 Buscar Livros Online",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        CampoTexto("Nome do livro", termo) {
            termo = it
        }

        Button(
            onClick = {
                if (termo.isNotBlank()) {
                    mensagem = "Buscando..."

                    livroService.buscarLivros(termo).enqueue(
                        object : Callback<LivroApiResponse> {
                            override fun onResponse(
                                call: Call<LivroApiResponse>,
                                response: Response<LivroApiResponse>
                            ) {
                                val resultado = response.body()?.docs ?: emptyList()
                                livrosApi = resultado.take(10)

                                mensagem = if (livrosApi.isEmpty()) {
                                    "Nenhum livro encontrado."
                                } else {
                                    "Resultado da busca:"
                                }
                            }

                            override fun onFailure(
                                call: Call<LivroApiResponse>,
                                t: Throwable
                            ) {
                                mensagem = "Erro ao buscar livros."
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C6AF7)
            )
        ) {
            Text("BUSCAR", color = Color.White)
        }

        Text(
            text = mensagem,
            color = Color.LightGray,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp)
        ) {
            items(livrosApi) { livro ->
                CardLivroApi(livro)
            }
        }

        Button(
            onClick = onVoltar,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2D2D4E)
            )
        ) {
            Text("VOLTAR", color = Color.White)
        }
    }
}

@Composable
fun CardLivroApi(livro: LivroApiEntity) {
    val capaUrl = livro.coverId?.let {
        "https://covers.openlibrary.org/b/id/$it-M.jpg"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D4E)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            if (capaUrl != null) {
                AsyncImage(
                    model = capaUrl,
                    contentDescription = livro.title,
                    modifier = Modifier
                        .width(80.dp)
                        .height(120.dp)
                )
            } else {
                Card(
                    modifier = Modifier
                        .width(80.dp)
                        .height(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF1A1A2E)
                    )
                ) {
                    Text(
                        text = "Sem capa",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = livro.title ?: "Título não informado",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = livro.authorName?.joinToString(", ") ?: "Autor não informado",
                    fontSize = 14.sp,
                    color = Color(0xFF7C6AF7),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}