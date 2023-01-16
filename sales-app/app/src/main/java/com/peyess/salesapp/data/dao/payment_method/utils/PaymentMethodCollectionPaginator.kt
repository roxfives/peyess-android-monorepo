package com.peyess.salesapp.data.dao.payment_method.utils

import com.google.firebase.firestore.Query
import com.peyess.salesapp.data.internal.firestore.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.model.payment_method.FSPaymentMethod

class PaymentMethodCollectionPaginator(
    query: Query,
    config: SimplePaginatorConfig
) : SimpleCollectionPaginator<FSPaymentMethod>(FSPaymentMethod::class, query, config)