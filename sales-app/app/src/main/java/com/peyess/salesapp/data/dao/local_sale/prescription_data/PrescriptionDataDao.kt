package com.peyess.salesapp.data.dao.local_sale.prescription_data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(prescriptionPictureEntity: PrescriptionDataEntity)

    @Query("SELECT * FROM ${PrescriptionDataEntity.tableName} as p WHERE p.so_id = :soId ")
    fun getById(soId: String): Flow<PrescriptionDataEntity?>

    @Query("SELECT * FROM ${PrescriptionDataEntity.tableName} as p WHERE p.so_id = :soId ")
    suspend fun getPrescriptionForServiceOrder(soId: String): PrescriptionDataEntity?
}
