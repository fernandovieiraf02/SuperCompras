package com.app.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.supercompras.ui.theme.SuperComprasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperComprasTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(56.dp),
                ) { innerPadding ->
                    Titulo(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Titulo(modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val coral = Color(0xFFF55B64)
        Text(
            text = "Lista de Compras",
            color = coral,
            fontSize = 20.sp
        )
        HorizontalDivider(thickness = 2.dp, color = coral)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperComprasTheme {
        Titulo()
    }
}