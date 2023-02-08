package com.peyess.salesapp.data.repository.edit_service_order.lens_comparison

import arrow.core.Either
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.InsertLensComparisonError
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.ReadLensComparisonError
import kotlinx.coroutines.flow.Flow

typealias EditLensComparisonInsertResponse = Either<InsertLensComparisonError, Unit>
typealias EditLensComparisonStreamResponse =
    Flow<Either<ReadLensComparisonError, LensComparisonDocument>>

interface EditLensComparisonRepository {
    suspend fun addLensComparison(
        lensComparison: LensComparisonDocument,
    ): EditLensComparisonInsertResponse

    fun streamLensComparisonsForServiceOrder(
        serviceOrderId: String,
    ): EditLensComparisonStreamResponse
}