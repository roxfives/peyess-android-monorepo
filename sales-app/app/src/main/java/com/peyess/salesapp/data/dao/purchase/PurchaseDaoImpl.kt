package com.peyess.salesapp.data.dao.purchase

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.purchase.toMappingUpdate
import com.peyess.salesapp.data.dao.purchase.error.UpdatePurchaseDaoError
import com.peyess.salesapp.data.dao.purchase.utils.PurchasePagingSource
import com.peyess.salesapp.data.model.sale.purchase.FSPurchase
import com.peyess.salesapp.data.model.sale.purchase.FSPurchaseUpdate
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.typing.sale.PurchaseState
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class PurchaseDaoImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
) : PurchaseDao {
    override suspend fun add(document: FSPurchase) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        val storeId = firebaseManager.currentStore?.uid
        val purchaseCollectionPath = salesApplication
            .stringResource(R.string.fs_col_purchase)
            .format(storeId)

        try {
            firestore.collection(purchaseCollectionPath)
                .document(document.id)
                .set(document)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding document $storeId at $purchaseCollectionPath")
            error(err)
        }
    }

    override suspend fun getById(id: String): FSPurchase? {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        val storeId = firebaseManager.currentStore?.uid
        val purchasePath = salesApplication
            .stringResource(R.string.fs_doc_purchase)
            .format(storeId, id)

        val purchaseDocRef = firestore.document(purchasePath)
        val purchaseDoc = purchaseDocRef.get().await()
        val fsPurchase = purchaseDoc.toObject(FSPurchase::class.java)

        return fsPurchase
    }

    override fun paginatePurchases(query: Query): PurchasePagingSource {
        return PurchasePagingSource(query)
    }

    override suspend fun updatePurchase(
        purchaseId: String,
        purchaseUpdate: FSPurchaseUpdate,
    ): UpdatePurchaseResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            UpdatePurchaseDaoError.Unexpected(
                description = "Firestore instance is uninitialized",
            )
        }

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            UpdatePurchaseDaoError.Unexpected(
                description = "Store user is not authenticated",
            )
        }

        val purchasePath = salesApplication
            .stringResource(R.string.fs_doc_purchase)
            .format(storeId, purchaseId)

        Either.catch {
            firestore.document(purchasePath)
                .update(purchaseUpdate.toMappingUpdate())
                .await()
        }.mapLeft {
            UpdatePurchaseDaoError.Unexpected(
                description = it.message ?: "Unknown error",
            )
        }
    }

    override suspend fun updatePurchaseState(
        purchaseId: String,
        state: PurchaseState,
        updatedBy: String
    ): UpdatePurchaseResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            UpdatePurchaseDaoError.Unexpected(
                description = "Firestore instance is uninitialized",
            )
        }

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            UpdatePurchaseDaoError.Unexpected(
                description = "Store user is not authenticated",
            )
        }

        val purchasePath = salesApplication
            .stringResource(R.string.fs_doc_purchase)
            .format(storeId, purchaseId)

        val update = mapOf(
            "state" to state.toName(),
            "updated" to Timestamp.now(),
            "updated_by" to updatedBy,
            "update_allowed_by" to updatedBy,
        )
        Either.catch {
            firestore.document(purchasePath)
                .update(update)
                .await()
        }.mapLeft {
            UpdatePurchaseDaoError.Unexpected(
                description = it.message ?: "Unknown error",
            )
        }
    }
}
