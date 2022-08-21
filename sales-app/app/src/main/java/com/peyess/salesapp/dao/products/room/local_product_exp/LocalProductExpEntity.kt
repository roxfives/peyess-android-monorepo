package com.peyess.salesapp.dao.products.room.local_product_exp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity

@Entity(
    tableName = LocalProductExpEntity.tableName,
    primaryKeys = ["product_id", "exp"],
    foreignKeys = [
        ForeignKey(
            entity = LocalLensEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onUpdate = CASCADE,
        ),
    ]
)
data class LocalProductExpEntity(
    @ColumnInfo(name = "product_id") val productId: String = "",
    @ColumnInfo(name = "exp") val exp: String = "",
) {
    companion object {
        const val tableName = "local_product_exp"
    }
}
