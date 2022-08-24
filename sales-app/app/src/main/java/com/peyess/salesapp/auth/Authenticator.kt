package com.peyess.salesapp.auth

import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.peyess.salesapp.auth.exception.InvalidCredentialsError
import com.peyess.salesapp.auth.exception.WrongAccountType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import timber.log.Timber

fun authenticateStore(
    email: String,
    password: String,
    firebaseApp: FirebaseApp,
): Flow<StoreAuthState> = flow {
    val auth = Firebase.auth(firebaseApp)

    Timber.i( "Signing in store")
    auth.signInWithEmailAndPassword(email, password).await()
    Timber.i( "User signed in")

    var isStore = false
    runBlocking {
        verifyUserIsStore(auth.currentUser).collect {
            isStore = it
        }
    }

    if (isStore) {
        Timber.i( "User signed in as a store")
        emit(StoreAuthState.Authenticated)
    } else {
        Timber.i( "Failed sign in, signing out just in case")

        auth.signOut()
        throw WrongAccountType("Account type should be store")
    }
}

fun authenticateUser(
    uid: String,
    email: String,
    password: String,
    firebaseApp: FirebaseApp,
): Flow<UserAuthenticationState> = flow {
    val auth = Firebase.auth(firebaseApp)

    Timber.i( "Signing in user")
    auth.signInWithEmailAndPassword(email, password).await()
    Timber.i( "User signed in")

    if (
        auth.currentUser != null
        && auth.currentUser!!.uid == uid
    ) {
        Timber.i( "User signed in to use store")
        emit(UserAuthenticationState.Authenticated)
    } else {
        Timber.i( "Failed sign in, signing out just in case")
        auth.signOut()
        error(InvalidCredentialsError())
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
    Log.i("SUPER_TAG", "The user's role: ${role}")
    Timber.i( "The user's role: ${role}")

    emit(role == "store")
}