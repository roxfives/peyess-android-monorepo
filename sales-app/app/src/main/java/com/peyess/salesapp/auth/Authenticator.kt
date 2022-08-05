package com.peyess.salesapp.auth

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.peyess.salesapp.auth.exception.WrongAccountType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber

fun authenticateStore(email: String, password: String): Flow<AuthState> = flow {
    val auth = Firebase.auth

    Timber.i( "Signing in user")
    auth.signInWithEmailAndPassword(email, password).await()
    Timber.i( "User signed in")

    var isStore = false
    runBlocking {
        verifyUserIsStore(auth.currentUser).collect {
            isStore = it
        }
    }

    if (isStore) {
        Timber.d( "User signed in as a store")
        emit(AuthState.Unauthorized)
    } else {
        Timber.d( "Failed sign in, signing out just in case")

        auth.signOut()
        throw WrongAccountType("Account type should be store")
    }
}

fun verifyUserIsStore(user: FirebaseUser?): Flow<Boolean> = flow {
    Timber.i("Verifying user is store")
    if (user == null) {
        Timber.e( "User is null")

        emit(false)
    }

    val task = user!!.getIdToken(false).await()

    val role = task.claims["role"]
    Timber.d( "The user's role: ${role}")

    emit(role == "store")
}