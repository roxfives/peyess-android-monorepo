package com.peyess.salesapp.dao.sale.active_so

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.peyess.salesapp.dao.sale.active_so.db_view.ServiceOrderDBView
import kotlinx.coroutines.flow.Flow

@Dao
interface ActiveSODao {
    @Query("SELECT * FROM ${ActiveSOEntity.tableName} as so WHERE so.id = :id")
    fun streamServiceOrderById(id: String): Flow<ActiveSOEntity?>

    @Query("SELECT * FROM ${ActiveSOEntity.tableName} as so WHERE so.id = :id")
    suspend fun getServiceOrderById(id: String): ActiveSOEntity?

    @Query("SELECT * FROM ${ActiveSOEntity.tableName} as so WHERE so.sale_id = :saleId")
    suspend fun getServiceOrdersForSale(saleId: String): List<ActiveSOEntity>

    @Query("""
        SELECT * FROM ${ServiceOrderDBView.viewName} 
        WHERE collaboratorId = :collaboratorId AND hasFinished = 0
    """)
    fun streamUnfinishedServiceOrdersForUser(
        collaboratorId: String,
    ): Flow<List<ServiceOrderDBView>>

    @Insert
    suspend fun add(activeSale: ActiveSOEntity)

    @Update
    fun update(activeSale: ActiveSOEntity)

    @Query("""
        UPDATE ${ActiveSOEntity.tableName} 
        SET client_name = :name
        WHERE id = :serviceOrderId
    """)
    suspend fun updateClientName(serviceOrderId: String, name: String)

    @Delete
    fun remove(activeSale: ActiveSOEntity)
}