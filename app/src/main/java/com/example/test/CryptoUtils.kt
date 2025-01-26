package com.example.test

import android.content.Context
import android.util.Log
import java.security.*
import java.security.spec.ECGenParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.util.Base64

object CryptoUtils {

    private const val EC_ALGORITHM = "EC"
    private const val ECDH_ALGORITHM = "ECDH"
    private const val CURVE_NAME = "secp256r1"
    private const val AES_ALGORITHM = "AES/GCM/NoPadding"
    private const val AES_KEY_SIZE = 16 // 128 bits
    private const val IV_SIZE = 12 // 96 bits for GCM

    // Генерація пари ключів EC
    fun generateECKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance(EC_ALGORITHM)
        val ecSpec = ECGenParameterSpec(CURVE_NAME)
        keyPairGenerator.initialize(ecSpec, SecureRandom())
        return keyPairGenerator.generateKeyPair()
    }

    // Сериалізація відкритого ключа в Base64 строку
    fun serializePublicKey(publicKey: PublicKey): String {
        return Base64.getEncoder().encodeToString(publicKey.encoded)
    }

    // Десериалізація Base64 строки у відкритий ключ
    fun deserializePublicKey(pem: String): PublicKey {
        val keyBytes = Base64.getDecoder().decode(pem)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance(EC_ALGORITHM)
        return keyFactory.generatePublic(keySpec)
    }

    // Генерація спільного секрету з приватного і відкритого ключів
    fun generateSharedSecret(privateKey: PrivateKey, publicKey: PublicKey): ByteArray {
        val keyAgreement = KeyAgreement.getInstance(ECDH_ALGORITHM)
        keyAgreement.init(privateKey)
        keyAgreement.doPhase(publicKey, true)
        val fullSharedSecret = keyAgreement.generateSecret()
        Log.d("CryptoUtils", "Generated shared secret (full): ${Base64.getEncoder().encodeToString(fullSharedSecret)}")
        val truncatedSharedSecret = fullSharedSecret.copyOfRange(0, AES_KEY_SIZE) // Обрізка до 16 байт для AES-128
        Log.d("CryptoUtils", "Truncated shared secret: ${Base64.getEncoder().encodeToString(truncatedSharedSecret)}")
        return truncatedSharedSecret
    }

    // Шифрування повідомлення з використанням AES-GCM
    fun encryptMessage(sharedSecret: ByteArray, message: String): String {
        val iv = ByteArray(IV_SIZE)
        SecureRandom().nextBytes(iv)
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        val keySpec = SecretKeySpec(sharedSecret, "AES")
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
        val ciphertext = cipher.doFinal(message.toByteArray())
        val encryptedMessage = iv + ciphertext
        val encoded = Base64.getEncoder().encodeToString(encryptedMessage)
        Log.d("CryptoUtils", "IV: ${Base64.getEncoder().encodeToString(iv)}")
        Log.d("CryptoUtils", "Ciphertext: ${Base64.getEncoder().encodeToString(ciphertext)}")
        Log.d("CryptoUtils", "Encrypted message: $encoded")
        return encoded
    }

    // Дешифрування повідомлення з використанням AES-GCM
    fun decryptMessage(sharedSecret: ByteArray, encryptedMessage: String): String {
        Log.d("CryptoUtils", "Decrypting message: $encryptedMessage")
        val encryptedBytes = Base64.getDecoder().decode(encryptedMessage)
        val iv = encryptedBytes.copyOfRange(0, IV_SIZE)
        val ciphertext = encryptedBytes.copyOfRange(IV_SIZE, encryptedBytes.size)
        Log.d("CryptoUtils", "IV: ${Base64.getEncoder().encodeToString(iv)}")
        Log.d("CryptoUtils", "Ciphertext: ${Base64.getEncoder().encodeToString(ciphertext)}")
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        val keySpec = SecretKeySpec(sharedSecret, "AES")
        val gcmSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
        val plainText = cipher.doFinal(ciphertext)
        return String(plainText)
    }

    // Отримання приватного ключа з SharedPreferences
    fun getPrivateKey(context: Context): PrivateKey? {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val privateKeyString = sharedPreferences.getString("privateKey", null) ?: return null
        val privateKeyBytes = Base64.getDecoder().decode(privateKeyString)

        val keyFactory = KeyFactory.getInstance(EC_ALGORITHM)
        val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
        return keyFactory.generatePrivate(privateKeySpec)
    }
}

