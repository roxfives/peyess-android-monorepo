package com.peyess.salesapp.typing.sale

sealed class PaymentDueDateMode {
    object Month : PaymentDueDateMode()
    object Day : PaymentDueDateMode()
    object None : PaymentDueDateMode()

    fun toName() = toName(this)

    companion object {
        fun fromName(value: String): PaymentDueDateMode {
            return when (value) {
                "month" -> Month
                "day" -> Day
                else -> None
            }
        }

        fun toName(value: PaymentDueDateMode): String {
            return when (value) {
                is Day -> "day"
                is Month -> "month"
                is None -> "none"
            }
        }
    }
}
