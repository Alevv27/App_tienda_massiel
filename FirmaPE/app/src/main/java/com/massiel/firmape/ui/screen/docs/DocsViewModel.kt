package com.massiel.firmape.ui.screen.docs

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.massiel.firmape.data.model.Documento
import com.massiel.firmape.data.repo.DocsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DocsLocalState(
    val loading: Boolean = false,
    val items: List<Documento> = emptyList(),
    val toast: String? = null
)

class DocsLocalViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = DocsRepository(app.applicationContext)
    private val _state = MutableStateFlow(DocsLocalState())
    val state: StateFlow<DocsLocalState> = _state

    fun cargar(estado: String?) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, toast = null)
            runCatching { repo.listar(estado) }
                .onSuccess { _state.value = DocsLocalState(items = it) }
                .onFailure { _state.value = DocsLocalState(toast = it.message) }
        }
    }

    fun subir(uri: Uri, titulo: String, tipo: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, toast = null)
            runCatching { repo.subirDesdeUri(uri, titulo, tipo) }
                .onSuccess {
                    cargar(null)
                    _state.value = _state.value.copy(toast = "¡Se subió con éxito!")
                }
                .onFailure { _state.value = _state.value.copy(loading = false, toast = it.message ?: "Error al subir") }
        }
    }

    fun firmar(id: Long,signer: String) = viewModelScope.launch { repo.cambiarEstado(id, "FIRMADO",signerName = signer); cargar(null) }
    fun rechazar(id: Long,signer: String) = viewModelScope.launch { repo.cambiarEstado(id, "RECHAZADO",signerName = signer); cargar(null) }
    fun eliminar(id: Long) = viewModelScope.launch { repo.eliminar(id); cargar(null) }
}