package com.peyess.salesapp.data.repository.local_sale.measuring

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.data.dao.local_sale.positioning.PositioningDao
import com.peyess.salesapp.data.adapter.positioning.toLocalMeasuringDocument
import com.peyess.salesapp.data.repository.local_sale.measuring.error.MeasuringDataNotFound
import com.peyess.salesapp.typing.general.Eye
import javax.inject.Inject

class LocalMeasuringRepositoryImpl @Inject constructor(
    private val positioningDao: PositioningDao,
): LocalMeasuringRepository {
    override suspend fun measuringForServiceOrder(
        soId: String, eye: Eye,
    ): LocalMeasuringResponse = Either.catch {
        positioningDao.getPositioningForServiceOrder(soId, eye)
    }.mapLeft {
        MeasuringDataNotFound(
            description = "Error while getting measuring data for service order $soId and eye $eye",
            error = it,
        )
    }.leftIfNull {
        MeasuringDataNotFound(
            description = "Error while getting measuring data for service order $soId and eye $eye",
        )
    }.map {
        it.toLocalMeasuringDocument()
    }
}
