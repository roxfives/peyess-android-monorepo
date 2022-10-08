package com.peyess.salesapp.dao.sale.payment

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peyess.salesapp.typing.sale.PaymentMethodType

@Entity(
    tableName = SalePaymentEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//            onDelete = CASCADE,
//        )
//    ]
)
data class SalePaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "so_id")
    val soId: String = "",

    // TODO: Normalize data for clients and (maybe) payment method
    @ColumnInfo(name = "client_id")
    val clientId: String = "",
    @ColumnInfo(name = "client_document")
    val clientDocument: String = "",
    @ColumnInfo(name = "client_name")
    val clientName: String = "",
    @ColumnInfo(name = "client_address")
    val clientAddress: String = "",
    @ColumnInfo(name = "client_picture")
    val clientPicture: Uri = Uri.EMPTY,


    @ColumnInfo(name = "method_id")
    val methodId: String = "",
    @ColumnInfo(name = "method_name")
    val methodName: String = "",
//    @ColumnInfo(name = "method_type")
//    val methodType: PaymentMethodType = PaymentMethodType.Money,

    @ColumnInfo(name = "value")
    val value: Double = 0.0,
    @ColumnInfo(name = "installments")
    val installments: Int = 1,

    @ColumnInfo(name = "document")
    val document: String = "",
    @ColumnInfo(name = "doc_pic")
    val docPicture: Uri = Uri.EMPTY,

    @ColumnInfo(name = "card_flag_name")
    val cardFlagName: String = "",
    @ColumnInfo(name = "card_flag_icon")
    val cardFlagIcon: Uri = Uri.EMPTY,
) {
    companion object {
        const val tableName = "payments"
    }
}
