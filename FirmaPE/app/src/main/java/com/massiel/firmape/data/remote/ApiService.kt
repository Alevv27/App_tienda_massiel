package com.massiel.firmape.data.remote

import com.massiel.firmape.data.model.dto.AuthRequest
import com.massiel.firmape.data.model.dto.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body req: AuthRequest): AuthResponse
}
