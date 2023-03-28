package com.peyess.salesapp.data.model.lens.room.treatment

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.NO_ACTION
import androidx.room.PrimaryKey

@Entity(
    tableName = LocalLensTreatmentExplanationEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = LocalLensTreatmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["treatment_id"],
            onDelete = NO_ACTION,
            onUpdate = NO_ACTION,
        )
    ]
)
data class LocalLensTreatmentExplanationEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,

    @ColumnInfo(name = "treatment_id", index = true)
    val treatmentId: String = "",

    @ColumnInfo(name = "explanation")
    val explanation: String = "",
) {
    companion object {
        const val tableName = "local_lens_treatment_exp"
    }
}
