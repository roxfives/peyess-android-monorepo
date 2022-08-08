package com.peyess.salesapp.repository.auth

import com.google.firebase.FirebaseApp
import com.peyess.salesapp.auth.PeyessUser
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.dao.store.OpticalStoreDao
import com.peyess.salesapp.dao.users.CollaboratorsDao
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    val firebaseManager: FirebaseManager,
    val collaboratorsDao: CollaboratorsDao,
    val storeDao: OpticalStoreDao,
): AuthenticationRepository {

    override val storeAuthState: Flow<StoreAuthState>
        get() = firebaseManager.storeAuthState()

    override val currentUser: Flow<PeyessUser?>
        get() = TODO("Not yet implemented")

    override val currentStore: Flow<OpticalStore>
        get() = storeDao.store(
                storeId = firebaseManager.currentStore!!.uid
            )

    override fun storeFirebaseApp(): FirebaseApp? {
        return firebaseManager.firebaseAppStore
    }

    override fun activeCollaborators(): Flow<List<Collaborator>> {
        return collaboratorsDao.subscribeToActiveAccounts()
    }
}