package com.peyess.salesapp.model.users

import android.net.Uri
import java.util.Date

sealed class AccountStatus {
    object Pending: AccountStatus()
    object Active: AccountStatus()
    object Inactivated: AccountStatus()
    object Unknown: AccountStatus()

    companion object {
        fun fromString(status: String): AccountStatus {
            return when (status) {
                "pending" -> Pending
                "active" -> Active
                "inactivated" ->Inactivated
                else ->  Unknown
            }
        }
    }

    fun name() : String {
        return when (this) {
            is Pending -> "pending"
            is Active -> "active"
            is Inactivated -> "inactivated"
            Unknown -> "unknown"
        }
    }
}

data class CollaboratorDocument(
    val nameDisplay:  String = "",
    val name:  String = "",
    val document:  String = "",

    val jobPosition:  String = "",
    val status:  AccountStatus = AccountStatus.Active, // AccountStatus

    val storeDiscountGroup:  String = "",
    val storePermissionGroup:  String = "",

    val id:  String = "",
    val storeId:  String = "",

    val createdAt: Date = Date(),
    val createdBy:  String = "",
    val createAllowedBy:  String = "",
    val updatedAt: Date = Date(),
    val updatedBy:  String = "",
    val updateAllowedBy:  String = "",
)
