package com.peyess.salesapp.data.model.client

sealed class Sex {
    object Male: Sex()
    object Female: Sex()
    object Unknown: Sex()

    fun toName() = toName(this)

    companion object {
        val options: List<Sex?> by lazy {
            listOf(
                Male,
                Female,
                Unknown,
            )
        }

        fun toName(position: Sex?): String {
            return when(position) {
                is Male -> "male"
                is Female -> "female"
                else -> "unknown"
            }
        }

        fun fromName(name: String?): Sex {
            return when(name?.lowercase()) {
                "male" ->  Male
                "female" ->  Female
                else -> Unknown
            }
        }
    }
}
