package com.peyess.salesapp.dao.store

import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.flow.Flow

interface OpticalStoreDao {
    fun store(storeId: String): Flow<OpticalStore>
}