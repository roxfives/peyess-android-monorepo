package com.peyess.salesapp.features.pdf.service_order.html

//<!-- numero da venda, data da venda, hora da venda, nome comprador, data nascimento comprador, nro cliente, cpf, rg, telefone, bairro, cidade, CEP -->

fun generateSaleData(
    saleHid: String,
    saleDate: String,
    saleHour: String,
    clientName: String,
    clientPhone: String,
    clientCellphone: String,
    clientBirthday: String,
    clientId: String,
    clientDocument: String,
    clientRg: String,
    clientNeighborhood: String,
    clientStreet: String,
    clientCity: String,
    clientState: String,
    clientHouseNumber: String,
    clientZipCode: String,
): String {
    var clientCommunication = clientCellphone
    if (clientPhone.isNotBlank()) {
        clientCommunication += " / $clientPhone"
    }

    val saleData = "<tr class=\"row0\"> <td class=\"column0 style92 null style92\" colspan=\"4\"></td><td class=\"column4 style93 s style93\" colspan=\"3\"> VENDA: $saleHid </td><td class=\"column7 style94 s style94\" colspan=\"4\"> DATA DA VENDA: <span style=\" font-weight: bold; color: #000000; font-family: 'Calibri'; font-size: 11pt; \" >$saleDate</span ><span style=\" color: #000000; font-family: 'Calibri'; font-size: 11pt; \" > ÀS </span ><span style=\" font-weight: bold; color: #000000; font-family: 'Calibri'; font-size: 11pt; \" >$saleHour</span ><span style=\" color: #000000; font-family: 'Calibri'; font-size: 11pt; \" >hrs</span > </td></tr><tr class=\"row1\"> <td class=\"column0 style36 s style38\" colspan=\"9\"> COMPRADOR: $clientName </td><td class=\"column9 style36 s style38\" colspan=\"2\"> NASC.: $clientBirthday </td></tr><tr class=\"row2\"> <td class=\"column0 style36 s style38\" colspan=\"2\"> N° Cliente: $clientId </td><td class=\"column2 style36 s style38\" colspan=\"3\">CPF: $clientDocument</td><td class=\"column5 style36 s style38\" colspan=\"3\">RG: $clientRg</td><td class=\"column8 style36 s style38\" colspan=\"3\"> $clientCommunication </td></tr><tr class=\"row3\"> <td class=\"column0 style36 s style38\" colspan=\"4\">END.: $clientStreet</td><td class=\"column4 style5 s\">N° $clientHouseNumber</td><td class=\"column5 style36 s style38\" colspan=\"2\"> $clientNeighborhood </td><td class=\"column7 style36 s style38\" colspan=\"2\"> $clientCity, $clientState </td><td class=\"column9 style36 s style38\" colspan=\"2\"> CEP: $clientZipCode </td></tr>"

    return saleData
}