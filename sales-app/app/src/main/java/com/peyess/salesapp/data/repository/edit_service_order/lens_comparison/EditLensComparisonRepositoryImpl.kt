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
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.UpdateLensComparisonError
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

    override suspend fun updateLensComparison(
        lensComparison: LensComparisonDocument,
    ): EditLensComparisonUpdateResponse = Either.catch {
        val entity = lensComparison.toEditLensComparisonEntity()

        lensComparisonDao.updateLensComparison(entity)
    }.mapLeft {
        UpdateLensComparisonError.Unexpected(
            description = "Error while updating lens comparison $lensComparison",
            throwable = it,
        )
    }

    override fun streamLensComparisonsForServiceOrder(
        serviceOrderId: String,
    ): EditLensComparisonStreamResponse {
        return lensComparisonDao.streamLensComparisonsForServiceOrder(serviceOrderId).map {
            if (it == null) {
                ReadLensComparisonError.LensComparisonNotFound(
                    description = "Lens comparison not found for service order $serviceOrderId",
                ).left()
            } else {
                it.map { l -> l.toLensComparisonDocument() }.right()
            }
        }.catch {
            ReadLensComparisonError.Unexpected(
                description = "Error while reading lens comparison for service order $serviceOrderId",
                throwable = it,
            ).left()
        }
    }

    override fun deleteById(id: Int): EditLensComparisonDeleteResponse = Either.catch {
        lensComparisonDao.deleteById(id)
    }.mapLeft {
        DeleteLensComparisonError.Unexpected(
            description = "Error while deleting lens comparison with id $id",
            throwable = it,
        )
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
