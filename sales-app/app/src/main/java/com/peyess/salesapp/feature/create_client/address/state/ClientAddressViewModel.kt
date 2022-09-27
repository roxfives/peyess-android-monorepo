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

    private fun findAddressByZipCode() = withState { state ->
        suspend {
            addressRepository.findAddressByZipCode(state.zipCode)
        }.execute {
            copy(addressLookup = it)
        }
    }

    private fun updateAddress(address: AddressModel) = setState {
        copy(
            street = address.street,
            complement = address.complement,
            neighborhood = address.neighborhood,
            city = address.city,
            state = address.state,
        )
    }

    private fun clearCurrentAddress() = setState {
        copy(
            street = "",
            complement = "",
            houseNumber = "",
            neighborhood = "",
            city = "",
            state = "",
        )
    }

    private fun updateClient(client: ClientModel) {
        viewModelScope.launch(Dispatchers.IO) {
            clientRepository.updateLocalClient(client)
        }
    }

    fun onZipCodeChanged(value: String) = setState {
        val zipCode = if (value.length <= maxZipCodeLength) {
            value
        } else {
            value.substring(0 until maxZipCodeLength)
        }

        if (zipCode.length == maxZipCodeLength) {
            clearCurrentAddress()
            findAddressByZipCode()
        }

        val update = client.copy(zipCode = zipCode)
        updateClient(update)

        copy(zipCode = zipCode)
    }

    fun onStreetChanged(value: String) = setState {
        val update = client.copy(street = value)
        updateClient(update)

        copy(street = value)
    }

    fun onHouseNumberChanged(value: String) = setState {
        val update = client.copy(houseNumber = value)
        updateClient(update)

        copy(houseNumber = value)
    }

    fun onComplementChanged(value: String) = setState {
        val update = client.copy(complement = value)
        updateClient(update)

        copy(complement = value)
    }

    fun onNeighbourhoodChanged(value: String) = setState {
        val update = client.copy(neighborhood = value)
        updateClient(update)

        copy(neighborhood = value)
    }

    fun onCityChanged(value: String) = setState {
        val update = client.copy(city = value)
        updateClient(update)

        copy(city = value)
    }

    fun onStateChanged(value: String) = setState {
        val update = client.copy(state = value)
        updateClient(update)

        copy(state = value)
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