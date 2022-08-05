package com.peyess.salesapp.repository.auth

import com.google.firebase.FirebaseApp
import com.peyess.salesapp.auth.PeyessUser
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    val firebaseManager: FirebaseManager,
): AuthenticationRepository {

    override val storeAuthState: Flow<StoreAuthState>
        get() = firebaseManager.storeAuthState()

    override val currentUser: Flow<PeyessUser?>
        get() = TODO("Not yet implemented")

    override fun storeFirebaseApp(): FirebaseApp? {
        return firebaseManager.firebaseAppStore
    }
}