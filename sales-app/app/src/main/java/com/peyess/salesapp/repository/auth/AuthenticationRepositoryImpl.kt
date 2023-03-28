package com.peyess.salesapp.repository.auth

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.LocalAuthorizationState
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.auth.exception.InvalidCredentialsError
import com.peyess.salesapp.dao.auth.store.OpticalStoreDao
import com.peyess.salesapp.dao.auth.store.OpticalStoreResponse
import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.screen.authentication_user.manager.LocalPasscodeManager
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.model.users.toDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

private const val currentUserThreshold = 10

class AuthenticationRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val localPasscodeManager: LocalPasscodeManager,
    private val collaboratorsDao: CollaboratorsDao,
    private val storeDao: OpticalStoreDao,
): AuthenticationRepository {
    private val Context.dataStoreCurrentUser: DataStore<Preferences>
        by preferencesDataStore(dataStoreFilename)

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val currentCollaboratorId by lazy {
        salesApplication
            .dataStoreCurrentUser
            .data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .shareIn(
                scope = repositoryScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1,
            )
    }

    override val storeAuthState: Flow<StoreAuthState>
        get() = firebaseManager.storeAuthState()

    override val currentStore: Flow<OpticalStore>
        get() = storeDao.store(
                storeId = firebaseManager.currentStore?.uid ?: ""
            )

    override suspend fun loadCurrentStore(): OpticalStoreResponse {
        return storeDao.loadCurrentStore()
    }

    override suspend fun pictureForStore(storeId: String): Uri {
        val storageRef = firebaseManager.storage?.reference
            ?: FirebaseStorage.getInstance().reference

        val storagePicturePath = salesApplication
            .stringResource(R.string.storage_store_picture)
            .format(storeId)

        val picture = try {
            storageRef.child(storagePicturePath)
                .downloadUrl
                .await()
        } catch (e: Exception) {
            Timber.e("Failed to download store picture for $storeId", e)
            Uri.EMPTY
        }

        if (picture == null) {
            Timber.e("Store picture with id $storeId does not exist")
            return Uri.EMPTY
        }

        return picture
    }

    override suspend fun activeStoreId(): String {
        return firebaseManager.currentStore?.uid ?: ""
    }

    override suspend fun updateCurrentUser(uid: String) {
        salesApplication.dataStoreCurrentUser.edit {
            it[currentCollaboratorKey] = uid
        }
    }

    override fun userSignOut(): Flow<UserAuthenticationState> {
        return salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .map {
                if (it == null) {
                    Timber.e("Current user is not defined")
                    error("Current user is not defined")
                }

                firebaseManager.userSignOut(it)
                localPasscodeManager.resetUserPasscode(it)

                salesApplication.dataStoreCurrentUser.edit { prefs ->
                    prefs[currentCollaboratorKey] = ""
                    prefs[currentCollaboratorAuthStateKey] =
                        LocalAuthorizationState.Unauthorized.toString()
                }

                UserAuthenticationState.Unauthenticated
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun userAuthenticationState(): Flow<UserAuthenticationState> {
        return salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .flatMapLatest {
                if (it == null) {
                    Timber.e("Current user is not defined")
                    error("Current user is not defined")
                }

                firebaseManager.userAuthState(it)
            }
    }

    override fun userLocalAuthenticationState(): Flow<LocalAuthorizationState> {
        return salesApplication.dataStoreCurrentUser.data.map { prefs ->
            val authStr = prefs[currentCollaboratorAuthStateKey] ?: ""

            LocalAuthorizationState.fromString(authStr)
        }
    }

    override fun authenticateLocalUser(passcode: String): Flow<LocalAuthorizationState> = flow {
        val uid = salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .first()

        val isSuccess = localPasscodeManager
            .signIn(uid ?: "", passcode)
            .first()

        if (isSuccess) {
            Timber.d("Is success")
            salesApplication.dataStoreCurrentUser.edit{ prefs ->
                prefs[currentCollaboratorAuthStateKey] =
                    LocalAuthorizationState.Authorized.toString()
            }

            emit(LocalAuthorizationState.Authorized)
        } else {
            Timber.d("Is not success")

            error(InvalidCredentialsError())
        }
    }

    override fun resetUserPasscode(): Flow<Boolean>  = flow {
        salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .take(1)
            .collect {
                if (it == null) {
                    Timber.e("Current user is not defined")
                    error("Current user is not defined")
                }

                localPasscodeManager.resetUserPasscode(it)
                emit(true)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun setUserPasscode(passcode: String): Flow<Boolean> {
        return salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .flatMapLatest {
                if (it == null) {
                    Timber.e("Current user is not defined")
//                    error("Current user is not defined")
                }

                localPasscodeManager.setUserPasscode(it ?: "", passcode)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun userHasPasscode(): Flow<Boolean> {
        return salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .flatMapLatest {
                if (it == null) {
                    Timber.e("Current user is not defined")
//                    error("Current user is not defined")
                }

                localPasscodeManager.userHasPassword(it ?: "")
            }
    }

    override fun resetCurrentUser(): Flow<Boolean> = callbackFlow {
        try {
            salesApplication.dataStoreCurrentUser.edit { prefs ->
                prefs.remove(currentCollaboratorKey)
                trySend(true)
            }
        } catch (e: Exception) {
            Timber.e("Failed to reset current user: ${e.message}", e)
        }

        awaitClose()
    }

    override fun currentUser(): Flow<CollaboratorDocument?> {
        return salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .map {
                if (it == null || it.isBlank()) {
                    Timber.i("Current user is not defined")
                    null
                } else {
                    // TODO: find why it returns null
                    collaboratorsDao.getById(it)?.toDocument()
                }
            }
    }

    override suspend fun fetchCurrentUserId(): String {
        var collaboratorId = ""

        currentCollaboratorId
            .take(1)
            .collect { collaboratorId = it ?: "" }

        return collaboratorId
    }

    override suspend fun fetchCurrentUserName(): String {
        var collaboratorName = ""

        salesApplication.dataStoreCurrentUser.data.map { prefs -> prefs[currentCollaboratorKey] }
            .map {
                if (it == null || it.isBlank()) {
                    Timber.i("Current user is not defined")
                    null
                } else {
                    // TODO: find why it returns null
                    collaboratorsDao.getById(it)?.toDocument()
                }
            }.take(1).collect {
                collaboratorName = it?.name ?: ""
            }

        return collaboratorName
    }


    override fun userFirebaseApp(uid: String): FirebaseApp? {
        return firebaseManager.userApplication(uid)
    }

    override fun storeFirebaseApp(): FirebaseApp? {
        return firebaseManager.firebaseAppStore
    }

    override fun noCacheFirebaseApp(): FirebaseApp? {
        return firebaseManager.noCacheFirebaseApp
    }

    override fun activeCollaborators(): Flow<List<CollaboratorDocument>> {
        return collaboratorsDao.streamActiveAccounts()
            .onEach {
                firebaseManager.loadedUsers(it.map { user -> user.id })
            }.map {
                it.map { collaborator ->
                    collaborator.toDocument()
                }
        }
    }

    override suspend fun exit() {
        salesApplication.dataStoreCurrentUser.edit { prefs ->
            prefs[currentCollaboratorKey] = ""
            prefs[currentCollaboratorAuthStateKey] =
                LocalAuthorizationState.Unauthorized.toString()
        }
    }

    companion object {
        val dataStoreFilename = "com.peyess.salesapp.repository.auth.AuthenticationRepositoryImpl" +
                "__currentUser"

        private const val collaboratorKey = "collaborator_key"
        private const val collaboratorAuthKey = "collaborator_auth_state_key"

        val currentCollaboratorKey = stringPreferencesKey(collaboratorKey)
        val currentCollaboratorAuthStateKey = stringPreferencesKey(collaboratorAuthKey)
    }
}