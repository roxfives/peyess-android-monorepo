package com.peyess.salesapp.data.repository.edit_service_order.lens_comparison

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.lens_comparison.toEditLensComparisonEntity
import com.peyess.salesapp.data.adapter.edit_service_order.lens_comparison.toLensComparisonDocument
import com.peyess.salesapp.data.dao.edit_service_order.lens_comparison.EditLensComparisonDao
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.DeleteLensComparisonError
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.InsertLensComparisonError
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.ReadLensComparisonError
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditLensComparisonRepositoryImpl @Inject constructor(
    private val lensComparisonDao: EditLensComparisonDao,
) : EditLensComparisonRepository {
    override suspend fun addLensComparison(
        lensComparison: LensComparisonDocument,
    ): EditLensComparisonInsertResponse = Either.catch {
        lensComparisonDao.addLensComparison(lensComparison.toEditLensComparisonEntity())
    }.mapLeft {
        InsertLensComparisonError.Unexpected(
            description = "Error while inserting lens comparison $lensComparison",
            throwable = it,
        )
    }

    override fun streamLensComparisonsForServiceOrder(
        serviceOrderId: String,
    ): EditLensComparisonStreamResponse {
        return lensComparisonDao.streamLensComparisonsForServiceOrder(serviceOrderId)
            .map {
                if (it == null) {
                    ReadLensComparisonError.LensComparisonNotFound(
                        description = "Lens comparison not found for service order $serviceOrderId",
                    ).left()
                } else {
                    it.toLensComparisonDocument().right()
                }
            }.catch {
                ReadLensComparisonError.Unexpected(
                    description = "Error while reading lens comparison for service order $serviceOrderId",
                    throwable = it,
                ).left()
            }
    }

    override suspend fun deleteComparisonsForServiceOrder(
        serviceOrderId: String,
    ): EditLensComparisonDeleteResponse = Either.catch {
        lensComparisonDao.deleteComparisonsForServiceOrder(serviceOrderId)
    }.mapLeft {
        DeleteLensComparisonError.Unexpected(
            description = "Error while deleting lens comparisons for service order $serviceOrderId",
            throwable = it,
        )
    }
}