//package com.example.test
//
//import android.content.Context
//import android.util.Log
//import java.security.*
//import java.security.spec.ECGenParameterSpec
//import java.security.spec.PKCS8EncodedKeySpec
//import java.security.spec.X509EncodedKeySpec
//import javax.crypto.Cipher
//import javax.crypto.KeyAgreement
//import javax.crypto.spec.GCMParameterSpec
//import javax.crypto.spec.SecretKeySpec
//import java.util.Base64
//
//object CryptoUtils {
//
//    private const val EC_ALGORITHM = "EC"
//    private const val ECDH_ALGORITHM = "ECDH"
//    private const val CURVE_NAME = "secp256r1"
//    private const val AES_ALGORITHM = "AES/GCM/NoPadding"
//
//    fun generateECKeyPair(): KeyPair {
//        val keyPairGenerator = KeyPairGenerator.getInstance(EC_ALGORITHM)
//        val ecSpec = ECGenParameterSpec(CURVE_NAME)
//        keyPairGenerator.initialize(ecSpec, SecureRandom())
//        return keyPairGenerator.generateKeyPair()
//    }
//
//    fun serializePublicKey(publicKey: PublicKey): String {
//        return Base64.getEncoder().encodeToString(publicKey.encoded)
//    }
//
//    fun deserializePublicKey(pem: String): PublicKey {
//        val keyBytes = Base64.getDecoder().decode(pem)
//        val keySpec = X509EncodedKeySpec(keyBytes)
//        val keyFactory = KeyFactory.getInstance(EC_ALGORITHM)
//        return keyFactory.generatePublic(keySpec)
//    }
//
//    fun generateSharedSecret(privateKey: PrivateKey, publicKey: PublicKey): ByteArray {
//        val keyAgreement = KeyAgreement.getInstance(ECDH_ALGORITHM)
//        keyAgreement.init(privateKey)
//        keyAgreement.doPhase(publicKey, true)
//        val sharedSecret = keyAgreement.generateSecret()
//        Log.d("CryptoUtils", "Generated shared secret (full): ${Base64.getEncoder().encodeToString(sharedSecret)}")
//        val truncatedSecret = sharedSecret.copyOfRange(0, 16) // Truncate to 16 bytes for AES-128
//        Log.d("CryptoUtils", "Truncated shared secret: ${Base64.getEncoder().encodeToString(truncatedSecret)}")
//        return truncatedSecret
//    }
//
//    fun encryptMessage(sharedSecret: ByteArray, message: String): String {
//        val iv = ByteArray(12)
//        SecureRandom().nextBytes(iv)
//        val cipher = Cipher.getInstance(AES_ALGORITHM)
//        val keySpec = SecretKeySpec(sharedSecret, "AES")
//        val gcmSpec = GCMParameterSpec(128, iv)
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
//        val ciphertext = cipher.doFinal(message.toByteArray())
//        val encryptedMessage = iv + ciphertext
//        val encoded = Base64.getEncoder().encodeToString(encryptedMessage)
//        Log.d("CryptoUtils", "IV: ${Base64.getEncoder().encodeToString(iv)}")
//        Log.d("CryptoUtils", "Ciphertext: ${Base64.getEncoder().encodeToString(ciphertext)}")
//        Log.d("CryptoUtils", "Encrypted message: $encoded")
//        return encoded
//    }
//
//    fun decryptMessage(sharedSecret: ByteArray, encryptedMessage: String): String {
//        Log.d("CryptoUtils", "Decrypting message: $encryptedMessage")
//        val encryptedBytes = Base64.getDecoder().decode(encryptedMessage)
//        val iv = encryptedBytes.copyOfRange(0, 12)
//        val ciphertext = encryptedBytes.copyOfRange(12, encryptedBytes.size)
//        Log.d("CryptoUtils", "IV: ${Base64.getEncoder().encodeToString(iv)}")
//        Log.d("CryptoUtils", "Ciphertext: ${Base64.getEncoder().encodeToString(ciphertext)}")
//        val cipher = Cipher.getInstance(AES_ALGORITHM)
//        val keySpec = SecretKeySpec(sharedSecret, "AES")
//        val gcmSpec = GCMParameterSpec(128, iv)
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
//        val plainText = cipher.doFinal(ciphertext)
//        return String(plainText)
//    }
//
//    fun getPrivateKey(context: Context): PrivateKey? {
//        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val privateKeyString = sharedPreferences.getString("privateKey", null) ?: return null
//        val privateKeyBytes = Base64.getDecoder().decode(privateKeyString)
//
//        val keyFactory = KeyFactory.getInstance(EC_ALGORITHM)
//        val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
//        return keyFactory.generatePrivate(privateKeySpec)
//    }
//}

