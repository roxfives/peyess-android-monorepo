package com.peyess.salesapp.features.pdf.service_order.html

fun generateServiceOrder(style: String, content: String): String {
    val serviceOrderHtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\"><html> $style <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"/> <meta name=\"author\" content=\"Peyess\"/> </head> <body> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" id=\"sheet0\" class=\"sheet0 gridlines\" > <col class=\"col0\"/> <col class=\"col1\"/> <col class=\"col2\"/> <col class=\"col3\"/> <col class=\"col4\"/> <col class=\"col5\"/> <col class=\"col6\"/> <col class=\"col7\"/> <col class=\"col8\"/> <col class=\"col9\"/> <col class=\"col10\"/> <tbody> $content </tbody> </table> </body></html>"

    return serviceOrderHtml
}