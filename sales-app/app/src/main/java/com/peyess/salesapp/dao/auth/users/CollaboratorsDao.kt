package com.peyess.salesapp.dao.auth.users

import android.net.Uri
import com.peyess.salesapp.model.users.FSCollaborator
import kotlinx.coroutines.flow.Flow

interface CollaboratorsDao {
    fun streamActiveAccounts(): Flow<List<FSCollaborator>>

    fun user(uid: String): Flow<FSCollaborator>

    suspend fun getById(uid: String): FSCollaborator?

    suspend fun pictureFor(uid: String): Uri
}