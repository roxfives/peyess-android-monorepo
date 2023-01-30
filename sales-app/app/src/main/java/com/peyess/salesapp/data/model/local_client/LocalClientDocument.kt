package com.peyess.salesapp.data.model.local_client

import android.net.Uri
import com.peyess.salesapp.typing.client.Sex
import java.time.ZonedDateTime

data class LocalClientDocument(
    val id: String = "",

    val name: String = "",
    val nameDisplay: String = "",

    val picture: Uri = Uri.EMPTY,

    val birthday: ZonedDateTime = ZonedDateTime.now(),
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
    val storesIds: List<String> = emptyList(),

    val doc_version: Int = 0,
    val is_editable: Boolean = false,

    val created: ZonedDateTime = ZonedDateTime.now(),
    val createdBy: String = "",
    val createAllowedBy: String = "",

    val updated: ZonedDateTime = ZonedDateTime.now(),
    val updatedBy: String = "",
    val updateAllowedBy: String = "",

    val hasBeenUploaded: Boolean = false,
    val hasBeenDownloaded: Boolean = false,

    val downloadedAt: ZonedDateTime = ZonedDateTime.now(),
    val uploadedAt: ZonedDateTime = ZonedDateTime.now(),
)

