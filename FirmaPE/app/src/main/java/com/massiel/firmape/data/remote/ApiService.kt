package com.massiel.firmape.data.remote

import com.massiel.firmape.data.model.dto.AuthResponse
import com.massiel.firmape.data.model.dto.DocumentosResponse
import com.massiel.firmape.data.model.dto.EmpresasResponse
import com.massiel.firmape.data.model.dto.UsuariosResponse
import retrofit2.http.GET

interface ApiService {
    @GET("api/usuarios.json") suspend fun usuarios(): UsuariosResponse
    @GET("api/empresas.json") suspend fun empresas(): EmpresasResponse
    @GET("api/documentos.json") suspend fun documentos(): DocumentosResponse
    @GET("api/auth.json") suspend fun auth(): AuthResponse
}