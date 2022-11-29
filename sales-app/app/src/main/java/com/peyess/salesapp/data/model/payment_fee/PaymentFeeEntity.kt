package com.peyess.salesapp.data.model.payment_fee

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod

@Entity(
    tableName = PaymentFeeEntity.tableName,
)
data class PaymentFeeEntity(
    @PrimaryKey
    @ColumnInfo(name = "sale_id")
    val saleId: String = "",

    @ColumnInfo(name = "method")
    val method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.None,
    @ColumnInfo(name = "value")
    val value: Double = 0.0,
) {
    companion object {
        const val tableName = "sale_payment_fee"
    }
}