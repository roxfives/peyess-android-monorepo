package com.peyess.salesapp.data.repository.cache

import arrow.core.Either
import arrow.core.leftIfNull
import com.peyess.salesapp.data.adapter.cache.toCacheCreateClientDocument
import com.peyess.salesapp.data.adapter.cache.toCacheCreateClientEntity
import com.peyess.salesapp.data.dao.cache.CacheCreateClientDao
import com.peyess.salesapp.data.model.cache.CacheCreateClientDocument
import com.peyess.salesapp.data.model.cache.CacheCreateClientEntity
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientCreateError
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientDeleteError
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientNotFoundError
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientUnexpected
import com.peyess.salesapp.data.repository.cache.error.CacheCreateClientUpdateError
import com.peyess.salesapp.firebase.FirebaseManager
import javax.inject.Inject

class CacheCreateClientRepositoryImpl @Inject constructor(
    private val firebaseManager: FirebaseManager,
    private val cacheCreateClientDao: CacheCreateClientDao,
): CacheCreateClientRepository {
    override suspend fun createClient(): CacheCreateClientCreateResponse = Either.catch {
        val uniqueId = firebaseManager.uniqueId()
        val entity = CacheCreateClientEntity(id = uniqueId, isCreating = true)

        cacheCreateClientDao.add(entity)
        entity.toCacheCreateClientDocument()
    }.mapLeft {
        CacheCreateClientCreateError(
            description = "Error creating client",
            error = it
        )
    }

    override suspend fun insertClient(
        client: CacheCreateClientDocument,
    ): CacheCreateClientInsertResponse = Either.catch {
        cacheCreateClientDao.add(client.toCacheCreateClientEntity())
    }.mapLeft {
        CacheCreateClientCreateError(
            description = "Error inserting client",
            error = it
        )
    }

    override suspend fun update(
        client: CacheCreateClientDocument,
    ): CacheCreateClientUpdateResponse = Either.catch {
        cacheCreateClientDao.update(client.toCacheCreateClientEntity())
    }.mapLeft {
        CacheCreateClientUpdateError(
            description = "Error updating client",
            error = it
        )
    }

    override suspend fun deleteById(
        id: String,
    ): CacheCreateClientDeleteResponse = Either.catch {
        cacheCreateClientDao.deleteById(id)
    }.mapLeft {
        CacheCreateClientDeleteError(
            description = "Error deleting client",
            error = it
        )
    }

    override suspend fun getById(
        id: String,
    ): CacheCreateClientFetchSingleResponse = Either.catch {
        cacheCreateClientDao.getById(id)?.toCacheCreateClientDocument()
    }.mapLeft {
        CacheCreateClientUnexpected(
            description = "Error fetching client",
            error = it
        )
    }.leftIfNull {
        CacheCreateClientNotFoundError(
            description = "Client with id $id not found",
        )
    }

    override suspend fun findCreating(): CacheCreateClientFetchSingleResponse = Either.catch {
        cacheCreateClientDao.findCreating()?.toCacheCreateClientDocument()
    }.mapLeft {
        CacheCreateClientUnexpected(
            description = "Error fetching client",
            error = it
        )
    }.leftIfNull {
        CacheCreateClientNotFoundError(
            description = "No client found as creating",
        )
    }
}
