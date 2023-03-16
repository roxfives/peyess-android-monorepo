package com.peyess.salesapp.data.utils.query

data class PeyessQuery(
    val queryFields: List<PeyessQueryField> = emptyList(),
    val orderBy: List<PeyessOrderBy> = emptyList(),
    val groupBy: List<PeyessGroupBy> = emptyList(),
    val defaultOp: String = "AND",
    val withLimit: Int? = null,
)
