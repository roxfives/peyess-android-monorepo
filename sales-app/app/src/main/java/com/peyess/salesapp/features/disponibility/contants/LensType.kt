package com.peyess.salesapp.features.disponibility.contants

sealed class LensType {
    object MonofocalFar: LensType()
    object MonofocalNear: LensType()
    object MultiFocal: LensType()

    object None: LensType()

    fun isLensTypeMono() = isLensTypeMono(this)

    companion object {
        fun isLensTypeMono(lensType: LensType): Boolean {
            return lensType is MonofocalFar || lensType is MonofocalNear
        }

        fun fromLensName(name: String?): LensType {
            return when(name?.lowercase() ?: "") {
                "monofocal" -> MonofocalNear
                "multifocal" -> MultiFocal
                else -> None
            }
        }
    }
}
