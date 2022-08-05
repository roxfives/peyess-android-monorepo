package com.peyess.salesapp.repository.auth

import com.google.firebase.FirebaseApp
import com.peyess.salesapp.auth.StoreAuthState
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val storeAuthState: Flow<StoreAuthState>

    fun storeFirebaseApp(): FirebaseApp?
}