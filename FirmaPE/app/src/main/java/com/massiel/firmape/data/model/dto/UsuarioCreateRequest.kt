package com.massiel.firmape.data.model.dto

data class UsuarioCreateRequest(
    val nombre: String,
    val email: String,
    val password: String,
    val perfil_id: Int,
    val empresa_id: Int
)