//package com.example.test
//
//import android.content.Context
//import java.security.*
//import java.security.spec.ECGenParameterSpec
//import java.security.spec.PKCS8EncodedKeySpec
//import java.security.spec.X509EncodedKeySpec
//import javax.crypto.Cipher
//import javax.crypto.KeyAgreement
//import javax.crypto.spec.GCMParameterSpec
//import javax.crypto.spec.SecretKeySpec
//import java.util.Base64
//
//object CryptoUtils {
//
//    private const val EC_ALGORITHM = "EC"
//    private const val ECDH_ALGORITHM = "ECDH"
//    private const val CURVE_NAME = "secp256r1"
//
//    fun generateECKeyPair(): KeyPair {
//        val keyPairGenerator = KeyPairGenerator.getInstance(EC_ALGORITHM)
//        val ecSpec = ECGenParameterSpec(CURVE_NAME)
//        keyPairGenerator.initialize(ecSpec, SecureRandom())
//        return keyPairGenerator.generateKeyPair()
//    }
//
//    fun serializePublicKey(publicKey: PublicKey): String {
//        return Base64.getEncoder().encodeToString(publicKey.encoded)
//    }
//
//    fun deserializePublicKey(pem: String): PublicKey {
//        val publicKeyPEM = pem
//            .replace("-----BEGIN PUBLIC KEY-----", "")
//            .replace("-----END PUBLIC KEY-----", "")
//            .replace("\\s".toRegex(), "")
//
//        val keyBytes = Base64.getDecoder().decode(publicKeyPEM)
//        val keySpec = X509EncodedKeySpec(keyBytes)
//        val keyFactory = KeyFactory.getInstance(EC_ALGORITHM)
//        return keyFactory.generatePublic(keySpec)
//    }
//
//    fun generateSharedSecret(privateKey: PrivateKey, publicKey: PublicKey): ByteArray {
//        val keyAgreement = KeyAgreement.getInstance(ECDH_ALGORITHM)
//        keyAgreement.init(privateKey)
//        keyAgreement.doPhase(publicKey, true)
//        return keyAgreement.generateSecret()
//    }
//
//    fun encryptMessage(sharedSecret: ByteArray, message: String): String {
//        val iv = ByteArray(12)
//        SecureRandom().nextBytes(iv)
//        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//        val keySpec = SecretKeySpec(sharedSecret, 0, 16, "AES")
//        val gcmSpec = GCMParameterSpec(128, iv)
//        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec)
//        val ciphertext = cipher.doFinal(message.toByteArray())
//        val encryptedMessage = iv + ciphertext
//        return Base64.getEncoder().encodeToString(encryptedMessage)
//    }
//
//    fun decryptMessage(sharedSecret: ByteArray, encryptedMessage: String): String {
//        val encryptedBytes = Base64.getDecoder().decode(encryptedMessage)
//        val iv = encryptedBytes.copyOfRange(0, 12)
//        val ciphertext = encryptedBytes.copyOfRange(12, encryptedBytes.size)
//        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//        val keySpec = SecretKeySpec(sharedSecret, 0, 16, "AES")
//        val gcmSpec = GCMParameterSpec(128, iv)
//        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec)
//        val plainText = cipher.doFinal(ciphertext)
//        return String(plainText)
//    }
//
//    fun getPrivateKey(context: Context): PrivateKey? {
//        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
//        val privateKeyString = sharedPreferences.getString("privateKey", null) ?: return null
//        val privateKeyBytes = Base64.getDecoder().decode(privateKeyString)
//
//        val keyFactory = KeyFactory.getInstance("EC")
//        val privateKeySpec = PKCS8EncodedKeySpec(privateKeyBytes)
//        return keyFactory.generatePrivate(privateKeySpec)
//    }
//}