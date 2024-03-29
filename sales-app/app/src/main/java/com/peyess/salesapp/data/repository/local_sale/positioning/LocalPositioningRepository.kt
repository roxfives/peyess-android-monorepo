package com.peyess.salesapp.data.repository.local_sale.positioning

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.repository.local_sale.positioning.error.LocalPositioningReadError
import com.peyess.salesapp.data.repository.local_sale.positioning.typing.PositioningPair
import com.peyess.salesapp.typing.general.Eye

typealias AddLocalPositioningResponse = Either<LocalPositioningReadError, Unit>

typealias LocalPositioningFetchSingleResponse =
        Either<LocalPositioningReadError, LocalPositioningDocument>

typealias LocalPositioningFetchBothResponse =
        Either<LocalPositioningReadError, PositioningPair>

interface LocalPositioningRepository {
    suspend fun addPositioning(positioning: LocalPositioningDocument): AddLocalPositioningResponse

    suspend fun positioningForServiceOrder(
        serviceOrderId: String,
        eye: Eye,
    ): LocalPositioningFetchSingleResponse

    suspend fun bothPositioningsForServiceOrder(
        serviceOrderId: String,
    ): LocalPositioningFetchBothResponse
}
