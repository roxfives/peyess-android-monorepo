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
                "WHERE sale.collaborator_uid = :uid AND sale.active = ${true}"
    )
    fun activeSalesFor(uid: String): Flow<List<ActiveSalesEntity>>

    @Query(
        "SELECT * FROM ${ActiveSalesEntity.tableName} as sale " +
                "WHERE sale.id = :id"
    )
    fun getById(id: String): Flow<ActiveSalesEntity>

    @Insert
    fun add(activeSale: ActiveSalesEntity)

    @Update
    fun update(activeSale: ActiveSalesEntity)

    @Delete
    fun remove(activeSale: ActiveSalesEntity)
}