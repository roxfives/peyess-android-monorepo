package com.peyess.salesapp.data.model.lens.description

import kotlinx.coroutines.flow.Flow

interface LensDescriptionDao {
    fun descriptions(): Flow<List<LensDescriptionDocument>>
    fun descriptionsFor(familyId: String): Flow<List<LensDescriptionDocument>>
}