package com.peyess.salesapp.utils.room

// Solution based on:
// https://stackoverflow.com/questions/73397270/clean-architecture-cannot-map-pagingsourceint-entities-to-pagingsourcerepos

import androidx.paging.PagingSource
import androidx.paging.PagingState

class MappingPagingSource<Key: Any, Value: Any, MappedValue: Any>(
    private val originalSource: PagingSource<Key, Value>,
    private val buildNewSource: () -> PagingSource<Key, Value>,
    private val mapper: (Value) -> MappedValue,
): PagingSource<Key, MappedValue>() {
    private var source: PagingSource<Key, Value>

    override val jumpingSupported: Boolean
        get() = originalSource.jumpingSupported

    init {
        source = originalSource
        source.registerInvalidatedCallback { this.invalidate() }
    }

    override fun getRefreshKey(state: PagingState<Key, MappedValue>): Key? {
        if (source.invalid) {
            source = buildNewSource()
            source.registerInvalidatedCallback { this.invalidate() }
        }

        return source.getRefreshKey(
            PagingState(
                pages = emptyList(),
                leadingPlaceholderCount = 0,
                anchorPosition = state.anchorPosition,
                config = state.config,
            )
        )
    }

    override suspend fun load(params: LoadParams<Key>): LoadResult<Key, MappedValue> {
//        val originalResult = originalSource.load(params)
//        val result = LoadResult.Page(
//            data = data,
//            prevKey = args.prevKey,
//            nextKey = args.nextKey,
//            itemsBefore = args.itemsBefore,
//            itemsAfter = args.itemsAfter
//        )
//        return result
        if (source.invalid) {
            source = buildNewSource()
            source.registerInvalidatedCallback { this.invalidate() }
        }

        return when (val originalResult = source.load(params)) {
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