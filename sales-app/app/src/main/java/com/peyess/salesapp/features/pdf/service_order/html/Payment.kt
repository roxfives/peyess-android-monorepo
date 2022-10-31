package com.peyess.salesapp.features.pdf.service_order.html

import android.content.Context
import androidx.core.os.ConfigurationCompat
import com.peyess.salesapp.data.model.sale.purchase.PaymentDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.features.pdf.utils.printDocument
import com.peyess.salesapp.features.pdf.utils.printPaymentMethodValue
import com.peyess.salesapp.features.pdf.utils.printValue
import com.peyess.salesapp.typing.sale.PaymentMethodType
import java.text.NumberFormat

//<!-- payment_text -->
//
//<!-- 1 pagador, integral -->
//<!-- Em razão do presente pedido, pago neste ato a importância de R$XXXX,XX através das Formas de Pagamento abaixo. -->
//
//<!--n pagadores, integral -->
//<!-- Declaramos para os devidos fins que somos responsáveis pelas obrigações assumidas pelo <span style="font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt">COMPRADOR</span><span style="color:#000000; font-family:'Calibri'; font-size:9pt"> e pagaremos o valor de </span><span style="font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt">R$XXXX,XX</span><span style="color:#000000; font-family:'Calibri'; font-size:9pt"> da seguinte forma:</span> -->
//
//<!-- 1 pagador, parcial -->
//<!-- Em razão do presente pedido, declaro que pagarei o valor de <span style="font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt">R$XXXX,XX</span><span style="color:#000000; font-family:'Calibri'; font-size:9pt"> da seguinte forma:</span> -->
//
//<!-- n pagadores, parcial -->
//<!-- Declaramos para os devidos fins que somos responsáveis pelas obrigações assumidas pelo <span style="font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt">COMPRADOR</span><span style="color:#000000; font-family:'Calibri'; font-size:9pt"> e pagaremos o valor de </span><span style="font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt">R$XXXX,XX</span><span style="color:#000000; font-family:'Calibri'; font-size:9pt"> da seguinte forma:</span> -->


//val header = "<tr class=\"row23\"> <td class=\"column0 style58 s style60\" colspan=\"11\">PAGAMENTOS</td></tr><tr class=\"row24\"> <td class=\"column0 style53 s style55\" colspan=\"11\">%s</td></tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, valor_pagamento -->
//val money = "<tr class=\"row25\"> <td class=\"column0 style24 s style25\" colspan=\"2\">N° Cliente: %s</td><td class=\"column2 style26 s style28\" colspan=\"6\">NOME: %s</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: %s</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: DINHEIRO</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, nome_cartao, valor_pagamento -->
//val debit = "<tr class=\"row25\">\n" + "    <td class=\"column0 style24 s style25\" colspan=\"2\">N° Cliente: %s</td>\n" + "    <td class=\"column2 style26 s style28\" colspan=\"6\">NOME: %s</td>\n" + "    <td class=\"column8 style26 s style28\" colspan=\"3\">CPF: %s</td>\n" + "</tr>\n" + "<tr class=\"row26\">\n" + "    <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: DÉBITO %s À VISTA</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td>\n" + "</tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, valor_pagamento -->
//val pix = "<tr class=\"row25\"> <td class=\"column0 style24 s style25\" colspan=\"2\">N° Cliente: %s</td><td class=\"column2 style26 s style28\" colspan=\"6\">NOME: %s</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: %s</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: PIX À VISTA</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, nome_cartao, valor_pagamento -->
//val creditFull = "<tr class=\"row25\"> <td class=\"column0 style24 s style25\" colspan=\"2\">N° Cliente: %s</td><td class=\"column2 style26 s style28\" colspan=\"6\">NOME: %s</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: %s</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: CRÉDITO %s À VISTA</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, nro_parcelas, nome_cartao, valor_pagamento -->
//val creditInstallments = "<tr class=\"row25\"> <td class=\"column0 style24 s style25\" colspan=\"2\">N° Cliente: %s</td><td class=\"column2 style26 s style28\" colspan=\"6\">NOME: %s</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: %s</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: %sX %s</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, valor_pagamento, data_vencimento, nro_parcelas -->
//val crediario = "<tr class=\"row28\"> <td class=\"column0 style78 s style63\" colspan=\"2\">N° Cliente: %s</td><td class=\"column2 style29 s style31\" colspan=\"6\">NOME: %s</td><td class=\"column8 style29 s style31\" colspan=\"3\">CPF: %s</td></tr><tr class=\"row29\"> <td class=\"column0 style102 s style103\" colspan=\"4\">FORMA PAGAMENTO: CARNÊ</td><td class=\"column4 style102 s style104\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style26 s style28\" colspan=\"3\">DATA 1º VENCIMENTO: %s</td><td class=\"column9 style26 s style28\" colspan=\"2\">MULTA: 1%</td></tr><tr class=\"row30\"> <td class=\"column0 style105 s style106\" colspan=\"2\">JUROS MORA: 2%</td><td class=\"column2 style26 s style28\" colspan=\"3\">NÚMERO DE PARCELAS: %s</td><td class=\"column5 style102 s style104\" colspan=\"6\">Assinatura:_____________________________________________________</td></tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, valor_pagamento -->
//val bankTransfer = "<tr class=\"row25\"> <td class=\"column0 style24 s style25\" colspan=\"2\">N° Cliente: %s</td><td class=\"column2 style26 s style28\" colspan=\"6\">NOME: %s</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: %s</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO:</td><td class=\"column2 style54 s style55\" colspan=\"2\">TRANSFERÊNCIA BANCÁRIA</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

