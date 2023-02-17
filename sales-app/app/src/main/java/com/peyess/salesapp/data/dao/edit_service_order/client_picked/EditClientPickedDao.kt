package com.peyess.salesapp.data.dao.edit_service_order.client_picked

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedEntity
import com.peyess.salesapp.typing.client.Sex
import com.peyess.salesapp.typing.sale.ClientRole
import kotlinx.coroutines.flow.Flow

@Dao
interface EditClientPickedDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertClientPicked(clientPicked: EditClientPickedEntity)

    @Query("""
        SELECT * FROM ${EditClientPickedEntity.tableName}
        WHERE so_id = :serviceOrderId AND role = :role
    """)
    suspend fun findClientPickedForServiceOrder(
        serviceOrderId: String,
        role: ClientRole,
    ): EditClientPickedEntity?

    @Query("""
        SELECT * FROM ${EditClientPickedEntity.tableName}
        WHERE so_id = :serviceOrderId AND role = :role
    """)
    fun streamClientPickedForServiceOrder(
        serviceOrderId: String,
        role: ClientRole,
    ): Flow<EditClientPickedEntity?>

    @Query("""
        UPDATE ${EditClientPickedEntity.tableName}
        SET role = :clientRole
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateClientRole(serviceOrderId: String, clientRole: ClientRole)

    @Query("""
        UPDATE ${EditClientPickedEntity.tableName}
        SET name_display = :name_display
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateNameDisplay(serviceOrderId: String, name_display: String)

    @Query("""
        UPDATE ${EditClientPickedEntity.tableName}
        SET name = :name
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateName(serviceOrderId: String, name: String)

    @Query("""
        UPDATE ${EditClientPickedEntity.tableName}
        SET sex = :sex
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateSex(serviceOrderId: String, sex: Sex)

    @Query("""
        UPDATE ${EditClientPickedEntity.tableName}
        SET email = :email
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateEmail(serviceOrderId: String, email: String)

    @Query("""
        UPDATE ${EditClientPickedEntity.tableName}
        SET document = :document
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateDocument(serviceOrderId: String, document: String)

    @Query("""
        UPDATE ${EditClientPickedEntity.tableName}
        SET short_address = :shortAddress
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateShortAddress(serviceOrderId: String, shortAddress: String)
}
