package com.peyess.salesapp.data.model.local_sale.payment

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
import java.time.ZonedDateTime

@Entity(
    tableName = LocalPaymentEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//            onDelete = CASCADE,
//        )
//    ]
)
data class LocalPaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "sale_id")
    val saleId: String = "",

    @ColumnInfo(name = "uuid")
    val uuid: String = "",

    // TODO: Normalize data for clients and (maybe) payment method
    @ColumnInfo(name = "client_id")
    val clientId: String = "",
    @ColumnInfo(name = "client_document")
    val clientDocument: String = "",
    @ColumnInfo(name = "client_name")
    val clientName: String = "",
    @ColumnInfo(name = "client_address")
    val clientAddress: String = "",

    @ColumnInfo(name = "method_id")
    val methodId: String = "",
    @ColumnInfo(name = "method_name")
    val methodName: String = "",
    @ColumnInfo(name = "method_type")
    val methodType: String = "unknown",

    @ColumnInfo(name = "value")
    val value: Double = 0.0,
    @ColumnInfo(name = "installments")
    val installments: Int = 1,

    @ColumnInfo(name = "document")
    val document: String = "",

    @ColumnInfo(name = "card_flag_name")
    val cardFlagName: String = "",
    @ColumnInfo(name = "card_flag_icon")
    val cardFlagIcon: Uri = Uri.EMPTY,
    @ColumnInfo(name = "card_nsu")
    val cardNsu: String = "",

    @ColumnInfo(name = "due_date")
    val dueDate: ZonedDateTime = ZonedDateTime.now(),
    @ColumnInfo(name = "due_date_period")
    val dueDatePeriod: Int = 0,
    @ColumnInfo(name = "due_date_mode")
    val dueDateMode: PaymentDueDateMode = PaymentDueDateMode.None,
) {
    companion object {
        const val tableName = "payments"
    }
}
