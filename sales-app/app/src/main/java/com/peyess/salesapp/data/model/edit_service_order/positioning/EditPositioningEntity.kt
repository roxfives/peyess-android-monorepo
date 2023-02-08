package com.peyess.salesapp.data.model.edit_service_order.positioning

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.peyess.salesapp.data.model.edit_service_order.service_order.EditServiceOrderEntity
import com.peyess.salesapp.typing.general.Eye

// TODO: refactor these values
private const val screenWidth = 2448.0
private const val screenHeight = 3264.0

@Entity(
    tableName = EditPositioningEntity.tableName,
    foreignKeys = [
        ForeignKey(
            entity = EditServiceOrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["so_id"],
            onDelete = CASCADE,
        ),
    ],
)
data class EditPositioningEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String = "",

    @ColumnInfo(name = "so_id", index = true)
    val soId: String = "",
    @ColumnInfo(name = "eye")
    val eye: Eye = Eye.None,

    @ColumnInfo(name = "picture")
    val picture: Uri = Uri.EMPTY,

    @ColumnInfo(name = "rotation")
    val rotation: Double = 0.0,
    @ColumnInfo(name = "device")
    val device: String = "",

    @ColumnInfo(name = "base_left")
    val baseLeft: Double = (if (eye == Eye.Left) 0.47 else 0.53) * screenWidth,
    @ColumnInfo(name = "base_left_rotation")
    val baseLeftRotation: Double = 0.0,

    @ColumnInfo(name = "base_right")
    val baseRight: Double = (if (eye == Eye.Left) 0.65 else 0.35) * screenWidth,
    @ColumnInfo(name = "base_right_rotation")
    val baseRightRotation: Double = 0.0,

    @ColumnInfo(name = "base_top")
    val baseTop: Double = 0.41 * screenHeight,
    @ColumnInfo(name = "base_bottom")
    val baseBottom: Double = 0.52 * screenHeight,

    @ColumnInfo(name = "top_point_length")
    val topPointLength: Double = 245.0,
    @ColumnInfo(name = "top_point_rotation")
    val topPointRotation: Double = if (eye == Eye.Left) -45.0 else -135.0,

    @ColumnInfo(name = "bottom_point_length")
    val bottomPointLength: Double = 245.0,
    @ColumnInfo(name = "bottom_point_rotation")
    val bottomPointRotation: Double = if (eye == Eye.Left) 45.0 else 135.0,

    @ColumnInfo(name = "bridge_pivot")
    val bridgePivot: Double = (if (eye == Eye.Left) 0.48 else 0.52) * screenWidth,

    @ColumnInfo(name = "check_bottom")
    val checkBottom: Double = 0.44 * screenHeight,
    @ColumnInfo(name = "check_top")
    val checkTop: Double = 0.42 * screenHeight,
    @ColumnInfo(name = "check_left")
    val checkLeft: Double = (if (eye == Eye.Left) 0.43 else 0.57) * screenWidth,
    @ColumnInfo(name = "check_left_rotation")
    val checkLeftRotation: Double = 0.0,
    @ColumnInfo(name = "check_middle")
    val checkMiddle: Double = 0.5 * screenWidth,
    @ColumnInfo(name = "check_right")
    val checkRight: Double = (if (eye == Eye.Left) 0.38 else 0.62) * screenWidth,
    @ColumnInfo(name = "check_right_rotation")
    val checkRightRotation: Double = 0.0,

    @ColumnInfo(name = "frames_bottom")
    val framesBottom: Double = 0.52 * screenHeight,
    @ColumnInfo(name = "frames_left")
    val framesLeft: Double = (if (eye == Eye.Left) 0.74 else 0.26) * screenWidth,
    @ColumnInfo(name = "frames_right")
    val framesRight: Double = (if (eye == Eye.Left) 0.55 else 0.45) * screenWidth,
    @ColumnInfo(name = "frames_top")
    val framesTop: Double = 0.41 * screenHeight,

    @ColumnInfo(name = "optic_center_radius")
    val opticCenterRadius: Double = 281.0,
    @ColumnInfo(name = "optic_center_x")
    val opticCenterX: Double = (if (eye == Eye.Left) 0.63 else 0.37) * screenWidth,
    @ColumnInfo(name = "optic_center_y")
    val opticCenterY: Double = 0.44 * screenHeight,

    @ColumnInfo(name = "height")
    val realParamHeight: Double = 38.7,
    @ColumnInfo(name = "width")
    val realParamWidth: Double = 28.3,

    @ColumnInfo(name = "proportion_to_picture_horizontal")
    val proportionToPictureHorizontal: Double = 0.0,
    @ColumnInfo(name = "proportion_to_picture_vertical")
    val proportionToPictureVertical: Double = 0.0,

    @ColumnInfo(name = "euler_angle_x")
    val eulerAngleX: Double = 0.0,
    @ColumnInfo(name = "euler_angle_y")
    val eulerAngleY: Double = 0.0,
    @ColumnInfo(name = "euler_angle_z")
    val eulerAngleZ: Double = 0.0,
) {
    companion object {
        const val tableName = "edit_positioning"
    }
}
