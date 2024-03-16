package com.artemla.passwordmanager.data.repositories

import androidx.annotation.WorkerThread
import com.artemla.passwordmanager.data.dao.PasswordsDao
import com.artemla.passwordmanager.domain.entities.Password
import com.artemla.passwordmanager.domain.repositories.PasswordRepository
import kotlinx.coroutines.flow.Flow

class PasswordRepositoryImpl(private val passwordsDao: PasswordsDao) : PasswordRepository {
    val data: Flow<Array<Password>> = passwordsDao.getAll()

    @WorkerThread
    override suspend fun add(password: Password) {
        passwordsDao.addPassword(password)
    }

    @WorkerThread
    override suspend fun delete(password: Password) {
        passwordsDao.delete(password)
    }

    @WorkerThread
    override suspend fun update(password: Password) {
        passwordsDao.updatePassword(password)
    }
}