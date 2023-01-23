package com.peyess.salesapp.feature.create_client.communication.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.client.firestore.toClientPickedEntity
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.data.adapter.client.toClientDocument
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
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
    private val clientRepository: ClientRepository,
    private val saleRepository: SaleRepository,
): MavericksViewModel<CommunicationState>(initialState) {

    init {
        loadClient()
        loadServiceOrderData()

        onAsync(CommunicationState::activeServiceOrderResponseAsync) {
            processServiceOrderDataResponse(it)
        }
    }

    private fun loadClient() = withState {
        clientRepository
            .latestLocalClientCreated()
            .execute {
                copy(_clientAsync = it)
            }
    }

    private fun processServiceOrderDataResponse(response: ActiveServiceOrderResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    activeServiceOrderResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(activeServiceOrderResponse = it) }
        )
    }

    private fun loadServiceOrderData() {
        suspend {
            saleRepository.currentServiceOrder()
        }.execute {
            copy(activeServiceOrderResponseAsync = it)
        }
    }

    private fun updateClient(client: ClientModel) {
        viewModelScope.launch(Dispatchers.IO) {
            clientRepository.updateLocalClient(client)
        }
    }

    private suspend fun addAllForServiceOrder(client: ClientDocument) {
        saleRepository.activeSO()
            .filterNotNull()
            .take(1)
            .collect { so ->
                saleRepository.pickClient(client.toClientPickedEntity(so.id, ClientRole.User))
                saleRepository.pickClient(client.toClientPickedEntity(so.id, ClientRole.Responsible))
            }
    }

    private suspend fun addOnlyOne(client: ClientDocument, role: ClientRole) {
        saleRepository.activeSO()
            .filterNotNull()
            .take(1)
            .collect { so ->
                saleRepository.pickClient(client.toClientPickedEntity(so.id, role))
            }
    }

    fun updatePickScenario(
        createScenarioParam: String,
        paymentId: Long,
    ) = setState {
        copy(
            createScenarioParam = CreateScenario.fromName(createScenarioParam)
                ?: CreateScenario.Home,
            paymentId = paymentId,
        )
    }

    fun onEmailChanged(email: String) = withState {
        val update = it.client.copy(email = email)
        updateClient(update)
    }

    fun onCellphoneChanged(cellphone: String) = withState {
        val cellphoneNumber = if (cellphone.length <= maxCellphoneLength) {
            cellphone
        } else {
            cellphone.substring(0 until maxCellphoneLength)
        }

        val whatsappUpdate = if (it.phoneHasWhatsApp) cellphoneNumber else it.whatsapp

        val update = it.client.copy(cellphone = cellphoneNumber, whatsapp = whatsappUpdate)
        updateClient(update)
    }

    fun onWhatsappChanged(whatsapp: String) = withState {
        val phoneNumber = if (whatsapp.length <= maxCellphoneLength) {
            whatsapp
        } else {
            whatsapp.substring(0 until maxCellphoneLength)
        }

        val update = it.client.copy(whatsapp = phoneNumber)
        updateClient(update)
    }

    fun onPhoneChanged(phone: String) = withState {
        val phoneNumber = if (phone.length <= maxPhoneLength) {
            phone
        } else {
            phone.substring(0 until maxPhoneLength)
        }

        val update = it.client.copy(phone = phoneNumber)
        updateClient(update)
    }

    fun onPhoneHasWhatsappChanged(cellphoneHasWhatsApp: Boolean) = setState {
        val whatsappUpdate = if (cellphoneHasWhatsApp) cellphone else ""

        val update = client.copy(whatsapp = whatsappUpdate)
        updateClient(update)

        copy(
            phoneHasWhatsApp = cellphoneHasWhatsApp,
        )
    }

    fun onHasPhoneChanged(hasPhone: Boolean) = setState {
        copy(hasPhoneContact = hasPhone)
    }

    fun onHasAcceptedPromotionalMessages(hasAccepted: Boolean) = setState {
        copy(hasAcceptedPromotionalMessages = hasAccepted)
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

    private suspend fun addClientToSale(clientModel: ClientModel, createScenarioParam: CreateScenario) {
        val client = clientModel.toClientDocument()

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

    fun createClient() = withState {
        val createdId = it.clientId

        suspend {
            Timber.i("Creating client ${it.client}")

            clientRepository.uploadClient(it.client, it.hasAcceptedPromotionalMessages)

            if (
                it.createScenarioParam != CreateScenario.Home
                    && it.createScenarioParam != CreateScenario.Payment
            ) {
                addClientToSale(it.client, it.createScenarioParam)
            }

            clientRepository.clearCreateClientCache(it.client.id)
        }.execute(Dispatchers.IO) { uploadState ->
            Timber.i("Upload client: $uploadState")
            copy(
                uploadClientAsync = uploadState,
                uploadedId = createdId,
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