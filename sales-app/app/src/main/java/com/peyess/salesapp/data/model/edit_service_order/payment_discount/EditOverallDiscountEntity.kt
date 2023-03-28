package com.peyess.salesapp.data.model.edit_service_order.payment_discount

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity
import com.peyess.salesapp.typing.products.DiscountCalcMethod

@Entity(
    tableName = EditOverallDiscountEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = EditSaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class EditOverallDiscountEntity(
    @PrimaryKey
    @ColumnInfo(name = "sale_id")
    val saleId: String = "",

    @ColumnInfo(name = "method")
    val method: DiscountCalcMethod = DiscountCalcMethod.None,
    @ColumnInfo(name = "value")
    val value: Double = 0.0,
) {
    companion object {
        const val tableName = "sale_overall_discount"
    }
}
