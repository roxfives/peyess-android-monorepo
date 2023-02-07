package com.peyess.salesapp.dao.sale.active_sale

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiveSalesDao {
    @Query(
        "SELECT * FROM ${ActiveSalesEntity.tableName} as sale " +
                "WHERE sale.collaborator_uid = :uid AND sale.active = 1"
    )
    suspend fun activeSalesFor(uid: String): List<ActiveSalesEntity>

    @Query(
        "SELECT * FROM ${ActiveSalesEntity.tableName} as sale " +
                "WHERE sale.collaborator_uid = :uid AND sale.active = 1"
    )
    fun activeSalesStreamFor(uid: String): Flow<List<ActiveSalesEntity>>

    @Query(
        "SELECT * FROM ${ActiveSalesEntity.tableName} as sale " +
                "WHERE sale.id = :id"
    )
    fun getById(id: String): Flow<ActiveSalesEntity?>

    @Query(
        "SELECT * FROM ${ActiveSalesEntity.tableName} as sale " +
                "WHERE sale.id = :id"
    )
    suspend fun getSaleById(id: String): ActiveSalesEntity?

    @Insert
    suspend fun add(activeSale: ActiveSalesEntity)

    @Update
    fun update(activeSale: ActiveSalesEntity)

    @Delete
    fun remove(activeSale: ActiveSalesEntity)
}