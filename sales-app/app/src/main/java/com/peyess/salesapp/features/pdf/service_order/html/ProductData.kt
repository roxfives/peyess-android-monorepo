package com.peyess.salesapp.features.pdf.service_order.html

//<!-- nro OS, nro cliente, indicado, data nasc, presc_proff, presc_date, lente_direito, lente_esquerdo -->
//<!-- ESF.       CIL.    EIXO  ADIÇÃO  PRISMA  POS. PRISMA  DNP  ALTURA (esquerdo) -->
//<!-- ESF.       CIL.    EIXO  ADIÇÃO  PRISMA  POS. PRISMA  DNP  ALTURA (direito) -->
//<!-- armação_grife, tratamento_direito, tratamento_esquerdo -->
//<!-- armação_ref, coloracao_direito, coloracao_esquerdo -->
//<!-- armação_cor, armação_estilo, armação_tipo,  -->
//<!-- ponte, aro_vertical, aro_horizontal -->
//<!-- companhamento_pedido -->

fun generateProductData(
    soHid: String = "",

    clientId: String = "",
    clientName: String = "",
    clientBirthday: String = "",

    lensLeft: String = "",
    lensRight: String = "",
    coloringLeft: String = "",
    coloringRight: String = "",
    treatmentLeft: String = "",
    treatmentRight: String = "",

    prescriptionDate: String = "",
    prescriptionProfessional: String = "",

    prescriptionLeftSpherical: String = "",
    prescriptionLeftCylindrical: String = "",
    prescriptionLeftAxis: String = "",
    prescriptionLeftAddition: String = "",
    prescriptionLeftPrismPosition: String = "",
    prescriptionLeftPrismDegree: String = "",
    prescriptionLeftDnp: String = "",
    prescriptionLeftHeight: String = "",

    prescriptionRightSpherical: String = "",
    prescriptionRightCylindrical: String = "",
    prescriptionRightAxis: String = "",
    prescriptionRightAddition: String = "",
    prescriptionRightPrismPosition: String = "",
    prescriptionRightPrismDegree: String = "",
    prescriptionRightDnp: String = "",
    prescriptionRightHeight: String = "",

    framesDesign: String = "",
    framesReference: String = "",
    framesColor: String = "",
    framesStyle: String = "",
    framesType: String = "",

    measuringBridge: String = "",
    measuringVHoop: String = "",
    measuringHHoop: String = "",

    trackingCode: String = "",
    trackingPortalLink: String = "",
): String {
    val trackingLinkMessage = if (trackingPortalLink.isEmpty()) {
        "-"
    } else {
        "Acesse $trackingPortalLink ou finalize seu cadastro."
    }

    val productData = "<tr class=\"row4\"> <td class=\"column0 style6 null\"></td><td class=\"column1 style6 null\"></td><td class=\"column2 style6 null\"></td><td class=\"column3 style93 s style93\" colspan=\"5\">DADOS PARA FABRICAÇÃO DO ÓCULOS</td><td class=\"column8 style95 s style95\" colspan=\"3\">O.S.: $soHid</td></tr><tr class=\"row5\"><td class=\"column2 style39 s style40\" colspan=\"9\">INDICADO: $clientName</td><td class=\"column9 style39 s style40\" colspan=\"2\">NASC.: $clientBirthday</td></tr><tr class=\"row6\"> <td class=\"column0 style39 s style68\" colspan=\"7\">AUTOR PRESCRIÇÃO: $prescriptionProfessional</td><td class=\"column7 style39 s style40\" colspan=\"4\">DATA DE PRESCRIÇÃO: $prescriptionDate</td></tr><tr class=\"row7\"> <td class=\"column0 style24 s style26\" colspan=\"5\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ESF. CIL. EIXO ADIÇÃO PRISMA POS. PRISMA DNP ALTURA</td><td class=\"column5 style33 s style35\" colspan=\"3\">LENTE OLHO DIREITO</td><td class=\"column8 style33 s style35\" colspan=\"3\">LENTE OLHO ESQUERDO</td></tr><tr class=\"row8\"> <td class=\"column0 style27 s style29\" colspan=\"5\"><span style=\"font-weight:bold; color:#000000; font-family:'Calibri'; font-size:10pt\">O.D</span><span style=\"color:#000000; font-family:'Calibri'; font-size:8pt\"> $prescriptionLeftSpherical $prescriptionLeftCylindrical $prescriptionLeftAxis $prescriptionLeftAddition $prescriptionLeftPrismDegree $prescriptionLeftPrismPosition $prescriptionLeftDnp $prescriptionLeftHeight </span></td><td class=\"column5 style18 s style23\" colspan=\"3\" rowspan=\"2\">$lensLeft</td><td class=\"column8 style18 s style23\" colspan=\"3\" rowspan=\"2\">$lensRight</td></tr><tr class=\"row9\"> <td class=\"column0 style30 s style32\" colspan=\"5\"><span style=\"font-weight:bold; color:#000000; font-family:'Calibri'; font-size:10pt\">O.E </span><span style=\"color:#000000; font-family:'Calibri'; font-size:8pt\"> $prescriptionRightSpherical $prescriptionRightCylindrical $prescriptionRightAxis $prescriptionRightAddition $prescriptionRightPrismDegree $prescriptionRightPrismPosition $prescriptionRightDnp $prescriptionRightHeight </span></td></tr><tr class=\"row10\"> <td class=\"column0 style47 s style48\" colspan=\"2\">DADOS ARMAÇÃO</td><td class=\"column2 style49 s style50\" colspan=\"2\">GRIFE: $framesDesign</td><td class=\"column4 style7 s\">TRATAMENTO</td><td class=\"column5 style44 s style45\" colspan=\"3\">$treatmentRight</td><td class=\"column8 style46 s style45\" colspan=\"3\">$treatmentLeft</td></tr><tr class=\"row11\"> <td class=\"column0 style49 s style52\" colspan=\"4\">REFERÊNCIA: ${framesReference.ifBlank { "-" }}</td><td class=\"column4 style7 s\">COLORAÇÃO</td><td class=\"column5 style44 s style45\" colspan=\"3\">$coloringLeft</td><td class=\"column8 style46 s style45\" colspan=\"3\">$coloringRight</td></tr><tr class=\"row12\"> <td class=\"column0 style49 s style51\" colspan=\"2\">COR: ${framesColor.ifBlank { "-" }}</td><td class=\"column2 style49 s style50\" colspan=\"2\">ESTILO: ${framesStyle.ifBlank { "-" }}</td><td class=\"column4 style49 s style50\" colspan=\"3\">TIPO: ${framesType.ifEmpty { "-" }}</td><td class=\"column7 style41 s style43\" colspan=\"4\"><span style=\"font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt\">PONTE</span><span style=\"color:#000000; font-family:'Calibri'; font-size:9pt\">: $measuringBridge </span><span style=\"font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt\">ARO VERTICAL</span><span style=\"color:#000000; font-family:'Calibri'; font-size:9pt\">: $measuringVHoop </span><span style=\"font-weight:bold; color:#000000; font-family:'Calibri'; font-size:9pt\">ARO HORIZONTAL</span><span style=\"color:#000000; font-family:'Calibri'; font-size:9pt\">: $measuringHHoop</span></td></tr><tr class=\"row13\"> <td class=\"column0 style47 s style48\" colspan=\"3\">ACOMPANHAMENTO DE PEDIDO:</td><td class=\"column3 style53 s style38\" colspan=\"2\">${trackingCode.ifBlank { "-" }}</td><td class=\"column5 style54 s style56\" colspan=\"6\">$trackingLinkMessage</td></tr>"

    return productData
}

