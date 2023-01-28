package com.peyess.salesapp.data.repository.collaborator

import android.net.Uri
import com.peyess.salesapp.dao.auth.users.CollaboratorsDao
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.model.users.toDocument
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CollaboratorsRepositoryImpl @Inject constructor(
    private val collaboratorsDao: CollaboratorsDao,
): CollaboratorsRepository {
    override fun subscribeToActiveAccounts(): Flow<List<CollaboratorDocument>> {
        return collaboratorsDao
            .streamActiveAccounts()
            .map {
                it.map { collaborator ->
                    collaborator.toDocument()
                }
            }
    }

    override fun user(uid: String): Flow<CollaboratorDocument> {
        return collaboratorsDao.user(uid).map {
            it.toDocument()
        }
    }

    override suspend fun pictureFor(uid: String): Uri {
        return collaboratorsDao.pictureFor(uid)
    }

    override suspend fun getById(uid: String): CollaboratorDocument? {
        return collaboratorsDao.getById(uid)?.toDocument()
    }
}
