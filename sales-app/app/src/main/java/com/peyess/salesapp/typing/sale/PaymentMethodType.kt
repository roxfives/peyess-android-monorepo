package com.peyess.salesapp.typing.sale

sealed class PaymentMethodType {
    object Money: PaymentMethodType()
    object Unknown: PaymentMethodType()

    fun toName() = toName(this)

    companion object {
        fun toName(state: PaymentMethodType): String {
            return when (state) {
                is Money -> "money"
                is Unknown -> "unknown"
            }
        }

        fun fromName(state: String): PaymentMethodType {
            return when (state) {
                "money" -> Money
                else -> Unknown
            }
        }
    }
}
