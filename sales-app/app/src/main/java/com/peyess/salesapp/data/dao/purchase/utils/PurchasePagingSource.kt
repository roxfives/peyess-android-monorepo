package com.peyess.salesapp.data.dao.purchase.utils

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.internal.firestore.paging_source.FirestorePagingSource
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase

class PurchasePagingSource constructor(
    query: Query,
): FirestorePagingSource<FSPurchase>(query, FSPurchase::class)
