package com.peyess.salesapp.typing.sale

sealed class FinancialInstitutionType {
    object Bank: FinancialInstitutionType()
    object Broker: FinancialInstitutionType()
    object None: FinancialInstitutionType()

    fun toName() = toName(this)

    companion object {
        fun toName(institutionType: FinancialInstitutionType): String {
            return when (institutionType) {
                is Bank -> "bank"
                is Broker -> "broker"
                is None -> "none"
            }
        }

        fun fromName(institutionType: String): FinancialInstitutionType {
            return when (institutionType) {
                "bank" -> Bank
                "broker" -> Broker
                else -> None
            }
        }
    }
}
