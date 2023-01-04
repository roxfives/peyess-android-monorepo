package com.peyess.salesapp.data.dao.local_sale.prescription_picture

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionPictureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(prescriptionPictureEntity: PrescriptionPictureEntity)

    @Query("SELECT * FROM ${PrescriptionPictureEntity.tableName} as p WHERE p.so_id = :soId ")
    fun getById(soId: String): Flow<PrescriptionPictureEntity?>

    @Query("SELECT * FROM ${PrescriptionPictureEntity.tableName} as p WHERE p.so_id = :soId ")
    suspend fun getPrescriptionForServiceOrder(soId: String): PrescriptionPictureEntity?
}