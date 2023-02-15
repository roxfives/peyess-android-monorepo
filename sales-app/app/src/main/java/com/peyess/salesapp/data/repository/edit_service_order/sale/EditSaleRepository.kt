package com.peyess.salesapp.data.repository.edit_service_order.sale

import arrow.core.Either
import com.peyess.salesapp.dao.sale.active_sale.LocalSaleDocument
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity
import com.peyess.salesapp.data.repository.edit_service_order.sale.error.InsertSaleError
import com.peyess.salesapp.data.repository.edit_service_order.sale.error.ReadSaleError
import com.peyess.salesapp.data.repository.edit_service_order.sale.error.UpdateSaleError
import kotlinx.coroutines.flow.Flow

typealias SaleExistsResponse = Either<ReadSaleError, Boolean>
typealias EditSaleInsertResponse = Either<InsertSaleError, Unit>

typealias EditSaleFetchResponse = Either<ReadSaleError, LocalSaleDocument>
typealias EditSaleStreamResponse = Flow<EditSaleFetchResponse>

typealias EditSaleUpdateResponse = Either<UpdateSaleError, Unit>

interface EditSaleRepository {
    suspend fun addSale(sale: LocalSaleDocument): EditSaleInsertResponse

    suspend fun saleExits(id: String): SaleExistsResponse
    suspend fun findSaleById(id: String): EditSaleFetchResponse
    fun streamSaleById(id: String): EditSaleStreamResponse

    suspend fun updateIsUploading(id: String, isUploading: Boolean): EditSaleUpdateResponse
}
