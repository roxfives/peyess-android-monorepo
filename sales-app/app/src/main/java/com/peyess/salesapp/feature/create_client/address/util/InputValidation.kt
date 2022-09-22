package com.peyess.salesapp.feature.create_client.address.util

import androidx.annotation.StringRes
import com.peyess.salesapp.R

@StringRes
fun validateZipCodeError(zipCodeError: String): Int? {
    if (zipCodeError.isEmpty()) {
        return R.string.default_error_empty_field
    }

    return null
}

@StringRes
fun validateStreetError(streetError: String): Int? {
    if (streetError.isEmpty()) {
        return R.string.default_error_empty_field
    }

    return null
}

@StringRes
fun validateHouseNumberError(houseNumberError: String): Int? {
    if (houseNumberError.isEmpty()) {
        return R.string.default_error_empty_field
    }

    return null
}

@StringRes
fun validateNeighbourhoodError(neighbourhoodError: String): Int? {
    if (neighbourhoodError.isEmpty()) {
        return R.string.default_error_empty_field
    }

    return null
}

@StringRes
fun validateCityError(cityError: String): Int? {
    if (cityError.isEmpty()) {
        return R.string.default_error_empty_field
    }

    return null
}

@StringRes
fun validateStateError(stateError: String): Int? {
    if (stateError.isEmpty()) {
        return R.string.default_error_empty_field
    }

    return null
}
