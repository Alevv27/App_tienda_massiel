package com.massiel.firmape.data.model.dto

data class UsuarioUpdateRequest(
    val nombre: String? = null,
    val email: String? = null,
    val password: String? = null,
    val perfil_id: Int? = null,
    val empresa_id: Int? = null,
    val activo: Boolean? = null
)
