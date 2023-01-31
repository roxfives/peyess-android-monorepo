package com.peyess.salesapp.data.dao.client.utils

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.internal.firestore.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.model.client.FSClient

class ClientsCollectionPaginator(
    query: Query,
    config: SimplePaginatorConfig
): SimpleCollectionPaginator<FSClient>(FSClient::class, query, config)