//<!-- nro_client, nome_pagador, cpf_pagador, valor_pagamento -->
//val bankCheck = "<tr class=\"row25\"> <td class=\"column0 style24 s style25\" colspan=\"2\">N° Cliente: %s</td><td class=\"column2 style26 s style28\" colspan=\"6\">NOME: %s</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: %s</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO:</td><td class=\"column2 style54 s style55\" colspan=\"2\">CHEQUE</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

//<!-- valor, data_vencimento,  -->
val notPayed = "<tr class=\"row27\"> <td class=\"column0 style29 s style30\" colspan=\"4\">FORMA PAGAMENTO: À RECEBER</td><td class=\"column4 style29 s style31\" colspan=\"2\">VALOR: R\$%s</td><td class=\"column6 style26 s style25\" colspan=\"3\">DATA 1º VENCIMENTO: %s</td><td class=\"column9 style26 s style25\" colspan=\"2\">MULTA: 1%</td></tr><tr class=\"row28\"> <td class=\"column0 style27 s style28\" colspan=\"2\">JUROS MORA: 2%</td><td class=\"column2 style26 s style25\" colspan=\"3\">NÚMERO DE PARCELAS: 1</td><td class=\"column5 style29 s style31\" colspan=\"6\">Assinatura:_____________________________________________________</td></tr><tr class=\"row29\"> <td class=\"column0 style65 s style67\" colspan=\"11\">Pagável somente em lojas físicas / após 15 dias da data de vencimento incidirá protesto automático</td></tr>"

private fun buildPaymentForMoney(context: Context, payment: PaymentDocument): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val clientName = payment.payerName
    val clientDocument = printDocument(payment.payerDocument)

    val paymentMethod = printPaymentMethodValue(
        context,
        "DINHEIRO",
        1,
    )

    val paymentValue = currencyFormat.format(payment.amount)

    val debit = "<tr class=\"row25\"><td class=\"column2 style26 s style28\" colspan=\"8\">NOME: $clientName</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: ${printValue(clientDocument)}</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: $paymentMethod</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: $paymentValue</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

    return debit
}

private fun buildPaymentForPix(context: Context, payment: PaymentDocument): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val clientName = payment.payerName
    val clientDocument = printDocument(payment.payerDocument)

    val paymentMethod = printPaymentMethodValue(
        context,
        "PIX",
        1,
    )

    val paymentValue = currencyFormat.format(payment.amount)

    val debit = "<tr class=\"row25\"><td class=\"column2 style26 s style28\" colspan=\"8\">NOME: $clientName</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: ${printValue(clientDocument)}</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: $paymentMethod</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: $paymentValue</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

    return debit
}

