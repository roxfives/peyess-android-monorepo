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

    val zipCode: String = "",
    val street: String = "",
    val houseNumber: String = "",
    val complement: String = "",
    val neighborhood: String = "",
    val city: String = "",
    val state: String = "",

    val email: String = "",
    val phone: String = "",
    val cellphone: String = "",
    val whatsapp: String = "",
) {
    val shortAddress = "$street, $houseNumber"
}
