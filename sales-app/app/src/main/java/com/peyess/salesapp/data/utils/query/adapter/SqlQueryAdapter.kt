package com.peyess.salesapp.data.utils.query.adapter

import androidx.sqlite.db.SimpleSQLiteQuery
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.types.Order
import timber.log.Timber

fun PeyessQuery.toSqlQuery(selectStatement: String): SimpleSQLiteQuery {
    var query = "$selectStatement "

    var whereClause: String
    var value: Any
    queryFields.forEach { peyessQuery ->
        whereClause = if (query.contains("WHERE")) {
            " AND "
        } else {
            " WHERE "
        }

        value = if (peyessQuery.value is Boolean) {
            if (peyessQuery.value as Boolean) 1 else 0
        } else if (peyessQuery.value is String) {
            "'${peyessQuery.value}'"
        } else {
            peyessQuery.value
        }

        when (peyessQuery.op) {
            PeyessQueryOperation.Equal ->
                query += "$whereClause ${peyessQuery.field} = $value"

            PeyessQueryOperation.Different ->
                query += "$whereClause ${peyessQuery.field} != $value"

            PeyessQueryOperation.GreaterThan ->
                query += "$whereClause ${peyessQuery.field} > $value"

            PeyessQueryOperation.GreaterThanOrEqual ->
                query += "$whereClause ${peyessQuery.field} >= $value"

            PeyessQueryOperation.LessThan ->
                query += "$whereClause ${peyessQuery.field} < $value"

            PeyessQueryOperation.LessThanOrEqual ->
                query += "$whereClause ${peyessQuery.field} <= $value"

            PeyessQueryOperation.Noop -> {
                Timber.i("Used a noop while building the query")
            }
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

    val limitClause = if (withLimit != null) {
        "LIMIT $withLimit"
    } else {
        ""
    }

    return SimpleSQLiteQuery("$query $groupByClause $orderByClause $limitClause")
}