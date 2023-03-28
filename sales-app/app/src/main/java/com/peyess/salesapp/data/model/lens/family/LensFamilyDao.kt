package com.peyess.salesapp.data.model.lens.family

import kotlinx.coroutines.flow.Flow

interface LensFamilyDao {
    fun families(): Flow<List<LensFamilyDocument>>
}