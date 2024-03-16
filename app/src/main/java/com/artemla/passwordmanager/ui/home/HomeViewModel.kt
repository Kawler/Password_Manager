package com.artemla.passwordmanager.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.artemla.passwordmanager.domain.adapters.PasswordsRVAdapter
import com.artemla.passwordmanager.domain.entities.Password
import com.artemla.passwordmanager.domain.usecases.GetPasswordsUseCase
import com.artemla.passwordmanager.domain.utils.CryptoManager

class HomeViewModel(private val getPasswordsUseCase: GetPasswordsUseCase) : ViewModel() {

    fun getData(): LiveData<Array<Password>> {
        return getPasswordsUseCase.getData().asLiveData()
    }

    fun fillAdapter(adapter: PasswordsRVAdapter, array: Array<Password>) {
        val cryptoManager = CryptoManager()

        if (array.isNotEmpty()) {
            val dataArray = Array(array.size) { index ->
                Password(
                    array[index].id,
                    array[index].website,
                    cryptoManager.decrypt(array[index].login),
                    cryptoManager.decrypt(array[index].password)
                )
            }
            adapter.data = dataArray
        } else {
            adapter.data = emptyArray()
        }
    }

}