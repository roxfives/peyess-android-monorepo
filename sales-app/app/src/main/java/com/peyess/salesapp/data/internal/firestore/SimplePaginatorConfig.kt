package com.peyess.salesapp.data.internal.firestore

data class SimplePaginatorConfig(
    val initialPageSize: Int = 20,
    val pageSize: Int = 10,
)