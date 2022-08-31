package com.peyess.salesapp.repository.sale

import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
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

    fun currentPositioning(eye: Eye): Flow<PositioningEntity>
    fun updatePositioning(positioning: PositioningEntity)

    fun currentFramesData(): Flow<FramesEntity>
    fun updateFramesData(frames: FramesEntity)

    fun lensTypeCategories(): Flow<List<LensTypeCategory>>

    fun addLensForComparison(comparisonEntity: LensComparisonEntity)
    fun comparisons(): Flow<List<LensComparisonEntity>>
    fun removeComparison(id: Int)
    fun updateSaleComparison(comparison: LensComparisonEntity)

    fun pickProduct(productPicked: ProductPickedEntity)
    fun pickedProduct(): Flow<ProductPickedEntity?>

    fun pickClient(client: ClientEntity)
    fun clientPicked(role: ClientRole): Flow<ClientEntity?>

    fun payments(): Flow<List<SalePaymentEntity>>
    fun paymentById(paymentId: Long): Flow<SalePaymentEntity?>
    fun addPayment(payment: SalePaymentEntity): Long
    fun updatePayment(payment: SalePaymentEntity)
}