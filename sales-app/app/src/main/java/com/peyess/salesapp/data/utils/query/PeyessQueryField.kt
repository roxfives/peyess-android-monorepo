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

private data class PeyessQueryFieldBoolean(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: Boolean,
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
    return PeyessQueryFieldBoolean(field, op, value)
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