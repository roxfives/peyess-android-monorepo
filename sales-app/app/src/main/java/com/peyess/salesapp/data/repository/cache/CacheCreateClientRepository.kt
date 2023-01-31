package com.peyess.salesapp.data.repository.cache

import arrow.core.Either
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientReadError
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientWriteError

typealias CacheCreateClientCreateResponse =
        Either<CacheCreateClientWriteError,CacheCreateClientDocument>
typealias CacheCreateClientUpdateResponse = Either<CacheCreateClientWriteError, Unit>
typealias CacheCreateClientDeleteResponse = Either<CacheCreateClientWriteError, Unit>
typealias CacheCreateClientFetchSingleResponse =
        Either<CacheCreateClientReadError, CacheCreateClientDocument>

interface CacheCreateClientRepository {
    suspend fun createClient(): CacheCreateClientCreateResponse
    suspend fun update(client: CacheCreateClientDocument): CacheCreateClientUpdateResponse
    suspend fun deleteById(id: String): CacheCreateClientDeleteResponse
    suspend fun getById(id: String): CacheCreateClientFetchSingleResponse
    suspend fun findCreating(): CacheCreateClientFetchSingleResponse
}