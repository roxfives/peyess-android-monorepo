package com.peyess.salesapp.repository.sale

import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.model.products.LensTypeCategory
import kotlinx.coroutines.flow.Flow

interface SaleRepository {
    fun createSale(): Flow<Boolean>

    fun activeSale(): Flow<ActiveSalesEntity?>
    fun updateActiveSO(activeSOEntity: ActiveSOEntity)
    fun activeSO(): Flow<ActiveSOEntity?>

    fun updateSO(so: ActiveSOEntity)

    fun currentPrescriptionPicture(): Flow<PrescriptionPictureEntity>
    fun updatePrescriptionPicture(prescriptionPictureEntity: PrescriptionPictureEntity)

    fun currentPrescriptionData(): Flow<PrescriptionDataEntity>
    fun updatePrescriptionData(prescriptionDataEntity: PrescriptionDataEntity)

    fun lensTypeCategories(): Flow<List<LensTypeCategory>>
}