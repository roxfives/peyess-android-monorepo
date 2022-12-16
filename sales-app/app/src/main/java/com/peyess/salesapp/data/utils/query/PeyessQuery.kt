package com.peyess.salesapp.data.utils.query

data class PeyessQuery(
    val queryFields: List<PeyessQueryField>,
    val orderBy: PeyessOrderBy,
    val withLimit: Int? = null,
)
