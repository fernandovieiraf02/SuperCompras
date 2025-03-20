package com.app.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
            SuperComprasTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(56.dp),
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        Titulo(
                            text = "Lista de Compras"
                        )
                        ItemSelecionavel()
                    }
                }
            }
        }
    }
}

@Composable
fun ItemSelecionavel(modifier: Modifier = Modifier) {
    Column {
        val marinho = Color(0xFF131730)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.padding(bottom = 8.dp)) {
            Checkbox(false, null, modifier = Modifier.padding(end = 8.dp))
            Text("Item da lista", fontSize = 16.sp, lineHeight = 20.sp, color = marinho)
            IconButton({}) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Excluir",
                    tint = marinho,
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton({}) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar",
                    tint = marinho,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        Text(
            "Segunda-feira (31/10/2022) às 08:30",
            fontSize = 12.sp,
            lineHeight = 20.sp,
            color = marinho
        )
    }
}

@Composable
fun Titulo(text: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val coral = Color(0xFFF55B64)
        Text(
            text = text,
            color = coral,
            fontSize = 20.sp
        )
        HorizontalDivider(thickness = 2.dp, color = coral)
    }
}

@Preview
@Composable
private fun ItemSelecionalPreview() {
    SuperComprasTheme {
        ItemSelecionavel()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperComprasTheme {
        Titulo(text = "Lista de Compras")
    }
}