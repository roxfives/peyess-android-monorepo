package com.peyess.salesapp.data.model.lens.categories

import kotlinx.coroutines.flow.Flow

interface LensTypeCategoryDao {
    fun categories(): Flow<List<LensTypeCategoryDocument>>
}