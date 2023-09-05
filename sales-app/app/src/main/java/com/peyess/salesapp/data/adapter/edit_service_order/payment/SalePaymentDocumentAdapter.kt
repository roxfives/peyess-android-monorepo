package com.peyess.salesapp.data.adapter.edit_service_order.payment

import com.peyess.salesapp.data.model.edit_service_order.payment.EditLocalPaymentEntity
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.utils.extentions.roundToDouble
import java.math.RoundingMode

fun LocalPaymentDocument.toEditLocalPaymentEntity(
    roundValue: Boolean = false,
): EditLocalPaymentEntity {
    return EditLocalPaymentEntity(
        id = id,
        uuid = uuid,
        saleId = saleId,
        clientId = clientId,
        methodId = methodId,
        methodName = methodName,
        methodType = methodType,
        value = value.roundToDouble(roundValue),
        installments = installments,
        document = document,

        hasLegalId = hasLegalId,
        legalId = legalId,

        cardFlagName = cardFlagName,
        cardFlagIcon = cardFlagIcon,

        dueDateMode = dueDateMode,
        dueDatePeriod = dueDatePeriod,
        dueDate = dueDate,
    )
}
