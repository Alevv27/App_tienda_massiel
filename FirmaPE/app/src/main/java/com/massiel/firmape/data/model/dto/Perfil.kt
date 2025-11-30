package com.massiel.firmape.data.model.dto

data class Perfil(
    val id: Int,
    val codigo: String,
    val administracion: Boolean,
    val gestion: Boolean,
    val firmar: Boolean
)
