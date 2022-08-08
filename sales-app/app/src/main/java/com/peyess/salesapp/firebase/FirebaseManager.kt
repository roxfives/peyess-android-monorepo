package com.peyess.salesapp.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.ktx.storage
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.StoreAuthState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class FirebaseManager @Inject constructor(application: SalesApplication) {
    private val salesApplication: SalesApplication

    private var firebaseUsers = mutableMapOf<String, FirebaseApp>()

    var firebaseAppStore: FirebaseApp? = null
        private set

    val storeFirestore: FirebaseFirestore?
        get() {
            return firebaseAppStore?.let { Firebase.firestore(it) }
        }

    val currentStore: FirebaseUser?
        get() {
            return firebaseAppStore?.let { Firebase.auth(it).currentUser }
        }

    init {
        this.salesApplication = application
    }

    private fun checkForEmulator(app: FirebaseApp?) {
        if (BuildConfig.DEBUG && app != null) {
            Timber.i("Connecting app ${app.name} to emulator")

//            Firebase.auth(app).useEmulator("10.0.2.2", 9099)
//            Firebase.firestore(app).useEmulator("10.0.2.2", 8080)
//            Firebase.storage(app).useEmulator("10.0.2.2", 9199)

            Firebase.auth(app).useEmulator("localhost", 9099)
            Firebase.firestore(app).useEmulator("localhost", 8080)
            Firebase.storage(app).useEmulator("localhost", 9199)
        } else {
            Timber.i("Connecting to real server")
        }
    }

    private fun firebaseOptions(): FirebaseOptions {
        // TODO: Add string based on build
        return FirebaseOptions.Builder()
            .setProjectId("qa-peyess-store-diniz-brasil")
            .setApplicationId("1:108515371906:android:d54553ddefd6029c48bd9b")
            .setApiKey("AIzaSyD-ojx10VsEL0TFJ0Bkymf8hYfPB25bIWg")
            .build()
    }

    private fun initializeFirebaseForStore() {
        if (firebaseAppStore != null) {
            return
        }

        firebaseAppStore = Firebase.initialize(salesApplication)
        checkForEmulator(firebaseAppStore)
    }

    private fun initializeFirebaseForUsers() {
        var userApp: FirebaseApp

        firebaseUsers.keys.forEach {
            userApp = Firebase.initialize(salesApplication, firebaseOptions(), it)
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

        awaitClose()
    }

    fun addUserApplication(uid: String, firebaseApp: FirebaseApp) {
        if (firebaseUsers[uid] == null) {
            firebaseUsers[uid] = firebaseApp
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