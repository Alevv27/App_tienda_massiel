package com.massiel.firmape.data.model.dto

import com.massiel.firmape.data.model.Usuario

data class UsuariosResponse(
    val ok: Boolean,
    val usuarios: List<Usuario>
)
