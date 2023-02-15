package com.peyess.salesapp.data.repository.edit_service_order.client_picked

import arrow.core.Either
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedDocument
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.InsertClientPickedError
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.ReadClientPickedError
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.UpdateClientPickedError
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.typing.sale.ClientRole
import kotlinx.coroutines.flow.Flow

typealias EditClientPickedAddResponse = Either<InsertClientPickedError, Unit>
typealias EditClientPickedFetchResponse = Either<ReadClientPickedError, EditClientPickedDocument>
typealias EditClientPickedStreamResponse = Flow<Either<ReadClientPickedError, EditClientPickedDocument>>
typealias EditClientPickedUpdateResponse = Either<UpdateClientPickedError, Unit>

interface EditClientPickedRepository {
    suspend fun insertClientPicked(
        clientPicked: EditClientPickedDocument,
    ): EditClientPickedAddResponse

    suspend fun findClientPickedForServiceOrder(
        serviceOrderId: String,
    ): EditClientPickedFetchResponse
    fun streamClientPickedForServiceOrder(
        serviceOrderId: String,
    ): EditClientPickedStreamResponse

    suspend fun updateClientRole(
        serviceOrderId: String,
        clientRole: ClientRole,
    ): EditClientPickedUpdateResponse
    suspend fun updateNameDisplay(
        serviceOrderId: String,
        name_display: String,
    ): EditClientPickedUpdateResponse
    suspend fun updateName(
        serviceOrderId: String,
        name: String,
    ): EditClientPickedUpdateResponse
    suspend fun updateSex(
        serviceOrderId: String,
        sex: Sex,
    ): EditClientPickedUpdateResponse
    suspend fun updateEmail(
        serviceOrderId: String,
        email: String,
    ): EditClientPickedUpdateResponse
    suspend fun updateDocument(
        serviceOrderId: String,
        document: String,
    ): EditClientPickedUpdateResponse
    suspend fun updateShortAddress(
        serviceOrderId: String,
        shortAddress: String,
    ): EditClientPickedUpdateResponse
}