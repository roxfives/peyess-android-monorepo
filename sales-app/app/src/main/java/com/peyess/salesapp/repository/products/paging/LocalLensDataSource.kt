//package com.peyess.salesapp.repository.products.paging
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import androidx.sqlite.db.SimpleSQLiteQuery
//import com.peyess.salesapp.dao.products.room.local_lens.LocalLensDao
//import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
//import com.peyess.salesapp.repository.products.LensFilter
//import com.peyess.salesapp.repository.products.ProductRepository
//import javax.inject.Inject
//
//class LocalLensDataSource @Inject constructor(
//    val localLensDao: LocalLensDao,
//) : PagingSource<Int, LocalLensEntity>() {
//    private fun buildQuery(lensFilter: LensFilter): SimpleSQLiteQuery {
//        val query = SimpleSQLiteQuery(
//            "SELECT * FROM ${LocalLensEntity.tableName} " +
//                    "ORDER BY priority DESC"
//        )
//
//        return query
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocalLensEntity> {
//
//
//        return try {
//            val nextPage = params.key ?: 1
//            val movieListResponse =
//
//            LoadResult.Page(
//                data = movieListResponse.results,
//                prevKey = if (nextPage == 1) null else nextPage - 1,
//                nextKey = movieListResponse.page.plus(1)
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, LocalLensEntity>): Int? {
//        TODO("Not yet implemented")
//    }
//
//}
//
//class LocalLensDataSource(private val movieApi: MovieApi, private val movieDB: MovieDB) :
//    MovieRemoteDataSource {
//    private val movieDao = movieDB.movieDao()
//
//    @OptIn(ExperimentalPagingApi::class)
//    override fun getPopularMovies(): Flow<PagingData<Movie>> {
//        val pagingSourceFactory = { movieDao.getAllMovies() }
//        return Pager(
//            config = PagingConfig(pageSize = 20),
//            remoteMediator = MovieRemoteMediator(
//                movieApi,
//                movieDB
//            ),
//            pagingSourceFactory = pagingSourceFactory,
//        ).flow
//    }
//}