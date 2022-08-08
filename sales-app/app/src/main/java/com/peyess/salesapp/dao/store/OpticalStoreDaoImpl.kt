package com.peyess.salesapp.dao.store

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
            Timber.d("Snap object is null")
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

            val store = snap.toObject(FSOpticalStore::class.java)
            emit(toStore(store))
        } else {
            Timber.e("Firestore is null")
            error("Firestore is null")
        }
    }
}