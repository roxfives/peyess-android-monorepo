package com.peyess.salesapp.screen.create_client.communication.state

import android.content.Context
import arrow.core.Either
import arrow.core.continuations.either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.client.error.UpdateClientRepositoryError
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.management_picture_upload.PictureUploadRepository
import com.peyess.salesapp.screen.create_client.adapter.toCacheCreateClientDocument
import com.peyess.salesapp.screen.create_client.adapter.toClient
import com.peyess.salesapp.screen.create_client.adapter.toClientModel
import com.peyess.salesapp.screen.create_client.adapter.toClientPickedEntity
import com.peyess.salesapp.screen.create_client.adapter.toLocalClientDocument
import com.peyess.salesapp.screen.create_client.model.Client
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.screen.create_client.adapter.toClientPickedDocument
import com.peyess.salesapp.screen.create_client.adapter.toPictureUploadDocument
import com.peyess.salesapp.screen.create_client.communication.error.CreateClientError
import com.peyess.salesapp.screen.create_client.communication.error.SearchExistingClientError
import com.peyess.salesapp.screen.create_client.communication.error.UpdateClientError
import com.peyess.salesapp.workmanager.picture_upload.enqueuePictureUploadManagerWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

private const val maxPhoneLength = 10
private const val maxCellphoneLength = 11

