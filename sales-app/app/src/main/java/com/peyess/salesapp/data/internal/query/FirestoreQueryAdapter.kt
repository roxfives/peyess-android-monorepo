package com.peyess.salesapp.data.internal.query

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.types.toFirestore
import timber.log.Timber

fun PeyessQuery.toFirestoreCollectionQuery(path: String, firestore: FirebaseFirestore): Query {
    val collection = firestore.collection(path)
    var query = collection.orderBy(orderBy.field, orderBy.order.toFirestore())

    if (withLimit != null) {
        query = query.limit(withLimit.toLong())
    }

    queryFields.forEach { peyessQuery ->

        when (peyessQuery.op) {
            PeyessQueryOperation.Equal -> {
                query = query.whereEqualTo(peyessQuery.field, peyessQuery.value)
            }

            PeyessQueryOperation.GreaterThan ->
                query = query.whereGreaterThan(peyessQuery.field, peyessQuery.value)

            PeyessQueryOperation.GreaterThanOrEqual ->
                query = query
                    .whereGreaterThanOrEqualTo(peyessQuery.field, peyessQuery.value)

            PeyessQueryOperation.LessThan -> {
                query = query.whereLessThan(peyessQuery.field, peyessQuery.value)
            }
            PeyessQueryOperation.LessThanOrEqual -> {
                query = query.whereLessThanOrEqualTo(peyessQuery.field, peyessQuery.value)
            }

            PeyessQueryOperation.Different -> {
                Timber.w("Tried using unsupported firestore operation")
            }
            PeyessQueryOperation.Noop -> {
                Timber.i("Used a noop while building the query")
            }
        }
    }

    return query
}