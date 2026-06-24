package com.agrodiario.livraria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agrodiario.livraria.database.LivroDatabase
import com.agrodiario.livraria.api.RetrofitClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val banco = LivroDatabase.getDatabase(this)
        val livroDAO = banco.livroDAO()
        val livroService = RetrofitClient.createLivroService()

        setContent {
            var telaAtual by remember { mutableStateOf("login") }

            when (telaAtual) {
                "login" -> LoginTela(
                    onLoginClick = { telaAtual = "catalogo" }
                )

                "catalogo" -> CatalogoTela(
                    livroDAO = livroDAO,
                    livroService = livroService
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginTela(onLoginClick: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var erroLogin by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.books),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Livraria",
            fontSize = 28.sp,
            color = Color.White
        )

        TextField(
            value = usuario,
            onValueChange = {
                usuario = it
                erroLogin = ""
            },
            label = { Text("Usuário") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )

        TextField(
            value = senha,
            onValueChange = {
                senha = it
                erroLogin = ""
            },
            label = { Text("Senha") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        Button(
            onClick = {
                if (usuario == "admin" && senha == "123") {
                    erroLogin = ""
                    onLoginClick()
                } else {
                    erroLogin = "Usuário ou senha inválidos"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Entrar")
        }

        if (erroLogin.isNotBlank()) {
            Text(
                text = erroLogin,
                color = Color(0xFFE94560),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Text(
            text = "Usuário: admin | Senha: 123",
            color = Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginTela(onLoginClick = {})
}