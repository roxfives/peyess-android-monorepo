package com.peyess.salesapp.data.internal.firestore

import arrow.core.Either
import arrow.core.continuations.either
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.peyess.salesapp.data.internal.firestore.error.FirestoreError
import com.peyess.salesapp.data.internal.firestore.error.Unexpected
import com.peyess.salesapp.data.internal.firestore.error.toFirestoreError
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import kotlin.reflect.KClass

abstract class SimpleCollectionPaginator<out F: Any> constructor(
    private val type : KClass<out F>,
    private val query: Query,
    private val config: SimplePaginatorConfig,
) {
    private var hasDownloadedTheFirstPage: Boolean = false
    private var hasFinished: Boolean = false
    private var latestPage: DocumentSnapshot? = null

    private fun toDocuments(snaps: QuerySnapshot): Either<Unexpected, List<F>> = Either.catch {
        val snapshots = snaps.documents.mapNotNull { it.toObject(type.javaObjectType) }

        Timber.i("The snaps are $snapshots")
        snapshots
    }.mapLeft {
        Unexpected("Failed to parse firestore data into $type object", it)
    }

    private suspend fun queryFirstPage(): Either<FirestoreError, QuerySnapshot> =
        Either.catch {
            hasDownloadedTheFirstPage = true

            query.limit(config.initialPageSize.toLong())
                .get()
                .await()
        }.mapLeft {
            if (it is FirebaseFirestoreException) {
                it.toFirestoreError()
            } else {
                Unexpected(
                    description = "Unexpected error while fetching the " +
                            "first page for $type for query $query",
                    error = it,
                )
            }
        }

    private suspend fun queryNextPage(): Either<FirestoreError, QuerySnapshot> =
        Either.catch {
            if (latestPage == null) {
                throw IllegalStateException("latestPage has not been initialized")
            }

            query
                // This cast is necessary, otherwise the query sill always return empty
                .startAfter(latestPage as DocumentSnapshot)
                .limit(config.pageSize.toLong())
                .get()
                .await()
        }.mapLeft {
            if (it is FirebaseFirestoreException) {
                it.toFirestoreError()
            } else {
                Unexpected(
                    description = "Unexpected error while fetching the page " +
                            "starting from $latestPage for $type for query $query",
                    error = it,
                )
            }
        }

    private fun updatePagingStatus(snaps: QuerySnapshot) {
        hasFinished = snaps.isEmpty
        Timber.i("Updating status with: hasFinished = $hasFinished")

        if (!hasFinished) {
            latestPage = snaps.documents.last()
            Timber.i("Updating snapshot with: latestPage = $latestPage")
        }
    }

    fun resetPagination() {
        hasDownloadedTheFirstPage = false
        hasFinished = false
//        latestPage = null
    }

    suspend fun page(): Either<FirestoreError, List<F>> = either {
        val querySnapshot = if (hasDownloadedTheFirstPage && latestPage != null) {
            queryNextPage().bind()
        } else {
            queryFirstPage().bind()
        }

        updatePagingStatus(querySnapshot)

        toDocuments(querySnapshot).bind()
    }
}