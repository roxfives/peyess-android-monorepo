package com.peyess.salesapp.data.dao.lenses

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.google.firebase.firestore.AggregateSource
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.internal.firestore.FetchCollectionResponse
import com.peyess.salesapp.data.dao.lenses.error.TotalLensesEnabledError
import com.peyess.salesapp.data.dao.lenses.utils.StoreLensCollectionPaginator
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.FirestoreError
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.Unexpected
import com.peyess.salesapp.data.utils.query.adapter.toFirestoreCollectionQuery
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val defaultLimit = 100

class StoreLensesDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): StoreLensesDao {
    override suspend fun totalLensesEnabled(): TotalLensesEnabledResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            TotalLensesEnabledError.DatabaseUnavailable()
        }

        val storeId = firebaseManager.currentStore?.uid ?: ""
        ensure(storeId.isNotBlank()) {
            TotalLensesEnabledError.StoreNotFound()
        }

        Either.catch {
            val lensesCollectionPath = salesApplication
                .stringResource(R.string.fs_col_lenses)
                .format(storeId)

            val snap = firestore
                .collection(lensesCollectionPath)
                .whereEqualTo(
                    salesApplication.stringResource(R.string.fs_field_lens_enabled),
                    true,
                ).count()
                .get(AggregateSource.SERVER)
                .await()

            snap.count.toInt()
        }.mapLeft {
            TotalLensesEnabledError.Unexpected(throwable = it)
        }.bind()
    }

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

    override suspend fun fetchCollection(
        query: PeyessQuery,
    ): FetchCollectionResponse<FSStoreLocalLens> = either {
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
        val fsQuery = query
            .copy(withLimit = defaultLimit)
            .toFirestoreCollectionQuery(path, firestore)

        val querySnapshot = fsQuery.get().await()
        querySnapshot.map {
            Pair(it.id, it.toObject(FSStoreLocalLens::class.java))
        }
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
                .stringResource(R.string.fs_doc_store_lens)
                .format(storeId, id)
            val docRef = firestore.document(localLensPath)

            fetchDocument(FSStoreLocalLens::class, docRef).bind()
        }
}