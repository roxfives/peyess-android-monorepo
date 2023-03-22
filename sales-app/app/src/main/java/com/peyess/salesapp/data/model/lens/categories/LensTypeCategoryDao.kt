package com.peyess.salesapp.data.model.lens.categories

import arrow.core.Either
import com.peyess.salesapp.data.model.lens.categories.error.LensCategoryDaoReadError
import kotlinx.coroutines.flow.Flow

typealias LensTypeCategoriesResponse =
        Either<LensCategoryDaoReadError, List<LensTypeCategoryDocument>>

typealias LensTypeCategoryResponse = Either<LensCategoryDaoReadError, LensTypeCategoryDocument>

interface LensTypeCategoryDao {
    fun streamCategories(): Flow<List<LensTypeCategoryDocument>>
    suspend fun typeCategories(): LensTypeCategoriesResponse
    suspend fun typeCategoryById(id: String): LensTypeCategoryResponse
}