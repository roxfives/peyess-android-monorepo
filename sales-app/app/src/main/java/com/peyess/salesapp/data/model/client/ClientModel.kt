package com.peyess.salesapp.data.model.client

import android.net.Uri
import java.time.ZonedDateTime

data class ClientModel(
    val id: String = "",
    val name: String = "",
    val nameDisplay: String = "",
    val picture: Uri = Uri.EMPTY,
    val birthday: ZonedDateTime = ZonedDateTime.now(),
    val document: String = "",
    val sex: Sex = Sex.Other,
    val email: String = "",

    val street: String = "",
    val number: String = "",
) {
    val shortAddress = "$street, $number"
}
