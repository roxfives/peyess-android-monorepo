package com.peyess.salesapp.dao.auth.store

import com.peyess.salesapp.model.store.OpticalStore
import kotlinx.coroutines.flow.Flow

interface OpticalStoreDao {
    fun store(storeId: String): Flow<OpticalStore>
}