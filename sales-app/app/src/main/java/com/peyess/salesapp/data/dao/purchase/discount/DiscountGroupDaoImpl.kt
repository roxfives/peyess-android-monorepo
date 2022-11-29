package com.peyess.salesapp.data.dao.purchase.discount

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.sale.purchase.discount.group.FSDiscountGroup
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DiscountGroupDaoImpl @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val salesApplication: SalesApplication,
) : DiscountGroupDao {
    override suspend fun getById(id: String): FSDiscountGroup {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("cound not find firestore instance: $firestore")
        }

        val storeId = firebaseManager.currentStore?.uid
        val discountGroupCollectionPath = salesApplication
            .stringResource(R.string.fs_col_discount_groups)
            .format(storeId)

        val document: FSDiscountGroup?
        try {
            val snap = firestore.collection(discountGroupCollectionPath)
                .document(id)
                .get()
                .await()

            document = snap.toObject(FSDiscountGroup::class.java)
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client")
            error(err)
        }

        return document ?: FSDiscountGroup()
    }
}