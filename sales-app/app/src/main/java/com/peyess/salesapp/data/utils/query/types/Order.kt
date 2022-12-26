package com.peyess.salesapp.data.utils.query.types

sealed class Order {
    object ASCENDING: Order()
    object DESCENDING: Order()
}
