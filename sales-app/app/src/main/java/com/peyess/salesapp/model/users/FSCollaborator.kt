package com.peyess.salesapp.model.users

import android.net.Uri
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import java.util.Date

@IgnoreExtraProperties
data class FSCollaborator(
    @Keep
    @JvmField
    @PropertyName("name_display")
    val nameDisplay:  String = "",

    @Keep
    @JvmField
    @PropertyName("name")
    val name:  String = "",

    // TODO: add email field in the data structure
//    @Keep
//    @JvmField
//    @PropertyName("email")
//    val email:  String = "",

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

//    @Keep
//    @JvmField
//    @PropertyName("picture")
//    val picture:  String = "",

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
    @PropertyName("create_allowed_by")
    val createAllowedBy:  String = "",

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
    @PropertyName("update_allowed_by")
    val updateAllowedBy:  String = "",

    @Keep
    @JvmField
    @PropertyName("created")
    val created: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("updated")
    val updated: Date = Date(),

    @Keep
    @JvmField
    @PropertyName("is_editable")
    val isEditable:  Boolean = true,
)

fun FSCollaborator.toDocument(): CollaboratorDocument {
    return CollaboratorDocument(
        nameDisplay = nameDisplay,
        name = name,
        document = document,
        jobPosition = jobPosition,
        status = AccountStatus.fromString(status = status),
        storeDiscountGroup = storeDiscountGroup,
        storePermissionGroup = storePermissionGroup,
        id = id,
        storeId = storeId,

        createdAt = this.createdAt,
        createdBy = this.createdBy,
        createAllowedBy = this.createAllowedBy,
        updatedAt = this.updatedAt,
        updatedBy = this.updatedBy,
        updateAllowedBy = this.updateAllowedBy,
    )
}
