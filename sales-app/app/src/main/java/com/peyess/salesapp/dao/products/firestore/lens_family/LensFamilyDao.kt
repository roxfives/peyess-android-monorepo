package com.peyess.salesapp.dao.products.firestore.lens_family

import kotlinx.coroutines.flow.Flow

interface LensFamilyDao {
    fun families(): Flow<List<LensFamily>>
}