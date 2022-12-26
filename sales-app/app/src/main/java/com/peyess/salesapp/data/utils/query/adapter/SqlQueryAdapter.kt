package com.peyess.salesapp.data.utils.query.adapter

import androidx.sqlite.db.SimpleSQLiteQuery
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.types.Order
import timber.log.Timber

fun PeyessQuery.toSqlQuery(selectClause: String): SimpleSQLiteQuery {
    var query = "$selectClause "

    var whereClause = ""
    queryFields.forEach { peyessQuery ->
        if (query.contains("WHERE")) {
            whereClause += " AND "
        } else {
            whereClause = " WHERE "
        }

        when (peyessQuery.op) {
            PeyessQueryOperation.Equal ->
                query += "$whereClause ${peyessQuery.field} = '${peyessQuery.value}'"

            PeyessQueryOperation.Different ->
                query += "$whereClause ${peyessQuery.field} != '${peyessQuery.value}'"

            PeyessQueryOperation.GreaterThan ->
                query += "$whereClause ${peyessQuery.field} > '${peyessQuery.value}'"

            PeyessQueryOperation.GreaterThanOrEqual ->
                query += "$whereClause ${peyessQuery.field} >= '${peyessQuery.value}'"

            PeyessQueryOperation.LessThan ->
                query += "$whereClause ${peyessQuery.field} < '${peyessQuery.value}'"

            PeyessQueryOperation.LessThanOrEqual ->
                query += "$whereClause ${peyessQuery.field} <= '${peyessQuery.value}'"

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

    return SimpleSQLiteQuery("$query $orderByClause")
}