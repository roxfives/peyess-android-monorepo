package com.peyess.salesapp.data.dao.local_sale.local_prescription

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalPrescriptionDao {
    @Insert
    fun insert(prescriptionPictureEntity: PrescriptionEntity)

    @Update
    fun update(prescriptionPictureEntity: PrescriptionEntity)

    @Query("SELECT * FROM ${PrescriptionEntity.tableName} as p WHERE p.so_id = :soId ")
    fun getById(soId: String): Flow<PrescriptionEntity?>

    @Query("SELECT COUNT(*) FROM ${PrescriptionEntity.tableName} as p WHERE p.so_id = :soId")
    fun streamExists(soId: String): Flow<Int>

    @Query("SELECT * FROM ${PrescriptionEntity.tableName} as p WHERE p.so_id = :soId ")
    suspend fun getPrescriptionForServiceOrder(soId: String): PrescriptionEntity?

    @Query("""
        UPDATE ${PrescriptionEntity.tableName}
        SET has_addition = :hasAddition
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateHasAddition(serviceOrderId: String, hasAddition: Int)

    @Query("SELECT COUNT(*) FROM ${PrescriptionEntity.tableName} WHERE so_id = :serviceOrderId")
    suspend fun exitsForServiceOrder(serviceOrderId: String): Int?
}
