package com.peyess.salesapp.data.internal.firestore.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.reflect.KClass

typealias PagingSourceResponse<F> = Pair<String, F>

abstract class FirestorePagingSource<F: Any> constructor(
    private val query: Query,
    private val type : KClass<out F>,
): PagingSource<DocumentSnapshot, PagingSourceResponse<F>>() {
    override fun getRefreshKey(
        state: PagingState<DocumentSnapshot, PagingSourceResponse<F>>,
    ): DocumentSnapshot? {
        return null
    }

    override suspend fun load(
        params: LoadParams<DocumentSnapshot>,
    ): LoadResult<DocumentSnapshot, PagingSourceResponse<F>> {


        val nextPageKey = params.key
        val pagingQuery = if (nextPageKey == null) {
            initialQueryPage(query, params.loadSize.toLong())
        } else {
            subsequentQueryPage(query, params.loadSize.toLong(), nextPageKey)
        }

        return suspendCancellableCoroutine { continuation ->
            pagingQuery.addSnapshotListener { snapshot, err ->
                if (err != null) {
                    if (continuation.isActive) {
                        continuation.resume(
                            LoadResult.Error(err)
                        )
                    } else {
                        invalidate()
                    }
                } else if (snapshot != null && !snapshot.isEmpty) {
                    val data = pageAdapter(snapshot)
                    val nextKey = findNextPageKey(snapshot)

                    if (continuation.isActive) {
                        continuation.resume(
                            LoadResult.Page(
                                data = data,
                                prevKey = null,
                                nextKey = nextKey,
                            )
                        )
                    } else {
                        invalidate()
                    }
                } else {
                    if (continuation.isActive) {
                        continuation.resume(
                            LoadResult.Page(
                                data = emptyList(),
                                prevKey = null,
                                nextKey = null,
                            )
                        )
                    } else {
                        invalidate()
                    }
                }
            }
        }
    }

    private fun initialQueryPage(query: Query, loadSize: Long): Query {
        return query.limit(loadSize)
    }

    private fun subsequentQueryPage(query: Query, loadSize: Long, last: DocumentSnapshot): Query {
        return query.startAfter(last).limit(loadSize)
    }

    private fun findNextPageKey(querySnapshot: QuerySnapshot): DocumentSnapshot {
        return querySnapshot.documents.last()
    }

    private fun pageAdapter(snapshot: QuerySnapshot): List<PagingSourceResponse<F>> {
        var obj: F?

        return snapshot.documents.mapNotNull {
            obj = it.toObject(type.javaObjectType)

            if (obj != null) {
                Pair(it.id, obj!!)
            } else {
                null
            }
        }
    }
}
