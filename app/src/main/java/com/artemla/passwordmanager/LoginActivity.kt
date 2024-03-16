package com.artemla.passwordmanager

import android.app.KeyguardManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.artemla.passwordmanager.databinding.ActivityLoginBinding

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
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent = Intent(this, PasswordAppIntro::class.java)
        startActivity(intent)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferences = PreferencesUtils.getInstance(context = applicationContext)

        if (!preferences.hasMasterPassword()) {
            binding.loginBtn.text = "Create password"
        }
        val cryptoManager = CryptoManager()
        binding.loginBtn.setOnClickListener {
            if (preferences.hasMasterPassword()) {
                if (binding.loginPassword.text.toString().trim() == cryptoManager.decrypt(preferences.getMasterPassword()!!)){
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    binding.loginPasswordWarning.text = "Wrong password"
                }
            } else {
                if (binding.loginPassword.text.toString().trim().length <6) {
                    binding.loginPasswordWarning.text = "Password should contain more than 5 characters"
                } else {
                    preferences.setMasterPassword(cryptoManager.encrypt(binding.loginPassword.text.toString().trim()))
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        if(!preferences.getFingerprintState() || !checkBiometricSupport()){
            binding.loginFingerprint.visibility = View.GONE
            preferences.setFingerprintAvailable(false)
        }

        binding.loginFingerprint.setOnClickListener {
            val biometricPrompt = BiometricPrompt.Builder(this)
                .setTitle("Fingerprint authentication")
                .setSubtitle("Authentication is required")
                .setDescription("This app uses fingerprint for the swift and secure access to password storage")
                .setNegativeButton("Cancel",
                    this.mainExecutor,
                    DialogInterface.OnClickListener { _, _ ->  })
                .build()

            biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"Fingerprint authentication is not permitted by your device", Toast.LENGTH_SHORT).show()
            return false
        }

        return packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
    }
}