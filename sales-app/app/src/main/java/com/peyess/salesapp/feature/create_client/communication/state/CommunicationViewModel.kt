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
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch

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
            .take(1)
            .execute {
                copy(_clientAsync = it)
            }
    }

    private fun updateClient(client: ClientModel) {
        viewModelScope.launch(Dispatchers.IO) {
            clientRepository.updateLocalClient(client)
        }
    }

    fun onEmailChanged(email: String) = setState {
        val update = client.copy(email = email)
        updateClient(update)

        copy(email = email)
    }

    fun onCellphoneChanged(cellphone: String) = setState {
        val cellphoneNumber = if (cellphone.length <= maxCellphoneLength) {
            cellphone
        } else {
            cellphone.substring(0 until maxCellphoneLength)
        }

        val whatsappUpdate = if (phoneHasWhatsApp) cellphoneNumber else whatsapp

        val update = client.copy(cellphone = cellphoneNumber, whatsapp = whatsappUpdate)
        updateClient(update)

        copy(cellphone = cellphoneNumber, whatsapp = whatsappUpdate)
    }

    fun onWhatsappChanged(whatsapp: String) = setState {
        val phoneNumber = if (whatsapp.length <= maxCellphoneLength) {
            whatsapp
        } else {
            whatsapp.substring(0 until maxCellphoneLength)
        }

        val update = client.copy(whatsapp = phoneNumber)
        updateClient(update)

        copy(whatsapp = phoneNumber)
    }

    fun onPhoneChanged(phone: String) = setState {
        val phoneNumber = if (phone.length <= maxPhoneLength) {
            phone
        } else {
            phone.substring(0 until maxPhoneLength)
        }

        val update = client.copy(phone = phoneNumber)
        updateClient(update)

        copy(phone = phoneNumber)
    }

    fun onPhoneHasWhatsappChanged(cellphoneHasWhatsApp: Boolean) = setState {
        val whatsappUpdate = if (cellphoneHasWhatsApp) cellphone else ""

        copy(
            phoneHasWhatsApp = cellphoneHasWhatsApp,
            whatsapp = whatsappUpdate,
        )
    }

    fun onHasPhoneChanged(hasPhone: Boolean) = setState {
        copy(hasPhoneContact = hasPhone)
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

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<CommunicationViewModel, CommunicationState> {
        override fun create(state: CommunicationState): CommunicationViewModel
    }

    companion object: MavericksViewModelFactory<CommunicationViewModel, CommunicationState>
        by hiltMavericksViewModelFactory()
}