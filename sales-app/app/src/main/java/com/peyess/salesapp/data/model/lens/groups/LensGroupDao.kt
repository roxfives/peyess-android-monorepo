package com.peyess.salesapp.data.model.lens.groups

import kotlinx.coroutines.flow.Flow

interface LensGroupDao {
    fun groups(): Flow<List<LensGroupDocument>>
}