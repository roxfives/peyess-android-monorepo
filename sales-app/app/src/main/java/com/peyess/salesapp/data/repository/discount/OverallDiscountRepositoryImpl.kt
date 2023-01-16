package com.peyess.salesapp.data.repository.discount

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.discount.toOverallDiscountDocument
import com.peyess.salesapp.data.adapter.discount.toOverallDiscountEntity
import com.peyess.salesapp.data.dao.discount.OverallDiscountDao
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.repository.discount.error.OverallDiscountNotFound
import com.peyess.salesapp.data.repository.discount.error.OverallDiscountReadError
import com.peyess.salesapp.data.repository.discount.error.Unexpected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

class OverallDiscountRepositoryImpl @Inject constructor(
    private val discountDao: OverallDiscountDao,
) : OverallDiscountRepository {
    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val currentDiscountForSale: MutableMap<String, Flow<OverallDiscountDocument>> = mutableMapOf()

    override fun watchDiscountForSale(saleId: String): Flow<OverallDiscountDocument> {
        return currentDiscountForSale[saleId]
            ?: createWatcherForSale(saleId)
    }

    override suspend fun getDiscountForSale(saleId: String): OverallDiscountDocument? {
        return discountDao
            .getDiscount(saleId)
            ?.toOverallDiscountDocument()
    }

    override suspend fun updateDiscountForSale(discount: OverallDiscountDocument) {
        val entity = discount.toOverallDiscountEntity()

        Timber.i("Updating discount $entity from $discount")

        discountDao.updateDiscount(entity)
    }

    override fun watchDiscount(saleId: String): Flow<OverallDiscountRepositoryResponse> {
        return discountDao.watchDiscount(saleId)
            .map {
                it?.toOverallDiscountDocument()
                    .right()
                    .leftIfNull {
                        OverallDiscountNotFound("Discount for sale $saleId not found")
                    } as OverallDiscountRepositoryResponse
            }.catch {
                Timber.e(it, "Error while watching discount for sale $saleId")
                val error = Unexpected(
                    description = it.message
                        ?: "Unknown error while watching discount for sale $saleId",
                    error = it,
                )

                emit(error.left())
            }
    }

    override suspend fun discountForSale(
        saleId: String,
    ): OverallDiscountRepositoryResponse = Either.catch {
        discountDao
            .getDiscount(saleId)
            ?.toOverallDiscountDocument()
    }.mapLeft {
        Unexpected(
            description = it.message
                ?: "Unexpected error while getting discount for sale $saleId",
            error = it,
        )
    }.leftIfNull {
        OverallDiscountNotFound(
            description = "Discount for sale $saleId not found",
        )
    }

    private fun createWatcherForSale(saleId: String): Flow<OverallDiscountDocument> {
        Timber.i("Creating flow for sale $saleId")

        val flow = discountDao
            .watchDiscount(saleId)
            .map { it?.toOverallDiscountDocument() ?: OverallDiscountDocument() }
            .shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )

        currentDiscountForSale[saleId] = flow
        return flow
    }
}