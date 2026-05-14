package com.agrodiario.livraria

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Livro(val titulo: String, val autor: String, val descricao: String, val capa: Int)

@Composable
fun CatalogoTela() {
    val livros = listOf(
        Livro("O Senhor dos Anéis", "J.R.R. Tolkien", "Uma épica jornada pela Terra Média para destruir o Um Anel.", R.drawable.senhor_dos_aneis),
        Livro("Harry Potter", "J.K. Rowling", "Um jovem bruxo descobre seus poderes e enfrenta o mal.", R.drawable.harry_potter),
        Livro("Duna", "Frank Herbert", "Numa galáxia distante, batalhas pelo controle do deserto.", R.drawable.duna),
        Livro("1984", "George Orwell", "Uma distopia sombria sobre vigilância e controle total.", R.drawable.mil_novecentos),
        Livro("O Hobbit", "J.R.R. Tolkien", "A aventura de Bilbo Bolseiro por terras desconhecidas.", R.drawable.hobbit)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp)
    ) {
        Text(
            text = "📚 Catálogo de Livros",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp, top = 32.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(livros) { livro ->
                CardLivro(livro)
            }
        }
    }
}

@Composable
fun CardLivro(livro: Livro) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2d2d4e)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = livro.capa),
                contentDescription = livro.titulo,
                modifier = Modifier
                    .width(80.dp)
                    .height(110.dp)
            )
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = livro.titulo,
                    fontSize = 16.sp,
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
                    color = Color(0xFF8888aa),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CatalogoPreview() {
    CatalogoTela()
}