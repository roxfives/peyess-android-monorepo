package com.peyess.salesapp.dao.sale.active_so

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiveSODao {
    @Query(
        "SELECT * FROM ${ActiveSOEntity.tableName} as so WHERE so.id = :id"
    )
    fun getById(id: String): Flow<ActiveSOEntity?>

    @Insert
    fun add(activeSale: ActiveSOEntity)

    @Update
    fun update(activeSale: ActiveSOEntity)

    @Delete
    fun remove(activeSale: ActiveSOEntity)
}