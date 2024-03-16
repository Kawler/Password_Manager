package com.artemla.passwordmanager.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.artemla.passwordmanager.PasswordsDao
import com.artemla.passwordmanager.dt.Password

@Database(entities = [Password::class], version = 1)
abstract class PasswordDB: RoomDatabase() {
    abstract fun passwordsDao(): PasswordsDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PasswordDB? = null

        fun getDatabase(context: Context): PasswordDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
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