package com.app.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.supercompras.ui.theme.Marinho
import com.app.supercompras.ui.theme.SuperComprasTheme
import com.app.supercompras.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperComprasTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaDeCompras(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ListaDeCompras(modifier: Modifier = Modifier) {
    var listaDeItens by rememberSaveable { mutableStateOf(listOf<ItemCompra>()) }

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        ImagemTopo()
        AdicionarItem(aoSalvarItem = { textNovo ->
            listaDeItens = listaDeItens + ItemCompra(textNovo)
        })
        Spacer(modifier = Modifier.height(48.dp))
        Titulo(
            texto = "Lista de Compras",
        )
        ListaDeItems(
            lista = listaDeItens.filter { !it.foiComprado },
            aoMudarStatus = { itemSelecionado ->
                listaDeItens = listaDeItens.map { itemMap ->
                    if (itemSelecionado == itemMap) {
                        itemSelecionado.copy(foiComprado = !itemSelecionado.foiComprado)
                    } else {
                        itemMap
                    }
                }
            },
            aoRemoverItem = { itemRemovido ->
                listaDeItens = listaDeItens - itemRemovido
            },
            aoEditarItem = { itemEditado ->
                listaDeItens = listaDeItens.map { itemAtual ->
                    if (itemAtual == itemEditado) {
                        itemAtual.copy(texto = itemEditado.texto)
                    } else {
                        itemAtual
                    }
                }
            }
        )

        Titulo(texto = "Comprado")

        if (listaDeItens.any { it.foiComprado }) {
            ListaDeItems(
                lista = listaDeItens.filter { it.foiComprado },
                aoMudarStatus = { itemSelecionado ->
                    listaDeItens = listaDeItens.map { itemMap ->
                        if (itemSelecionado == itemMap) {
                            itemSelecionado.copy(foiComprado = !itemSelecionado.foiComprado)
                        } else {
                            itemMap
                        }
                    }
                },
                aoRemoverItem = { itemRemovido ->
                    listaDeItens = listaDeItens - itemRemovido
                },
                aoEditarItem = { itemEditado ->
                    listaDeItens = listaDeItens.map { itemAtual ->
                        if (itemAtual == itemEditado) {
                            itemAtual.copy(texto = itemEditado.texto)
                        } else {
                            itemAtual
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ListaDeItems(
    lista: List<ItemCompra>,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        lista.forEach { item ->
            ItemDaLista(
                item = item,
                aoMudarStatus = aoMudarStatus,
                aoRemoverItem = aoRemoverItem,
                aoEditarItem = aoEditarItem
            )
        }
    }
}

@Composable
fun AdicionarItem(aoSalvarItem: (texto: String) -> Unit, modifier: Modifier = Modifier) {
    var texto = rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = texto.value,
        onValueChange = { texto.value = it },
        placeholder = {
            Text(
                text = "Digite o item que deseja adicionar",
                color = Color.Gray,
                style = Typography.bodyMedium
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        singleLine = true,
        shape = RoundedCornerShape(24.dp)
    )

    Button(
        shape = RoundedCornerShape(24.dp),
        onClick = {
            aoSalvarItem(texto.value)
            texto.value = ""
        },
        modifier = modifier
    ) {
        Text(
            text = "Salvar item",
            color = Color.White,
            style = Typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}

@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(text = texto, style = Typography.headlineLarge, modifier = modifier)
}

@Composable
fun ItemDaLista(
    item: ItemCompra,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.Top, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = item.foiComprado,
                onCheckedChange = {
                    aoMudarStatus(item)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )
            Text(
                text = item.texto,
                modifier = Modifier.weight(1f),
                style = Typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            IconButton(
                onClick = { aoRemoverItem(item) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icone(
                    Icons.Default.Delete,
                    modifier = Modifier
                        .size(16.dp)
                )
            }
            IconButton(
                onClick = { aoEditarItem(item) }
            ) {
                Icone(Icons.Default.Edit, modifier = Modifier.size(16.dp))
            }
        }
        Text(
            "Segunda-feira (31/10/2022) às 08:30",
            Modifier.padding(top = 8.dp),
            style = Typography.labelSmall,
        )

    }
}

@Composable
fun ImagemTopo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.image_topo),
        contentDescription = null,
        modifier = modifier.size(160.dp)
    )
}

@Composable
fun Icone(icone: ImageVector, modifier: Modifier = Modifier) {
    Icon(icone, contentDescription = "Editar", modifier = modifier, tint = Marinho)
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello Fernando",
        modifier = modifier
    )
}

@Preview
@Composable
private fun AdicionarItemPreview() {
    SuperComprasTheme {
        AdicionarItem(aoSalvarItem = {})
    }
}

@Preview
@Composable
fun ItemDaListaPreview() {
    SuperComprasTheme {
        ItemDaLista(item = ItemCompra("Suco"))
    }
}

@Preview
@Composable
fun IconeEditPreview() {
    SuperComprasTheme {
        Icone(icone = Icons.Default.Delete)
    }
}

@Preview
@Composable
fun ImagemTopoPreview() {
    SuperComprasTheme {
        ImagemTopo()
    }
}

@Preview
@Composable
fun TituloPreview() {
    SuperComprasTheme {
        Titulo(texto = "Lista de Compras")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SuperComprasTheme {
        Greeting("Android")
    }
}

data class ItemCompra(
    val texto: String,
    var foiComprado: Boolean = false
)