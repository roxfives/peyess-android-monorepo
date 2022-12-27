package com.peyess.salesapp.data.utils.query

import java.math.BigDecimal

sealed interface PeyessQueryField {
    val field: String
    val op: PeyessQueryOperation
    val value: Any
}

private data class PeyessQueryFieldDouble(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: Double,
): PeyessQueryField

private data class PeyessQueryFieldInt(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: Int,
): PeyessQueryField

private data class PeyessQueryFieldString(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: String,
): PeyessQueryField

fun buildQueryField(field: String, op: PeyessQueryOperation, value: Int): PeyessQueryField {
    return PeyessQueryFieldInt(field, op, value)
}

fun buildQueryField(field: String, op: PeyessQueryOperation, value: Boolean): PeyessQueryField {
    val actualValue = if (value) 1 else 0

    return PeyessQueryFieldInt(field, op, actualValue)
}

fun buildQueryField(field: String, op: PeyessQueryOperation, value: Double): PeyessQueryField {
    return PeyessQueryFieldDouble(field, op, value)
}

fun buildQueryField(field: String, op: PeyessQueryOperation, value: BigDecimal): PeyessQueryField {
    return PeyessQueryFieldDouble(field, op, value.toDouble())
}

fun buildQueryField(field: String, op: PeyessQueryOperation, value: String): PeyessQueryField {
    return PeyessQueryFieldString(field, op, value)
}