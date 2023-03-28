package com.peyess.salesapp.data.repository.local_sale.measuring

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.data.repository.local_sale.measuring.error.LocalMeasuringResponseError
import com.peyess.salesapp.typing.general.Eye

typealias LocalMeasuringResponse = Either<LocalMeasuringResponseError, LocalMeasuringDocument>

interface LocalMeasuringRepository {
    suspend fun measuringForServiceOrder(soId: String, eye: Eye): LocalMeasuringResponse
}
