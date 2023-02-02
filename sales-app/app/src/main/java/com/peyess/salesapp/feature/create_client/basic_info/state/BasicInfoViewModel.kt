package com.peyess.salesapp.feature.create_client.basic_info.state

import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.feature.create_client.adapter.toCacheCreateClientDocument
import com.peyess.salesapp.feature.create_client.adapter.toClient
import com.peyess.salesapp.feature.create_client.model.Client
import com.peyess.salesapp.navigation.create_client.CreateScenario
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
        onEach {
            Timber.d("Current client (${it.documentInput}): ${it.client}")
        }

        onEach(BasicInfoState::clientId) { loadClient(it) }

        onAsync(BasicInfoState::loadClientResponseAsync) { processLoadClientResponse(it) }
    }

    private fun updateClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            cacheCreateClientRepository.update(client.toCacheCreateClientDocument())
        }
    }

    private fun loadClient(clientId: String) = withState {
        suspend {
            cacheCreateClientRepository.getById(clientId)
        }.execute(Dispatchers.IO) {
            copy(loadClientResponseAsync = it)
        }
    }

    private fun processLoadClientResponse(
        response: CacheCreateClientFetchSingleResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                Timber.e("Error while loading client: $it")
                copy(
                    loadClientResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                Timber.i("Created client: $it")
                val client = it.toClient()

                copy(
                    client = client,
                    nameInput = client.name,
                    nameDisplayInput = client.nameDisplay,
                    pictureInput = client.picture,
                    birthdayInput = client.birthday,
                    documentInput = client.document,
                    sexInput = client.sex,
                )
            }
        )
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

    fun onPictureChanged(picture: Uri) = setState {
        val update = this.client.copy(picture = picture)

        updateClient(update)
        copy(
            client = update,
            pictureInput = picture,
        )
    }

    fun onNameChanged(value: String) = setState {
        val update = this.client.copy(name = value)

        updateClient(update)
        copy(
            client = update,
            nameInput = value,
        )
    }

    fun onNameDisplayChanged(value: String) = setState {
        val update = this.client.copy(nameDisplay = value)

        updateClient(update)
        copy(
            client = update,
            nameDisplayInput = value,
        )
    }

    fun onBirthdayChanged(value: LocalDate) = setState {
        val instant = value.atStartOfDay(ZoneId.systemDefault()).toInstant()
        val day = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault())
        val update = client.copy(birthday = day)

        updateClient(update)
        copy(
            client = update,
            birthdayInput = day,
        )
    }

    fun onSexChanged(value: Sex) = setState {
        val update = this.client.copy(sex = value)

        updateClient(update)
        copy(
            client = update,
            sexInput = value,
        )
    }

    fun onDocumentChanged(value: String) = setState {
        val document = if (value.length <= maxDocumentLength) {
            value
        } else {
            value.substring(0 until maxDocumentLength)
        }
        val update = this.client.copy(document = document)

        updateClient(update)
        copy(
            client = update,
            documentInput = document,
        )
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

    fun onFinishBasicInfo() = withState {
        suspend {
            cacheCreateClientRepository.update(it.client.toCacheCreateClientDocument())
        }.execute(Dispatchers.IO) {
            copy(hasFinishedSettingBasicInfo = it is Success)
        }
    }

    fun onNavigate() = setState {
        copy(hasFinishedSettingBasicInfo = false)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<BasicInfoViewModel, BasicInfoState> {
        override fun create(state: BasicInfoState): BasicInfoViewModel
    }

    companion object: MavericksViewModelFactory<BasicInfoViewModel, BasicInfoState> by hiltMavericksViewModelFactory()
}