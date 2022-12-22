package com.peyess.salesapp.data.utils.query

import com.peyess.salesapp.data.utils.query.types.Order

data class PeyessQuery(
    val queryFields: List<PeyessQueryField> = emptyList(),
    val orderBy: PeyessOrderBy = PeyessOrderBy(field = "", order = Order.ASCENDING),
    val withLimit: Int? = null,
)
