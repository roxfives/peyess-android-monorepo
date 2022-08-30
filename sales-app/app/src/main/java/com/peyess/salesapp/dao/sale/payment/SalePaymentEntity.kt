package com.peyess.salesapp.dao.sale.payment

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "so_id") val soId: String = "",

    @ColumnInfo(name = "client_id") val clientId: String = "",
    @ColumnInfo(name = "method_id") val methodId: String = "",

    @ColumnInfo(name = "value") val value: Double = 0.0,
    @ColumnInfo(name = "installments") val installments: Int = 0,

    @ColumnInfo(name = "document") val document: String = "",
    @ColumnInfo(name = "doc_pic") val docPicture: Uri = Uri.EMPTY,
) {
    companion object {
        const val tableName = "payments"
    }
}
