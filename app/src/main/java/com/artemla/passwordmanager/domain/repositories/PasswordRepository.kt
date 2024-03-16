package com.artemla.passwordmanager.domain.repositories

import com.artemla.passwordmanager.domain.entities.Password

interface PasswordRepository {
    suspend fun add(password: Password)
    suspend fun delete(password: Password)

    suspend fun update(password: Password)
}