package com.peyess.salesapp.feature.create_client.address.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.address_lookup.AddressModel
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.feature.create_client.address.util.validateCityError
import com.peyess.salesapp.feature.create_client.address.util.validateHouseNumberError
import com.peyess.salesapp.feature.create_client.address.util.validateNeighbourhoodError
import com.peyess.salesapp.feature.create_client.address.util.validateStateError
import com.peyess.salesapp.feature.create_client.address.util.validateStreetError
import com.peyess.salesapp.feature.create_client.address.util.validateZipCodeError

data class ClientAddressState(
    private val _clientAsync: Async<ClientModel?> = Uninitialized,
    val addressLookup: Async<AddressModel> = Uninitialized,

    val zipCode: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val complement: String = "",
    val neighborhood: String = "",
    val city: String = "",
    val state: String = "",

    @PersistState val detectZipCodeError: Boolean = false,
    @PersistState val detectStreetError: Boolean = false,
    @PersistState val detectHouseNumberError: Boolean = false,
    @PersistState val detectNeighbourhoodError: Boolean = false,
    @PersistState val detectCityError: Boolean = false,
    @PersistState val detectStateError: Boolean = false,
): MavericksState {
    val isClientLoading = _clientAsync is Loading
            || (_clientAsync is Success && _clientAsync.invoke() == null)
    val client = _clientAsync.invoke() ?: ClientModel()

    val isAddressLoading = addressLookup is Loading
    val addressLookUpFailed = addressLookup is Fail
    val isAddressEnabled = addressLookup !is Uninitialized && addressLookup !is Loading

    private val _zipCodeErrorId = validateZipCodeError(zipCode)
    val zipCodeErrorId = _zipCodeErrorId ?: R.string.empty_string
    val zipCodeHasError = detectZipCodeError && _zipCodeErrorId != null

    private val _streetErrorId = validateStreetError(street)
    val streetErrorId = _streetErrorId ?: R.string.empty_string
    val streetHasError = detectStreetError && _streetErrorId != null

    private val _houseNumberErrorId = validateHouseNumberError(houseNumber)
    val houseNumberErrorId = _houseNumberErrorId ?: R.string.empty_string
    val houseNumberHasError = detectHouseNumberError && _houseNumberErrorId != null

    private val _neighbourhoodErrorId = validateNeighbourhoodError(neighborhood)
    val neighbourhoodErrorId = _neighbourhoodErrorId ?: R.string.empty_string
    val neighbourhoodHasError = detectNeighbourhoodError && _neighbourhoodErrorId != null

    private val _cityErrorId = validateCityError(city)
    val cityErrorId = _cityErrorId ?: R.string.empty_string
    val cityHasError = detectCityError && _cityErrorId != null

    private val _stateErrorId = validateStateError(state)
    val stateErrorId = _stateErrorId ?: R.string.empty_string
    val stateHasError = detectStateError && _stateErrorId != null

    val isInputValid = _zipCodeErrorId == null
            && _streetErrorId == null
            && _houseNumberErrorId == null
            && _neighbourhoodErrorId == null
            && _cityErrorId == null
            && _stateErrorId == null
}
