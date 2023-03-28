package com.peyess.salesapp.data.dao.edit_service_order.positioning

import android.net.Uri
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.positioning.EditPositioningEntity
import com.peyess.salesapp.typing.general.Eye
import kotlinx.coroutines.flow.Flow

@Dao
interface EditPositioningDao {
    @Insert(onConflict = REPLACE)
    suspend fun addPositioning(positioning: EditPositioningEntity)

    @Query("""
        SELECT * FROM ${EditPositioningEntity.tableName}
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun positioningForServiceOrder(serviceOrderId: String, eye: Eye): EditPositioningEntity?

    @Query("""
        SELECT * FROM ${EditPositioningEntity.tableName}
        WHERE id = :positioningId
    """)
    suspend fun positioningById(positioningId: String): EditPositioningEntity?

    @Query("""
        SELECT * FROM ${EditPositioningEntity.tableName}
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    fun streamPositioningForServiceOrder(
        serviceOrderId: String,
        eye: Eye,
    ): Flow<EditPositioningEntity?>

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET picture = :picture
        WHERE id = :prescriptionId
    """)
    suspend fun updatePictureById(prescriptionId: String, picture: Uri)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET picture = :picture
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updatePicture(serviceOrderId: String, eye: Eye, picture: Uri)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET device = :device
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateDevice(serviceOrderId: String, eye: Eye, device: String)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET base_left = :baseLeft
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBaseLeft(serviceOrderId: String, eye: Eye, baseLeft: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET base_left_rotation = :baseLeftRotation
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBaseLeftRotation(serviceOrderId: String, eye: Eye, baseLeftRotation: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET base_right = :baseRight
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBaseRight(serviceOrderId: String, eye: Eye, baseRight: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET base_right_rotation = :baseRightRotation
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBaseRightRotation(serviceOrderId: String, eye: Eye, baseRightRotation: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET base_top = :baseTop
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBaseTop(serviceOrderId: String, eye: Eye, baseTop: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET base_bottom = :baseBottom
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBaseBottom(serviceOrderId: String, eye: Eye, baseBottom: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET top_point_length = :topPointLength
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateTopPointLength(serviceOrderId: String, eye: Eye, topPointLength: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET top_point_rotation = :topPointRotation
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateTopPointRotation(serviceOrderId: String, eye: Eye, topPointRotation: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET bottom_point_length = :bottomPointLength
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBottomPointLength(serviceOrderId: String, eye: Eye, bottomPointLength: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET bottom_point_rotation = :bottomPointRotation
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBottomPointRotation(serviceOrderId: String, eye: Eye, bottomPointRotation: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET bridge_pivot = :bridgePivot
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateBridgePivot(serviceOrderId: String, eye: Eye, bridgePivot: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET check_bottom = :checkBottom
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateCheckBottom(serviceOrderId: String, eye: Eye, checkBottom: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET check_top = :checkTop
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateCheckTop(serviceOrderId: String, eye: Eye, checkTop: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET check_left = :checkLeft
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateCheckLeft(serviceOrderId: String, eye: Eye, checkLeft: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET check_left_rotation = :checkLeftRotation
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateCheckLeftRotation(serviceOrderId: String, eye: Eye, checkLeftRotation: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET check_middle = :checkMiddle
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateCheckMiddle(serviceOrderId: String, eye: Eye, checkMiddle: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET check_right = :checkRight
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateCheckRight(serviceOrderId: String, eye: Eye, checkRight: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET check_right_rotation = :checkRightRotation
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateCheckRightRotation(serviceOrderId: String, eye: Eye, checkRightRotation: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET frames_bottom = :framesBottom
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateFramesBottom(serviceOrderId: String, eye: Eye, framesBottom: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET frames_left = :framesLeft
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateFramesLeft(serviceOrderId: String, eye: Eye, framesLeft: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET frames_right = :framesRight
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateFramesRight(serviceOrderId: String, eye: Eye, framesRight: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET frames_top = :framesTop
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateFramesTop(serviceOrderId: String, eye: Eye, framesTop: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET optic_center_radius = :centerRadius
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateOpticCenterRadius(serviceOrderId: String, eye: Eye, centerRadius: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET optic_center_x = :centerX
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateOpticCenterX(serviceOrderId: String, eye: Eye, centerX: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET optic_center_y = :centerY
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateOpticCenterY(serviceOrderId: String, eye: Eye, centerY: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET height = :height
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateHeight(serviceOrderId: String, eye: Eye, height: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET width = :width
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateWidth(serviceOrderId: String, eye: Eye, width: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET proportion_to_picture_horizontal = :proportionToPictureHorizontal
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateProportionToPictureHorizontal(serviceOrderId: String, eye: Eye, proportionToPictureHorizontal: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET proportion_to_picture_vertical = :proportionToPictureVertical
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateProportionToPictureVertical(serviceOrderId: String, eye: Eye, proportionToPictureVertical: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET euler_angle_x = :eulerAngleX
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateEulerAngleX(serviceOrderId: String, eye: Eye, eulerAngleX: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET euler_angle_y = :eulerAngleY
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateEulerAngleY(serviceOrderId: String, eye: Eye, eulerAngleY: Double)

    @Query("""
        UPDATE ${EditPositioningEntity.tableName}
        SET euler_angle_z = :eulerAngleZ
        WHERE so_id = :serviceOrderId AND eye = :eye
    """)
    suspend fun updateEulerAngleZ(serviceOrderId: String, eye: Eye, eulerAngleZ: Double)

    @Query("""
        DELETE FROM ${EditPositioningEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    suspend fun deletePositioningsForServiceOrder(serviceOrderId: String)
}
