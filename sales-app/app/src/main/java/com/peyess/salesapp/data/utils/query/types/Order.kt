package com.peyess.salesapp.data.utils.query.types

import com.google.firebase.firestore.Query

sealed class Order {
    object ASCENDING: Order()
    object DESCENDING: Order()
}

fun Order.toFirestore(): Query.Direction {
    return when (this) {
        Order.ASCENDING -> Query.Direction.ASCENDING
        Order.DESCENDING -> Query.Direction.DESCENDING
    }
}
