package com.peyess.salesapp.dao.auth.store

import arrow.core.Either
import com.peyess.salesapp.dao.auth.store.error.OpticalStoreError
import com.peyess.salesapp.model.store.OpticalStore
import kotlinx.coroutines.flow.Flow

typealias OpticalStoreResponse = Either<OpticalStoreError, OpticalStore>

interface OpticalStoreDao {
    suspend fun loadCurrentStore(): OpticalStoreResponse

    fun store(storeId: String): Flow<OpticalStore>
}