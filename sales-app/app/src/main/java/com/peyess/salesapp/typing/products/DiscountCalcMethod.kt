package com.peyess.salesapp.typing.products

sealed class DiscountCalcMethod {
    object Percentage: DiscountCalcMethod()
    object Whole: DiscountCalcMethod()
    object None: DiscountCalcMethod()

    fun toName() = toName(this)

    companion object {
        fun toName(method: DiscountCalcMethod?): String {
            return when (method) {
                Percentage -> "percentage"
                Whole -> "whole"
                else -> "none"
            }
        }

        fun fromName(method: String?): DiscountCalcMethod {
            return when (method) {
                "percentage" -> Percentage
                "whole" -> Whole
                else -> None
            }
        }
    }
}
