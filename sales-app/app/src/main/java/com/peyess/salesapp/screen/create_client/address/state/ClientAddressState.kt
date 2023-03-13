package com.peyess.salesapp.screen.create_client.address.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.address_lookup.AddressModel
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.screen.create_client.address.util.validateCityError
import com.peyess.salesapp.screen.create_client.address.util.validateHouseNumberError
import com.peyess.salesapp.screen.create_client.address.util.validateNeighbourhoodError
import com.peyess.salesapp.screen.create_client.address.util.validateStateError
import com.peyess.salesapp.screen.create_client.address.util.validateStreetError
import com.peyess.salesapp.screen.create_client.address.util.validateZipCodeError
import com.peyess.salesapp.feature.client_data.model.Client
import com.peyess.salesapp.navigation.create_client.CreateScenario

data class ClientAddressState(
    val saleId: String = "",
    val serviceOrderId: String = "",

    val clientId: String = "",
    val paymentId: Long = 0L,
    val createScenario: CreateScenario = CreateScenario.Home,

    val loadClientResponseAsync: Async<CacheCreateClientFetchSingleResponse> = Uninitialized,
    val client: Client = Client(),

    val loadedFromCache: Boolean = false,
    val addressLookupAsync: Async<AddressModel> = Uninitialized,

    val zipCodeInput: String = "",
    val streetInput: String = "",
    val houseNumberInput: String = "",
    val complementInput: String = "",
    val neighborhoodInput: String = "",
    val cityInput: String = "",
    val stateInput: String = "",

    val hasFinishedSettingAddress: Boolean = false,

    @PersistState
    val detectZipCodeError: Boolean = false,
    @PersistState
    val detectStreetError: Boolean = false,
    @PersistState
    val detectHouseNumberError: Boolean = false,
    @PersistState
    val detectNeighbourhoodError: Boolean = false,
    @PersistState
    val detectCityError: Boolean = false,
    @PersistState
    val detectStateError: Boolean = false,
): MavericksState {
    private val isAddressComplete =
        zipCodeInput.isNotBlank() &&
                streetInput.isNotBlank() &&
                neighborhoodInput.isNotBlank() &&
                cityInput.isNotBlank() &&
                stateInput.isNotBlank()

    val isAddressLoading = addressLookupAsync is Loading
    val addressNotFound = addressLookupAsync is Fail
    val isAddressEnabled = isAddressComplete ||
            (addressLookupAsync !is Uninitialized && addressLookupAsync !is Loading)

    private val _zipCodeErrorId = validateZipCodeError(zipCodeInput)
    val zipCodeErrorId = _zipCodeErrorId ?: R.string.empty_string
    val zipCodeHasError = detectZipCodeError && _zipCodeErrorId != null

    private val _streetErrorId = validateStreetError(streetInput)
    val streetErrorId = _streetErrorId ?: R.string.empty_string
    val streetHasError = detectStreetError && _streetErrorId != null

    private val _houseNumberErrorId = validateHouseNumberError(houseNumberInput)
    val houseNumberErrorId = _houseNumberErrorId ?: R.string.empty_string
    val houseNumberHasError = detectHouseNumberError && _houseNumberErrorId != null

    private val _neighbourhoodErrorId = validateNeighbourhoodError(neighborhoodInput)
    val neighbourhoodErrorId = _neighbourhoodErrorId ?: R.string.empty_string
    val neighbourhoodHasError = detectNeighbourhoodError && _neighbourhoodErrorId != null

    private val _cityErrorId = validateCityError(cityInput)
    val cityErrorId = _cityErrorId ?: R.string.empty_string
    val cityHasError = detectCityError && _cityErrorId != null

    private val _stateErrorId = validateStateError(stateInput)
    val stateErrorId = _stateErrorId ?: R.string.empty_string
    val stateHasError = detectStateError && _stateErrorId != null

    val isInputValid = _zipCodeErrorId == null
            && _streetErrorId == null
            && _houseNumberErrorId == null
            && _neighbourhoodErrorId == null
            && _cityErrorId == null
            && _stateErrorId == null
}
