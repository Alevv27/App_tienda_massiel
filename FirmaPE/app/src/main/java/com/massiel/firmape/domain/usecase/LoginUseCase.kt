package com.massiel.firmape.domain.usecase

import com.massiel.firmape.data.model.Usuario
import com.massiel.firmape.data.repo.AuthRepository

class LoginUseCase(private val repo: AuthRepository = AuthRepository()) {
    suspend operator fun invoke(email:String, pass:String): Usuario? = repo.login(email, pass)
}