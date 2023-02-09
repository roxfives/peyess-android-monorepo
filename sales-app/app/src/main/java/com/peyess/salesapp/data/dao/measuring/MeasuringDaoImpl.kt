package com.peyess.salesapp.data.dao.measuring

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.measuring.error.MeasuringDaoErrors
import com.peyess.salesapp.data.dao.measuring.error.ReadMeasuringDaoError
import com.peyess.salesapp.data.model.measuring.FSMeasuring
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class MeasuringDaoImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
): MeasuringDao {
    override suspend fun add(document: FSMeasuring) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        val storeId = firebaseManager.currentStore?.uid
        val measuringCollectionPath = salesApplication
            .stringResource(R.string.fs_col_measuring)
            .format(storeId)

        try {
            firestore.collection(measuringCollectionPath)
                .document(document.id)
                .set(document)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client")
            error(err)
        }
    }

    override suspend fun measuringById(
        measuringId: String,
    ): ReadMeasuringDaoResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            ReadMeasuringDaoError.Unexpected(
                description = "Firestore instance is uninitialized"
            )
        }

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            ReadMeasuringDaoError.Unexpected(
                description = "Current store is null"
            )
        }

        val measuringCollectionPath = salesApplication
            .stringResource(R.string.fs_col_measuring)
            .format(storeId)
        val snap = Either.catch {
            firestore.collection(measuringCollectionPath)
                .document(measuringId)
                .get()
                .await()
        }.mapLeft {
            ReadMeasuringDaoError.Unexpected(
                description = it.message ?: "Unknown error",
                throwable = it,
            )
        }.bind()

        ensure(snap.exists()) {
            ReadMeasuringDaoError.NotFound(
                description = "Measuring with id $measuringId not found"
            )
        }

        val response = snap.toObject(FSMeasuring::class.java)
        ensureNotNull(response) {
            ReadMeasuringDaoError.Unexpected(
                description = "Measuring with id $measuringId is null"
            )
        }

        response
    }
}
