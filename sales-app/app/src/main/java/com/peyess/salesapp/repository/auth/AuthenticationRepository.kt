package com.peyess.salesapp.repository.auth

import com.google.firebase.FirebaseApp
import com.peyess.salesapp.auth.LocalAuthorizationState
import com.peyess.salesapp.auth.PeyessUser
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val storeAuthState: Flow<StoreAuthState>

    val currentStore: Flow<OpticalStore>

    fun resetCurrentUser(): Flow<Boolean>

    fun currentUser(): Flow<Collaborator>

    fun userSignOut(): Flow<UserAuthenticationState>

    fun userAuthenticationState(): Flow<UserAuthenticationState>

    fun userLocalAuthenticationState(): Flow<LocalAuthorizationState>

    fun authenticateLocalUser(passcode: String): Flow<LocalAuthorizationState>

    fun userHasPasscode(): Flow<Boolean>

    fun resetUserPasscode(): Flow<Boolean>

    fun setUserPasscode(passcode: String): Flow<Boolean>

    suspend fun updateCurrentUser(uid: String)

    fun storeFirebaseApp(): FirebaseApp?

    fun userFirebaseApp(uid: String): FirebaseApp?

    fun activeCollaborators(): Flow<List<Collaborator>>
}