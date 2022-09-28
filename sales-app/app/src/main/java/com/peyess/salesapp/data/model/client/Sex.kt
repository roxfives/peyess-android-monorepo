package com.peyess.salesapp.data.model.client

sealed class Sex {
    object None: Sex()
    object Male: Sex()
    object Female: Sex()
    object Other: Sex()

    fun toName() = toName(this)

    companion object {
        val options: List<Sex?> = listOf(
            None,
            Male,
            Female,
            Other,
        )

        fun toName(position: Sex?): String {
            return when(position) {
                is None -> "none"
                is Male -> "male"
                is Female -> "female"
                else -> "other"
            }
        }

        fun fromName(name: String?): Sex {
            return when(name?.lowercase()) {
                "none" ->  None
                "male" ->  Male
                "female" ->  Female
                else -> Other
            }
        }
    }
}
