package com.artemla.passwordmanager.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artemla.passwordmanager.data.db.PasswordDB
import com.artemla.passwordmanager.data.repositories.PasswordRepositoryImpl
import com.artemla.passwordmanager.domain.usecases.GetPasswordsUseCase

class HomeViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val passwordRepositoryImpl by lazy(LazyThreadSafetyMode.NONE) {
        val db = PasswordDB.getDatabase(context)
        PasswordRepositoryImpl(db.passwordsDao())
    }

    private val getPasswordsUseCase by lazy(LazyThreadSafetyMode.NONE) {
        GetPasswordsUseCase(passwordRepositoryImpl)
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(
            getPasswordsUseCase
        ) as T
    }
}