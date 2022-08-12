package com.peyess.salesapp.dao.sale.prescription_picture

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PrescriptionPictureDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(prescriptionPictureEntity: PrescriptionPictureEntity)

    @Query("SELECT * FROM ${PrescriptionPictureEntity.tableName} as p WHERE p.so_id = :soId ")
    fun getById(soId: String): Flow<PrescriptionPictureEntity?>
}