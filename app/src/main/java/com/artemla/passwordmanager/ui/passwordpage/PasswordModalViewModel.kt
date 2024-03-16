package com.artemla.passwordmanager.ui.passwordpage

import androidx.lifecycle.ViewModel
import com.artemla.passwordmanager.domain.entities.Password
import com.artemla.passwordmanager.domain.usecases.GetPasswordsUseCase
import com.artemla.passwordmanager.domain.utils.CryptoManager

class PasswordModalViewModel(private val getPasswordsUseCase: GetPasswordsUseCase) : ViewModel() {

    fun generatePassword(): String {
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf(
            '!',
            '@',
            '#',
            '$',
            '%',
            '^',
            '&',
            '*',
            '(',
            ')',
            '-',
            '_'
        )
        return (1..15)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    suspend fun addNewPasswordToDatabase(website: String, login: String, password: String) {
        val cryptoManager = CryptoManager()
        getPasswordsUseCase.addPassword(
            Password(
                id = null,
                website = website.trim(),
                login = cryptoManager.encrypt(login.trim()),
                password = cryptoManager.encrypt(password.trim())
            )
        )
    }

    suspend fun updatePasswordInDatabase(
        password: Password,
        website: String,
        login: String,
        newPassword: String
    ) {
        val cryptoManager = CryptoManager()
        getPasswordsUseCase.updatePassword(
            Password(
                id = password.id,
                website = website.trim(),
                login = cryptoManager.encrypt(login.trim()),
                password = cryptoManager.encrypt(newPassword.trim())
            )
        )
    }
}