package com.peyess.salesapp.dao.products.firestore.lens_categories

import com.peyess.salesapp.model.products.LensTypeCategory
import kotlinx.coroutines.flow.Flow

interface LensTypeCategoryDao {
    fun categories(): Flow<List<LensTypeCategory>>
}