package com.artemla.passwordmanager.domain.utils

import android.content.Context
import android.content.SharedPreferences


class PreferencesUtils private constructor(context: Context) {
    private var preferences: SharedPreferences
    private val MASTER_PASSWORD_KEY = "master_password"
    private val PREFERENCES_NAME = "settings"
    private val FIRST_LAUNCH = "first_launch"
    private val FINGERPRINT_STATE = "fingerprint_state"
    private val FINGERPRINT_AVAILABLE = "fingerprint_available"

    companion object {
        private var instance: PreferencesUtils? = null
        private lateinit var editor: SharedPreferences.Editor

        fun getInstance(context: Context): PreferencesUtils {
            if (instance == null) {
                synchronized(PreferencesUtils::class.java) {
                    if (instance == null) {
                        instance = PreferencesUtils(context)
                    }
                }
            }
            return instance!!
        }
    }

    init {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun isFirstLaunch(): Boolean {
        return if (preferences.contains(FIRST_LAUNCH)) {
            false
        } else {
            setFirstLaunch()
            true
        }
    }

    fun setMasterPassword(password: String) {
        editor = preferences.edit()
        editor.putString(MASTER_PASSWORD_KEY, password).apply()
    }

    fun getMasterPassword(): String? {
        return preferences.getString(MASTER_PASSWORD_KEY, null)
    }

    fun hasMasterPassword(): Boolean {
        return preferences.contains(MASTER_PASSWORD_KEY)
    }

    fun setFingerprintState(state: Boolean) {
        editor = preferences.edit()
        editor.putBoolean(FINGERPRINT_STATE, state).apply()
    }

    fun getFingerprintState(): Boolean {
        return preferences.getBoolean(FINGERPRINT_STATE, true)
    }

    fun setFingerprintAvailable(state: Boolean) {
        editor = preferences.edit()
        editor.putBoolean(FINGERPRINT_AVAILABLE, state).apply()
    }

    fun getFingerprintAvailable(): Boolean {
        return preferences.getBoolean(FINGERPRINT_AVAILABLE, true)
    }

    private fun setFirstLaunch() {
        editor = preferences.edit()
        editor.putBoolean(FIRST_LAUNCH, true).apply()
    }
}