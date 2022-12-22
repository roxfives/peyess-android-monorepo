package com.peyess.salesapp.data.dao.lenses

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.internal.firestore.error.FirestoreError
import com.peyess.salesapp.data.internal.firestore.error.Unexpected
import com.peyess.salesapp.data.internal.query.toFirestoreCollectionQuery
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.firebase.FirebaseManager
import javax.inject.Inject

class StoreLensesDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): StoreLensesDao {

    override suspend fun simpleCollectionPaginator(
        query: PeyessQuery,
        config: SimplePaginatorConfig,
    ): Either<Unexpected, StoreLensCollectionPaginator> =
        either {
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
                .stringResource(R.string.fs_col_lenses)
                .format(storeId)

            StoreLensCollectionPaginator(
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

    override suspend fun getById(id: String): Either<FirestoreError, FSStoreLocalLens> =
        either {
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
                .stringResource(R.string.fs_doc_client)
                .format(storeId, id)
            val docRef = firestore.document(localLensPath)

            fetchDocument(FSStoreLocalLens::class, docRef).bind()
        }
}