package com.peyess.salesapp.data.repository.discount

import com.peyess.salesapp.data.adapter.discount.toOverallDiscountDocument
import com.peyess.salesapp.data.adapter.discount.toOverallDiscountEntity
import com.peyess.salesapp.data.dao.discount.OverallDiscountDao
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
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