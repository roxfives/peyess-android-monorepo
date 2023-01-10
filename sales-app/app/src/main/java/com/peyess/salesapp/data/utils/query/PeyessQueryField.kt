package com.peyess.salesapp.data.utils.query

import java.math.BigDecimal

sealed interface PeyessQueryField

sealed interface PeyessQueryConstantField: PeyessQueryField {
    val value: Any
}

sealed interface PeyessQueryRegularField: PeyessQueryField {
    val field: String
    val op: PeyessQueryOperation
    val value: Any
}

sealed interface PeyessQueryArithmeticExpressionField: PeyessQueryField {
    val fields: List<String>
    val arithmeticOp: PeyessQueryArithmeticOperation
    val compareOp: PeyessQueryOperation
    val value: Double
}

sealed interface PeyessQueryPredicateExpressionField: PeyessQueryField {
    val op: PeyessQueryPredicateOperation
    val expression: PeyessQueryField
    val ifTrue: PeyessQueryField
    val ifFalse: PeyessQueryField
}

private data class PeyessQueryFieldDouble(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: Double,
): PeyessQueryRegularField

private data class PeyessQueryFieldInt(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: Int,
): PeyessQueryRegularField

private data class PeyessQueryFieldBoolean(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: Boolean,
): PeyessQueryRegularField

private data class PeyessQueryFieldString(
    override val field: String,
    override val op: PeyessQueryOperation,
    override val value: String,
): PeyessQueryRegularField

private data class PeyessQueryFieldSumOperation(
    override val fields: List<String>,
    override val arithmeticOp: PeyessQueryArithmeticOperation,
    override val compareOp: PeyessQueryOperation,
    override val value: Double,
): PeyessQueryArithmeticExpressionField

private data class PeyessQueryFieldIFFOperation(
    override val op: PeyessQueryPredicateOperation,
    override val expression: PeyessQueryField,
    override val ifTrue: PeyessQueryField,
    override val ifFalse: PeyessQueryField,

): PeyessQueryPredicateExpressionField

private data class PeyessQueryConstantDouble(
    override val value: Double,
): PeyessQueryConstantField

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

fun buildQueryField(
    fields: List<String>,
    arithmeticOp: PeyessQueryArithmeticOperation.Sum,
    compareOp: PeyessQueryOperation,
    value: Double,
): PeyessQueryField {
    return PeyessQueryFieldSumOperation(fields, arithmeticOp, compareOp, value)
}

fun buildQueryField(
    op: PeyessQueryPredicateOperation,
    expression: PeyessQueryField,
    ifTrue: PeyessQueryField,
    ifFalse: PeyessQueryField,
): PeyessQueryField {
    return PeyessQueryFieldIFFOperation(op, expression, ifTrue, ifFalse)
}

fun buildQueryField(constant: Double): PeyessQueryField {
    return PeyessQueryConstantDouble(value = constant)
}