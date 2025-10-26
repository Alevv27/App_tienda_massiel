package com.massiel.firmape.data.model.dto

import com.google.gson.annotations.SerializedName
import com.massiel.firmape.data.model.Usuario

data class AuthResponse(
    @SerializedName("ok") val success: Boolean,   // <- mapea 'ok' del backend a 'success'
    val usuario: Usuario? = null,
    val error: String? = null
)
