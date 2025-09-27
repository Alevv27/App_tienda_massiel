package com.massiel.firmape.data.repo

import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.data.remote.RetrofitClient

class AuthRepository {
    private val api = RetrofitClient.api()

    suspend fun login(email:String, password:String): Usuario? {
        val auth = api.auth().credenciales.find { it.email==email && it.password==password } ?: return null
        val usuarios = api.usuarios().usuarios
        return usuarios.find { it.id == auth.usuarioId }
    }
}