package com.agrodiario.livraria

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agrodiario.livraria.api.LivroApiService
import com.agrodiario.livraria.dao.LivroDAO
import com.agrodiario.livraria.entity.LivroEntity

@Composable
fun CatalogoTela(
    livroDAO: LivroDAO,
    livroService: LivroApiService
) {
    var telaAtual by remember { mutableStateOf("catalogo") }
    var filtro by remember { mutableStateOf("Todos") }
    var livroEditando by remember { mutableStateOf<LivroEntity?>(null) }

    fun carregarLivros(): List<LivroEntity> {
        return if (filtro == "Todos") {
            livroDAO.getAllLivros()
        } else {
            livroDAO.getLivrosPorStatus(filtro)
        }
    }

    var livros by remember { mutableStateOf(carregarLivros()) }

    fun atualizarLista() {
        livros = carregarLivros()
    }

    when (telaAtual) {
        "catalogo" -> TelaListaLivros(
            livros = livros,
            filtro = filtro,
            onFiltroClick = {
                filtro = it
                livros = if (it == "Todos") {
                    livroDAO.getAllLivros()
                } else {
                    livroDAO.getLivrosPorStatus(it)
                }
            },
            onAdicionarClick = {
                livroEditando = null
                telaAtual = "formulario"
            },
            onBuscarOnlineClick = {
                telaAtual = "api"
            },
            onEditarClick = {
                livroEditando = it
                telaAtual = "formulario"
            },
            onExcluirClick = {
                livroDAO.delete(it)
                atualizarLista()
            }
        )

        "formulario" -> TelaFormularioLivro(
            livroEditando = livroEditando,
            onVoltar = {
                telaAtual = "catalogo"
                atualizarLista()
            },
            onSalvar = { livro ->
                if (livroEditando == null) {
                    livroDAO.insert(livro)
                } else {
                    livroDAO.update(livro)
                }

                telaAtual = "catalogo"
                atualizarLista()
            }
        )

        "api" -> BuscaOnlineTela(
            livroService = livroService,
            onVoltar = {
                telaAtual = "catalogo"
                atualizarLista()
            }
        )
    }
}

@Composable
fun TelaListaLivros(
    livros: List<LivroEntity>,
    filtro: String,
    onFiltroClick: (String) -> Unit,
    onAdicionarClick: () -> Unit,
    onBuscarOnlineClick: () -> Unit,
    onEditarClick: (LivroEntity) -> Unit,
    onExcluirClick: (LivroEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        Text(
            text = "📚 Minha Biblioteca",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            BotaoFiltro("Todos", filtro, onFiltroClick)
            BotaoFiltro("Quero ler", filtro, onFiltroClick)
            BotaoFiltro("Lendo", filtro, onFiltroClick)
            BotaoFiltro("Lido", filtro, onFiltroClick)
        }

        Button(
            onClick = onBuscarOnlineClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            )
        ) {
            Text("BUSCAR LIVROS ONLINE", color = Color.White)
        }

        if (livros.isEmpty()) {
            Text(
                text = "Nenhum livro cadastrado ainda.",
                color = Color.LightGray,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(livros) { livro ->
                    CardLivroCRUD(
                        livro = livro,
                        onEditar = { onEditarClick(livro) },
                        onExcluir = { onExcluirClick(livro) }
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                onClick = onAdicionarClick,
                containerColor = Color(0xFF7C6AF7)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Livro",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun BotaoFiltro(
    texto: String,
    filtroAtual: String,
    onClick: (String) -> Unit
) {
    val corBotao = when (texto) {
        "Lido" -> Color(0xFF4CAF50)
        "Lendo" -> Color(0xFF2196F3)
        "Quero ler" -> Color(0xFF7C6AF7)
        else -> Color(0xFF607D8B)
    }

    Button(
        onClick = { onClick(texto) },
        colors = ButtonDefaults.buttonColors(
            containerColor = corBotao
        )
    ) {
        Text(
            text = texto,
            fontSize = 11.sp,
            color = Color.White,
            fontWeight = if (filtroAtual == texto) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaFormularioLivro(
    livroEditando: LivroEntity?,
    onVoltar: () -> Unit,
    onSalvar: (LivroEntity) -> Unit
) {
    var titulo by remember { mutableStateOf(livroEditando?.titulo ?: "") }
    var autor by remember { mutableStateOf(livroEditando?.autor ?: "") }
    var descricao by remember { mutableStateOf(livroEditando?.descricao ?: "") }
    var status by remember { mutableStateOf(livroEditando?.status ?: "Quero ler") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        Text(
            text = if (livroEditando == null) "➕ Adicionar Livro" else "✏️ Editar Livro",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 32.dp, bottom = 20.dp)
        )

        CampoTexto("Título", titulo) { titulo = it }
        CampoTexto("Autor", autor) { autor = it }
        CampoTexto("Descrição", descricao) { descricao = it }

        Text(
            text = "Status",
            color = Color.LightGray,
            modifier = Modifier.padding(top = 12.dp, bottom = 8.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BotaoFiltro("Quero ler", status) { status = it }
            BotaoFiltro("Lendo", status) { status = it }
            BotaoFiltro("Lido", status) { status = it }
        }

        Button(
            onClick = {
                if (titulo.isNotBlank() && autor.isNotBlank()) {
                    val livro = LivroEntity(
                        id = livroEditando?.id ?: 0,
                        titulo = titulo,
                        autor = autor,
                        descricao = descricao,
                        status = status
                    )

                    onSalvar(livro)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7C6AF7)
            )
        ) {
            Text(
                text = if (livroEditando == null) "SALVAR LIVRO" else "SALVAR ALTERAÇÕES",
                color = Color.White
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTexto(
    label: String,
    valor: String,
    onValorChange: (String) -> Unit
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = Color(0xFF7C6AF7),
            unfocusedLabelColor = Color.LightGray,
            focusedBorderColor = Color(0xFF7C6AF7),
            unfocusedBorderColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    )
}

@Composable
fun CardLivroCRUD(
    livro: LivroEntity,
    onEditar: () -> Unit,
    onExcluir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D2D4E)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = livro.titulo,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = livro.autor,
                fontSize = 14.sp,
                color = Color(0xFF7C6AF7),
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = livro.descricao,
                fontSize = 13.sp,
                color = Color(0xFFBBBBCC),
                modifier = Modifier.padding(top = 8.dp)
            )

            Text(
                text = livro.status,
                fontSize = 13.sp,
                color = when (livro.status) {
                    "Lido" -> Color(0xFF4CAF50)
                    "Lendo" -> Color(0xFF2196F3)
                    else -> Color(0xFF7C6AF7)
                },
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 12.dp)
            ) {
                Button(
                    onClick = onEditar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7C6AF7)
                    )
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = onExcluir,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE94560)
                    )
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogoPreview() {
    Text("Preview indisponível porque a tela precisa do banco de dados.")
}