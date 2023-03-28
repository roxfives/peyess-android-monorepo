package com.peyess.salesapp.data.utils.query.adapter

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.utils.query.types.Order

fun Order.toFirestore(): Query.Direction {
    return when (this) {
        Order.ASCENDING -> Query.Direction.ASCENDING
        Order.DESCENDING -> Query.Direction.DESCENDING
    }
}