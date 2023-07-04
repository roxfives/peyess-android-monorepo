package com.peyess.salesapp.data.model.local_sale.payment

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
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

    @ColumnInfo(name = "has_due_date")
    val hasDueDate: Boolean = false,
    @ColumnInfo(name = "due_date")
    val dueDate: ZonedDateTime = ZonedDateTime.now(),
) {
    companion object {
        const val tableName = "payments"
    }
}
