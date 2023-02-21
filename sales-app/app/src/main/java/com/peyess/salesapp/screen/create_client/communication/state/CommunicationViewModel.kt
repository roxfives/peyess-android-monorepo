package com.peyess.salesapp.screen.create_client.communication.state

import android.content.Context
import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.management_picture_upload.PictureUploadRepository
import com.peyess.salesapp.screen.create_client.adapter.toCacheCreateClientDocument
import com.peyess.salesapp.screen.create_client.adapter.toClient
import com.peyess.salesapp.screen.create_client.adapter.toClientModel
import com.peyess.salesapp.screen.create_client.adapter.toClientPickedEntity
import com.peyess.salesapp.screen.create_client.adapter.toLocalClientDocument
import com.peyess.salesapp.screen.create_client.adapter.toPictureUploadDocument
import com.peyess.salesapp.screen.create_client.model.Client
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
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
): MavericksViewModel<CommunicationState>(initialState) {

    init {
        onEach(CommunicationState::clientId) { loadClient(it) }

        onAsync(CommunicationState::clientResponseAsync) { processLoadClientResponse(it) }
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

    private suspend fun addClientToSale(
        client: Client,
        createScenarioParam: CreateScenario,
    ) {
        when (createScenarioParam) {
            CreateScenario.ServiceOrder -> addAllForServiceOrder(client)
            CreateScenario.Responsible -> addOnlyOne(client, ClientRole.Responsible)
            CreateScenario.User -> addOnlyOne(client, ClientRole.User)
            CreateScenario.Witness -> addOnlyOne(client, ClientRole.Witness)
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

    fun createClient() = withState {
        val createdId = it.clientId

        suspend {
            val collaboratorId = authenticationRepository.fetchCurrentUserId()

            Timber.i("Creating client ${it.client}")
            if (
                it.createScenario != CreateScenario.Home
                && it.createScenario != CreateScenario.Payment
            ) {
                addClientToSale(it.client, it.createScenario)
            }

            clientRepository.uploadClient(
                clientModel = it.client.toClientModel(),
                hasAcceptedPromotionalMessages = it.hasAcceptedPromotionalMessages,
            )
            localClientRepository.insertClient(
                it.client.toLocalClientDocument(collaboratorId)
            )

            clientRepository.clearCreateClientCache(it.client.id)

            if (it.client.picture != Uri.EMPTY) {
                val pictureDocument = it.client.toPictureUploadDocument(
                    salesApplication = salesApplication,
                    clientId = it.client.id,
                )

                addClientPictureToUpload(pictureDocument)
            }
        }.execute(Dispatchers.IO) { uploadState ->
            Timber.i("Upload client: $uploadState")
            copy(
                uploadClientAsync = uploadState,
                uploadedId = createdId,
                clientCreated = true,
            )
        }
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<CommunicationViewModel, CommunicationState> {
        override fun create(state: CommunicationState): CommunicationViewModel
    }

    companion object: MavericksViewModelFactory<CommunicationViewModel, CommunicationState>
        by hiltMavericksViewModelFactory()
}