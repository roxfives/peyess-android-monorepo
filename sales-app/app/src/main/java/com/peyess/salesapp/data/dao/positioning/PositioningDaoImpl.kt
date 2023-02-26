package com.peyess.salesapp.data.dao.positioning

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.positioning.error.ReadPositioningDaoError
import com.peyess.salesapp.data.dao.positioning.error.UpdatePositioningDaoError
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

    override suspend fun positioningById(
        positioningId: String,
    ): ReadPositioningResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            ReadPositioningDaoError.Unexpected(
                description = "Firestore instance is uninitialized",
            )
        }

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            ReadPositioningDaoError.Unexpected(
                description = "Current store is null",
            )
        }

        val positioningCollectionPath = salesApplication
            .stringResource(R.string.fs_col_positioning)
            .format(storeId)

        val response = Either.catch {
            firestore.collection(positioningCollectionPath)
                .document(positioningId)
                .get()
                .await()
        }.mapLeft {
            ReadPositioningDaoError.Unexpected(
                description = "Unexpected error while reading positioning",
                throwable = it,
            )
        }.bind()

        ensure(response.exists()) {
            ReadPositioningDaoError.NotFound(
                description = "Positioning with id $positioningId not found",
            )
        }

        val positioning = response.toObject(FSPositioning::class.java)
        ensureNotNull(positioning) {
            ReadPositioningDaoError.Unexpected(
                description = "Positioning with id $positioningId not found",
            )
        }

        positioning
    }

    override suspend fun updatePositioning(
        positioningId: String,
        positioningUpdate: Map<String, Any>
    ): UpdatePositioningResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            UpdatePositioningDaoError.Unexpected(
                description = "Firestore instance is uninitialized",
            )
        }

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            UpdatePositioningDaoError.Unexpected(
                description = "Current store is null",
            )
        }

        val positioningCollectionPath = salesApplication
            .stringResource(R.string.fs_col_positioning)
            .format(storeId)

        Either.catch {
            firestore.collection(positioningCollectionPath)
                .document(positioningId)
                .update(positioningUpdate)
                .await()
        }.mapLeft {
            UpdatePositioningDaoError.Unexpected(
                description = "Unexpected error while reading positioning",
                throwable = it,
            )
        }.bind()
    }
}
