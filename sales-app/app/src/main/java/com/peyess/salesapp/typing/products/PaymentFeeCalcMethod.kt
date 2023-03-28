package com.peyess.salesapp.typing.products

sealed class PaymentFeeCalcMethod {
    object Percentage: PaymentFeeCalcMethod()
    object Whole: PaymentFeeCalcMethod()
    object None: PaymentFeeCalcMethod()

    fun toName() = toName(this)

    companion object {
        fun toName(method: PaymentFeeCalcMethod?): String {
            return when (method) {
                Percentage -> "percentage"
                Whole -> "whole"
                else -> "none"
            }
        }

        fun fromName(method: String?): PaymentFeeCalcMethod {
            return when (method) {
                "percentage" -> Percentage
                "whole" -> Whole
                else -> None
        }
        }
    }
}
