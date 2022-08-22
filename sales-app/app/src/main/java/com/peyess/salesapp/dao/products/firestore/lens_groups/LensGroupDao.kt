package com.peyess.salesapp.dao.products.firestore.lens_groups

import com.peyess.salesapp.model.products.LensGroup
import com.peyess.salesapp.model.products.LensTypeCategory
import kotlinx.coroutines.flow.Flow

interface LensGroupDao {
    fun groups(): Flow<List<LensGroup>>
}