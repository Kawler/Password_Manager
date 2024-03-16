package com.artemla.passwordmanager.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artemla.passwordmanager.data.dao.PasswordsDao
import com.artemla.passwordmanager.domain.entities.Password

@Database(entities = [Password::class], version = 2)
abstract class PasswordDB : RoomDatabase() {
    abstract fun passwordsDao(): PasswordsDao

    companion object {
        @Volatile
        private var INSTANCE: PasswordDB? = null

        fun getDatabase(context: Context): PasswordDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PasswordDB::class.java,
                    "passwords"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}