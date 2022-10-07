package com.peyess.salesapp.dao.service_order

import com.google.firebase.firestore.Query
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ServiceOrderDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): ServiceOrderDao {
    override fun serviceOrders(): Flow<List<FSServiceOrder>> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val storeId = firebaseManager.currentStore!!.uid

        val soPath = salesApplication
            .stringResource(id = R.string.fs_col_so)
            .format(storeId)

        val snaps = firestore
            .collection(soPath)
            .orderBy(
                salesApplication.stringResource(R.string.fs_field_so_created),
                Query.Direction.DESCENDING,
            )
            .get()
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Timber.e(it.exception)
                }
            }
            .await()

        val serviceOrders = snaps.mapNotNull {
                it.toObject(FSServiceOrder::class.java)
            }

        emit(serviceOrders)
    }

    override suspend fun add(serviceOrder: FSServiceOrder) {
        TODO("Not yet implemented")
    }
}