package com.peyess.salesapp.data.dao.lenses

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.internal.firestore.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig

class StoreLensCollectionPaginator(
    query: Query,
    config: SimplePaginatorConfig
) : SimpleCollectionPaginator<FSStoreLocalLens>(FSStoreLocalLens::class, query, config)