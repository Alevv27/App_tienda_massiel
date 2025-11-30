package com.massiel.firmape.data.model

data class Usuario(
    val id: Long,
    val nombre: String,
    val email: String,
    val perfil: String,
    val empresaId: Int? = null,
    val activo: Boolean? = null
)
