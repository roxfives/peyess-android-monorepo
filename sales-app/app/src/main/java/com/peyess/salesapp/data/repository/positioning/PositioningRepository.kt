package com.peyess.salesapp.data.repository.positioning

import android.net.Uri
import arrow.core.Either
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.data.model.positioning.PositioningUpdateDocument
import com.peyess.salesapp.data.repository.positioning.error.GetPictureUriPositioningRepositoryError
import com.peyess.salesapp.data.repository.positioning.error.ReadPositioningRepositoryError
import com.peyess.salesapp.data.repository.positioning.error.UpdatePositioningRepositoryError

typealias ReadPositioningResponse = Either<ReadPositioningRepositoryError, PositioningDocument>

typealias UpdatePositioningResponse = Either<UpdatePositioningRepositoryError, Unit>

typealias GetPictureUrlPositioningRepositoryResponse =
        Either<GetPictureUriPositioningRepositoryError, Uri>

interface PositioningRepository {
    suspend fun add(positioning: PositioningDocument)

    suspend fun positioningById(positioningId: String): ReadPositioningResponse

    suspend fun updatePositioning(
        positioningId: String,
        positioningUpdate: PositioningUpdateDocument,
    ): UpdatePositioningResponse

    suspend fun getPictureUri(
        positioning: PositioningDocument,
    ): GetPictureUrlPositioningRepositoryResponse
}
