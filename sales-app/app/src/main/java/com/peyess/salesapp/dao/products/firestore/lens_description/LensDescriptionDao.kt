package com.peyess.salesapp.dao.products.firestore.lens_description

import kotlinx.coroutines.flow.Flow

interface LensDescriptionDao {
    fun descriptions(): Flow<List<LensDescription>>
    fun descriptionsFor(familyId: String): Flow<List<LensDescription>>
}