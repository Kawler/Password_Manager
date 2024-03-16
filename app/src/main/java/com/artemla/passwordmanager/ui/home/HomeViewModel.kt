package com.artemla.passwordmanager.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.artemla.passwordmanager.GetPasswordsUseCase
import com.artemla.passwordmanager.dt.Password

class HomeViewModel(private val getPasswordsUseCase: GetPasswordsUseCase) : ViewModel() {

    fun getData(): LiveData<List<Password>> {
        return getPasswordsUseCase.getData().asLiveData()
    }

    suspend fun deletePassword(password: Password){
        getPasswordsUseCase.deletePassword(password)
    }

    suspend fun addPassword(password: Password){
        getPasswordsUseCase.addPassword(password)
    }

    suspend fun getPasswordById(id: Int): Password {
        return getPasswordsUseCase.getPasswordById(id)
    }

}