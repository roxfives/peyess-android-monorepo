package com.peyess.salesapp.data.dao.local_sale.client_picked

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.typing.sale.ClientRole
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientPickedDao {

    @Insert(onConflict = REPLACE)
    fun add(clientEntity: ClientPickedEntity)

    @Query("""
        SELECT * FROM ${ClientPickedEntity.tableName}
        WHERE so_id = :soId  
    """)
    suspend fun allClientsForServiceOrder(soId: String): List<ClientPickedEntity>

    @Query("SELECT * FROM ${ClientPickedEntity.tableName} " +
            "WHERE role = :role " +
            "AND so_id = :soId ")
    fun getClientForSO(role: ClientRole, soId: String): Flow<ClientPickedEntity?>

    @Query("SELECT * FROM ${ClientPickedEntity.tableName} " +
            "WHERE role = :role " +
            "AND so_id = :soId ")
    suspend fun getClientForServiceOrder(role: ClientRole, soId: String): ClientPickedEntity?
}