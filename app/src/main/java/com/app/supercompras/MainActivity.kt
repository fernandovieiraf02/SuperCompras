package com.app.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.app.supercompras.ui.theme.Coral
import com.app.supercompras.ui.theme.Marinho
import com.app.supercompras.ui.theme.SuperComprasTheme
import com.app.supercompras.ui.theme.Typography

class MainActivity : ComponentActivity() {
    private val viewModel: ListaComprasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperComprasTheme(darkTheme = false, dynamicColor = false) {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 56.dp, horizontal = 56.dp),
                ) { innerPadding ->
                    ListaDeCompras(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun ListaDeCompras(modifier: Modifier = Modifier, viewModel: ListaComprasViewModel) {
    val itens by viewModel.itens.collectAsState()

    Column(modifier = modifier) {
        CampoAdicionar(
            aoSalvarItem = { novoItem ->
                viewModel.adicionarItem(novoItem)
            }
        )

        Spacer(modifier = Modifier.size(48.dp))

        Titulo(text = "Lista de Compras")

        // Lista de itens não comprados
        ListaItens(
            itens = itens.filter { !it.comprado },
            onCheckChange = { item -> viewModel.alterarStatusItem(item) },
            onDelete = { item -> viewModel.excluirItem(item) },
            onEdit = { item, novoNome -> viewModel.editarItem(item, novoNome) }
        )

        if (itens.any { it.comprado }) {
            Spacer(modifier = Modifier.size(48.dp))
            Titulo(text = "Comprado")

            ListaItens(
                itens = itens.filter { it.comprado },
                onCheckChange = { item -> viewModel.alterarStatusItem(item) },
                onDelete = { item -> viewModel.excluirItem(item) },
                onEdit = { item, novoNome -> viewModel.editarItem(item, novoNome) }
            )
        }
    }
}

@Composable
fun CampoAdicionar(aoSalvarItem: (String) -> Unit) {
    var texto by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        OutlinedTextField(
            value = texto,
            onValueChange = { novoTexto -> texto = novoTexto },
            placeholder = { Text("Digite o item que deseja adicionar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                if (texto.isNotBlank()) {
                    aoSalvarItem(texto)
                    texto = ""
                }
            }
        ) {
            Text("Salvar Item", style = Typography.bodyLarge)
        }
    }
}

@Composable
fun ListaItens(
    itens: List<ItemCompra>,
    onCheckChange: (ItemCompra) -> Unit,
    onDelete: (ItemCompra) -> Unit,
    onEdit: (ItemCompra, String) -> Unit
) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        itens.forEach { item ->
            ItemLista(
                item = item,
                onCheckChange = { onCheckChange(item) },
                onDelete = { onDelete(item) },
                onEdit = { novoNome -> onEdit(item, novoNome) }
            )
            Spacer(Modifier.size(16.dp))
        }
    }
}

@Composable
fun ItemLista(
    item: ItemCompra,
    onCheckChange: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    var editando by remember { mutableStateOf(false) }
    var textoEdicao by remember { mutableStateOf(item.nome) }

    Column(horizontalAlignment = Alignment.Start) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Checkbox(
                checked = item.comprado,
                onCheckedChange = { onCheckChange() },
                colors = CheckboxDefaults.colors(checkedColor = Marinho, uncheckedColor = Marinho),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .requiredSize(24.dp)
            )

            if (editando) {
                OutlinedTextField(
                    value = textoEdicao,
                    onValueChange = { textoEdicao = it },
                    modifier = Modifier
                        .padding(end = 8.dp),
                    singleLine = true
                )

                IconButton(
                    onClick = {
                        if (textoEdicao.isNotBlank()) {
                            onEdit(textoEdicao)
                            editando = false
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Salvar",
                        modifier = Modifier.size(16.dp),
                        tint = Marinho
                    )
                }
            } else {
                Text(
                    text = item.nome,
                    modifier = Modifier.weight(1f),
                    textDecoration = if (item.comprado) TextDecoration.LineThrough else TextDecoration.None,
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Start,
                )

                IconButton(onClick = onDelete, modifier = Modifier.size(16.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        tint = Marinho
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                IconButton(
                    onClick = {
                        editando = true
                        textoEdicao = item.nome
                    },
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = Marinho
                    )
                }
            }
        }

        Text(
            text = item.dataHora,
            textAlign = TextAlign.Start,
            style = Typography.labelSmall
        )
    }
}

@Composable
fun Titulo(text: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.Start) {
        Text(
            text = text,
            modifier = Modifier.padding(bottom = 8.dp),
            style = Typography.headlineLarge
        )
        HorizontalDivider(thickness = 2.dp, color = Coral)
    }
}

data class ItemCompra(
    val nome: String,
    val comprado: Boolean,
    val dataHora: String = "Segunda-feira (31/10/2022) às 08:30"
)