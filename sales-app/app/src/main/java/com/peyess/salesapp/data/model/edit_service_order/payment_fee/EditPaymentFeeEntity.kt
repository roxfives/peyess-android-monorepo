package com.peyess.salesapp.data.model.edit_service_order.payment_fee

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.edit_service_order.sale.EditSaleEntity
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

@Entity(
    tableName = EditPaymentFeeEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = EditSaleEntity::class,
            parentColumns = ["id"],
            childColumns = ["sale_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class EditPaymentFeeEntity(
    @PrimaryKey
    @ColumnInfo(name = "sale_id")
    val saleId: String = "",

    @ColumnInfo(name = "method")
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    @ColumnInfo(name = "value")
    val value: Double = 0.0,
) {
    companion object {
        const val tableName = "edit_payment_fee"
    }
}
