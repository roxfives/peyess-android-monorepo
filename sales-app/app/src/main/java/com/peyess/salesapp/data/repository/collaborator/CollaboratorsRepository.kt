package com.peyess.salesapp.data.repository.collaborator

import com.peyess.salesapp.model.users.CollaboratorDocument
import kotlinx.coroutines.flow.Flow

interface CollaboratorsRepository {
    fun subscribeToActiveAccounts(): Flow<List<CollaboratorDocument>>

    fun user(uid: String): Flow<CollaboratorDocument>

    suspend fun getById(uid: String): CollaboratorDocument?
}
