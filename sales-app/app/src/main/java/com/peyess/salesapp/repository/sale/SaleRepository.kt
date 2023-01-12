package com.peyess.salesapp.repository.sale

import arrow.core.Either
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument
import com.peyess.salesapp.repository.sale.error.ActiveSaleError
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderError
import com.peyess.salesapp.repository.sale.error.ProductPickedError
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import kotlinx.coroutines.flow.Flow

typealias ActiveSaleResponse = Either<ActiveSaleError, ActiveSalesEntity>
typealias ActiveServiceOrderResponse = Either<ActiveServiceOrderError, ActiveSOEntity>

typealias ProductPickedResponse = Either<ProductPickedError, ProductPickedDocument>

interface SaleRepository {
    fun createSale(): Flow<Boolean>

    fun activeSale(): Flow<ActiveSalesEntity?>
    suspend fun currentSale(): ActiveSaleResponse

    fun updateActiveSO(activeSOEntity: ActiveSOEntity)
    fun activeSO(): Flow<ActiveSOEntity?>
    suspend fun currentServiceOrder(): ActiveServiceOrderResponse

    fun updateSO(so: ActiveSOEntity)

    fun currentPrescriptionPicture(): Flow<PrescriptionPictureEntity>
    fun updatePrescriptionPicture(prescriptionPictureEntity: PrescriptionPictureEntity)

    fun currentPrescriptionData(): Flow<PrescriptionDataEntity>
    fun updatePrescriptionData(prescriptionDataEntity: PrescriptionDataEntity)

    fun currentPositioning(eye: Eye): Flow<PositioningEntity>
    fun updatePositioning(positioning: PositioningEntity)

    fun currentFramesData(): Flow<FramesEntity>
    fun updateFramesData(frames: FramesEntity)

    fun lensTypeCategories(): Flow<List<LensTypeCategoryDocument>>

    fun pickProduct(productPicked: ProductPickedEntity)
    fun pickedProduct(): Flow<ProductPickedEntity?>

    suspend fun productPicked(serviceOrderId: String): ProductPickedResponse

    fun pickClient(client: ClientEntity)
    fun clientPicked(role: ClientRole): Flow<ClientEntity?>

    fun payments(): Flow<List<SalePaymentEntity>>
    fun paymentById(paymentId: Long): Flow<SalePaymentEntity?>
    fun addPayment(payment: SalePaymentEntity): Long
    fun updatePayment(payment: SalePaymentEntity)
    fun deletePayment(payment: SalePaymentEntity)
}