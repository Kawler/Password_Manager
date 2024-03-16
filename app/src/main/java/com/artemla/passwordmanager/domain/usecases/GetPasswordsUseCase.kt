package com.artemla.passwordmanager.domain.usecases

import com.artemla.passwordmanager.data.repositories.PasswordRepositoryImpl
import com.artemla.passwordmanager.domain.entities.Password
import kotlinx.coroutines.flow.Flow

class GetPasswordsUseCase(private val passwordRepositoryImpl: PasswordRepositoryImpl) {
    fun getData(): Flow<Array<Password>> {
        return passwordRepositoryImpl.data
    }

    suspend fun addPassword(password: Password) {
        passwordRepositoryImpl.add(password)
    }

    suspend fun updatePassword(password: Password) {
        passwordRepositoryImpl.update(password)
    }
}