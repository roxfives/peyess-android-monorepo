package com.peyess.salesapp.data.utils.query.adapter

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryArithmeticExpressionField
import com.peyess.salesapp.data.utils.query.PeyessQueryConstantField
import com.peyess.salesapp.data.utils.query.PeyessQueryMinMaxField
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.PeyessQueryPredicateExpressionField
import com.peyess.salesapp.data.utils.query.PeyessQueryRegularField
import timber.log.Timber

fun PeyessQuery.toFirestoreCollectionQuery(path: String, firestore: FirebaseFirestore): Query {
    val collection = firestore.collection(path)
    var query = if (orderBy.isEmpty()) {
        collection
    } else {
        var orderedQuery = collection as Query

        orderBy.forEach {
            orderedQuery = orderedQuery.orderBy(it.field, it.order.toFirestore())
        }

        orderedQuery
    }

    if (withLimit != null) {
        query = query.limit(withLimit.toLong())
    }

    queryFields.forEach {
        when (it) {
            is PeyessQueryRegularField -> {
                query = query.buildQueryForRegularField(it)
            }

            is PeyessQueryConstantField,
            is PeyessQueryMinMaxField,
            is PeyessQueryArithmeticExpressionField,
            is PeyessQueryPredicateExpressionField -> {
                Timber.e("Unsupported field type for Firestore query")
            }
        }
    }

    return query
}

private fun Query.buildQueryForRegularField(
    peyessQuery: PeyessQueryRegularField,
): Query {
    return when (peyessQuery.op) {
        PeyessQueryOperation.Equal -> {
            this.whereEqualTo(peyessQuery.field, peyessQuery.value)
        }

        PeyessQueryOperation.GreaterThan ->
            this.whereGreaterThan(peyessQuery.field, peyessQuery.value)

        PeyessQueryOperation.GreaterThanOrEqual ->
            this.whereGreaterThanOrEqualTo(peyessQuery.field, peyessQuery.value)

        PeyessQueryOperation.LessThan ->
            this.whereLessThan(peyessQuery.field, peyessQuery.value)

        PeyessQueryOperation.LessThanOrEqual ->
            this.whereLessThanOrEqualTo(peyessQuery.field, peyessQuery.value)

        PeyessQueryOperation.Different -> {
            Timber.w("Tried using unsupported firestore operation")
            this
        }
        PeyessQueryOperation.Noop -> {
            Timber.i("Used a noop while building the query")
            this
        }
    }
}
