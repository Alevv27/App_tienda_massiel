package com.massiel.firmape.ui.screen.docs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massiel.firmape.data.model.Documento
import com.massiel.firmape.domain.usecase.ListDocsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DocsState(
    val loading:Boolean=false,
    val items: List<Documento> = emptyList(),
    val error:String? = null
)

class DocsViewModel(
    private val listUse: ListDocsUseCase = ListDocsUseCase()
) : ViewModel() {
    private val _state = MutableStateFlow(DocsState())
    val state: StateFlow<DocsState> = _state

    fun load(empresaId:Int, estado:String?) {
        _state.value = DocsState(loading=true)
        viewModelScope.launch {
            runCatching { listUse(empresaId, estado) }
                .onSuccess { _state.value = DocsState(items=it) }
                .onFailure { _state.value = DocsState(error=it.message) }
        }
    }
}