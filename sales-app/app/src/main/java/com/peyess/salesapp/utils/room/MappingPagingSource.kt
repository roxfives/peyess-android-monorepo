package com.peyess.salesapp.utils.room

// Solution based on:
// https://stackoverflow.com/questions/73397270/clean-architecture-cannot-map-pagingsourceint-entities-to-pagingsourcerepos

import androidx.paging.PagingSource
import androidx.paging.PagingState

class MappingPagingSource<Key: Any, Value: Any, MappedValue: Any>(
    private val originalSource: PagingSource<Key, Value>,
    private val mapper: (Value) -> MappedValue,
): PagingSource<Key, MappedValue>() {
    override val jumpingSupported: Boolean
        get() = originalSource.jumpingSupported

    init {
        originalSource.registerInvalidatedCallback { this.invalidate() }
    }

    override fun getRefreshKey(state: PagingState<Key, MappedValue>): Key? {
        return originalSource.getRefreshKey(
            PagingState(
                pages = emptyList(),
                leadingPlaceholderCount = 0,
                anchorPosition = state.anchorPosition,
                config = state.config,
            )
        )
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, MappedValue> {
        return when (val originalResult = originalSource.load(params)) {
            is LoadResult.Error ->
                LoadResult.Error(originalResult.throwable)

            is LoadResult.Invalid ->
                LoadResult.Invalid()

            is LoadResult.Page ->
                LoadResult.Page(
                    data = originalResult.data.map(mapper),
                    prevKey = originalResult.prevKey,
                    nextKey = originalResult.nextKey,
                )
        }
    }
}