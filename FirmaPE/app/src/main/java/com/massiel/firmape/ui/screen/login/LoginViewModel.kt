package com.massiel.firmape.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginState(
    val loading: Boolean = false,
    val user: Usuario? = null,
    val error: String? = null
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase()
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun login(email: String, pass: String) {
        _state.value = LoginState(loading = true)
        viewModelScope.launch {
            val result = loginUseCase(email, pass)
            _state.value = result.fold(
                onSuccess = { LoginState(user = it) },
                onFailure = { LoginState(error = it.message ?: "Error de login") }
            )
        }
    }
}
