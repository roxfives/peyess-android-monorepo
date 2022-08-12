package com.peyess.salesapp.repository.sale

import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun startSale(): Flow<ActiveSalesEntity>

    fun activeSale(): Flow<ActiveSalesEntity>
    fun activeSO(): Flow<ActiveSOEntity>

    fun currentPrescriptionPicture(): Flow<PrescriptionPictureEntity>
    fun updatePrescriptionPicture(prescriptionPictureEntity: PrescriptionPictureEntity)
}