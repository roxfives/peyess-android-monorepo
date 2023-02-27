package com.peyess.salesapp.data.repository.positioning

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.leftIfNull
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.positioning.toFSPositioning
import com.peyess.salesapp.data.adapter.positioning.toFSPositioningUpdate
import com.peyess.salesapp.data.dao.positioning.PositioningDao
import com.peyess.salesapp.data.dao.positioning.error.ReadPositioningDaoError
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.data.model.positioning.PositioningUpdateDocument
import com.peyess.salesapp.data.repository.positioning.adapter.toPositioningDocument
import com.peyess.salesapp.data.repository.positioning.error.GetPictureUriPositioningRepositoryError
import com.peyess.salesapp.data.repository.positioning.error.ReadPositioningRepositoryError
import com.peyess.salesapp.data.repository.positioning.error.UpdatePositioningRepositoryError
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.utils.cloud.storage.appendStoragePaths
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PositioningRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
    private val positioningDao: PositioningDao,
): PositioningRepository {
    override suspend fun add(positioning: PositioningDocument) {
        val fsPositioning = positioning.toFSPositioning()

        positioningDao.add(fsPositioning)
    }

    override suspend fun positioningById(
        positioningId: String,
    ): ReadPositioningResponse = either {
        positioningDao
            .positioningById(positioningId)
            .mapLeft {
                when(it) {
                    is ReadPositioningDaoError.NotFound ->
                        ReadPositioningRepositoryError.NotFound(
                            description = it.description,
                            throwable = it.throwable,
                        )
                    is ReadPositioningDaoError.Unexpected ->
                        ReadPositioningRepositoryError.Unexpected(
                            description = it.description,
                            throwable = it.throwable,
                        )
                }
            }.bind()
            .toPositioningDocument()
    }

    override suspend fun updatePositioning(
        positioningId: String,
        positioningUpdate: PositioningUpdateDocument
    ): UpdatePositioningResponse = either {
        positioningDao
            .updatePositioning(positioningId, positioningUpdate.toFSPositioningUpdate())
            .mapLeft {
                UpdatePositioningRepositoryError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()
    }

    override suspend fun getPictureUri(
        positioning: PositioningDocument,
    ): GetPictureUrlPositioningRepositoryResponse {
        val storageRef = firebaseManager.storage?.reference
            ?: FirebaseStorage.getInstance().reference

        val storagePicturePath = salesApplication
            .stringResource(R.string.storage_client_positioning)
            .format(
                positioning.storeId,
                positioning.patientUid,
                positioning.id,
            )
        val filename = salesApplication.stringResource(R.string.storage_client_positioning)

        val storagePath = appendStoragePaths(listOf(storagePicturePath, filename))

        return Either.catch {
            storageRef.child(storagePath)
                .downloadUrl
                .await()
        }.mapLeft {
            Timber.e("Failed to download positioning at $storagePath", it)

            GetPictureUriPositioningRepositoryError.Unexpected(
                description = "Failed to download positioning at $storagePath",
                throwable = it,
            )
        }.leftIfNull {
            Timber.e("Positioning picture at $storagePicturePath does not exist")

            GetPictureUriPositioningRepositoryError.NotFound(
                description = "Failed to download positioning at $storagePicturePath",
            )
        }
    }
}
