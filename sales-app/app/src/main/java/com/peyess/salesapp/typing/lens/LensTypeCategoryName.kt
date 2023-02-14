package com.peyess.salesapp.typing.lens

sealed class LensTypeCategoryName {
    object Near: LensTypeCategoryName()
    object Far: LensTypeCategoryName()
    object Multi: LensTypeCategoryName()
    object None: LensTypeCategoryName()

    fun toName() = fromType(this)

    fun isMono(): Boolean {
        return this == Near || this == Far
    }

    companion object {
        fun fromName(name: String?): LensTypeCategoryName {
            return when(name?.lowercase() ?: "") {
                "perto" -> Near
                "longe" -> Far
                "multifocal" -> Multi
                else -> None
            }
        }

        fun fromType(type: LensTypeCategoryName?): String {
            return when(type) {
                Near -> "perto"
                Far -> "longe"
                Multi -> "multifocal"
                else -> "none"
            }
        }
    }
}
