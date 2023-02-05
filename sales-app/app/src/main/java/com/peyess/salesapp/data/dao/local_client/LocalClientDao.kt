package com.peyess.salesapp.data.dao.local_client

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import com.peyess.salesapp.data.model.local_client.LocalClientEntity
import com.peyess.salesapp.data.model.local_client.LocalClientStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalClientDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertClientStatus(clientStatus: LocalClientStatusEntity)

    @Query("""
        SELECT COUNT(id) FROM ${LocalClientEntity.tableName}
    """)
    suspend fun totalClients(): Int?

    @Query("SELECT * FROM ${LocalClientStatusEntity.tableName} WHERE id = :id")
    suspend fun clientStatus(id: Long): LocalClientStatusEntity?
    @Query("SELECT * FROM ${LocalClientStatusEntity.tableName} WHERE id = :id")
    fun streamClientStatus(id: Long): Flow<LocalClientStatusEntity?>

    @Insert(onConflict = REPLACE)
    suspend fun insertClient(clientStatus: LocalClientEntity)
    @Update
    suspend fun updateClient(clientStatus: LocalClientEntity)

    @Query("""
        SELECT * FROM ${LocalClientEntity.tableName}
        ORDER BY remote_updated DESC
        LIMIT 1
    """)
    suspend fun latestClientUpdated(): LocalClientEntity?

    @Query("SELECT * FROM ${LocalClientEntity.tableName} WHERE id = :clientId")
    suspend fun clientById(clientId: String): LocalClientEntity?
    @Query("SELECT * FROM ${LocalClientEntity.tableName} WHERE id = :clientId")
    fun streamClientById(clientId: String): Flow<LocalClientEntity?>

    @RawQuery(observedEntities = [LocalClientEntity::class])
    fun paginateClients(query: SimpleSQLiteQuery): PagingSource<Int, LocalClientEntity>
}