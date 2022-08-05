package com.peyess.salesapp.repository.auth

import com.google.firebase.FirebaseApp
import com.peyess.salesapp.auth.PeyessUser
import com.peyess.salesapp.auth.StoreAuthState
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val storeAuthState: Flow<StoreAuthState>

    val currentUser: Flow<PeyessUser?>

    fun storeFirebaseApp(): FirebaseApp?
}