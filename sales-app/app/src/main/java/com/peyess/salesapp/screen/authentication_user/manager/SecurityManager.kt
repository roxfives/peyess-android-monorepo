package com.peyess.salesapp.screen.authentication_user.manager

import android.util.Base64
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.spec.KeySpec
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// https://stackoverflow.com/questions/23374233/which-hashing-algorithms-are-available-in-android
object SecurityManager {
    private const val ITERATIONS = 1000

    val appropriateHash: HashMethod?
        get() {
            var method: HashMethod? = null

            if (isPBKDFAvailable) {
                method = HashMethod.PBKDF2
            } else if (isDigestAvailable(HashMethod.SHA512.hashString)) {
                method = HashMethod.SHA512
            } else if (isDigestAvailable(HashMethod.SHA384.hashString)) {
                method = HashMethod.SHA384
            } else if (isDigestAvailable(HashMethod.SHA256.hashString)) {
                method = HashMethod.SHA256
            } else if (isDigestAvailable(HashMethod.SHA1.hashString)) {
                method = HashMethod.SHA1
            }

            return method
        }

    private val isPBKDFAvailable: Boolean
        get() {
            try {
                SecretKeyFactory.getInstance(HashMethod.PBKDF2.hashString)
            } catch (notAvailable: Exception) {
                Timber.w("PBKDF2 not available")

                return false
            }

            return true
        }

    private fun isDigestAvailable(method: String): Boolean {
        try {
            MessageDigest.getInstance(method)
        } catch (notAvailable: Exception) {
            Timber.w("Digest not available")

            return false
        }
        return true
    }

    private fun generatePBKDF(password: String, salt: ByteArray): String {
        // Generate a 512-bit key
        val outputKeyLength = 512
        val chars = CharArray(password.length)

        password.toCharArray(chars, 0, 0, password.length)

        var hashedPassBytes = ByteArray(0)

        try {
            val secretKeyFactory = SecretKeyFactory.getInstance(HashMethod.PBKDF2.hashString)
            val keySpec: KeySpec = PBEKeySpec(chars, salt, ITERATIONS, outputKeyLength)

            hashedPassBytes = secretKeyFactory.generateSecret(keySpec).encoded
        } catch (e: Exception) {
            Timber.w("Password hashing failed", e)
        }

        return Base64.encodeToString(hashedPassBytes, Base64.DEFAULT)
    }

    private fun generateDigestPassword(password: String, algorithm: String): String {
        var digest = ByteArray(0)
        val buffer = password.toByteArray()

        try {
            val messageDigest = MessageDigest.getInstance(algorithm)

            messageDigest.reset()
            messageDigest.update(buffer)
            digest = messageDigest.digest()
        } catch (e: NoSuchAlgorithmException) {
            Timber.w("Failed to generate password", e)
        }

        return Base64.encodeToString(digest, Base64.DEFAULT)
    }

    fun getHashedPassword(method: HashMethod, password: String, salt: ByteArray): String {
        var hashed: String

        if (HashMethod.PBKDF2.hashString == method.hashString) {
            hashed = generatePBKDF(password, salt)
        } else {
            hashed = password

            for (i in 0 until ITERATIONS) {
                hashed = generateDigestPassword(password, method.hashString)
            }
        }

        return hashed
    }

    enum class HashMethod {
        PBKDF2 {
            override val hashString: String = "PBKDF2WithHmacSHA1"
        },

        SHA512 {
            override val hashString: String = "SHA-512"
        },

        SHA384 {
            override val hashString: String = "SHA-384"
        },

        SHA256 {
            override val hashString: String = "SHA-256"
        },

        SHA1 {
            override val hashString: String = "SHA-1"
        };

        abstract val hashString: String
    }
}