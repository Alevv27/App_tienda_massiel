package com.massiel.firmape.data.model

data class Usuario(
    val id:Int,
    val nombre:String,
    val email:String,
    val perfil:String,   // ADMIN | GESTOR | FIRMANTE
    val empresaId:Int
)