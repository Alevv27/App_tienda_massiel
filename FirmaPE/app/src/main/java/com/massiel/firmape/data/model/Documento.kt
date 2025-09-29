package com.massiel.firmape.data.model

data class Documento(
    val id: Long,
    val titulo: String,
    val tipo: String,           // Carta | Memo | Boleta | Otros
    val sizeBytes: Long,
    val estado: String,         // PENDIENTE | FIRMADO | RECHAZADO
    val path: String,           // ruta privada absoluta en filesDir
    val fileName: String,       // nombre original
    val fechaRegistro: Long
)