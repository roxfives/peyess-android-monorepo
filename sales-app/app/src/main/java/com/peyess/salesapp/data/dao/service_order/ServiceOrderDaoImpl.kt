package com.peyess.salesapp.data.dao.service_order

import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.google.firebase.firestore.Query
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.service_order.errors.ServiceOrderDaoFetchError
import com.peyess.salesapp.data.dao.service_order.utils.ServiceOrderPagingSource
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ServiceOrderDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): ServiceOrderDao {
    override fun paginateServiceOrder(query: Query): ServiceOrderPagingSource {
        return ServiceOrderPagingSource(query)
    }

    override fun serviceOrders(): Flow<List<FSServiceOrder>> = callbackFlow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@callbackFlow
        }

        val storeId = firebaseManager.currentStore!!.uid
        val soPath = salesApplication
            .stringResource(id = R.string.fs_col_so)
            .format(storeId)

        // Filter by store access
        firestore.collection(soPath)
            .orderBy(
                salesApplication.stringResource(R.string.fs_field_so_created),
                Query.Direction.DESCENDING,
            )
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Timber.e(exception, "Failed while fetching clients")
                }

                val serviceOrders = snapshot?.mapNotNull {
                    try {
                        it.toObject(FSServiceOrder::class.java)
                    } catch (error: Throwable) {
                        Timber.e(error, "Failed to convert client document")
                        null
                    }
                }

                Timber.i("Sending service orders $serviceOrders")
                trySend(serviceOrders ?: emptyList())
            }

        awaitClose()

//        val firestore = firebaseManager.storeFirestore
//        if (firestore == null) {
//            return@flow
//        }
//
//        val storeId = firebaseManager.currentStore!!.uid
//
//        val soPath = salesApplication
//            .stringResource(id = R.string.fs_col_so)
//            .format(storeId)
//
//        val snaps = firestore
//            .collection(soPath)
//            .orderBy(
//                salesApplication.stringResource(R.string.fs_field_so_created),
//                Query.Direction.DESCENDING,
//            )
//            .get()
//            .addOnCompleteListener {
//                if (!it.isSuccessful) {
//                    Timber.e(it.exception)
//                }
//            }
//            .await()
//
//        val serviceOrders = snaps.mapNotNull {
//                it.toObject(FSServiceOrder::class.java)
//            }
//
//        emit(serviceOrders)
    }

    override suspend fun add(serviceOrder: FSServiceOrder) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) { return }

        val storeId = firebaseManager.currentStore!!.uid

        val id = serviceOrder.id
        val soPath = salesApplication
            .stringResource(id = R.string.fs_col_so)
            .format(storeId)

        firestore
            .collection(soPath)
            .document(id)
            .set(serviceOrder)
            .addOnCompleteListener {
                Timber.i("Completed with $it (${it.isSuccessful})")

                if (!it.isSuccessful) {
                    Timber.e(
                        it.exception,
                        "Error while uploading document with id ${id} at $soPath",
                    )
                }
            }
            .await()
    }

    override suspend fun serviceOrderById(
        serviceOrderId: String,
    ): ServiceOrderFetchResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            ServiceOrderDaoFetchError.Unexpected(
                description = "Firestore instance is null",
            )
        }

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            ServiceOrderDaoFetchError.Unexpected(
                description = "Store not authenticated",
            )
        }

        val soPath = salesApplication
            .stringResource(id = R.string.fs_col_so)
            .format(storeId)

        val response = firestore
            .collection(soPath)
            .document(serviceOrderId)
            .get()
            .await()
        ensure(response.exists()) {
            ServiceOrderDaoFetchError.ServiceOrderNotFound(
                description = "Service order with id $serviceOrderId not found",
            )
        }

        val fsServiceOrder = response.toObject(FSServiceOrder::class.java)
        ensureNotNull(fsServiceOrder) {
            ServiceOrderDaoFetchError.Unexpected(
                description = "Failed to convert service order document",
            )
        }

        fsServiceOrder
    }
}