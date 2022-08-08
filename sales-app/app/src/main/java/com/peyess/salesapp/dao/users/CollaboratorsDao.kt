package com.peyess.salesapp.dao.users

import com.peyess.salesapp.model.users.Collaborator
import kotlinx.coroutines.flow.Flow

interface CollaboratorsDao {
    fun subscribeToActiveAccounts(): Flow<List<Collaborator>>
}