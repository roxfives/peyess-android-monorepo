package com.peyess.salesapp.repository.auth

import android.net.Uri
import com.google.firebase.FirebaseApp
import com.peyess.salesapp.auth.LocalAuthorizationState
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.dao.auth.store.OpticalStoreResponse
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val storeAuthState: Flow<StoreAuthState>

    val currentStore: Flow<OpticalStore>

    suspend fun loadCurrentStore(): OpticalStoreResponse

    suspend fun pictureForStore(storeId: String): Uri

    suspend fun activeStoreId(): String

    fun resetCurrentUser(): Flow<Boolean>

    fun currentUser(): Flow<CollaboratorDocument?>
    suspend fun fetchCurrentUserId(): String
    suspend fun fetchCurrentUserName(): String

    fun userSignOut(): Flow<UserAuthenticationState>

    fun userAuthenticationState(): Flow<UserAuthenticationState>

    fun userLocalAuthenticationState(): Flow<LocalAuthorizationState>

    fun authenticateLocalUser(passcode: String): Flow<LocalAuthorizationState>

    fun userHasPasscode(): Flow<Boolean>

    fun resetUserPasscode(): Flow<Boolean>

    fun setUserPasscode(passcode: String): Flow<Boolean>

    suspend fun updateCurrentUser(uid: String)

    fun storeFirebaseApp(): FirebaseApp?

    fun noCacheFirebaseApp(): FirebaseApp?

    fun userFirebaseApp(uid: String): FirebaseApp?

    fun activeCollaborators(): Flow<List<CollaboratorDocument>>

    suspend fun exit()
}