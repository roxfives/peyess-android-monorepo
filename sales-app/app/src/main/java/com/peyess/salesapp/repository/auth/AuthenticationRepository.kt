package com.peyess.salesapp.repository.auth

import com.google.firebase.FirebaseApp
import com.peyess.salesapp.auth.PeyessUser
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val storeAuthState: Flow<StoreAuthState>

    val currentUser: Flow<PeyessUser?>

    val currentStore: Flow<OpticalStore>

    fun storeFirebaseApp(): FirebaseApp?

    fun activeCollaborators(): Flow<List<Collaborator>>
}