package com.peyess.salesapp.features.pdf.service_order.html

import com.peyess.salesapp.features.pdf.utils.printDocument
import com.peyess.salesapp.features.pdf.utils.printValue

fun buildTakeAwaySection(
    name: String,
    document: String,
): String {
    val takeaway = "<tr class=\"row34\"> <td class=\"column0 style88 s style63\" style=\"color:#FF0000;\" colspan=\"11\"><br />Sendo também autorizado a retirada dos produtos por <span style=\"font-weight:bold; color:#FF0000; font-family:'Calibri'; font-size:8pt\">${printValue(name)}</span><span style=\"color:#FF0000; font-family:'Calibri'; font-size:8pt\"> inscrito no CPF/MF de </span><span style=\"font-weight:bold; color:#FF0000; font-family:'Calibri'; font-size:8pt\">${printDocument(document)}</span><span style=\"color:#FF0000; font-family:'Calibri'; font-size:8pt\">.<br />&#8203;</span></td></tr>"

    return takeaway
}