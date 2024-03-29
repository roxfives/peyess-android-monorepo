package com.peyess.salesapp.model.store

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.utils.time.toZonedDateTime
import java.util.Date

@IgnoreExtraProperties
data class FSOpticalStore(
    @Keep
    @JvmField
    @PropertyName("name_display")
    val nameDisplay: String = "",

    @Keep
    @JvmField
    @PropertyName("type_id")
    val typeId: String = "",

    @Keep
    @JvmField
    @PropertyName("type")
    val type: String = OpticalStoreType.Regular.name(), // optical_store_type

    @Keep
    @JvmField
    @PropertyName("status")
    val status: String = OpticalStoreStatus.Active.name(), // store_status

    @Keep
    @JvmField
    @PropertyName("reason_deactivated")
    val reasonDeactivated: String = "",

    @Keep
    @JvmField
    @PropertyName("reason_banned")
    val reasonBanned: String = "",

    @Keep
    @JvmField
    @PropertyName("opening")
    val opening: Timestamp = Timestamp.now(),

    @Keep
    @JvmField
    @PropertyName("director_uid")
    val directorUid: String = "",

    @Keep
    @JvmField
    @PropertyName("responsible_name")
    val responsibleName: String = "",

    @Keep
    @JvmField
    @PropertyName("responsible_uid")
    val responsibleUid: String = "",

    @Keep
    @JvmField
    @PropertyName("accepts_receivable")
    val acceptsReceivable: Boolean = true,

    @Keep
    @JvmField
    @PropertyName("payment_min_set")
    val paymentMinSet: Float = 0f,

    @Keep
    @JvmField
    @PropertyName("payment_total_received")
    val paymentTotalReceived: Float = 0f,

    @Keep
    @JvmField
    @PropertyName("days_to_take_from_store")
    val daysToTakeFromStore: Int = 0,

    @Keep
    @JvmField
    @PropertyName("additional_check_days")
    val additionalCheckDays: Int = 0,

    @Keep
    @JvmField
    @PropertyName("has_custom_legal_text")
    val hasCustomLegalText: Boolean = false,

    @Keep
    @JvmField
    @PropertyName("is_enabled")
    val isEnabled: Boolean = true,

    @Keep
    @JvmField
    @PropertyName("state")
    val state: String = "",

    @Keep
    @JvmField
    @PropertyName("city")
    val city: String = "",

    @Keep
    @JvmField
    @PropertyName("zip_code")
    val zipCode: String = "",

    @Keep
    @JvmField
    @PropertyName("neighborhood")
    val neighborhood: String = "",

    @Keep
    @JvmField
    @PropertyName("street")
    val street: String = "",

    @Keep
    @JvmField
    @PropertyName("number")
    val number: String = "",

    @Keep
    @JvmField
    @PropertyName("complement")
    val complement: String = "",

    @Keep
    @JvmField
    @PropertyName("public_place")
    val publicPlace: String = "",

    @Keep
    @JvmField
    @PropertyName("ibge")
    val ibge: String = "",

    @Keep
    @JvmField
    @PropertyName("siafi")
    val siafi: String = "",

    @Keep
    @JvmField
    @PropertyName("gia")
    val gia: String = "",

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val isEditable:  Boolean = true,
)

fun FSOpticalStore.toDocument(id: String): OpticalStore {
    return OpticalStore(
        id = id,
        nameDisplay = nameDisplay,
        typeId = typeId,
        type = type,
        status = status,
        reasonDeactivated = reasonDeactivated,
        reasonBanned = reasonBanned,
        opening = opening.toZonedDateTime(),
        directorUid = directorUid,
        responsibleName = responsibleName,
        responsibleUid = responsibleUid,
        acceptsReceivable = acceptsReceivable,
        paymentMinSet = paymentMinSet,
        paymentTotalReceived = paymentTotalReceived,
        daysToTakeFromStore = daysToTakeFromStore,
        additionalCheckDays = additionalCheckDays,
        hasCustomLegalText = hasCustomLegalText,
        isEnabled = isEnabled,

        state = state,
        city = city,
        zipCode = zipCode,
        neighborhood = neighborhood,
        street = street,
        number = number,
        complement = complement,
        publicPlace = publicPlace,
        ibge = ibge,
        siafi = siafi,
        gia = gia,

        createdAt = created,
        createdBy = createdBy,
        createAllowedBy = createAllowedBy,
        updatedAt = updated,
        updatedBy = updatedBy,
        updateAllowedBy = updateAllowedBy,
    )
}