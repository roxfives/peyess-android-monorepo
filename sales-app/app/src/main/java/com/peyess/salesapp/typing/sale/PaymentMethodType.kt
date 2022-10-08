package com.peyess.salesapp.typing.sale

sealed class PaymentMethodType {
    object Money: PaymentMethodType()
    object Debit: PaymentMethodType()
    object Pix: PaymentMethodType()
    object CreditFull: PaymentMethodType()
    object Credit: PaymentMethodType()
    object Crediario: PaymentMethodType()
    object BankDeposit: PaymentMethodType()
    object BankCheck: PaymentMethodType()
    object Unknown: PaymentMethodType()

    fun toName() = toName(this)

    companion object {
        fun toName(state: PaymentMethodType): String {
            return when (state) {
                is Money -> "money"
                is Debit -> "debit"
                is Pix -> "pix"
                is CreditFull -> "credit-full"
                is Credit -> "credit"
                is BankDeposit -> "bank-deposit"
                is BankCheck -> "bank-check"
                is Crediario -> "crediario"
                is Unknown -> "unknown"
            }
        }

        fun fromName(state: String): PaymentMethodType {
            return when (state) {
                "money" -> Money
                "debit" -> Debit
                "pix" -> Pix
                "credit-full" -> CreditFull
                "credit" -> Credit
                "bank-deposit" -> BankDeposit
                "bank-check" -> BankCheck
                "crediario" -> Crediario
                else -> Unknown
            }
        }
    }
}
