package com.peyess.salesapp.data.model.sale.purchase

import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentMethodType


data class PaymentDocument(
    val uuid: String = "",

    val methodName: PaymentMethodType = PaymentMethodType.Unknown,
    val methodId: String = "",
    val amount: Double = 0.0,
    val installments: Int = 0,
    val currency: String = "BRL",
    val document: String = "",
    val financialInst: FinancialInstitutionType = FinancialInstitutionType.None,

    val cardNsu: String = "",
    val cardFlagName: String = "",
    val cardFlagIcon: String = "",

    val payerUid: String = "",
    val payerDocument: String = "",
    val payerName: String = "",
)
