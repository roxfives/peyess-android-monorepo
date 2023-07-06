package com.peyess.salesapp.data.model.edit_service_order.payment

import android.net.Uri
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.data.model.local_client.LocalClientEntity
import com.peyess.salesapp.typing.sale.PaymentDueDateMode
import java.time.ZonedDateTime

@Entity(
    tableName = EditLocalPaymentEntity.tableName,
//    foreignKeys = [
//        ForeignKey(
//            entity = LocalClientEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["client_id"],
//            onDelete = ForeignKey.NO_ACTION,
//            onUpdate = ForeignKey.NO_ACTION,
//        ),
//    ],
)
data class EditLocalPaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "uuid")
    val uuid: String = "",

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

    @ColumnInfo(name = "has_legal_id")
    val hasLegalId: Boolean = false,
    @ColumnInfo(name = "legal_id")
    val legalId: String = "",

    @ColumnInfo(name = "card_flag_name")
    val cardFlagName: String = "",
    @ColumnInfo(name = "card_flag_icon")
    val cardFlagIcon: Uri = Uri.EMPTY,

    @ColumnInfo(name = "due_date_mode")
    val dueDateMode: PaymentDueDateMode = PaymentDueDateMode.None,
    @ColumnInfo(name = "due_date_period")
    val dueDatePeriod: Int = 0,
    @ColumnInfo(name = "due_date")
    val dueDate: ZonedDateTime = ZonedDateTime.now(),
) {
    companion object {
        const val tableName = "edit_sale_payment"
    }
}
