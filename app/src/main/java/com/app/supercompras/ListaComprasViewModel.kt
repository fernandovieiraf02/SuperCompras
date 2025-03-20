package com.app.supercompras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListaComprasViewModel : ViewModel() {
    private val _itens = MutableStateFlow<List<ItemCompra>>(emptyList())
    val itens: StateFlow<List<ItemCompra>> = _itens

    fun adicionarItem(nome: String) {
        val dataHora = SimpleDateFormat("EEEE (dd/MM/yyyy) 'às' HH:mm", Locale("pt", "BR"))
            .format(Date())
        
        val novoItem = ItemCompra(nome = nome, comprado = false, dataHora = dataHora)
        
        viewModelScope.launch {
            _itens.update { listaAtual ->
                listaAtual + novoItem
            }
        }
    }

    fun alterarStatusItem(item: ItemCompra) {
        viewModelScope.launch {
            _itens.update { listaAtual ->
                listaAtual.map { 
                    if (it == item) it.copy(comprado = !it.comprado)
                    else it
                }
            }
        }
    }

    fun excluirItem(item: ItemCompra) {
        viewModelScope.launch {
            _itens.update { listaAtual ->
                listaAtual.filter { it != item }
            }
        }
    }

    fun editarItem(itemAntigo: ItemCompra, novoNome: String) {
        viewModelScope.launch {
            _itens.update { listaAtual ->
                listaAtual.map { 
                    if (it == itemAntigo) it.copy(nome = novoNome)
                    else it
                }
            }
        }
    }
} 