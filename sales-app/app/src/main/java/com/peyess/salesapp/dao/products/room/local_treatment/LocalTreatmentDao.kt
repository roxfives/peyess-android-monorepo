package com.peyess.salesapp.dao.products.room.local_treatment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.join_lens_treatment.JoinLensTreatmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalTreatmentDao {

    @Insert(onConflict = REPLACE)
    fun add(treatmentEntity: LocalTreatmentEntity)

    @Query(
        "SELECT * FROM ${LocalTreatmentEntity.tableName} AS Treatments " +
                "JOIN ${JoinLensTreatmentEntity.tableName} AS JoinTable " +
                "ON Treatments.id = JoinTable.treatment_id " +
                "WHERE lens_id = :lensId"

    )
    fun treatmentsForLens(lensId: String): Flow<List<LocalTreatmentEntity>>

    @Query("SELECT * FROM ${LocalTreatmentEntity.tableName} WHERE id = :treatmentId")
    fun getById(treatmentId: String): Flow<LocalTreatmentEntity?>
}