package com.peyess.salesapp.data.repository.local_sale.positioning.error

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.data.adapter.positioning.toLocalPositioningDocument
import com.peyess.salesapp.data.dao.local_sale.positioning.PositioningDao
import com.peyess.salesapp.data.repository.local_sale.positioning.LocalPositioningFetchBothResponse
import com.peyess.salesapp.data.repository.local_sale.positioning.LocalPositioningRepository
import com.peyess.salesapp.data.repository.local_sale.positioning.LocalPositioningFetchSingleResponse
import com.peyess.salesapp.data.repository.local_sale.positioning.typing.PositioningPair
import com.peyess.salesapp.feature.sale.frames.landing.state.Eye
import javax.inject.Inject

class LocalPositioningRepositoryImpl @Inject constructor(
    private val positioningDao: PositioningDao,
): LocalPositioningRepository {
    override suspend fun positioningForServiceOrder(
        serviceOrderId: String,
        eye: Eye,
    ): LocalPositioningFetchSingleResponse = Either.catch {
        positioningDao.getPositioningForServiceOrder(serviceOrderId, eye)
    }.mapLeft {
        Unexpected(
            "Unexpected error while getting positioning for service order $serviceOrderId and eye $eye",
            it,
        )
    }.leftIfNull {
        LocalPositioningNotFoundError(
            "Positioning not found for service order $serviceOrderId and eye $eye",
        )
    }.map {
        it.toLocalPositioningDocument()
    }

    override suspend fun bothPositioningsForServiceOrder(
        serviceOrderId: String,
    ): LocalPositioningFetchBothResponse = Either.catch {
        val left = positioningDao.getPositioningForServiceOrder(serviceOrderId, Eye.Left)
        val right = positioningDao.getPositioningForServiceOrder(serviceOrderId, Eye.Right)

        if (left == null || right == null) {
            error("Positioning not found for service order $serviceOrderId for both eyes")
        }

        Pair(left, right)
    }.mapLeft {
        Unexpected(
            "Unexpected error while getting positioning for service order $serviceOrderId for both eyes",
            it,
        )
    }.map {
        PositioningPair(
            left = it.first.toLocalPositioningDocument(),
            right = it.second.toLocalPositioningDocument(),
        )
    }
}
