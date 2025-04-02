package com.app.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.supercompras.ui.theme.Coral
import com.app.supercompras.ui.theme.Marinho
import com.app.supercompras.ui.theme.SuperComprasTheme
import com.app.supercompras.ui.theme.Typography
import kotlinx.coroutines.flow.toCollection
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : ComponentActivity() {

    val viewModel: SuperComprasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperComprasTheme() {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ListaDeCompras(Modifier.padding(innerPadding), viewModel)
                }
            }
        }
    }
}

@Composable
fun ListaDeCompras(modifier: Modifier = Modifier, viewModel: SuperComprasViewModel) {
    val listaDeItens by viewModel.listaDeItens.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            ImagemTopo()
            AdicionarItem(aoSalvarItem = { novoItem ->
                viewModel.adicionarItem(novoItem)
            })
            Spacer(modifier = Modifier.height(48.dp))
            Titulo(
                texto = "Lista de Compras",
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if(listaDeItens.isEmpty()) {
            item {
                Text(
                    text = "Sua lista está vazia. Adicione itens a ela para não esquecer nada na próxima compra!",
                    style = Typography.bodyLarge
                )
            }
        }

        ListaDeItems(
            lista = listaDeItens.filter { !it.foiComprado },
            aoMudarStatus = { itemSelecionado ->
                viewModel.mudarStatus(itemSelecionado)
            },
            aoRemoverItem = { itemRemovido ->
                viewModel.removerItem(itemRemovido)
            },
            aoEditarItem = { itemEditado, novoTexto ->
                viewModel.editarItem(itemEditado, novoTexto)
            }
        )

        if (listaDeItens.any { it.foiComprado }) {
            item {
                Spacer(modifier = Modifier.height(40.dp))
                Titulo(texto = "Comprado")
                Spacer(modifier = Modifier.height(24.dp))
            }

            ListaDeItems(
                lista = listaDeItens.filter { it.foiComprado },
                aoMudarStatus = { itemSelecionado ->
                    viewModel.mudarStatus(itemSelecionado)
                },
                aoRemoverItem = { itemRemovido ->
                    viewModel.removerItem(itemRemovido)
                },
                aoEditarItem = { itemEditado, novoTexto ->
                    viewModel.editarItem(itemEditado, novoTexto)
                }
            )
        }
    }
}

fun LazyListScope.ListaDeItems(
    lista: List<ItemCompra>,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra, novoTexto: String) -> Unit = { _, _ -> }
) {
    items(lista.size) { index ->
        ItemDaLista(
            item = lista[index],
            aoMudarStatus = aoMudarStatus,
            aoRemoverItem = aoRemoverItem,
            aoEditarItem = aoEditarItem
        )
    }
}

@Composable
fun AdicionarItem(aoSalvarItem: (item: ItemCompra) -> Unit, modifier: Modifier = Modifier) {
    var texto by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        value = texto,
        onValueChange = { texto = it },
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
            aoSalvarItem(ItemCompra(texto, false, getDataHora()))
            texto = ""
        },
        modifier = modifier,
        contentPadding = PaddingValues(16.dp, 12.dp)
    ) {
        Text(
            text = "Salvar item",
            color = Color.White,
            style = Typography.bodyLarge
        )
    }
}

fun getDataHora(): String {
    val dataHoraAtual = System.currentTimeMillis()
    val dataHoraFormata = SimpleDateFormat("EEEE (dd/MM/yyyy) 'às' HH:mm", Locale("pt", "BR"))
    return dataHoraFormata.format(dataHoraAtual)
}

@Composable
fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = Typography.headlineLarge,
        modifier = modifier.padding(bottom = 8.dp).fillMaxWidth(),
        textAlign = TextAlign.Left
    )
    LinhaPontilhada(modifier = modifier)
}

@Composable
fun LinhaPontilhada(modifier: Modifier = Modifier) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 2.5f)
    Canvas(modifier = modifier.fillMaxWidth()) {
        drawLine(
            color = Coral,
            pathEffect = pathEffect,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 4f
        )
    }
}

@Composable
fun ItemDaLista(
    item: ItemCompra,
    aoMudarStatus: (item: ItemCompra) -> Unit = {},
    aoRemoverItem: (item: ItemCompra) -> Unit = {},
    aoEditarItem: (item: ItemCompra, novoTexto: String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.Top, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            var textoEditado by rememberSaveable { mutableStateOf(item.texto) }
            var edicao by rememberSaveable { mutableStateOf(false) }

            Checkbox(
                checked = item.foiComprado,
                onCheckedChange = {
                    aoMudarStatus(item)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )

            if (edicao) {
                OutlinedTextField(
                    value = textoEditado,
                    onValueChange = { textoEditado = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )

                IconButton(
                    onClick = {
                        aoEditarItem(item, textoEditado)
                        edicao = false
                    }
                ) {
                    Icone(Icons.Default.Done, modifier = Modifier.size(16.dp))
                }
            } else {
                Text(
                    text = item.texto,
                    modifier = Modifier.weight(1f),
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Start
                )
            }
            IconButton(
                onClick = { aoRemoverItem(item) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(16.dp)
            ) {
                Icone(
                    Icons.Default.Delete
                )
            }
            IconButton(
                onClick = {
                    edicao = true
                },
                modifier = Modifier.size(16.dp)
            ) {
                Icone(Icons.Default.Edit)
            }
        }
        Text(
            item.datahora,
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
        ItemDaLista(item = ItemCompra("Suco", false, "Segunda-feira"))
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
    var foiComprado: Boolean = false,
    val datahora: String
)