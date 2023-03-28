package com.peyess.salesapp.data.dao.payment_method

import com.peyess.salesapp.data.dao.internal.firestore.ReadOnlyFirestoreDao
import com.peyess.salesapp.data.model.payment_method.FSPaymentMethod

interface PaymentMethodDao: ReadOnlyFirestoreDao<FSPaymentMethod>
