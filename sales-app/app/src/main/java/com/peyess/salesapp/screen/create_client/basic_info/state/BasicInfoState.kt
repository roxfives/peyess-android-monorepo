package com.peyess.salesapp.screen.create_client.basic_info.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.data.repository.cache.CacheCreateClientFetchSingleResponse
import com.peyess.salesapp.screen.create_client.basic_info.utils.validateBirthDate
import com.peyess.salesapp.screen.create_client.basic_info.utils.validateDocument
import com.peyess.salesapp.screen.create_client.basic_info.utils.validateName
import com.peyess.salesapp.screen.create_client.basic_info.utils.validateNameDisplay
import com.peyess.salesapp.feature.client_data.model.Client
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.peyess.salesapp.typing.client.Sex
import java.time.ZonedDateTime

data class BasicInfoState(
    val saleId: String = "",
    val serviceOrderId: String = "",

    val clientId: String = "",
    val paymentId: Long = 0L,
    val createScenario: CreateScenario = CreateScenario.Home,

    val loadClientResponseAsync: Async<CacheCreateClientFetchSingleResponse> = Uninitialized,
    val client: Client = Client(),

    val nameInput: String = "",
    val nameDisplayInput: String = "",
    val pictureInput: Uri = Uri.EMPTY,
    val birthdayInput: ZonedDateTime = ZonedDateTime.now(),
    val documentInput: String = "",
    val sexInput: Sex = Sex.Unknown,

    val isPicking: Boolean = false,
    val pickScenarioParam: String = PickScenario.ServiceOrder.toName(),

    val hasFinishedSettingBasicInfo: Boolean = false,

    @PersistState
    val detectNameError: Boolean = false,
    @PersistState
    val detectNameDisplayError: Boolean = false,
    @PersistState
    val detectDocumentError: Boolean = false,
): MavericksState {
    val isLoadingClient = loadClientResponseAsync is Loading

    private val _nameErrorId = validateName(nameInput)
    val nameErrorId = _nameErrorId ?: R.string.empty_string
    val nameHasError = detectNameError && _nameErrorId != null

    private val _nameDisplayErrorId = validateNameDisplay(nameDisplayInput)
    val nameDisplayErrorId = _nameDisplayErrorId ?: R.string.empty_string
    val nameDisplayHasError = detectNameDisplayError && _nameDisplayErrorId != null

    private val _birthdayErrorId = validateBirthDate(birthdayInput)
    val birthdayErrorId = _birthdayErrorId ?: R.string.empty_string
    val birthdayHasError = _birthdayErrorId != null

    private val _documentErrorId = validateDocument(documentInput)
    val documentErrorId = _documentErrorId ?: R.string.empty_string
    val documentHasError = detectDocumentError && _documentErrorId != null

    val isInputValid = _nameErrorId == null
            && _nameDisplayErrorId == null
            && _birthdayErrorId == null
            && _documentErrorId == null
}
