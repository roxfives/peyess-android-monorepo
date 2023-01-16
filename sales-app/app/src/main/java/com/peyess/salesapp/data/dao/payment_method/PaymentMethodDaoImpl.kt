package com.peyess.salesapp.data.dao.payment_method

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.internal.firestore.FetchCollectionResponse
import com.peyess.salesapp.data.dao.payment_method.utils.PaymentMethodCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.internal.firestore.error.FirestoreError
import com.peyess.salesapp.data.internal.firestore.error.Unexpected
import com.peyess.salesapp.data.model.payment_method.FSPaymentMethod
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.adapter.toFirestoreCollectionQuery
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val defaultLimit = 100

class PaymentMethodDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): PaymentMethodDao {
    override suspend fun getById(id: String): Either<FirestoreError, FSPaymentMethod>  = either {
        val firestore = firebaseManager.storeFirestore
        val storeId = firebaseManager.currentStore?.uid

        ensureNotNull(firestore) {
            Unexpected("Firestore instance is null", null)
        }

        ensureNotNull(storeId) {
            Unexpected("Active store is null", null)
        }
        ensure(storeId.isNotBlank()) {
            Unexpected("Active store has an empty id", null)
        }

        val localLensPath = salesApplication
            .stringResource(R.string.fs_doc_payment_method)
            .format(storeId, id)
        val docRef = firestore.document(localLensPath)

        fetchDocument(FSPaymentMethod::class, docRef).bind()
    }

    override suspend fun simpleCollectionPaginator(
        query: PeyessQuery, config: SimplePaginatorConfig
    ): Either<Unexpected, PaymentMethodCollectionPaginator> = either {
        val firestore = firebaseManager.storeFirestore
        val storeId = firebaseManager.currentStore?.uid

        ensureNotNull(firestore) {
            Unexpected("Firestore instance is null", null)
        }

        ensureNotNull(storeId) {
            Unexpected("Active store is null", null)
        }
        ensure(storeId.isNotBlank()) {
            Unexpected("Active store has no id", null)
        }

        val path = salesApplication
            .stringResource(R.string.fs_col_payment_methods)
            .format(storeId)

        PaymentMethodCollectionPaginator(
            query = query.toFirestoreCollectionQuery(
                path = path,
                firestore = firestore,
            ),
            config = SimplePaginatorConfig(
                initialPageSize = config.initialPageSize,
                pageSize = config.pageSize,
            ),
        )
    }

    override suspend fun fetchCollection(
        query: PeyessQuery,
    ): FetchCollectionResponse<FSPaymentMethod> = either {
        val firestore = firebaseManager.storeFirestore
        val storeId = firebaseManager.currentStore?.uid

        ensureNotNull(firestore) {
            Unexpected("Firestore instance is null", null)
        }

        ensureNotNull(storeId) {
            Unexpected("Active store is null", null)
        }
        ensure(storeId.isNotBlank()) {
            Unexpected("Active store has no id", null)
        }

        val path = salesApplication
            .stringResource(R.string.fs_col_payment_methods)
            .format(storeId)
        val fsQuery = query
            .copy(withLimit = defaultLimit)
            .toFirestoreCollectionQuery(path, firestore)

        val querySnapshot = fsQuery.get().await()
        querySnapshot.map {
            Pair(it.id, it.toObject(FSPaymentMethod::class.java))
        }
    }
}