package com.peyess.salesapp.dao.auth.users

import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.model.users.FSCollaborator
import kotlinx.coroutines.flow.Flow

interface CollaboratorsDao {
    fun subscribeToActiveAccounts(): Flow<List<CollaboratorDocument>>

    fun user(uid: String): Flow<CollaboratorDocument>

    suspend fun getById(uid: String): FSCollaborator?
}