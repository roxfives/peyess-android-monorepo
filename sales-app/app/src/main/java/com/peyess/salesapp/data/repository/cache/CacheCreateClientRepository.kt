package com.peyess.salesapp.data.repository.cache

import arrow.core.Either
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientReadError
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientWriteError

typealias CacheClientCreateResponse = Either<CacheCreateClientWriteError, String>
typealias CacheClientUpdateResponse = Either<CacheCreateClientWriteError, Unit>
typealias CacheClientDeleteResponse = Either<CacheCreateClientWriteError, Unit>
typealias CacheClientFetchSingleResponse =
        Either<CacheCreateClientReadError, CacheCreateClientDocument>

interface CacheCreateClientRepository {
    suspend fun createClient(): CacheClientCreateResponse
    suspend fun update(client: CacheCreateClientDocument): CacheClientUpdateResponse
    suspend fun deleteById(id: String): CacheClientDeleteResponse
    suspend fun getById(id: String): CacheClientFetchSingleResponse
    suspend fun findCreating(): CacheClientFetchSingleResponse
}