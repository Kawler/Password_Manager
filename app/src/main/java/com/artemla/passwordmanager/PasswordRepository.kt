package com.artemla.passwordmanager

import androidx.annotation.WorkerThread
import com.artemla.passwordmanager.dt.Password
import kotlinx.coroutines.flow.Flow

class PasswordRepository(private val passwordsDao: PasswordsDao) {
    val data: Flow<List<Password>> = passwordsDao.getAll()

    @WorkerThread
    suspend fun add(password: Password) {
        passwordsDao.addPassword(password)
    }

    @WorkerThread
    suspend fun delete(password: Password){
        passwordsDao.delete(password)
    }

    @WorkerThread
    suspend fun getPasswordById(id: Int): Password{
        return passwordsDao.getPasswordById(id)
    }
}