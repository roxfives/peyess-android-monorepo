package com.peyess.salesapp.data.internal.firestore.simple_paginator

data class SimplePaginatorConfig(
    val initialPageSize: Int = 20,
    val pageSize: Int = 10,
)