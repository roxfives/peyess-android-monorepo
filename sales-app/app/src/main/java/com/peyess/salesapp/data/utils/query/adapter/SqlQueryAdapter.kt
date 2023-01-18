package com.peyess.salesapp.data.utils.query.adapter

import androidx.sqlite.db.SimpleSQLiteQuery
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryArithmeticExpressionField
import com.peyess.salesapp.data.utils.query.PeyessQueryArithmeticOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryConstantField
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryMinMaxField
import com.peyess.salesapp.data.utils.query.PeyessQueryFunctionOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryPredicateExpressionField
import com.peyess.salesapp.data.utils.query.PeyessQueryPredicateOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryRegularField
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import timber.log.Timber

fun PeyessQuery.toSqlQuery(selectStatement: String): SimpleSQLiteQuery {
    var query = "$selectStatement "

    var whereClause: String
    var clauseExpression: String
    queryFields.forEach { peyessQuery ->
        whereClause = if (query.contains("WHERE")) {
            " AND "
        } else {
            " WHERE "
        }

        clauseExpression = buildExpressionForQueryField(peyessQuery)
        query += if (clauseExpression.isEmpty()) {
            ""
        } else {
            whereClause + clauseExpression
        }
    }

    var orderByClause: String
    var order: String
    orderByClause = orderBy.joinToString(separator = ", ") { peyessOrderBy ->
        order = when (peyessOrderBy.order) {
            Order.ASCENDING -> "ASC"
            Order.DESCENDING -> "DESC"
        }

        "${peyessOrderBy.field} $order"
    }
    if (orderByClause.isNotBlank()) {
        orderByClause = " ORDER BY $orderByClause"
    }

    var groupByClause = groupBy.joinToString(separator = ", ") { peyessGroupBy ->
        peyessGroupBy.field
    }
    if (groupByClause.isNotBlank()) {
        groupByClause = " GROUP BY $groupByClause"
    }

    val havingClauseFields = queryFields.filterIsInstance<PeyessQueryMinMaxField>()
    val havingClause = if (havingClauseFields.isEmpty()) {
        ""
    } else {
        buildHavingClause(havingClauseFields)
    }

    val limitClause = if (withLimit != null) {
        "LIMIT $withLimit"
    } else {
        ""
    }

    return SimpleSQLiteQuery("$query $groupByClause $havingClause $orderByClause $limitClause")
}

private fun buildExpressionForQueryField(
    queryField: PeyessQueryField,
): String {
    return when (queryField) {
        is PeyessQueryConstantField ->
            buildExpressionForConstantField(queryField)
        is PeyessQueryRegularField ->
            buildExpressionForRegularField(queryField)
        is PeyessQueryArithmeticExpressionField ->
            buildExpressionForArithmeticField(queryField)
        is PeyessQueryPredicateExpressionField ->
            buildExpressionForPredicateField(queryField)
        is PeyessQueryMinMaxField -> ""
    }
}

private fun buildExpressionForConstantField(field: PeyessQueryConstantField): String {
    val value = if (field.value is Boolean) {
        if (field.value as Boolean) 1 else 0
    } else if (field.value is String) {
        "'${field.value}'"
    } else {
        field.value
    }

    return "$value"
}

private fun buildExpressionForRegularField(
    queryField: PeyessQueryRegularField
): String {
    val value = if (queryField.value is Boolean) {
        if (queryField.value as Boolean) 1 else 0
    } else if (queryField.value is String) {
        "'${queryField.value}'"
    } else {
        queryField.value
    }

    return when (queryField.op) {
        PeyessQueryOperation.Equal ->
            "${queryField.field} = $value"

        PeyessQueryOperation.Different ->
            "${queryField.field} != $value"

        PeyessQueryOperation.GreaterThan ->
            "${queryField.field} > $value"

        PeyessQueryOperation.GreaterThanOrEqual ->
            "${queryField.field} >= $value"

        PeyessQueryOperation.LessThan ->
            "${queryField.field} < $value"

        PeyessQueryOperation.LessThanOrEqual ->
            "${queryField.field} <= $value"

        PeyessQueryOperation.Noop -> {
            Timber.i("Used a noop while building the query")
            ""
        }
    }
}

private fun buildHavingClause(queryFields: List<PeyessQueryMinMaxField>): String {
    var havingClause = ""

    queryFields.forEach { queryField ->
        havingClause += if (havingClause.contains("HAVING")) {
            " AND "
        } else {
            " HAVING "
        }

        havingClause += buildExpressionForRegularField(
            buildQueryField(
                "__${queryField.field}__",
                queryField.op,
                queryField.value,
            ) as PeyessQueryRegularField
        )
    }

    return havingClause
}

private fun buildExpressionForArithmeticField(
    query: PeyessQueryArithmeticExpressionField,
): String {
    return when (query.arithmeticOp) {
        is PeyessQueryArithmeticOperation.Sum -> {
            val fieldSum = query.fields.joinToString(separator = " + ") { field ->
                field
            }

            buildExpressionForQueryField(
                queryField = buildQueryField(
                    field = fieldSum,
                    op = query.compareOp,
                    value = query.value,
                )
            )
        }
    }
}

private fun buildExpressionForPredicateField(
    queryField: PeyessQueryPredicateExpressionField
): String {
    return when(queryField.op) {
        PeyessQueryPredicateOperation.IFF ->
            "IIF(${buildExpressionForQueryField(queryField.expression)}, " +
                    "${buildExpressionForQueryField(queryField.ifTrue)}, " +
                    "${buildExpressionForQueryField(queryField.ifFalse)})"
    }
}