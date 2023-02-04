package com.peyess.salesapp.dao.auth.store

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.auth.store.error.OpticalStoreUnexpected
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.store.FSOpticalStore
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.store.toDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class OpticalStoreDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
) : OpticalStoreDao {
    val firestore = firebaseManager.storeFirestore

    private fun toStore(id: String, fsStore: FSOpticalStore?): OpticalStore {
        Timber.i("Converting to document")

        return if (fsStore != null) {
            fsStore.toDocument(id)
        } else {
            Timber.e("Snap object is null")
            error("Snap object is null")
        }
    }

    override suspend fun loadCurrentStore(): OpticalStoreResponse = either {
        val firestore = firebaseManager.storeFirestore
        val storeId = firebaseManager.currentStore?.uid

        ensureNotNull(firestore) {
            OpticalStoreUnexpected("Firestore instance is null")
        }

        ensureNotNull(storeId) {
            OpticalStoreUnexpected("Active store is null")
        }
        ensure(storeId.isNotBlank()) {
            OpticalStoreUnexpected("Active store has no id")
        }

        Either.catch {
            val snap = firestore
                .collection(salesApplication.stringResource(R.string.fs_col_store))
                .document(storeId)
                .get()
                .await()

            Timber.i("Converting to object")
            val fsStore = snap.toObject(FSOpticalStore::class.java)

            Timber.i("Converting to document")
            toStore(snap.id, fsStore)
        }.mapLeft {
            OpticalStoreUnexpected(it.message ?: "Unknown error")
        }.bind()
    }

    override fun store(storeId: String): Flow<OpticalStore>  = flow {
        if (firestore != null) {
            Timber.i("Loading store ${storeId}")

            val snap = firestore
                .collection(salesApplication.stringResource(R.string.fs_col_store))
                .document(storeId)
                .get()
                .await()
            Timber.i("Finished loading store ${storeId}")

            Timber.i("Converting to object")
            val fsStore = snap.toObject(FSOpticalStore::class.java)

            Timber.i("Converting to document")
            val store = toStore(snap.id, fsStore)

            Timber.i("Emitting store ${store}")
            emit(store)
        } else {
            Timber.e("Firestore is null")
            error("Firestore is null")
        }
    }
}