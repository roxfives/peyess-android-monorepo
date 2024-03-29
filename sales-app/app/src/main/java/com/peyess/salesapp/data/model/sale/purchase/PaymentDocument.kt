package com.peyess.salesapp.data.model.sale.purchase

import com.peyess.salesapp.typing.sale.FinancialInstitutionType
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
import com.peyess.salesapp.typing.sale.PaymentMethodType
import java.math.BigDecimal
import java.time.ZonedDateTime

data class PaymentDocument(
    val uuid: String = "",

    val methodName: PaymentMethodType = PaymentMethodType.Unknown,
    val methodId: String = "",
    val amount: BigDecimal = BigDecimal.ZERO,
    val installments: Int = 0,
    val currency: String = "BRL",
    val document: String = "",
    val financialInst: FinancialInstitutionType = FinancialInstitutionType.None,

    val hasLegalId: Boolean = false,
    val legalId: String = "",

    val cardFlagName: String = "",
    val cardFlagIcon: String = "",

    val payerUid: String = "",
    val payerDocument: String = "",
    val payerName: String = "",

    val dueDateMode: PaymentDueDateMode = PaymentDueDateMode.None,
    val dueDatePeriod: Int = 0,
    val dueDate: ZonedDateTime = ZonedDateTime.now(),
)
