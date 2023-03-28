package com.peyess.salesapp.data.model.discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.typing.products.DiscountCalcMethod

@Entity(
    tableName = OverallDiscountEntity.tableName,
)
data class OverallDiscountEntity(
    @PrimaryKey
    @ColumnInfo(name = "sale_id")
    val saleId: String = "",

    @ColumnInfo(name = "method")
    val method: DiscountCalcMethod = DiscountCalcMethod.None,
    @ColumnInfo(name = "value")
    val value: Double = 0.0,
) {
    companion object {
        const val tableName = "sale_discount"
    }
}