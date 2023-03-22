package com.peyess.salesapp.data.dao.local_sale.local_prescription

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.peyess.salesapp.screen.sale.prescription_lens_type.model.LensTypeCategory
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
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

    @Query("""
        UPDATE ${PrescriptionEntity.tableName}
        SET lens_type_category_id = :lensTypeCategoryId, 
            lens_type_category = :lensTypeCategory
        WHERE so_id = :serviceOrderId
    """)
    suspend fun updateLensTypeCategory(
        serviceOrderId: String,
        lensTypeCategoryId: String,
        lensTypeCategory: LensTypeCategoryName,
    )

    @Query("SELECT COUNT(*) FROM ${PrescriptionEntity.tableName} WHERE so_id = :serviceOrderId")
    suspend fun exitsForServiceOrder(serviceOrderId: String): Int?
}
