package com.peyess.salesapp.data.internal.firestore.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

typealias PagingSourceResponse<F> = Pair<String, F>

abstract class FirestorePagingSource<F: Any> constructor(
    private val query: Query,
    private val type : KClass<out F>,
): PagingSource<QuerySnapshot, PagingSourceResponse<F>>() {
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

    private suspend fun findNextPage(currentPage: QuerySnapshot): QuerySnapshot {
        val index = (currentPage.size() - 1).coerceAtLeast(0)
        val lastVisibleProduct = currentPage.documents[index]

        return query.startAfter(lastVisibleProduct)
            .get()
            .await()
    }

    override fun getRefreshKey(
        state: PagingState<QuerySnapshot, PagingSourceResponse<F>>,
    ): QuerySnapshot? {
        return null
    }

    override suspend fun load(
        params: LoadParams<QuerySnapshot>,
    ): LoadResult<QuerySnapshot, PagingSourceResponse<F>> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val nextPage = findNextPage(currentPage)
            val data = pageAdapter(currentPage)

            LoadResult.Page(
                data = data,
                prevKey = null,
                nextKey = nextPage
            )
        } catch (throwable: Throwable) {
            LoadResult.Error(throwable)
        }
    }
}
