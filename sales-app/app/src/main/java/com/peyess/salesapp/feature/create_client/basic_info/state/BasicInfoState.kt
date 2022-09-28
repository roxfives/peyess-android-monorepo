package com.peyess.salesapp.feature.create_client.basic_info.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.client.ClientModel
import com.peyess.salesapp.data.model.client.Sex
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateBirthDate
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateDocument
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateName
import com.peyess.salesapp.feature.create_client.basic_info.utils.validateNameDisplay
import java.time.ZonedDateTime

data class BasicInfoState(
    private val _clientAsync: Async<ClientModel?> = Uninitialized,

    val picture: Uri = Uri.EMPTY,
    val name: String = "",
    val nameDisplay: String = "",
    val birthday: ZonedDateTime = ZonedDateTime.now(),
    val document: String = "",
    val sex: Sex = Sex.None,

    @PersistState val detectNameError: Boolean = false,
    @PersistState val detectNameDisplayError: Boolean = false,
    @PersistState val detectDocumentError: Boolean = false,
): MavericksState {
    val isClientLoading = _clientAsync is Loading
            || (_clientAsync is Success && _clientAsync.invoke() == null)
    val client = _clientAsync.invoke() ?: ClientModel()

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
