package com.peyess.salesapp.data.model.edit_service_order.payment

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.local_client.LocalClientEntity

@Entity(
    tableName = EditSalePaymentEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = LocalClientEntity::class,
            parentColumns = ["id"],
            childColumns = ["client_id"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.NO_ACTION,
        ),
    ],
)
data class EditSalePaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "sale_id")
    val saleId: String = "",

    // TODO: Normalize data for clients and (maybe) payment method
//    @ColumnInfo(name = "client_id")
//    val clientId: String = "",
//    @ColumnInfo(name = "client_document")
//    val clientDocument: String = "",
//    @ColumnInfo(name = "client_name")
//    val clientName: String = "",
//    @ColumnInfo(name = "client_address")
//    val clientAddress: String = "",
    @ColumnInfo(name = "client_id", index = true)
    val clientId: String = "",

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
) {
    companion object {
        const val tableName = "edit_sale_payment"
    }
}
