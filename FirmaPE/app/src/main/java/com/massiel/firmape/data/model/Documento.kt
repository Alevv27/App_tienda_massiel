package com.massiel.firmape.data.model

data class Documento(
    val id:Int,
    val titulo:String,
    val tipo:String,
    val sizeMB:Double,
    val estado:String, // PENDIENTE|FIRMADO|RECHAZADO
    val usuarioRegistroId:Int,
    val empresaId:Int,
    val fechaRegistro:String
)