private fun buildPaymentForDebit(context: Context, payment: PaymentDocument): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val clientName = payment.payerName
    val clientDocument = printDocument(payment.payerDocument)

    val paymentMethod = printPaymentMethodValue(
        context,
        "DÉBITO ${payment.cardFlagName.uppercase(currentLocale)}",
        1,
    )

    val paymentValue = currencyFormat.format(payment.amount)

    val debit = "<tr class=\"row25\"><td class=\"column2 style26 s style28\" colspan=\"8\">NOME: $clientName</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: ${printValue(clientDocument)}</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: $paymentMethod</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: $paymentValue</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

    return debit
}

private fun buildPaymentForCredit(context: Context, payment: PaymentDocument): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val clientName = payment.payerName
    val clientDocument = printDocument(payment.payerDocument)

    val paymentMethod = printPaymentMethodValue(
        context,
        "CRÉDITO ${payment.cardFlagName.uppercase(currentLocale)}",
        payment.installments,
    )

    val paymentValue = currencyFormat.format(payment.amount)

    val credit = "<tr class=\"row25\"><td class=\"column2 style26 s style28\" colspan=\"8\">NOME: $clientName</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: ${printValue(clientDocument)}</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: $paymentMethod</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: $paymentValue</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

    return credit
}

private fun buildPaymentForCrediario(context: Context, payment: PaymentDocument): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val clientName = payment.payerName
    val clientDocument = printDocument(payment.payerDocument)

    val paymentMethod = printPaymentMethodValue(context, "CARNÊ", payment.installments)

    val paymentValue = currencyFormat.format(payment.amount)

    val crediario = "<tr class=\"row25\"><td class=\"column2 style26 s style28\" colspan=\"8\">NOME: $clientName</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: ${printValue(clientDocument)}</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: $paymentMethod</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: $paymentValue</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

    return crediario
}

private fun buildPaymentForBankTransfer(context: Context, payment: PaymentDocument): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val clientName = payment.payerName
    val clientDocument = printDocument(payment.payerDocument)

    val paymentMethod = printPaymentMethodValue(context, "TRANSFERÊNCIA BANCÁRIA", payment.installments)

    val paymentValue = currencyFormat.format(payment.amount)

    val bankTransfer = "<tr class=\"row25\"><td class=\"column2 style26 s style28\" colspan=\"8\">NOME: $clientName</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: ${printValue(clientDocument)}</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: $paymentMethod</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: $paymentValue</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

    return bankTransfer
}

private fun buildPaymentForBankCheck(context: Context, payment: PaymentDocument): String {
    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormat = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormat.minimumFractionDigits = 2
    currencyFormat.maximumFractionDigits = 2

    val clientName = payment.payerName
    val clientDocument = printDocument(payment.payerDocument)

    val paymentMethod = printPaymentMethodValue(context, "CHEQUE", payment.installments)

    val paymentValue = currencyFormat.format(payment.amount)

    val bankCheck = "<tr class=\"row25\"><td class=\"column2 style26 s style28\" colspan=\"8\">NOME: ${printValue(clientName)}</td><td class=\"column8 style26 s style28\" colspan=\"3\">CPF: ${printValue(clientDocument)}</td></tr><tr class=\"row26\"> <td class=\"column0 style100 s style54\" colspan=\"4\">FORMA PAGAMENTO: $paymentMethod</td><td class=\"column4 style48 s style49\" colspan=\"2\">VALOR: ${printValue(paymentValue)}</td><td class=\"column6 style50 s style52\" colspan=\"5\">Assinatura:_______________________________________</td></tr>"

    return bankCheck
}

