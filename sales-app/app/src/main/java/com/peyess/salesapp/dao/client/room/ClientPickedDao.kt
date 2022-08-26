package com.peyess.salesapp.dao.client.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientPickedDao {

    @Insert(onConflict = REPLACE)
    fun add(clientEntity: ClientEntity)

    @Query("SELECT * FROM ${ClientEntity.tableName} " +
            "WHERE role = :role " +
            "AND so_id = :soId ")
    fun getClientForSO(role: ClientRole, soId: String): Flow<ClientEntity?>
}