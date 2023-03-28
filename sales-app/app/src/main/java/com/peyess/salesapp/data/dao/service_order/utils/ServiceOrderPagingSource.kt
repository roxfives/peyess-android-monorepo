package com.peyess.salesapp.data.dao.service_order.utils

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.internal.firestore.paging_source.FirestorePagingSource
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder

class ServiceOrderPagingSource constructor(
    query: Query,
): FirestorePagingSource<FSServiceOrder>(query, FSServiceOrder::class)
