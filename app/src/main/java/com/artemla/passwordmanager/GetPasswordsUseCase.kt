package com.artemla.passwordmanager

import com.artemla.passwordmanager.dt.Password
import kotlinx.coroutines.flow.Flow

class GetPasswordsUseCase(private val passwordRepository: PasswordRepository) {
    fun getData(): Flow<List<Password>>{
        return passwordRepository.data
    }

    suspend fun addPassword(password: Password) {
        passwordRepository.add(password)
    }

    suspend fun deletePassword(password: Password) {
        passwordRepository.delete(password)
    }

    suspend fun getPasswordById(id: Int): Password {
        return passwordRepository.getPasswordById(id)
    }
}