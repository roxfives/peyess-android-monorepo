package com.peyess.salesapp.dao.auth.users

import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.flow.Flow

interface CollaboratorsDao {
    fun subscribeToActiveAccounts(): Flow<List<Collaborator>>

    fun user(uid: String): Flow<Collaborator>
}