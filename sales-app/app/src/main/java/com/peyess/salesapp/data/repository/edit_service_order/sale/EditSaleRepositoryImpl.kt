package com.peyess.salesapp.data.repository.edit_service_order.sale

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.dao.sale.active_sale.LocalSaleDocument
import com.peyess.salesapp.data.adapter.edit_service_order.sale.toEditSaleEntity
import com.peyess.salesapp.data.adapter.edit_service_order.sale.toSaleDocument
import com.peyess.salesapp.data.dao.edit_service_order.sale.EditSaleDao
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity
import com.peyess.salesapp.data.repository.edit_service_order.sale.error.InsertSaleError
import com.peyess.salesapp.data.repository.edit_service_order.sale.error.ReadSaleError
import com.peyess.salesapp.data.repository.edit_service_order.sale.error.SaleExistsError
import com.peyess.salesapp.data.repository.edit_service_order.sale.error.UpdateSaleError
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditSaleRepositoryImpl @Inject constructor(
    private val saleDao: EditSaleDao,
): EditSaleRepository {
    override suspend fun addSale(
        sale: LocalSaleDocument,
    ): EditSaleInsertResponse = Either.catch {
        saleDao.addSale(sale.toEditSaleEntity())
    }.mapLeft {
        InsertSaleError.Unexpected(
            description = "Error while inserting sale $sale",
            throwable = it,
        )
    }

    override suspend fun saleExits(id: String): SaleExistsResponse = Either.catch {
        saleDao.saleExists(id) > 0
    }.mapLeft {
        SaleExistsError.Unexpected(
            description = "Error while checking if sale exists with id $id",
        )
    }

    override suspend fun findSaleById(
        id: String,
    ): EditSaleFetchResponse = Either.catch {
        saleDao.findSaleById(id)?.toSaleDocument()
    }.mapLeft {
        ReadSaleError.Unexpected(
            description = "Error while reading sale with id $id",
            throwable = it,
        )
    }.leftIfNull {
        ReadSaleError.SaleNotFound(
            description = "Sale not found with id $id"
        )
    }

    override fun streamSaleById(id: String): EditSaleStreamResponse {
        return saleDao.streamSaleById(id).map {
            if (it != null) {
                it.toSaleDocument().right()
            } else {
                ReadSaleError.SaleNotFound(
                    description = "Sale not found with id $id"
                ).left()
            }
        }
    }

    override suspend fun updateIsUploading(
        id: String,
        isUploading: Boolean,
    ): EditSaleUpdateResponse = Either.catch {
        saleDao.updateIsUploading(id, isUploading)
    }.mapLeft {
        UpdateSaleError.Unexpected(
            description = "Error while updating isUploading for sale with id $id",
            throwable = it,
        )
    }
}
