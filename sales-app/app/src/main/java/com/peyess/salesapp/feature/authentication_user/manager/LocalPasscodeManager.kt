package com.peyess.salesapp.feature.authentication_user.manager

import android.util.Base64
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.security.SecureRandom
import javax.inject.Inject

class LocalPasscodeManager @Inject constructor(
    val application: SalesApplication,
    val firebaseManager: FirebaseManager,
) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val passCodeSP = EncryptedSharedPreferences.create(
        passcodeFilename,
        masterKeyAlias,
        application,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val saltSP = EncryptedSharedPreferences.create(
        saltFilename,
        masterKeyAlias,
        application,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun signIn(uid: String, passcode: String): Flow<Boolean> = flow {
        val salt = Base64.decode(saltSP.getString(uid, ""), Base64.DEFAULT)
        val passcodeSet = passCodeSP.getString(uid, "")

        Timber.d("Using salt set ${Base64.encodeToString(salt, Base64.DEFAULT)}")
        Timber.d("Using passcode set $passcodeSet")

        val hashMethod = SecurityManager.appropriateHash
        Timber.i("Using hash method $hashMethod")
        if (hashMethod == null) {
            emit(false)
            return@flow
        }

        val passcodeHashed = SecurityManager.getHashedPassword(hashMethod, passcode, salt)

        Timber.d("Using calculated passcode $passcodeHashed")
        Timber.d("Success: ${passcodeSet == passcodeHashed}")
        emit(passcodeSet == passcodeHashed)
    }

    fun userHasPassword(uid: String): Flow<Boolean> = flow {
        val has = passCodeSP.contains(uid)
        Timber.i("User $uid has password: $has")
        emit(has)
    }

    fun resetUserPasscode(uid: String) {
        passCodeSP.edit {
            this.remove(uid).commit()
        }

        saltSP.edit {
            this.remove(uid).commit()
        }
    }

    fun setUserPasscode(uid: String, passcode: String): Flow<Boolean> = flow {
        val random = SecureRandom()
        val salt = ByteArray(256)
        random.nextBytes(salt)

        val hashMethod = SecurityManager.appropriateHash
        Timber.i("Using hash method $hashMethod")
        if (hashMethod == null) {
            emit(false)
            return@flow
        }

        val passcodeHashed = SecurityManager.getHashedPassword(hashMethod, passcode, salt)

        Timber.d("Using salt ${Base64.encodeToString(salt, Base64.DEFAULT)}")
        saltSP.edit {
            this.putString(uid, Base64.encodeToString(salt, Base64.DEFAULT)).commit()
        }

        passCodeSP.edit {
            Timber.d("Using passcode $passcodeHashed")

            Timber.i("Setting passcode for $uid")
            this.putString(uid, passcodeHashed).commit()
        }

        emit(true)
    }

    companion object {
        val passcodeFilename =
            "com.peyess.salesapp.feature.authentication_user.manager.LocalPasswordManager__passcode"

        val saltFilename =
            "com.peyess.salesapp.feature.authentication_user.manager.LocalPasswordManager__salt"
    }
}