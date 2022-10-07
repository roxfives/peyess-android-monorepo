package com.peyess.salesapp.data.dao.positioning

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.positioning.FSPositioning
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PositioningDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): PositioningDao {
    override suspend fun add(document: FSPositioning) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        val storeId = firebaseManager.currentStore?.uid
        val positioningCollectionPath = salesApplication
            .stringResource(R.string.fs_col_positioning)
            .format(storeId)

        try {
            firestore.collection(positioningCollectionPath)
                .document(document.id)
                .set(document)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client")
            error(err)
        }
    }
}