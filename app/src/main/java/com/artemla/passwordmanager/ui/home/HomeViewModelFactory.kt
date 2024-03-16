package com.artemla.passwordmanager.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.artemla.passwordmanager.GetPasswordsUseCase
import com.artemla.passwordmanager.PasswordRepository
import com.artemla.passwordmanager.PasswordsDao
import com.artemla.passwordmanager.db.PasswordDB

class HomeViewModelFactory(context: Context): ViewModelProvider.Factory {
    private val passwordRepository by lazy(LazyThreadSafetyMode.NONE) {
        val db = PasswordDB.getDatabase(context)
        PasswordRepository(db.passwordsDao())
    }

    private val getPasswordsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetPasswordsUseCase(passwordRepository)
    }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            getPasswordsUseCase
        ) as T
    }
}