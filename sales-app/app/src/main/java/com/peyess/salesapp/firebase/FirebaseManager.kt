package com.peyess.salesapp.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.StoreAuthState
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class FirebaseManager @Inject constructor(application: SalesApplication) {
    private val application: SalesApplication

    var firebaseAppStore: FirebaseApp? = null
        private set

    private var firebaseUsers = mutableMapOf<String, FirebaseApp>()

    init {
        this.application = application
    }

    private fun checkForEmulator(app: FirebaseApp?) {
        if (BuildConfig.DEBUG && app != null) {
            Firebase.auth(app).useEmulator("10.0.2.2", 9099)
        }
    }

    private fun firebaseOptions(): FirebaseOptions {
        return FirebaseOptions.Builder()
            .setApiKey("qa-peyess-store-diniz-brasil")
            .setApplicationId("1:108515371906:android:d54553ddefd6029c48bd9b")
            .setApiKey("AIzaSyD-ojx10VsEL0TFJ0Bkymf8hYfPB25bIWg")
            .build()
    }

    private fun initializeFirebaseForStore() {
        if (firebaseAppStore != null) {
            return
        }

        firebaseAppStore = Firebase.initialize(application)
        checkForEmulator(firebaseAppStore)
    }

    private fun initializeFirebaseForUsers() {
        var userApp: FirebaseApp

        firebaseUsers.keys.forEach {
            userApp = Firebase.initialize(application, firebaseOptions(), it)
            checkForEmulator(userApp)

            firebaseUsers[it] = userApp
        }
    }

    fun initializeFirebase() {
        initializeFirebaseForStore()
        initializeFirebaseForUsers()
    }

    fun storeAuthState() = callbackFlow {
        Firebase.auth(firebaseAppStore!!).apply {
            addAuthStateListener {
                if (it.currentUser == null) {
                    trySend(StoreAuthState.Unauthenticated)
                } else {
                    trySend(StoreAuthState.Authenticated)
                }
            }
        }
    }

    fun userAuthState(uid: String) = callbackFlow {
        val userApp = firebaseUsers.get(uid)

        if (userApp == null) {
            trySend(StoreAuthState.Unauthenticated)
            return@callbackFlow
        }

        Firebase.auth(userApp).apply {
            addAuthStateListener {
                if (it.currentUser == null) {
                    trySend(StoreAuthState.Unauthenticated)
                } else {
                    trySend(StoreAuthState.Authenticated)
                }
            }
        }
    }
}