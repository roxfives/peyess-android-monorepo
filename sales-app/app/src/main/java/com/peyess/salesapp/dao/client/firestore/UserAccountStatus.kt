package com.peyess.salesapp.dao.client.firestore

sealed class UserAccountStatus {
    object Pending: UserAccountStatus()
    object Uninitialized: UserAccountStatus()
    object Activated: UserAccountStatus()
    object Deactivated: UserAccountStatus()

    companion object {
        fun fromName(name: String?): UserAccountStatus? {

            return when (name?.lowercase() ?: "") {
                "pending" -> Pending
                "uninitialized" -> Uninitialized
                "activated" -> Activated
                "deactivated" -> Deactivated
                else -> null
            }
        }

        fun fromType(type: UserAccountStatus?): String? {

            return when (type) {
                is Pending -> "pending"
                is Uninitialized -> "uninitialized"
                is Activated -> "activated"
                is Deactivated -> "deactivated"
                else -> null
            }
        }
    }
}
