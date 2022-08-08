package com.peyess.salesapp.model.store

sealed class OpticalStoreStatus {
    object Active: OpticalStoreStatus()
    object Deactivated: OpticalStoreStatus()
    object Creating: OpticalStoreStatus()
    object Banned: OpticalStoreStatus()
    object Unknown: OpticalStoreStatus()

    companion object {
        fun fromString(status: String): OpticalStoreStatus {
            return when (status) {
                "active" -> Active
                "deactivated" -> Deactivated
                "creating" ->Creating
                "banned" ->Banned
                else ->  Unknown
            }
        }
    }

    fun name() : String {
        return when (this) {
            is Active -> "active"
            is Deactivated -> "deactivated"
            is Creating -> "creating"
            is Banned -> "banned"
            Unknown -> "unknown"
        }
    }
}
