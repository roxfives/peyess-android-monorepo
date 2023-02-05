package com.peyess.salesapp.data.dao.client.utils

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig
import com.peyess.salesapp.data.model.client.FSClient

class ClientsCollectionPaginator(
    query: Query,
    config: SimplePaginatorConfig
): SimpleCollectionPaginator<FSClient>(FSClient::class, query, config)