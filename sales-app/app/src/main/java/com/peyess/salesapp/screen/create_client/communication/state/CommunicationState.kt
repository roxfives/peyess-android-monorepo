package com.peyess.salesapp.screen.create_client.communication.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.screen.create_client.communication.util.validateCellphone
import com.peyess.salesapp.screen.create_client.communication.util.validateEmail
import com.peyess.salesapp.screen.create_client.communication.util.validatePhone
import com.peyess.salesapp.screen.create_client.communication.util.validateWhatsapp
import com.peyess.salesapp.screen.create_client.model.Client
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse

data class CommunicationState(
    val saleId: String = "",
    val serviceOrderId: String = "",

    val clientId: String = "",
    val paymentId: Long = 0L,
    val createScenario: CreateScenario = CreateScenario.Home,

    val clientResponseAsync: Async<CacheCreateClientFetchSingleResponse> = Uninitialized,
    val client: Client = Client(),

    val emailInput: String = "",
    val cellphoneInput: String = "",
    val whatsappInput: String = "",
    val phoneInput: String = "",
    val phoneHasWhatsApp: Boolean = true,
    val hasPhoneContact: Boolean = false,
    val hasAcceptedPromotionalMessages: Boolean = false,

    val uploadClientAsync: Async<String> = Uninitialized,

    val uploadedId: String = "",
    val clientCreated: Boolean = false,

    val hasFinishedSettingCommunication: Boolean = false,

    @PersistState
    val detectEmailError: Boolean = false,
    @PersistState
    val detectCellphoneError: Boolean = false,
    @PersistState
    val detectWhatsappError: Boolean = false,
    @PersistState
    val detectPhoneError: Boolean = false,
): MavericksState {
    private val _detectWhatsappError: Boolean = detectWhatsappError && phoneHasWhatsApp
    private val _detectPhoneError: Boolean = detectPhoneError && hasPhoneContact

    val isUploadingClient = uploadClientAsync is Loading
    val uploadSuccessful = uploadClientAsync is Success

    private val _emailErrorId = validateEmail(emailInput)
    val emailErrorId = _emailErrorId ?: R.string.empty_string
    val emailHasError = detectEmailError && _emailErrorId != null

    private val _cellphoneErrorId = validateCellphone(cellphoneInput)
    val cellphoneErrorId = _cellphoneErrorId ?: R.string.empty_string
    val cellphoneHasError = detectCellphoneError && _cellphoneErrorId != null

    private val _whatsappErrorId = validateWhatsapp(whatsappInput)
    val whatsappErrorId = _whatsappErrorId ?: R.string.empty_string
    val whatsappHasError = _detectWhatsappError && _whatsappErrorId != null

    private val _phoneErrorId = validatePhone(phoneInput)
    val phoneErrorId = _phoneErrorId ?: R.string.empty_string
    val phoneHasError = _detectPhoneError && _phoneErrorId != null

    val isInputValid = _emailErrorId == null
            && _cellphoneErrorId == null
            && (_whatsappErrorId == null || phoneHasWhatsApp)
            && (_phoneErrorId == null || !hasPhoneContact)
}