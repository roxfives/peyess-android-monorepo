package com.peyess.salesapp.data.repository.local_sale.measuring

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.dao.sale.frames_measure.PositioningDao
import com.peyess.salesapp.dao.sale.frames_measure.adapter.toLocalMeasuringDocument
import com.peyess.salesapp.data.repository.local_sale.measuring.error.MeasuringDataNotFound
import com.peyess.salesapp.feature.sale.frames.state.Eye
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
            message = "Error while getting measuring data for service order $soId and eye $eye",
            error = it,
        )
    }.leftIfNull {
        MeasuringDataNotFound(
            message = "Error while getting measuring data for service order $soId and eye $eye",
        )
    }.map {
        it.toLocalMeasuringDocument()
    }
}
