package com.peyess.salesapp.dao.products.room.local_product_exp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
        ),
        ForeignKey(
            entity = LocalColoringEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
        ),
        ForeignKey(
            entity = LocalTreatmentEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
        ),
    ]
)
data class LocalProductExpEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "product_id") val productId: String = "",
    @ColumnInfo(name = "exp") val exp: String = "",
) {
    companion object {
        const val tableName = "local_product_exp"
    }
}
