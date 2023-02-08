package com.peyess.salesapp.data.dao.edit_service_order.service_order

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import kotlinx.coroutines.flow.Flow

@Dao
interface EditServiceOrderDao {
    @Insert(onConflict = REPLACE)
    suspend fun addServiceOrder(serviceOrder: EditServiceOrderEntity)

    @Query(
        """
            SELECT * FROM ${EditServiceOrderEntity.tableName}
            WHERE id = :serviceOrderId
        """
    )
    suspend fun serviceOrderById(serviceOrderId: String): EditServiceOrderEntity?

    @Query(
        """
            SELECT * FROM ${EditServiceOrderEntity.tableName}
            WHERE id = :serviceOrderId
        """
    )
    fun streamServiceOrderById(serviceOrderId: String): Flow<EditServiceOrderEntity?>

    @Query(
        """ 
            UPDATE ${EditServiceOrderEntity.tableName}
            SET has_prescription = :hasPrescription
            WHERE id = :id
        """
    )
    suspend fun updateHasPrescription(
        id: String,
        hasPrescription: Int,
    )

    @Query(
        """
            UPDATE ${EditServiceOrderEntity.tableName}
            SET client_name = :clientName
            WHERE id = :id
        """
    )
    suspend fun updateClientName(
        id: String,
        clientName: String,
    )

    @Query(
        """
            UPDATE ${EditServiceOrderEntity.tableName}
            SET lens_type_name = :lensTypeCategoryName
            WHERE id = :id
        """
    )
    suspend fun updateLensTypeCategoryName(
        id: String,
        lensTypeCategoryName: LensTypeCategoryName,
    )

    @Query(
        """
            UPDATE ${EditServiceOrderEntity.tableName}
            SET is_lens_type_mono = :isLensTypeMono
            WHERE id = :id
        """
    )
    suspend fun updateIsLensTypeMono(
        id: String,
        isLensTypeMono: Int,
    )
}