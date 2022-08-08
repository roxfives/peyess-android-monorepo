package com.peyess.salesapp.model.store

sealed class OpticalStoreType {
    object Mobile: OpticalStoreType()
    object MallRegular: OpticalStoreType()
    object MallMiddle: OpticalStoreType()
    object Regular: OpticalStoreType()
    object Unknown: OpticalStoreType()

    companion object {
        fun fromString(status: String): OpticalStoreType {
            return when (status) {
                "mobile" -> Mobile
                "mall-regular" -> MallRegular
                "mall-middle" ->MallMiddle
                "regular" ->Regular
                else ->  Unknown
            }
        }
    }

    fun name() : String {
        return when (this) {
            is Mobile -> "mobile"
            is MallRegular -> "mall-regular"
            is MallMiddle -> "mall-middle"
            is Regular -> "regular"
            Unknown -> "unknown"
        }
    }
}
