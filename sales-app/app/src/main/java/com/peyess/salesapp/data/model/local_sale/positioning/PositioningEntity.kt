package com.peyess.salesapp.data.model.local_sale.positioning

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
import kotlin.math.abs

private const val screenWidth = 2448.0
private const val screenHeight = 3264.0

@Entity(
    tableName = PositioningEntity.tableName,
    primaryKeys = ["so_id", "eye"],
//    foreignKeys = [
//        ForeignKey(
//            entity = ActiveSOEntity::class,
//            parentColumns = ["id"],
//            childColumns = ["so_id"],
//            onDelete = ForeignKey.CASCADE,
//        )
//    ]
)
data class PositioningEntity(
    @ColumnInfo(name = "so_id")
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
        const val tableName = "positioning_data"
    }
}

fun PositioningEntity.updateProportion(): PositioningEntity {
    val verticalLength = (abs(baseLeft - baseRight) * realParamHeight) / realParamWidth

    return copy(
        proportionToPictureVertical = realParamHeight / verticalLength,
        proportionToPictureHorizontal = realParamWidth / abs(baseLeft - baseRight),
    )
}

fun PositioningEntity.updateInitialPositioningState(): PositioningEntity {
    val opticCenterX = (if (eye == Eye.Left) 0.60 else 0.39) * screenWidth
    val opticCenterY = (if (eye == Eye.Left) 0.39 else 0.44) * screenHeight
    val radius = 281.0

    val innerFramesDistance = 180f
    val bridgeDistance = 160

    val baseLeftDistance = if (eye == Eye.Left) -295.0 else 20.0
    val checkLeftDistance = if (eye == Eye.Left) 70 else 100
    val checkRightDistance = if (eye == Eye.Left) 179 else 195
    val baseRightDistance = if (eye == Eye.Left) 262 else 235
    val checkBottomDistance = 90f
    val framesTopDistance = -25f

    val checkTopDistance = -98f
    val framesBottomDistance = 175f

    val framesLeft = (if (eye == Eye.Left) opticCenterX - innerFramesDistance else opticCenterX - radius)
    val framesRight = (if (eye == Eye.Left) opticCenterX + radius else opticCenterX + innerFramesDistance)
    val bridgePivot = (if (eye == Eye.Left) framesLeft - bridgeDistance else framesRight + bridgeDistance)
    val baseLeft = bridgePivot + baseLeftDistance
    val checkTop = opticCenterY + checkTopDistance

    val bridgeHelper = (if (eye == Eye.Right) framesRight else bridgePivot) +
            abs(bridgePivot - (if (eye == Eye.Right) framesRight else framesLeft)) / 2.0

    return copy(
        baseLeft = baseLeft, // (if (eye == Eye.Left) 0.33 else 0.53) * screenWidth,
        baseRight = baseLeft + baseRightDistance, // (if (eye == Eye.Left) 0.45 else 0.65) * screenWidth,
        bridgePivot = bridgePivot, // 0.5 * screenWidth + (if (eye == Eye.Left) 7 else -7),
        checkMiddle = bridgeHelper + (if (eye == Eye.Left) 7 else -7), // 0.5 * screenWidth,
        checkLeft = baseLeft + checkLeftDistance, // (if (eye == Eye.Left) 0.36 else 0.57) * screenWidth,
        checkRight = baseLeft + checkRightDistance, // (if (eye == Eye.Left) 0.41 else 0.62) * screenWidth,
        framesLeft = framesLeft, // (if (eye == Eye.Left) 0.53 else 0.28) * screenWidth,
        framesRight = framesRight, // (if (eye == Eye.Left) 0.72 else 0.45) * screenWidth,
        opticCenterX = opticCenterX, // (if (eye == Eye.Left) 0.60 else 0.39) * screenWidth,

        opticCenterY = opticCenterY, // (if (eye == Eye.Left) 0.39 else 0.44) * screenHeight,
        checkTop = checkTop, // (if (eye == Eye.Left) 0.35 else 0.42) * screenHeight,
        checkBottom = checkTop + checkBottomDistance, // (if (eye == Eye.Left) 0.38 else 0.42) * screenHeight,
        framesBottom = opticCenterY + framesBottomDistance, // (if (eye == Eye.Left) 0.46 else 0.52) * screenHeight,
        framesTop = checkTop + framesTopDistance, // (if (eye == Eye.Left) 0.39 else 0.41) * screenHeight,

        topPointRotation = if (eye == Eye.Left) -19.0 else -161.0,
        bottomPointRotation = if (eye == Eye.Left) 45.0 else 135.0,

        opticCenterRadius = radius,
        topPointLength = radius,
        bottomPointLength = radius,
    )
}