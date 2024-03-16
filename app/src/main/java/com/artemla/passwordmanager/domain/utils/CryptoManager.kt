package com.artemla.passwordmanager.domain.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

class CryptoManager {

    private val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private fun getKey(): SecretKey {
        val existingKey = keyStore.getEntry("secret", null) as? KeyStore.SecretKeyEntry
        return existingKey?.secretKey ?: createKey()
    }

    private fun createKey(): SecretKey {
        return KeyGenerator.getInstance(ALGORITHM).apply {
            init(
                KeyGenParameterSpec.Builder(
                    "secret",
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                    .setBlockModes(BLOCK_MODE)
                    .setEncryptionPaddings(PADDING)
                    .setUserAuthenticationRequired(false)
                    .setRandomizedEncryptionRequired(true)
                    .build()
            )
        }.generateKey()
    }

    fun encrypt(input: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = getKey()
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val iv = cipher.iv
        val encryptedBytes = cipher.doFinal(input.toByteArray())
        return Base64.getEncoder().encodeToString(combineByteArrays(iv, encryptedBytes))
    }

    fun decrypt(input: String): String {
        val decodedInput = Base64.getDecoder().decode(input)
        val iv = extractIV(decodedInput)
        val encryptedBytes = extractEncryptedBytes(decodedInput)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val secretKey = getKey()
        val ivParams = IvParameterSpec(iv)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams)

        return String(cipher.doFinal(encryptedBytes))
    }

    private fun extractIV(combined: ByteArray): ByteArray {
        val iv = ByteArray(16)
        System.arraycopy(combined, 0, iv, 0, 16)
        return iv
    }

    private fun extractEncryptedBytes(combined: ByteArray): ByteArray {
        val encryptedBytes = ByteArray(combined.size - 16)
        System.arraycopy(combined, 16, encryptedBytes, 0, combined.size - 16)
        return encryptedBytes
    }

    private fun combineByteArrays(array1: ByteArray, array2: ByteArray): ByteArray {
        val combined = ByteArray(array1.size + array2.size)
        System.arraycopy(array1, 0, combined, 0, array1.size)
        System.arraycopy(array2, 0, combined, array1.size, array2.size)
        return combined
    }

    companion object {
        private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
    }

}