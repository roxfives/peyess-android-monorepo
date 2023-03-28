package com.peyess.salesapp.data.model.edit_service_order.frames

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity
import com.peyess.salesapp.typing.frames.FramesType

@Entity(
    tableName = EditFramesDataEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = EditServiceOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["so_id"],
            onDelete = CASCADE,
        )
    ]
)
data class EditFramesDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "so_id")
    val soId: String = "",

    @ColumnInfo(name = "is_new")
    val areFramesNew: Boolean = false,

    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "reference")
    val reference: String = "",
    @ColumnInfo(name = "value")
    val value: Double = 0.0,
    @ColumnInfo(name = "tag_code")
    val tagCode: String = "",
    @ColumnInfo(name = "type")
    val type: FramesType = FramesType.None,

    @ColumnInfo(name = "info")
    val framesInfo: String = "",
) {
    companion object {
        const val tableName = "edit_frames_data"
    }
}
