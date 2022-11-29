package com.peyess.salesapp.data.repository.collaborator

import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.model.users.toDocument
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollaboratorsRepositoryImpl @Inject constructor(
    private val collaboratorsDao: CollaboratorsDao,
): CollaboratorsRepository {
    override fun subscribeToActiveAccounts(): Flow<List<CollaboratorDocument>> {
        return collaboratorsDao.subscribeToActiveAccounts()
    }

    override fun user(uid: String): Flow<CollaboratorDocument> {
        return collaboratorsDao.user(uid)
    }

    override suspend fun getById(uid: String): CollaboratorDocument? {
        val document = collaboratorsDao
            .getById(uid)
            ?.toDocument()

        return document
    }
}