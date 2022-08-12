package com.peyess.salesapp.firebase

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.ktx.storage
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.auth.UserAuthenticationState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class FirebaseManager @Inject constructor(application: SalesApplication) {
    private val salesApplication: SalesApplication

    private var firebaseUsers = mutableMapOf<String, FirebaseApp>()

    var firebaseAppStore: FirebaseApp? = null
        private set
    var noCacheFirebaseApp: FirebaseApp? = null
        private set

    var storeFirestore: FirebaseFirestore? = null
        private set
    var noCacheFirestore: FirebaseFirestore? = null
        private set

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
        noCacheFirebaseApp = Firebase
            .initialize(salesApplication, firebaseOptions(), "no-cache")

        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }

        storeFirestore = Firebase.firestore(firebaseAppStore!!)
        noCacheFirestore = Firebase.firestore(noCacheFirebaseApp!!)
            .apply { firestoreSettings = settings }

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

    fun storeSignOut() {
        firebaseUsers.forEach { _, app ->
            Firebase.auth(app).signOut()
        }

        Firebase.auth(firebaseAppStore!!).signOut()
    }

    fun userSignOut(uid: String) {
        val firebaseApp = firebaseUsers[uid]

        if (firebaseApp != null) {
            Firebase.auth(firebaseApp).signOut()
        }
    }

    fun loadedUsers(ids: List<String>) {
        var userApp: FirebaseApp

        ids.forEach {
            if (firebaseUsers[it] == null) {
                userApp = Firebase.initialize(salesApplication, firebaseOptions(), it)
                checkForEmulator(userApp)

                firebaseUsers[it] = userApp
            }
        }
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

    fun userApplication(uid: String): FirebaseApp? {
        return firebaseUsers[uid]
    }

    fun userAuthState(uid: String): Flow<UserAuthenticationState> = callbackFlow {
        val userApp = firebaseUsers.get(uid)

        if (userApp == null) {
            trySend(UserAuthenticationState.Unauthenticated)
            return@callbackFlow
        }

        Firebase.auth(userApp).apply {
            addAuthStateListener {
                if (it.currentUser == null) {
                    trySend(UserAuthenticationState.Unauthenticated)
                } else {
                    trySend(UserAuthenticationState.Authenticated)
                }
            }
        }

        awaitClose()
    }

    fun uniqueId(): String {
        return storeFirestore?.document("random/path")?.id ?: ""
    }
}