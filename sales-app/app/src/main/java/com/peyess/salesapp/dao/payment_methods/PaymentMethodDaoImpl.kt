package com.peyess.salesapp.dao.payment_methods

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PaymentMethodDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): PaymentMethodDao {
    override fun payments(): Flow<List<PaymentMethod>> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val snaps = firestore
            .collection(
                salesApplication.stringResource(R.string.fs_col_payments)
                    .format(firebaseManager.currentStore!!.uid)
            )
            .whereEqualTo(salesApplication.stringResource(R.string.fs_field_payments_is_enabled), true)
            .orderBy(salesApplication.stringResource(R.string.fs_field_payments_priority))
            .get()
            .await()

        val payments = snaps.mapNotNull {
                it.toObject(FSPayment::class.java).toDocument(it.id)
            }

        emit(payments)
    }
}