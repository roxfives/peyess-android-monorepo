package com.peyess.salesapp.model.store

import java.util.Date

data class OpticalStore(
    val picture: String = "",

    val nameDisplay: String = "",

    val typeId: String = "",

    val type: String = "", // optical_store_type

    val status: String = "", // store_status

    val reasonDeactivated: String = "",

    val reasonBanned: String = "",

    val opening: String = "",

    val directorUid: String = "",

    val responsibleName: String = "",

    val responsibleUid: String = "",

    val acceptsReceivable: Boolean = true,

    val paymentMinSet: Float = 0f,

    val paymentTotalReceived: Float = 0f,

    val daysToTakeFromStore: Float = 0f,

    val hasCustomLegalText: Boolean = false,

    val isEnabled: Boolean = true,

    val state: String = "",
    val city: String = "",
    val zipCode: String = "",
    val neighborhood: String = "",
    val street: String = "",
    val number: String = "",
    val complement: String = "",
    val publicPlace: String = "",
    val ibge: String = "",
    val siafi: String = "",
    val gia: String = "",

    val createdAt: Date = Date(),
    val createdBy:  String = "",
    val createAllowedBy:  String = "",
    val updatedAt: Date = Date(),
    val updatedBy:  String = "",
    val updateAllowedBy:  String = "",
) {
    val shortAddress = "$neighborhood, $city"
}