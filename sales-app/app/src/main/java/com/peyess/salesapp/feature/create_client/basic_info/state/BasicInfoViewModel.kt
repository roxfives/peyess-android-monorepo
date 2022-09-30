package com.peyess.salesapp.feature.create_client.basic_info.state

import android.net.Uri
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.model.client.Sex
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
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
        viewModelScope.launch(Dispatchers.IO) {
            Timber.i("Creating new local client now")
            clientRepository.createNewLocalClient()
        }
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
                Timber.i("Loading client is $it with ${it.invoke()}")

                if (it is Success && it.invoke() == null) {
                    Timber.i("Creating new client")
                    createNewClient()
                }

                copy(_clientAsync = it)
            }
    }

    fun onPictureChanged(picture: Uri) = withState {
        val client = it.client.copy(picture = picture)
        updateClient(client)
    }

    fun onNameChanged(value: String) = withState {
        val client = it.client.copy(name = value)
        updateClient(client)
    }

    fun onNameDisplayChanged(value: String) = withState {
        val client = it.client.copy(nameDisplay = value)
        updateClient(client)
    }

    fun onBirthdayChanged(value: LocalDate) = withState {
        val day = value.atStartOfDay(ZoneId.systemDefault())

        val client = it.client.copy(birthday = day)
        updateClient(client)
    }

    fun onSexChanged(value: Sex) = withState {
        val client = it.client.copy(sex = value)
        updateClient(client)
    }

    fun onDocumentChanged(value: String) = withState {
        val document = if (value.length <= maxDocumentLength) {
            value
        } else {
            value.substring(0 until maxDocumentLength)
        }

        val client = it.client.copy(document = document)
        updateClient(client)
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