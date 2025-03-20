package com.app.supercompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.supercompras.ui.theme.SuperComprasTheme

class MainActivity : ComponentActivity() {
    private val viewModel: ListaComprasViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SuperComprasTheme {
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
        
        Titulo(text = "Lista de Compras")
        
        // Lista de itens não comprados
        ListaItens(
            itens = itens.filter { !it.comprado },
            onCheckChange = { item -> viewModel.alterarStatusItem(item) },
            onDelete = { item -> viewModel.excluirItem(item) },
            onEdit = { item, novoNome -> viewModel.editarItem(item, novoNome) }
        )
        
        if (itens.any { it.comprado }) {
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
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF55B64)
            )
        ) {
            Text("Salvar Item")
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
    Column {
        itens.forEach { item ->
            ItemLista(
                item = item,
                onCheckChange = { onCheckChange(item) },
                onDelete = { onDelete(item) },
                onEdit = { novoNome -> onEdit(item, novoNome) }
            )
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

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Checkbox(
                checked = item.comprado,
                onCheckedChange = { onCheckChange() },
                modifier = Modifier.padding(end = 8.dp)
            )
            
            if (editando) {
                OutlinedTextField(
                    value = textoEdicao,
                    onValueChange = { textoEdicao = it },
                    modifier = Modifier
                        .weight(1f)
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
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                Text(
                    text = item.nome,
                    modifier = Modifier.weight(1f)
                )
                
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Excluir",
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                IconButton(
                    onClick = { 
                        editando = true
                        textoEdicao = item.nome
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        
        Text(
            text = item.dataHora,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(start = 40.dp)
        )
    }
}

@Composable
fun Titulo(text: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.Start) {
        val coral = Color(0xFFF55B64)
        Text(
            text = text,
            color = coral,
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HorizontalDivider(thickness = 2.dp, color = coral)
    }
}

data class ItemCompra(
    val nome: String,
    val comprado: Boolean,
    val dataHora: String = "Segunda-feira (31/10/2022) às 08:30"
)