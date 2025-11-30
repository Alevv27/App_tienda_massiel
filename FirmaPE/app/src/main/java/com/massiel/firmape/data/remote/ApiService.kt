package com.massiel.firmape.data.remote

import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.data.model.dto.AdminCommonResponse
import com.massiel.firmape.data.model.dto.AuthRequest
import com.massiel.firmape.data.model.dto.AuthResponse
import com.massiel.firmape.data.model.dto.EmpresasResponse
import com.massiel.firmape.data.model.dto.PerfilesResponse
import com.massiel.firmape.data.model.dto.UsuarioCreateRequest
import com.massiel.firmape.data.model.dto.UsuarioUpdateRequest
import com.massiel.firmape.data.model.dto.UsuariosResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body req: AuthRequest): AuthResponse
    // ===== ADMIN: Usuarios =====
    @GET("api/usuarios")
    suspend fun getUsuarios(): UsuariosResponse

    @POST("api/usuarios")
    suspend fun createUsuario(@Body body: UsuarioCreateRequest): Usuario

    @PATCH("api/usuarios/{id}")
    suspend fun updateUsuario(
        @Path("id") id: Long,
        @Body body: UsuarioUpdateRequest
    ): Usuario

    @DELETE("api/usuarios/{id}")
    suspend fun deleteUsuario(@Path("id") id: Long): AdminCommonResponse

    // ===== ADMIN: Perfiles y Empresas =====
    @GET("api/perfiles")
    suspend fun getPerfiles(): PerfilesResponse

    @GET("api/empresas")
    suspend fun getEmpresas(): EmpresasResponse
}
