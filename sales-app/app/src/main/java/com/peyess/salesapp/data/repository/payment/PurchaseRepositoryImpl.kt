package com.peyess.salesapp.data.repository.payment

import com.peyess.salesapp.data.adapter.purchase.toFSPurchase
import com.peyess.salesapp.data.adapter.purchase.toPurchaseDocument
import com.peyess.salesapp.data.dao.prescription.PrescriptionDao
import com.peyess.salesapp.data.dao.purchase.PurchaseDao
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseDao: PurchaseDao,
): PurchaseRepository {
    override suspend fun add(purchase: PurchaseDocument) {
        val fsPositioning = purchase.toFSPurchase()

        purchaseDao.add(fsPositioning)
    }

    override suspend fun getById(id: String): PurchaseDocument? {
        return purchaseDao.getById(id)?.toPurchaseDocument()
    }
}
