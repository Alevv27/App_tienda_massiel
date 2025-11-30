package com.massiel.firmape.data.model.dto

import com.massiel.firmape.data.model.Empresa

data class EmpresasResponse(
    val ok: Boolean,
    val empresas: List<Empresa>
)
