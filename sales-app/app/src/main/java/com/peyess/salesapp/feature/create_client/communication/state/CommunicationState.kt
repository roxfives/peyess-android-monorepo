package com.peyess.salesapp.feature.create_client.communication.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.feature.create_client.communication.util.validateCellphone
import com.peyess.salesapp.feature.create_client.communication.util.validateEmail
import com.peyess.salesapp.feature.create_client.communication.util.validatePhone
import com.peyess.salesapp.feature.create_client.communication.util.validateWhatsapp

data class CommunicationState(
    private val _clientAsync: Async<ClientModel?> = Uninitialized,

    val phoneHasWhatsApp: Boolean = true,
    val hasPhoneContact: Boolean = false,

//    val email: String = "",
//    val cellphone: String = "",
//    val whatsapp: String = "",
//    val phone: String = "",

    val uploadClientAsync: Async<Unit> = Uninitialized,

    @PersistState val detectEmailError: Boolean = false,
    @PersistState val detectCellphoneError: Boolean = false,
    @PersistState val detectWhatsappError: Boolean = false,
    @PersistState val detectPhoneError: Boolean = false,
): MavericksState {
    private val _detectWhatsappError: Boolean = detectWhatsappError && phoneHasWhatsApp
    private val _detectPhoneError: Boolean = detectPhoneError && hasPhoneContact

    val isUploadingClient = uploadClientAsync is Loading
    val uploadSuccessful = uploadClientAsync is Success

    val isClientLoading = _clientAsync is Loading
            || (_clientAsync is Success && _clientAsync.invoke() == null)
    val client = _clientAsync.invoke() ?: ClientModel()

    val email = client.email
    val cellphone = client.cellphone
    val whatsapp = client.whatsapp
    val phone = client.phone

    private val _emailErrorId = validateEmail(email)
    val emailErrorId = _emailErrorId ?: R.string.empty_string
    val emailHasError = detectEmailError && _emailErrorId != null

    private val _cellphoneErrorId = validateCellphone(cellphone)
    val cellphoneErrorId = _cellphoneErrorId ?: R.string.empty_string
    val cellphoneHasError = detectCellphoneError && _cellphoneErrorId != null

    private val _whatsappErrorId = validateWhatsapp(whatsapp)
    val whatsappErrorId = _whatsappErrorId ?: R.string.empty_string
    val whatsappHasError = _detectWhatsappError && _whatsappErrorId != null

    private val _phoneErrorId = validatePhone(phone)
    val phoneErrorId = _phoneErrorId ?: R.string.empty_string
    val phoneHasError = _detectPhoneError && _phoneErrorId != null

    val isInputValid = _emailErrorId == null
            && _cellphoneErrorId == null
            && (_whatsappErrorId == null || phoneHasWhatsApp)
            && (_phoneErrorId == null || !hasPhoneContact)
}