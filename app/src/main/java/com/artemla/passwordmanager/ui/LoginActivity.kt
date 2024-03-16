package com.artemla.passwordmanager.ui

import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.artemla.passwordmanager.R
import com.artemla.passwordmanager.databinding.ActivityLoginBinding
import com.artemla.passwordmanager.domain.utils.CryptoManager
import com.artemla.passwordmanager.domain.utils.PreferencesUtils

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    startMain()
                    finish()
                }
            }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = PreferencesUtils.getInstance(context = applicationContext)
        startIntro(preferences.isFirstLaunch())
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoginBtnText(preferences)
        passwordHandler(preferences)
        setFingerprintBtn(preferences)
    }

    private fun setLoginBtnText(preferences: PreferencesUtils) {
        if (!preferences.hasMasterPassword()) {
            binding.loginBtn.text = getString(R.string.create_password)
        }
    }

    private fun passwordHandler(preferences: PreferencesUtils) {
        val cryptoManager = CryptoManager()
        binding.loginBtn.setOnClickListener {
            if (preferences.hasMasterPassword()) {
                if (binding.loginPassword.text.toString().trim() == cryptoManager.decrypt(
                        preferences.getMasterPassword()!!
                    )
                ) {
                    startMain()
                    finish()
                } else {
                    binding.loginPasswordWarning.text = getString(R.string.wrong_password)
                }
            } else {
                if (binding.loginPassword.text.toString().trim().length < 6) {
                    binding.loginPasswordWarning.text =
                        getString(R.string.password_symbols_warning)
                } else {
                    preferences.setMasterPassword(
                        cryptoManager.encrypt(
                            binding.loginPassword.text.toString().trim()
                        )
                    )
                    startMain()
                    finish()
                }
            }
        }
    }

    private fun setFingerprintBtn(preferences: PreferencesUtils) {
        if (!checkBiometricSupport()) {
            preferences.setFingerprintAvailable(false)
        }
        if (!preferences.getFingerprintState() || !checkBiometricSupport()) {
            binding.loginFingerprint.visibility = View.GONE
        } else {
            setFingerprintBtnListener()
        }
    }

    private fun setFingerprintBtnListener() {
        binding.loginFingerprint.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Fingerprint authentication")
                .setSubtitle("Authentication is required")
                .setDescription("This app uses fingerprint for the swift and secure access to password storage")
                .setNegativeButton("Cancel",
                    this.mainExecutor
                ) { _, _ -> }
                .build()

            biometricPrompt.authenticate(
                getCancellationSignal(),
                mainExecutor,
                authenticationCallback
            )
        }
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            binding.loginFingerprint.visibility = View.GONE
            return false
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                this,
                "Fingerprint authentication is not permitted by your device",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }

        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }

    private fun startMain() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
    }

    private fun startIntro(firstLaunch: Boolean) {
        if (firstLaunch) {
            intent = Intent(applicationContext, PasswordAppIntro::class.java)
            startActivity(intent)
        }
    }
}