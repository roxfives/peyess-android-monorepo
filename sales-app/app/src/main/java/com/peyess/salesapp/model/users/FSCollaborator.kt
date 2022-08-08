package com.peyess.salesapp.model.users

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import java.util.Date

data class FSCollaborator(
    @Keep
    @JvmField
    @PropertyName("name_display")
    val nameDisplay:  String = "",

    @Keep
    @JvmField
    @PropertyName("name")
    val name:  String = "",

    @Keep
    @JvmField
    @PropertyName("document")
    val document:  String = "",

    @Keep
    @JvmField
    @PropertyName("job_position")
    val jobPosition:  String = "",

    @Keep
    @JvmField
    @PropertyName("status")
    val status:  String = AccountStatus.Active.name(), // AccountStatus

    @Keep
    @JvmField
    @PropertyName("picture")
    val picture:  String = "",

    @Keep
    @JvmField
    @PropertyName("store_discount_group")
    val storeDiscountGroup:  String = "",

    @Keep
    @JvmField
    @PropertyName("store_permission_group")
    val storePermissionGroup:  String = "",


    @Keep
    @JvmField
    @PropertyName("id")
    val id:  String = "",

    @Keep
    @JvmField
    @PropertyName("store_id")
    val storeId:  String = "",

    @Keep
    @JvmField
    @PropertyName("created_at")
    val createdAt:  Date = Date(),

    @Keep
    @JvmField
    @PropertyName("created_by")
    val createdBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("created_allowed_by")
    val createdAllowedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("created_at")
    val updatedAt:  Date = Date(),

    @Keep
    @JvmField
    @PropertyName("updated_by")
    val updatedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("updated_allowed_by")
    val updatedAllowedBy:  String = "",
)

fun FSCollaborator.toDocument(): Collaborator {
    return Collaborator(
        nameDisplay = nameDisplay,
        name = name,
        document = document,
        jobPosition = jobPosition,
        status = AccountStatus.fromString(status = status),
        picture = picture,
        storeDiscountGroup = storeDiscountGroup,
        storePermissionGroup = storePermissionGroup,
        id = id,
        storeId = storeId,

        createdAt = this.createdAt,
        createdBy = this.createdBy,
        createdAllowedBy = this.createdAllowedBy,
        updatedAt = this.updatedAt,
        updatedBy = this.updatedBy,
        updatedAllowedBy = this.updatedAllowedBy,
    )
}
