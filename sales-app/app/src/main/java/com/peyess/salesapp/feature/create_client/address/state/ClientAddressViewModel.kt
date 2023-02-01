package com.peyess.salesapp.feature.create_client.address.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.address_lookup.AddressModel
import com.peyess.salesapp.data.repository.address_lookup.AddressLookupRepository
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.data.repository.cache.CacheCreateClientRepository
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

private const val maxZipCodeLength = 8

class ClientAddressViewModel @AssistedInject constructor(
    @Assisted initialState: ClientAddressState,
    private val addressRepository: AddressLookupRepository,
    private val cacheCreateClientRepository: CacheCreateClientRepository,
) : MavericksViewModel<ClientAddressState>(initialState) {

    init {
        onEach(ClientAddressState::clientId) { loadClient(it) }

        onAsync(ClientAddressState::loadClientResponseAsync) { processLoadClientResponse(it) }
        onAsync(ClientAddressState::addressLookupAsync) { updateAddress(it) }
    }

    private fun loadClient(clientId: String) {
        suspend {
            cacheCreateClientRepository.getById(clientId)
        }.execute(Dispatchers.IO) {
            copy(loadClientResponseAsync = it)
        }
    }

    private fun updateClient(client: Client) {
        viewModelScope.launch(Dispatchers.IO) {
            cacheCreateClientRepository.update(client.toCacheCreateClientDocument())
        }
    }

    private fun processLoadClientResponse(
        response: CacheCreateClientFetchSingleResponse,
    ) = setState {
        Timber.i("Loading client: $response")

        response.fold(
            ifLeft = {
                val error = it.error ?: Throwable(it.description)

                Timber.e("Error loading client: ${it.description}", error)
                copy(loadClientResponseAsync = Fail (error))
            },

            ifRight = {
                Timber.i("Updating state with: $it")

                val client = it.toClient()
                copy(
                    loadedFromCache = true,

                    client = client,
                    zipCodeInput = it.zipCode,
                    streetInput = it.street,
                    complementInput = it.complement,
                    houseNumberInput = it.houseNumber,
                    neighborhoodInput = it.neighborhood,
                    cityInput = it.city,
                    stateInput = it.state,
                )
            }
        )
    }

    private fun findAddressByZipCode(zipCode: String) {
        suspend {
            addressRepository.findAddressByZipCode(zipCode)
        }.execute {
            copy(addressLookupAsync = it)
        }
    }

    private fun updateAddress(address: AddressModel) = setState {
        val client = this.client.copy(
            street = address.street,
            complement = address.complement,
            neighborhood = address.neighborhood,
            city = address.city,
            state = address.state,
        )

        updateClient(client)
        copy(
            client = client,

            streetInput = address.street,
            complementInput = address.complement,
            houseNumberInput = "",
            neighborhoodInput = address.neighborhood,
            cityInput = address.city,
            stateInput = address.state,
        )
    }

    private fun clearCurrentAddress(zipCode: String) = setState {
        val client = this.client.copy(
            zipCode = zipCode,
            street = "",
            complement = "",
            houseNumber = "",
            neighborhood = "",
            city = "",
            state = "",
        )

        updateClient(client)
        copy(
            client = client,
            zipCodeInput = zipCode,
            streetInput = "",
            complementInput = "",
            houseNumberInput = "",
            neighborhoodInput = "",
            cityInput = "",
            stateInput = "",
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

    fun onZipCodeChanged(value: String) = setState {
        val zipCode = if (value.length <= maxZipCodeLength) {
            value
        } else {
            value.substring(0 until maxZipCodeLength)
        }

        val update = this.client.copy(zipCode = zipCode)
        updateClient(update)

        if (!this.loadedFromCache && zipCode.length == maxZipCodeLength) {
            clearCurrentAddress(zipCode)
            findAddressByZipCode(zipCode)

            copy(client = client)
        } else {
            copy(
                loadedFromCache = false,

                client = update,
                zipCodeInput = zipCode,
            )
        }
    }

    fun onStreetChanged(value: String) = setState {
        val update = this.client.copy(street = value)

        updateClient(update)
        copy(client = update, streetInput = value)
    }

    fun onHouseNumberChanged(value: String) = setState {
        val update = this.client.copy(houseNumber = value)

        updateClient(update)
        copy(client = update, houseNumberInput = value)
    }

    fun onComplementChanged(value: String) = setState {
        val update = this.client.copy(complement = value)

        updateClient(update)
        copy(client = update, complementInput = value)
    }

    fun onNeighbourhoodChanged(value: String) = setState {
        val update = this.client.copy(neighborhood = value)

        updateClient(update)
        copy(client = update, neighborhoodInput = value)
    }

    fun onCityChanged(value: String) = setState {
        val update = this.client.copy(city = value)

        updateClient(update)
        copy(client = update, cityInput = value)
    }

    fun onStateChanged(value: String) = setState {
        val update = this.client.copy(state = value)

        updateClient(update)
        copy(client = update, stateInput = value)
    }

    fun onDetectZipCodeError() = setState {
        copy(detectZipCodeError = true)
    }

    fun onDetectStreetError() = setState {
        copy(detectStreetError = true)
    }

    fun onDetectHouseNumberError() = setState {
        copy(detectHouseNumberError = true)
    }

    fun onDetectNeighbourhoodError() = setState {
        copy(detectNeighbourhoodError = true)
    }

    fun onDetectCityError() = setState {
        copy(detectCityError = true)
    }

    fun onDetectStateError() = setState {
        copy(detectStateError = true)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<ClientAddressViewModel, ClientAddressState> {
        override fun create(state: ClientAddressState): ClientAddressViewModel
    }

    companion object: MavericksViewModelFactory<ClientAddressViewModel, ClientAddressState>
        by hiltMavericksViewModelFactory()
}