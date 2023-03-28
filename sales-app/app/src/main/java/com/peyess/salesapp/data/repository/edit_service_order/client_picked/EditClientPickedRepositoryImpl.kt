package com.peyess.salesapp.data.repository.edit_service_order.client_picked

import arrow.core.Either
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.data.adapter.edit_service_order.client_picked.toEditClientPickedDocument
import com.peyess.salesapp.data.adapter.edit_service_order.client_picked.toEditClientPickedEntity
import com.peyess.salesapp.data.dao.edit_service_order.client_picked.EditClientPickedDao
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedDocument
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedEntity
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.DeleteClientPickedError
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.InsertClientPickedError
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.ReadClientPickedError
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.UpdateClientPickedError
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.typing.sale.ClientRole
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EditClientPickedRepositoryImpl @Inject constructor(
    private val clientPickedDao: EditClientPickedDao
): EditClientPickedRepository {
    override suspend fun insertClientPicked(
        clientPicked: EditClientPickedDocument,
    ): EditClientPickedAddResponse = Either.catch {
        clientPickedDao.insertClientPicked(clientPicked.toEditClientPickedEntity())
    }.mapLeft {
        InsertClientPickedError.Unexpected(
            description = "Unexpected error when inserting client picked $clientPicked",
            throwable = it,
        )
    }

    override suspend fun allClientsForServiceOrder(
        serviceOrderId: String,
    ): EditClientPickedFetchAllResponse = Either.catch {
        clientPickedDao.allClientsPickedForServiceOrder(serviceOrderId).map {
            it.toEditClientPickedDocument()
        }
    }.mapLeft {
        ReadClientPickedError.Unexpected(
            description = "Unexpected error when reading all " +
                    "clients picked for service order $serviceOrderId",
            throwable = it,
        )
    }

    override suspend fun findClientPickedForServiceOrder(
        serviceOrderId: String,
        role: ClientRole,
    ): EditClientPickedFetchResponse = Either.catch {
        clientPickedDao
            .findClientPickedForServiceOrder(serviceOrderId, role)
            ?.toEditClientPickedDocument()
    }.mapLeft {
        ReadClientPickedError.Unexpected(
            description = "Unexpected error when reading client picked for service order $serviceOrderId",
            throwable = it,
        )
    }.leftIfNull {
        ReadClientPickedError.ClientPickedNotFound(
            description = "Client picked not found for service order $serviceOrderId",
        )
    }

    override fun streamClientPickedForServiceOrder(
        serviceOrderId: String,
        role: ClientRole,
    ): EditClientPickedStreamResponse {
        return clientPickedDao.streamClientPickedForServiceOrder(serviceOrderId, role).map {
            if (it != null) {
                it.toEditClientPickedDocument().right()
            } else {
                ReadClientPickedError.ClientPickedNotFound(
                    description = "Client picked not found for service order $serviceOrderId",
                ).left()
            }
        }
    }

    override suspend fun updateClientRole(
        serviceOrderId: String,
        clientRole: ClientRole,
    ): EditClientPickedUpdateResponse = Either.catch {
        clientPickedDao.updateClientRole(serviceOrderId, clientRole)
    }.mapLeft {
        UpdateClientPickedError.Unexpected(
            description = "Error while updating client for so $serviceOrderId " +
                    "with clientRole = $clientRole",
            throwable = it,
        )
    }

    override suspend fun updateNameDisplay(
        serviceOrderId: String,
        name_display: String,
    ): EditClientPickedUpdateResponse = Either.catch {
        clientPickedDao.updateNameDisplay(serviceOrderId, name_display)
    }.mapLeft {
        UpdateClientPickedError.Unexpected(
            description = "Error while updating client for so $serviceOrderId " +
                    "with name_display = $name_display",
            throwable = it,
        )
    }

    override suspend fun updateName(
        serviceOrderId: String,
        name: String,
    ): EditClientPickedUpdateResponse = Either.catch {
        clientPickedDao.updateName(serviceOrderId, name)
    }.mapLeft {
        UpdateClientPickedError.Unexpected(
            description = "Error while updating client for so $serviceOrderId " +
                    "with name = $name",
            throwable = it,
        )
    }

    override suspend fun updateSex(
        serviceOrderId: String,
        sex: Sex,
    ): EditClientPickedUpdateResponse = Either.catch {
        clientPickedDao.updateSex(serviceOrderId, sex)
    }.mapLeft {
        UpdateClientPickedError.Unexpected(
            description = "Error while updating client for so $serviceOrderId " +
                    "with sex = $sex",
            throwable = it,
        )
    }

    override suspend fun updateEmail(
        serviceOrderId: String,
        email: String,
    ): EditClientPickedUpdateResponse = Either.catch {
        clientPickedDao.updateEmail(serviceOrderId, email)
    }.mapLeft {
        UpdateClientPickedError.Unexpected(
            description = "Error while updating client for so $serviceOrderId " +
                    "with email = $email",
            throwable = it,
        )
    }

    override suspend fun updateDocument(
        serviceOrderId: String,
        document: String,
    ): EditClientPickedUpdateResponse = Either.catch {
        clientPickedDao.updateDocument(serviceOrderId, document)
    }.mapLeft {
        UpdateClientPickedError.Unexpected(
            description = "Error while updating client for so $serviceOrderId " +
                    "with document = $document",
            throwable = it,
        )
    }

    override suspend fun updateShortAddress(
        serviceOrderId: String,
        shortAddress: String,
    ): EditClientPickedUpdateResponse = Either.catch {
        clientPickedDao.updateShortAddress(serviceOrderId, shortAddress)
    }.mapLeft {
        UpdateClientPickedError.Unexpected(
            description = "Error while updating client for so $serviceOrderId " +
                    "with shortAddress = $shortAddress",
            throwable = it,
        )
    }

    override suspend fun deleteClientsPickedForServiceOrder(
        serviceOrderId: String,
    ): EditClientPickedDeleteResponse = Either.catch {
        clientPickedDao.deleteClientsPickedForServiceOrder(serviceOrderId)
    }.mapLeft {
        DeleteClientPickedError.Unexpected(
            description = "Error while deleting client for so $serviceOrderId",
            throwable = it,
        )
    }
}
