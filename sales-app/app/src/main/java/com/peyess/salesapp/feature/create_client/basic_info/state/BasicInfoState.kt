package com.peyess.salesapp.feature.create_client.basic_info.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateBirthDate
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateDocument
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateName
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateNameDisplay
import com.peyess.salesapp.navigation.pick_client.PickScenario

data class BasicInfoState(
    private val _clientAsync: Async<ClientModel?> = Uninitialized,

//    val picture: Uri = Uri.EMPTY,
//    val name: String = "",
//    val nameDisplay: String = "",
//    val birthday: ZonedDateTime = ZonedDateTime
//        .parse("2000-01-01T10:15:30+01:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME),
//    val document: String = "",
//    val sex: Sex = Sex.None,

    val isPicking: Boolean = false,
    val pickScenarioParam: String = PickScenario.ServiceOrder.toName(),
    val paymentId: Long = 0L,

    @PersistState val detectNameError: Boolean = false,
    @PersistState val detectNameDisplayError: Boolean = false,
    @PersistState val detectDocumentError: Boolean = false,
): MavericksState {
    val isClientLoading = _clientAsync is Loading
            || (_clientAsync is Success && _clientAsync.invoke() == null)
    val client = _clientAsync.invoke() ?: ClientModel()

    val picture = client.picture
    val name = client.name
    val nameDisplay = client.nameDisplay
    val birthday = client.birthday
    val document = client.document
    val sex = client.sex

    private val _nameErrorId = validateName(name)
    val nameErrorId = _nameErrorId ?: R.string.empty_string
    val nameHasError = detectNameError && _nameErrorId != null

    private val _nameDisplayErrorId = validateNameDisplay(nameDisplay)
    val nameDisplayErrorId = _nameDisplayErrorId ?: R.string.empty_string
    val nameDisplayHasError = detectNameDisplayError && _nameDisplayErrorId != null

    private val _birthdayErrorId = validateBirthDate(birthday)
    val birthdayErrorId = _birthdayErrorId ?: R.string.empty_string
    val birthdayHasError = _birthdayErrorId != null

    private val _documentErrorId = validateDocument(document)
    val documentErrorId = _documentErrorId ?: R.string.empty_string
    val documentHasError = detectDocumentError && _documentErrorId != null

    val isInputValid = _nameErrorId == null
            && _nameDisplayErrorId == null
            && _birthdayErrorId == null
            && _documentErrorId == null
}