class CommunicationViewModel @AssistedInject constructor(
    @Assisted initialState: CommunicationState,
    private val salesApplication: SalesApplication,
    private val authenticationRepository: AuthenticationRepository,
    private val pictureUploadRepository: PictureUploadRepository,
    private val clientRepository: ClientRepository,
    private val localClientRepository: LocalClientRepository,
    private val cacheCreateClientRepository: CacheCreateClientRepository,
    private val saleRepository: SaleRepository,
    private val editClientPickedRepository: EditClientPickedRepository,
): MavericksViewModel<CommunicationState>(initialState) {

    init {
        onEach(CommunicationState::clientId) { loadClient(it) }

        onAsync(CommunicationState::clientResponseAsync) { processLoadClientResponse(it) }

        onAsync(CommunicationState::uploadClientResponseAsync) {
            processCreateOrUpdateClientResponse(it)
        }
    }

    private fun updateClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            cacheCreateClientRepository.update(client.toCacheCreateClientDocument())
        }
    }

    private fun loadClient(clientId: String) {
        suspend {
            cacheCreateClientRepository.getById(clientId)
        }.execute {
            copy(clientResponseAsync = it)
        }
    }

    private fun processLoadClientResponse(
        response: CacheCreateClientFetchSingleResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    clientResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(
                    client = it.toClient(),

                    emailInput = it.email,
                    cellphoneInput = it.cellphone,
                    whatsappInput = it.whatsapp,
                    phoneInput = it.phone,
                    phoneHasWhatsApp = it.phoneHasWhatsApp,
                    hasPhoneContact = it.hasPhoneContact,
                    hasAcceptedPromotionalMessages = it.hasAcceptedPromotionalMessages,
                )
            },
        )
    }

    private suspend fun addAllForServiceOrder(client: Client) {
        saleRepository.activeSO()
            .filterNotNull()
            .take(1)
            .collect { so ->
                saleRepository.pickClient(client.toClientPickedEntity(so.id, ClientRole.User))
                saleRepository.pickClient(client.toClientPickedEntity(so.id, ClientRole.Responsible))
            }
    }

    private suspend fun addOnlyOne(client: Client, role: ClientRole) {
        saleRepository.activeSO()
            .filterNotNull()
            .take(1)
            .collect { so ->
                saleRepository.pickClient(client.toClientPickedEntity(so.id, role))
            }
    }

    private suspend fun addOnlyOneForEdit(client: Client, role: ClientRole) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            editClientPickedRepository.insertClientPicked(
                clientPicked = client.toClientPickedDocument(
                    serviceOrderId = it.serviceOrderId,
                     role = role,
                )
            )
        }
    }

    private suspend fun addClientToSale(
        client: Client,
        createScenarioParam: CreateScenario,
    ) {
        when (createScenarioParam) {
            CreateScenario.ServiceOrder -> addAllForServiceOrder(client)
            CreateScenario.Responsible -> addOnlyOne(client, ClientRole.Responsible)
            CreateScenario.User -> addOnlyOne(client, ClientRole.User)
            CreateScenario.Witness -> addOnlyOne(client, ClientRole.Witness)

            CreateScenario.EditResponsible -> addOnlyOneForEdit(client, ClientRole.Responsible)
            CreateScenario.EditUser -> addOnlyOneForEdit(client, ClientRole.User)
            CreateScenario.EditWitness -> addOnlyOneForEdit(client, ClientRole.Witness)

            else -> {
                Timber.w(
                    "Function addClientToSale should not " +
                            "have been called with parameter $createScenarioParam"
                )
            }
        }
    }

    private suspend fun addClientPictureToUpload(pictureUploadDocument: PictureUploadDocument) {
        pictureUploadRepository.addPicture(pictureUploadDocument).tap {
            enqueuePictureUploadManagerWorker(
                context = salesApplication as Context,
                uploadEntryId = it,
            )
        }
    }

    private suspend fun createOrUpdateClient(
        client: Client,
        hasAcceptedPromotionalMessages: Boolean,
        createScenario: CreateScenario,
    ): CreateOrUpdateClientResponse = either {
        val collaboratorId = authenticationRepository.fetchCurrentUserId()
        val clientModel = client.toClientModel()
        val localClient = client.toLocalClientDocument(collaboratorId)

        val existingClientId = clientRepository
            .clientExistsByDocument(client.document)
            .mapLeft {
                Timber.e("Error while checking if client exists: $it")
                SearchExistingClientError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()

        val clientExists = existingClientId.isNotBlank()
        val clientId = existingClientId.ifBlank { client.id }

        if (clientExists) {
            updateClient(clientModel.copy(id = clientId), hasAcceptedPromotionalMessages).bind()
            localClientRepository.createClient(localClient.copy(id = clientId)).mapLeft {
                Timber.e("Error while inserting client: $it")
                CreateClientError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()
        } else {
            uploadClient(clientModel, hasAcceptedPromotionalMessages).bind()
            localClientRepository.insertClient(localClient).mapLeft {
                Timber.e("Error while inserting client: $it")
                CreateClientError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()
        }

        val clientPicture = client.toPictureUploadDocument(salesApplication, clientId)
        addClientPictureToUpload(clientPicture)

        checkCreateScenario(client.copy(id = clientId), createScenario)
        clientId
    }

    private fun processCreateOrUpdateClientResponse(
        response: CreateOrUpdateClientResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    clientResponseAsync = Fail(it.error),
                    uploadedId = "",
                )
            },

            ifRight = {
                copy(
                    uploadedId = it,
                    clientCreated = true,
                )
            }
        )
    }

    private suspend fun checkCreateScenario(
        client: Client,
        createScenario: CreateScenario,
    ) {
        if (createScenario != CreateScenario.Home && createScenario != CreateScenario.Payment) {
            addClientToSale(client, createScenario)
        }
    }

    private suspend fun updateClient(
        client: ClientModel,
        hasAcceptedPromotionalMessages: Boolean,
    ): Either<UpdateClientError, Unit> {
        return clientRepository.updateClient(
            clientModel = client,
            hasAcceptedPromotionalMessages = hasAcceptedPromotionalMessages,
        ).mapLeft {
            Timber.e("Error while updating client: $it")

            when (it) {
                is UpdateClientRepositoryError.NotFound ->
                    UpdateClientError.NotFound(
                        description = it.description,
                        throwable = it.error,
                    )
                is UpdateClientRepositoryError.Unexpected ->
                    UpdateClientError.Unexpected(
                        description = it.description,
                        throwable = it.error,
                    )
            }
        }
    }

    private suspend fun uploadClient(
        client: ClientModel,
        hasAcceptedPromotionalMessages: Boolean,
    ): Either<CreateClientError, Unit> {
        return clientRepository.uploadClient(
            clientModel = client,
            hasAcceptedPromotionalMessages = hasAcceptedPromotionalMessages,
        ).mapLeft {
            Timber.e("Error while creating client: $it")
            CreateClientError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    fun onSaleIdChanged(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onServiceOrderIdChanged(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun onClientIdChanged(clientId: String) = setState {
        copy(clientId = clientId)
    }

    fun onPaymentIdChanged(paymentId: Long) = setState {
        copy(paymentId = paymentId)
    }

    fun onCreateScenarioChanged(createScenario: CreateScenario) = setState {
        copy(createScenario = createScenario)
    }

    fun onEmailChanged(email: String) = setState {
        val update = client.copy(email = email)

        updateClient(update)
        copy(
            client = update,
            emailInput = email,
        )
    }

    fun onCellphoneChanged(cellphone: String) = setState {
        val cellphoneNumber = if (cellphone.length <= maxCellphoneLength) {
            cellphone
        } else {
            cellphone.substring(0 until maxCellphoneLength)
        }
        val whatsappUpdate = if (phoneHasWhatsApp) cellphoneNumber else whatsappInput

        val update = client.copy(
            cellphone = cellphoneNumber,
            whatsapp = whatsappUpdate,
        )

        updateClient(update)
        copy(
            client = update,

            cellphoneInput = cellphoneNumber,
            whatsappInput = whatsappUpdate,
        )
    }

    fun onWhatsappChanged(whatsapp: String) = setState {
        val phoneNumber = if (whatsapp.length <= maxCellphoneLength) {
            whatsapp
        } else {
            whatsapp.substring(0 until maxCellphoneLength)
        }

        val update = client.copy(whatsapp = phoneNumber)
        updateClient(update)

        copy(
            client = update,
            whatsappInput = phoneNumber,
        )
    }

    fun onPhoneChanged(phone: String) = setState {
        val phoneNumber = if (phone.length <= maxPhoneLength) {
            phone
        } else {
            phone.substring(0 until maxPhoneLength)
        }

        val update = client.copy(phone = phoneNumber)

        updateClient(update)
        copy(
            client = update,
            phoneInput = phoneNumber,
        )
    }

    fun onPhoneHasWhatsappChanged(cellphoneHasWhatsApp: Boolean) = setState {
        val whatsappUpdate = if (cellphoneHasWhatsApp) cellphoneInput else ""

        val update = client.copy(whatsapp = whatsappUpdate)

        updateClient(update)
        copy(
            client = update,
            phoneHasWhatsApp = cellphoneHasWhatsApp,
        )
    }

    fun onHasPhoneChanged(hasPhone: Boolean) = setState {
        val update = client.copy(hasPhoneContact = hasPhone)

        updateClient(update)
        copy(
            client = update,
            hasPhoneContact = hasPhone,
        )
    }

    fun onHasAcceptedPromotionalMessages(hasAccepted: Boolean) = setState {
        val update = client.copy(hasAcceptedPromotionalMessages = hasAccepted)

        updateClient(update)
        copy(
            client = update,
            hasAcceptedPromotionalMessages = hasAccepted,
        )
    }

    fun onFinishSettingCommunication()  = withState {
        suspend {
            cacheCreateClientRepository.update(it.client.toCacheCreateClientDocument())
        }.execute {
            copy(hasFinishedSettingCommunication = it is Success)
        }
    }

    fun onNavigate() = setState {
        copy(hasFinishedSettingCommunication = false)
    }

    fun onDetectEmailError() = setState {
        copy(detectEmailError = true)
    }

    fun onDetectCellphoneError() = setState {
        copy(detectCellphoneError = true)
    }

    fun onDetectWhatsappError() = setState {
        copy(detectWhatsappError = true)
    }

    fun onDetectPhoneError() = setState {
        copy(detectPhoneError = true)
    }

    fun createClient() = withState {
        suspend {
            createOrUpdateClient(
                client = it.client,
                hasAcceptedPromotionalMessages = it.hasAcceptedPromotionalMessages,
                createScenario = it.createScenario,
            )
        }.execute(Dispatchers.IO) {
            copy(uploadClientResponseAsync = it)
        }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<CommunicationViewModel, CommunicationState> {
        override fun create(state: CommunicationState): CommunicationViewModel
    }

    companion object: MavericksViewModelFactory<CommunicationViewModel, CommunicationState>
        by hiltMavericksViewModelFactory()
}