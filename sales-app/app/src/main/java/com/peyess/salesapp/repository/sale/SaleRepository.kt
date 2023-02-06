package com.peyess.salesapp.repository.sale

import arrow.core.Either
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.repository.sale.error.ActiveSaleError
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderError
import com.peyess.salesapp.repository.sale.error.ProductPickedError
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import kotlinx.coroutines.flow.Flow

typealias ActiveSaleResponse = Either<ActiveSaleError, ActiveSalesEntity>
typealias ActiveServiceOrderResponse = Either<ActiveServiceOrderError, ActiveSOEntity>
typealias ActiveServiceOrderStreamResponse = Flow<Either<ActiveServiceOrderError, ActiveSOEntity>>

typealias ActiveSalesResponse = Either<ActiveSaleError, List<ActiveSalesEntity>>
typealias ActiveSalesStreamResponse = Either<ActiveSaleError, Flow<List<ActiveSalesEntity>>>

typealias LensTypeCategoriesResponse = Either<ActiveSaleError, List<LensTypeCategoryDocument>>

typealias ProductPickedResponse = Either<ProductPickedError, ProductPickedDocument>

typealias CancelSaleResponse = Either<ActiveSaleError, Unit>
typealias ResumeSaleResponse = Either<ActiveSaleError, Unit>

interface SaleRepository {
    fun createSale(): Flow<Boolean>

    suspend fun cancelCurrentSale(): CancelSaleResponse

    suspend fun cancelSale(sale: ActiveSalesEntity): CancelSaleResponse

    suspend fun findActiveSaleFor(collaboratorId: String): ActiveSalesResponse

    fun activeSalesStreamFor(collaboratorId: String): ActiveSalesStreamResponse

    suspend fun resumeSale(activeSale: ActiveSalesEntity): ResumeSaleResponse

    fun activeSale(): Flow<ActiveSalesEntity?>
    suspend fun currentSale(): ActiveSaleResponse
    suspend fun saleForServiceOrder(serviceOrderId: String): ActiveSaleResponse

    fun updateActiveSO(activeSOEntity: ActiveSOEntity)
    fun activeSO(): Flow<ActiveSOEntity?>
    suspend fun currentServiceOrder(): ActiveServiceOrderResponse
    suspend fun serviceOrder(serviceOrderId: String): ActiveServiceOrderResponse
    fun streamServiceOrder(serviceOrderId: String): ActiveServiceOrderStreamResponse

    fun updateSO(so: ActiveSOEntity)


    fun currentPositioning(eye: Eye): Flow<PositioningEntity>
    fun updatePositioning(positioning: PositioningEntity)

    fun currentFramesData(): Flow<FramesEntity>
    suspend fun updateFrames(frames: FramesEntity)

    suspend fun lensTypeCategories(): LensTypeCategoriesResponse

    fun pickProduct(productPicked: ProductPickedEntity)
    fun pickedProduct(): Flow<ProductPickedEntity?>

    suspend fun productPicked(serviceOrderId: String): ProductPickedResponse

    fun pickClient(client: ClientPickedEntity)
    fun clientPicked(role: ClientRole): Flow<ClientPickedEntity?>

    fun payments(): Flow<List<SalePaymentEntity>>
    fun paymentById(paymentId: Long): Flow<SalePaymentEntity?>
    fun addPayment(payment: SalePaymentEntity): Long
    fun updatePayment(payment: SalePaymentEntity)
    fun deletePayment(payment: SalePaymentEntity)
}