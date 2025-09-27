package com.massiel.firmape.domain.usecase

import com.massiel.firmape.data.model.Documento
import com.massiel.firmape.data.repo.DocsRepository

class ListDocsUseCase(private val repo: DocsRepository = DocsRepository()) {
    suspend operator fun invoke(empresaId:Int, estado:String?): List<Documento> =
        repo.documentos(empresaId, estado)
}