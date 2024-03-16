package com.artemla.passwordmanager

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.artemla.passwordmanager.dt.Password
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordsDao {
    @Query("SELECT * FROM Password")
    fun getAll(): Flow<List<Password>>

    @Query("SELECT * FROM Password WHERE id == (:passwordId)")
    fun getPasswordById(passwordId: Int): Password

    @Update
    suspend fun updatePassword(password: Password)

    @Insert
    suspend fun addPassword(password: Password)

    @Delete
    suspend fun delete(password: Password)
}