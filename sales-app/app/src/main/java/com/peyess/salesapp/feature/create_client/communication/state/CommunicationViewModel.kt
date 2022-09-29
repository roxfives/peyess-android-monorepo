package com.peyess.salesapp.feature.create_client.communication.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.repository.client.ClientRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

private const val maxPhoneLength = 10
private const val maxCellphoneLength = 11

class CommunicationViewModel @AssistedInject constructor(
    @Assisted initialState: CommunicationState,
    private val clientRepository: ClientRepository,
): MavericksViewModel<CommunicationState>(initialState) {

    init {
        loadClient()
    }

    private fun loadClient() = withState {
        clientRepository
            .latestLocalClientCreated()
            .execute {
                copy(_clientAsync = it)
            }
    }

    private fun updateClient(client: ClientModel) {
        viewModelScope.launch(Dispatchers.IO) {
            clientRepository.updateLocalClient(client)
        }
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

    fun createClient() = withState {
        suspend {
            clientRepository.uploadClient(it.client, it.hasAcceptedPromotionalMessages)
            clientRepository.clearCreateClientCache(it.client.id)
        }.execute(Dispatchers.IO) {
            Timber.i("Upload client: $it")
            copy(uploadClientAsync = it)
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