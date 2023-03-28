package com.peyess.salesapp.data.dao.lenses.utils

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig

class StoreLensCollectionPaginator(
    query: Query,
    config: SimplePaginatorConfig
) : SimpleCollectionPaginator<FSStoreLocalLens>(FSStoreLocalLens::class, query, config)