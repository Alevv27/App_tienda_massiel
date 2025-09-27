package com.massiel.firmape.data.repo

import com.massiel.firmape.data.model.Documento
import com.massiel.firmape.data.remote.RetrofitClient

class DocsRepository {
    private val api = RetrofitClient.api()

    suspend fun documentos(empresaId:Int, estado:String?): List<Documento> {
        val all = api.documentos().documentos
        return all.filter { it.empresaId==empresaId && (estado==null || it.estado==estado) }
            .sortedByDescending { it.fechaRegistro }
    }
}