private fun buildPaymentMethod(context: Context, payment: PaymentDocument): String {
    return when (payment.methodName) {
        PaymentMethodType.BankCheck -> buildPaymentForBankCheck(context, payment)
        PaymentMethodType.BankDeposit -> buildPaymentForBankTransfer(context, payment)
        PaymentMethodType.Crediario -> buildPaymentForCrediario(context, payment)
        PaymentMethodType.Credit -> buildPaymentForCredit(context, payment)
        PaymentMethodType.CreditFull -> buildPaymentForCredit(context, payment)
        PaymentMethodType.Debit -> buildPaymentForDebit(context, payment)
        PaymentMethodType.Money -> buildPaymentForMoney(context, payment)
        PaymentMethodType.Pix -> buildPaymentForPix(context, payment)
        else -> ""
    }
}

private fun legalTextForFullPayment(
    context: Context,
    purchaseDocument: PurchaseDocument,
    serviceOrderDocument: ServiceOrderDocument,
): String {
    val hasMultiplePayers = purchaseDocument.payerUids.size > 1

    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormatter.maximumFractionDigits = 2
    currencyFormatter.minimumFractionDigits = 2

    val totalPaid = currencyFormatter.format(serviceOrderDocument.totalPaid)

    return if (hasMultiplePayers) {
        "Declaramos para os devidos fins que somos responsáveis pelas obrigações assumidas pelo " +
                "COMPRADOR e pagaremos o valor de $totalPaid da seguinte forma:"
    } else {
        "Em razão do presente pedido, pago neste ato a importância de $totalPaid através das " +
                "Formas de Pagamento abaixo:"
    }
}

private fun legalTextWithoutFullPayment(
    context: Context,
    purchaseDocument: PurchaseDocument,
    serviceOrderDocument: ServiceOrderDocument,
): String {
    val hasMultiplePayers = purchaseDocument.payerUids.size > 1

    val currentLocale = ConfigurationCompat.getLocales(context.resources.configuration)[0]!!

    val currencyFormatter = NumberFormat.getCurrencyInstance(currentLocale)
    currencyFormatter.maximumFractionDigits = 2
    currencyFormatter.minimumFractionDigits = 2

    val totalPaid = currencyFormatter.format(serviceOrderDocument.totalPaid)

    return if (hasMultiplePayers) {
        "Declaramos para os devidos fins que somos responsáveis pelas obrigações assumidas pelo " +
                "COMPRADOR e pagaremos o valor de $totalPaid da seguinte forma:"
    } else {
        "Em razão do presente pedido, declaro que pagarei o valor de $totalPaid da seguinte forma:"
    }
}

private fun legalText(
    context: Context,
    purchaseDocument: PurchaseDocument,
    serviceOrderDocument: ServiceOrderDocument,
): String {
    val isPaymentFull = serviceOrderDocument.isPaymentFull

    return if (isPaymentFull) {
        legalTextForFullPayment(context, purchaseDocument, serviceOrderDocument)
    } else {
        legalTextWithoutFullPayment(context, purchaseDocument, serviceOrderDocument)
    }
}

private fun buildHeader(
    context: Context,
    purchaseDocument: PurchaseDocument,
    serviceOrderDocument: ServiceOrderDocument,
): String {
    val legal = legalText(context, purchaseDocument, serviceOrderDocument)
    val header = "<tr class=\"row23\"> <td class=\"column0 style92 style58 s style60\" colspan=\"11\">PAGAMENTOS</td></tr><tr class=\"row24\"> <td class=\"column0 s style55 style58\" colspan=\"11\">$legal</td></tr>"

    return header
}

private fun buildPaymentList(context: Context, payments: List<PaymentDocument>): String {
    var paymentStr = ""
    payments.forEach {
        paymentStr += buildPaymentMethod(context, it)
    }

    return paymentStr
}

fun buildPaymentSection(
    context: Context,
    purchase: PurchaseDocument,
    serviceOrder: ServiceOrderDocument,
): String {
    val header = buildHeader(context, purchase, serviceOrder)
    val paymentList = buildPaymentList(context, purchase.payments)

    return header + paymentList
}
