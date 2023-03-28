package com.peyess.salesapp.dao.products.room.join_lens_treatment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity

@Entity(
    tableName = JoinLensTreatmentEntity.tableName,
    primaryKeys = ["lens_id",  "treatment_id"],
//    foreignKeys = [
//        ForeignKey(
//            entity = LocalLensEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["lens_id"],
//        ),
//        ForeignKey(
//            entity = LocalTreatmentEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["treatment_id"],
//        ),
//    ]
)
data class JoinLensTreatmentEntity(
    @ColumnInfo(name = "lens_id") val lensId: String = "",
    @ColumnInfo(name = "treatment_id") val treatmentId: String = "",
) {
    companion object {
        const val tableName = "join_lens_treatment"
    }
}
