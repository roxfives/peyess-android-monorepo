package com.peyess.salesapp.data.model.cache

import android.net.Uri
import com.peyess.salesapp.typing.client.Sex
import java.time.OffsetDateTime

data class CacheCreateClientDocument(
    val id: String = "",

    val name: String = "",
    val nameDisplay: String = "",
    val picture: Uri = Uri.EMPTY,
    val birthday: OffsetDateTime = OffsetDateTime.now(),
    val document: String = "",
    val sex: Sex = Sex.Unknown,

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

    val isCreating: Boolean = false,
)
