package com.massiel.firmape.data.repo

import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.data.model.dto.AuthRequest
import com.massiel.firmape.data.remote.RetrofitClient
import retrofit2.HttpException
import java.io.IOException

class AuthRepository {
    private val api = RetrofitClient.api()

    suspend fun login(email: String, password: String): Result<Usuario> {
        return try {
            val resp = api.login(AuthRequest(email, password))
            if (resp.success && resp.usuario != null) {
                Result.success(resp.usuario)
            } else {
                Result.failure(IllegalArgumentException(resp.error ?: "Credenciales inválidas"))
            }
        } catch (e: HttpException) {
            // Mapea códigos del backend a mensajes claros
            val msg = when (e.code()) {
                404 -> "Usuario no encontrado"
                401 -> "Credenciales inválidas"
                else -> "Error de servidor (${e.code()})"
            }
            Result.failure(Exception(msg))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión a Internet"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

