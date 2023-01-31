package com.peyess.salesapp.feature.create_client.basic_info.state

import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.cache.CacheCreateClientCreateResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.feature.create_client.adapter.toCacheCreateClientDocument
import com.peyess.salesapp.feature.create_client.adapter.toClient
import com.peyess.salesapp.feature.create_client.model.Client
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class BasicInfoViewModel @AssistedInject constructor(
    @Assisted initialState: BasicInfoState,
    private val cacheCreateClientRepository: CacheCreateClientRepository,
): MavericksViewModel<BasicInfoState>(initialState) {
    private val maxDocumentLength = 11

    init {
        loadOrCreateClient()

        onEach(BasicInfoState::creatingClient) { updateInput(it) }

        onAsync(BasicInfoState::findCreatingResponseAsync) { processFindCreatingResponse(it) }
        onAsync(BasicInfoState::createClientResponseAsync) { processCreateClientResponse(it) }
    }

    private fun updateClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            cacheCreateClientRepository.update(client.toCacheCreateClientDocument())
        }
    }

    private fun loadOrCreateClient() = withState {
        suspend {
            cacheCreateClientRepository.findCreating()
        }.execute(Dispatchers.IO) {
            copy(findCreatingResponseAsync = it)
        }
    }

    private fun processFindCreatingResponse(
        response: CacheCreateClientFetchSingleResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Error while finding creating client: $it")
                copy(
                    findCreatingResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                Timber.i("Found creating client: $it")
                copy(
                    hasFoundAnyCreating = true,
                    clientFound = it.toClient(),
                )
            }
        )
    }

    private fun deleteClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            cacheCreateClientRepository.deleteById(client.id)
        }
    }

    private fun createNewClient() {
        suspend {
            cacheCreateClientRepository.createClient()
        }.execute(Dispatchers.IO) {
            copy(createClientResponseAsync = it)
        }
    }

    private fun processCreateClientResponse(
        response: CacheCreateClientCreateResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Error while creating client: $it")
                copy(
                    createClientResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                Timber.i("Created client: $it")
                copy(creatingClient = it.toClient())
            }
        )
    }

    private fun updateInput(client: Client) = setState {
        copy(creatingClient = client)
    }

    fun resetCacheAndCreateNewClient() = withState {
        if (it.hasFoundAnyCreating) {
            deleteClient(it.clientFound)
        }

        createNewClient()
    }

    fun onPictureChanged(picture: Uri) = setState {
        val update = this.creatingClient.copy(picture = picture)

        updateClient(update)
        copy(creatingClient = update)
    }

    fun onNameChanged(value: String) = setState {
        val update = this.creatingClient.copy(name = value)

        updateClient(update)
        copy(creatingClient = update)
    }

    fun onNameDisplayChanged(value: String) = setState {
        val update = this.creatingClient.copy(nameDisplay = value)

        updateClient(update)
        copy(creatingClient = update)
    }

    fun onBirthdayChanged(value: LocalDate) = setState {
        val instant = value.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val day = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        val update = this.creatingClient.copy(birthday = day)

        updateClient(update)
        copy(creatingClient = update)
    }

    fun onSexChanged(value: Sex) = setState {
        val update = this.creatingClient.copy(sex = value)

        updateClient(update)
        copy(creatingClient = update)
    }

    fun onDocumentChanged(value: String) = setState {
        val document = if (value.length <= maxDocumentLength) {
            value
        } else {
            value.substring(0 until maxDocumentLength)
        }
        val update = this.creatingClient.copy(document = document)

        updateClient(update)
        copy(creatingClient = update)
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