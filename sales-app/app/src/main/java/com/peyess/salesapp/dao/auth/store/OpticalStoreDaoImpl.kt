package com.peyess.salesapp.dao.auth.store

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
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
    val firebaseManager: FirebaseManager,
    val application: SalesApplication,
) : OpticalStoreDao {
    val firestore = firebaseManager.storeFirestore

    private fun toStore(fsStore: FSOpticalStore?): OpticalStore {
        Timber.i("Converting to document")

        return if (fsStore != null) {
            fsStore.toDocument()
        } else {
            Timber.e("Snap object is null")
            error("Snap object is null")
        }
    }

    override fun store(storeId: String): Flow<OpticalStore>  = flow {
        if (firestore != null) {
            Timber.i("Loading store ${storeId}")

            val snap = firestore
                .collection(application.stringResource(R.string.fs_col_store))
                .document(storeId)
                .get()
                .await()
            Timber.i("Finished loading store ${storeId}")

            Timber.i("Converting to object")
            val fsStore = snap.toObject(FSOpticalStore::class.java)

            Timber.i("Converting to document")
            val store = toStore(fsStore)

            Timber.i("Emitting store ${store}")
            emit(store)
        } else {
            Timber.e("Firestore is null")
            error("Firestore is null")
        }
    }
}