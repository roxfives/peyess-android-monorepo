package com.peyess.salesapp.workmanager.clients.utils

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.model.local_client.LocalClientStatusDocument
import com.peyess.salesapp.data.utils.query.PeyessGroupBy
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryField
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import java.time.ZonedDateTime

private const val defaultThreshold = 1L

private fun buildQueryFields(
    salesApplication: SalesApplication,
    storeId: String,
    after: ZonedDateTime,
): List<PeyessQueryField> {
    return listOf(
        buildQueryField(
            field = salesApplication.getString(R.string.fs_field_clients_store_ids),
            op = PeyessQueryOperation.ArrayContains,
            value = storeId,
        ),

        buildQueryField(
            field = salesApplication.getString(R.string.fs_field_clients_updated),
            op = PeyessQueryOperation.GreaterThan,
            value = after,
        )
    )
}

private fun buildOrderBy(salesApplication: SalesApplication): List<PeyessOrderBy> {
    return listOf(
        PeyessOrderBy(
            field = salesApplication.stringResource(R.string.fs_field_clients_updated),
            order = Order.ASCENDING,
        )
    )
}

fun buildQueryForClients(
    salesApplication: SalesApplication,
    storeId: String,
    after: ZonedDateTime,
): Pair<PeyessQuery, SimplePaginatorConfig> {

    val queryFields = buildQueryFields(salesApplication, storeId, after)
    val orderBy = buildOrderBy(salesApplication)

    val query = PeyessQuery(
        queryFields = queryFields,
        orderBy = orderBy,
        groupBy = emptyList(),
    )

    val queryConfig = SimplePaginatorConfig(
        initialPageSize = 100,
        pageSize = 100,
    )

    return Pair(query, queryConfig)
}