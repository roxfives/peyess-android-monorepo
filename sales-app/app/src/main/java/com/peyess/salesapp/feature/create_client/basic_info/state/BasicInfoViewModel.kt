package com.peyess.salesapp.feature.create_client.basic_info.state

import android.net.Uri
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
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
import java.time.LocalDate
import java.time.ZoneId

class BasicInfoViewModel @AssistedInject constructor(
    @Assisted initialState: BasicInfoState,
    private val clientRepository: ClientRepository,
): MavericksViewModel<BasicInfoState>(initialState) {
    private val maxDocumentLength = 11

    init {
        loadOrCreateClient()
    }

    private fun createNewClient() {
        suspend { clientRepository.createNewLocalClient() }
    }


    private fun updateClient(client: ClientModel) {
        viewModelScope.launch(Dispatchers.IO) {
            clientRepository.updateLocalClient(client)
        }
    }

    private fun loadOrCreateClient() = withState {
        clientRepository
            .latestLocalClientCreated()
            .execute {
                if (it is Success && it.invoke() == null) {
                    createNewClient()
                }

                copy(_clientAsync = it)
            }
    }

    fun onPictureChanged(picture: Uri) = setState {
        val client = client.copy(picture = picture)
        updateClient(client)

        copy(picture = picture)
    }

    fun onNameChanged(value: String) = setState {
        val client = client.copy(name = value)
        updateClient(client)

        copy(name = value)
    }

    fun onNameDisplayChanged(value: String) = setState {
        val client = client.copy(nameDisplay = value)
        updateClient(client)

        copy(nameDisplay = value)
    }

    fun onBirthdayChanged(value: LocalDate) = setState {
        val day = value.atStartOfDay(ZoneId.systemDefault())

        val client = client.copy(birthday = day)
        updateClient(client)

        copy(birthday = day)
    }

    fun onDocumentChanged(value: String) = setState {
        val document = if (value.length <= maxDocumentLength) {
            value
        } else {
            value.substring(0 until maxDocumentLength)
        }

        val client = client.copy(document = document)
        updateClient(client)

        copy(document = document)
    }

    fun onDetectNameError() = setState {
        copy(detectNameError = true)
    }

    fun onDetectNameDisplayError() = setState {
        copy(detectNameDisplayError = true)
    }

    fun onDetectDocumentError() = setState {
        copy(detectDocumentError = true)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<BasicInfoViewModel, BasicInfoState> {
        override fun create(state: BasicInfoState): BasicInfoViewModel
    }

    companion object: MavericksViewModelFactory<BasicInfoViewModel, BasicInfoState> by hiltMavericksViewModelFactory()
}