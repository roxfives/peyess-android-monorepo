package com.peyess.salesapp.repository.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.FirebaseApp
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.dao.store.OpticalStoreDao
import com.peyess.salesapp.dao.users.CollaboratorsDao
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
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

        val currentCollaboratorKey = stringPreferencesKey(dataStoreFilename)
    }
}