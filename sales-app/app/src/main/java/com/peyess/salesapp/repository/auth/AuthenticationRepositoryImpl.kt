package com.peyess.salesapp.repository.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.LocalAuthorizationState
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.auth.exception.InvalidCredentialsError
import com.peyess.salesapp.dao.store.OpticalStoreDao
import com.peyess.salesapp.dao.users.CollaboratorsDao
import com.peyess.salesapp.feature.authentication_user.manager.LocalPasscodeManager
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import timber.log.Timber
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
    val localPasscodeManager: LocalPasscodeManager,
    val collaboratorsDao: CollaboratorsDao,
    val storeDao: OpticalStoreDao,
): AuthenticationRepository {
    val Context.dataStoreCurrentUser: DataStore<Preferences>
        by preferencesDataStore(dataStoreFilename)

    override val storeAuthState: Flow<StoreAuthState>
        get() = firebaseManager.storeAuthState()

    override val currentStore: Flow<OpticalStore>
        get() = storeDao.store(
                storeId = firebaseManager.currentStore!!.uid
            )

    override suspend fun updateCurrentUser(uid: String) {
        salesApplication.dataStoreCurrentUser.edit {
            it[currentCollaboratorKey] = uid
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
                    error("Current user is not defined")
                }

                localPasscodeManager.setUserPasscode(it, passcode)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun userHasPasscode(): Flow<Boolean> {
        return salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .flatMapLatest {
                if (it == null) {
                    Timber.e("Current user is not defined")
                    error("Current user is not defined")
                }

                localPasscodeManager.userHasPassword(it)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun currentUser(): Flow<Collaborator> {
        return salesApplication.dataStoreCurrentUser.data
            .map { prefs -> prefs[currentCollaboratorKey] }
            .flatMapLatest {
                if (it == null) {
                    Timber.e("Current user is not defined")
                    error("Current user is not defined")
                }

                collaboratorsDao.user(uid = it)
            }
    }

    override fun userFirebaseApp(uid: String): FirebaseApp? {
        return firebaseManager.userApplication(uid)
    }

    override fun storeFirebaseApp(): FirebaseApp? {
        return firebaseManager.firebaseAppStore
    }

    override fun activeCollaborators(): Flow<List<Collaborator>> {
        return collaboratorsDao.subscribeToActiveAccounts().onEach {
            val ids = it.map { user -> user.id }

            firebaseManager.loadedUsers(ids)
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