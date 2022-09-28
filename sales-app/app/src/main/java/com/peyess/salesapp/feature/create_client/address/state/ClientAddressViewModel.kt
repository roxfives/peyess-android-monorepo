package com.peyess.salesapp.feature.create_client.address.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.address_lookup.AddressModel
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.repository.address_lookup.AddressLookupRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val maxZipCodeLength = 8

class ClientAddressViewModel @AssistedInject constructor(
    @Assisted initialState: ClientAddressState,
    private val clientRepository: ClientRepository,
    private val addressRepository: AddressLookupRepository,
) : MavericksViewModel<ClientAddressState>(initialState) {

    init {
        loadClient()

        onAsync(ClientAddressState::addressLookup) {
            updateAddress(it)
        }
    }

    private fun loadClient() = withState {
        clientRepository
            .latestLocalClientCreated()
            .execute {
                copy(_clientAsync = it)
            }
    }

    private fun findAddressByZipCode(zipCode: String) = withState { state ->
        suspend {
            addressRepository.findAddressByZipCode(zipCode)
        }.execute {
            copy(addressLookup = it)
        }
    }

    private fun updateClient(client: ClientModel) {
        viewModelScope.launch(Dispatchers.IO) {
            clientRepository.updateLocalClient(client)
        }
    }

    private fun updateAddress(address: AddressModel) = withState {
        val client = it.client.copy(
            street = address.street,
            complement = address.complement,
            neighborhood = address.neighborhood,
            city = address.city,
            state = address.state,
        )

        updateClient(client)
    }

    private fun clearCurrentAddress(zipCode: String) = withState {
        val client = it.client.copy(
            zipCode = zipCode,
            street = "",
            complement = "",
            houseNumber = "",
            neighborhood = "",
            city = "",
            state = "",
        )

        updateClient(client)
    }

    fun onZipCodeChanged(value: String) = withState {
        val zipCode = if (value.length <= maxZipCodeLength) {
            value
        } else {
            value.substring(0 until maxZipCodeLength)
        }

        if (zipCode.length == maxZipCodeLength) {
            clearCurrentAddress(zipCode)
            findAddressByZipCode(zipCode)
        } else {
            val update = it.client.copy(zipCode = zipCode)
            updateClient(update)
        }
    }

    fun onStreetChanged(value: String) = withState {
        val update = it.client.copy(street = value)
        updateClient(update)
    }

    fun onHouseNumberChanged(value: String) = withState {
        val update = it.client.copy(houseNumber = value)
        updateClient(update)
    }

    fun onComplementChanged(value: String) = withState {
        val update = it.client.copy(complement = value)
        updateClient(update)
    }

    fun onNeighbourhoodChanged(value: String) = withState {
        val update = it.client.copy(neighborhood = value)
        updateClient(update)
    }

    fun onCityChanged(value: String) = withState {
        val update = it.client.copy(city = value)
        updateClient(update)
    }

    fun onStateChanged(value: String) = withState {
        val update = it.client.copy(state = value)
        updateClient(update)
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