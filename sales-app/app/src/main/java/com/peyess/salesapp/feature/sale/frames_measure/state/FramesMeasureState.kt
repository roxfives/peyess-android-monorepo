package com.peyess.salesapp.feature.sale.frames_measure.state

import android.net.Uri
import androidx.annotation.Keep
import com.airbnb.mvrx.MavericksState
import com.google.firebase.firestore.PropertyName
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter.MeasuringParameter
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.PositioningAnimationState
import java.util.Date
import kotlin.math.abs

// TODO: Remove hardcoded sizes
private const val screenWidth = 2448.0
private const val screenHeight = 3264.0

sealed class HelperZoomState {
    object ZoomOut : HelperZoomState()
    object ZoomNormal : HelperZoomState()
}

sealed class HeadState {
    object Idle : HeadState()
    object LookingForward : HeadState()
    object TooManyPeople : HeadState()
    object NoPerson : HeadState()
    object TiltedLeft : HeadState()
    object TiltedRight : HeadState()
    object TurnedLeft : HeadState()
    object TurnedRight : HeadState()
    object Lifted : HeadState()
    object Down : HeadState()
}

sealed class CameraSetUpState {
    object Idle : CameraSetUpState()
    object SettingUp : CameraSetUpState()
    object SetUp : CameraSetUpState()
}

const val slow = 1.0
const val speedUnit = 1.0
const val fastest = 100.0

data class FramesMeasureState(
    val picture: Uri = Uri.EMPTY,

    val eye: Eye = Eye.None,

    val positioning: Positioning = Positioning(),

    val cameraSetUpState: CameraSetUpState = CameraSetUpState.Idle,

    val takingHeadZoomState: HelperZoomState = HelperZoomState.ZoomNormal,

    val hasAcceptedCheckMiddleInvalid: Boolean = false,

    val facesFound: Int  = 0,
    val latestEulerX: Double = 0.0,
    val latestEulerY: Double = 0.0,
    val latestEulerZ: Double = 0.0,
    val latestDeviceRotation: Double = 0.0,

    val movableParameter: Parameter? = null,
    val measuringParameters: Map<Parameter, MeasuringParameter> = emptyMap(),
    val positioningAnimationState: PositioningAnimationState =
        PositioningAnimationState.Idle,
    val zoomHelperState: HelperZoomState = HelperZoomState.ZoomNormal,

    val isPointTopAttached: Boolean = true,
    val isPointBottomAttached: Boolean = true,

    val isOuterFramesAttached: Boolean = true,
    val isInnerFramesAttached: Boolean = true,
    val isBridgeAttached: Boolean = true,

    val isBaseLeftAttached: Boolean = true,
    val isBaseRightAttached: Boolean = true,
    val isCheckLeftAttached: Boolean = true,
    val isCheckRightAttached: Boolean = true,

    val isCheckMiddleAttached: Boolean = true,

    val isCheckTopAttached: Boolean = true,
    val isCheckBottomAttached: Boolean = true,
    val isFramesTopAttached: Boolean = true,
    val isFramesBottomAttached: Boolean = true,

    val standardMiddleY: Double = 0.44 * screenHeight,
    val standardMiddleX: Double = 1224.0,
    val standardHeight: Double = 720.0,
    val standardLength: Double = 1200.0,
    val standardRotation: Double = 0.0,

    val personInputErrorCode: String = "",
    val framesInputErrorCode: String = "",
    val measureInputErrorCode: String = "",

    val positioningUploadError: String = "",
    val measuringUploadError: String = "",

    val canAddMeasure: Boolean = false,

    val currentSpeed: Double = slow,


): MavericksState {
    private val tiltFreeThresholdLeftEye = 0.0 // tilt left
    private val tiltBlockedThresholdLeftEye = -7.0 // -tilt right

    private val tiltFreeThresholdRightEye = -5.0 // tilt left
    private val tiltBlockedThresholdRightEye = 2.0 // -tilt right

    private val freeHorizontalRotationThresholdLeftEye = -0.5 // 0.0 // 0.5 // 1.0 // 1.5 (virar para direita)
    private val blockedHorizontalRotationLeftEye = -3.5 // 3.5  //-4.0 // -3.5 // -2.5 (virar para esquerda)

    private val freeHorizontalRotationThresholdRightEye = -1.0 // -2.5 // 0
    private val blockedHorizontalRotationRightEye = 3.0 // 4

    // abaixar (mais positivo aumenta o range)
    private val topVerticalRotationThreshold = -5.0 // -7 (texto erro) // -6 // -3.0
    // levantar (menos positivo aumenta o range)
    private val bottomVerticalRotationThreshold = -11.0 // -12 // -12 // -8.0

    private val checkMiddleThreshold = 7

    // TODO: Refactor this please
    val takingHeadState = if (facesFound <= 0) {
        HeadState.NoPerson
    } else if (facesFound > 1) {
        HeadState.TooManyPeople
    } else if (isHorizontalBad()) {
        if ((eye == Eye.Left && latestEulerY < blockedHorizontalRotationLeftEye) ||
            (eye == Eye.Right && latestEulerY < freeHorizontalRotationThresholdRightEye)
        ) {
            HeadState.TurnedRight
        } else {
            HeadState.TurnedLeft
        }
    } else if (isVerticalBad()) {
        if (latestEulerX < bottomVerticalRotationThreshold) {
            HeadState.Down
        } else {
            HeadState.Lifted
        }
    } else if (isTiltBad()) {
        if ((eye == Eye.Left && latestEulerZ < tiltBlockedThresholdLeftEye) ||
            (eye == Eye.Right && latestEulerZ < tiltFreeThresholdRightEye)
        ) {
            HeadState.TiltedLeft
        } else {
            HeadState.TiltedRight
        }
    } else {
        HeadState.LookingForward
    }

    val isCheckMiddleInvalid: Boolean = if (movableParameter == Parameter.CheckMiddle) {
        val framesRight = positioning.framesRight
        val framesLeft = positioning.framesLeft
        val bridgePivot = positioning.bridgePivot
        val bridgeHelper = (if (eye == Eye.Right) framesRight else bridgePivot) +
                abs(bridgePivot - (if (eye == Eye.Right) framesRight else framesLeft)) / 2.0

        abs(positioning.checkMiddle - bridgeHelper) > checkMiddleThreshold
    } else {
        false
    }


    private fun isHorizontalBad(): Boolean {
        return (eye == Eye.Left && !(latestEulerY < freeHorizontalRotationThresholdLeftEye && latestEulerY > blockedHorizontalRotationLeftEye)) ||
                (eye == Eye.Right && !(latestEulerY > freeHorizontalRotationThresholdRightEye && latestEulerY < blockedHorizontalRotationRightEye))
    }

    private fun isVerticalBad(): Boolean {
        return !(latestEulerX > bottomVerticalRotationThreshold && latestEulerX < topVerticalRotationThreshold)
    }

    private fun isTiltBad(): Boolean {
        return (eye == Eye.Left && !(latestEulerZ > tiltBlockedThresholdLeftEye && latestEulerZ < tiltFreeThresholdLeftEye)) ||
                (eye == Eye.Right && !(latestEulerZ > tiltFreeThresholdRightEye && latestEulerZ < tiltBlockedThresholdRightEye))
    }
}

data class Pqp(val pqp: String = "bla")

@Keep
data class Positioning(
    @JvmField
    @PropertyName("frames_id")
    val framesId: String = "unknown",

    @JvmField
    @PropertyName("director_uid")
    val directorUid: String = "unknown",
    @JvmField
    @PropertyName("store_id")
    val storeId: String = "unknown",
    @JvmField
    @PropertyName("prescription_id")
    val prescriptionId: String = "unknown",
    @JvmField
    @PropertyName("measuring_id")
    val measuringId: String = "unknown",

    val picture: String = "",

    val eye: Eye = Eye.None,

    val rotation: Double = 0.0,
    val device: String = "",

    @JvmField
    @PropertyName("taken_by_uid")
    val takenByUid: String = "",
    @JvmField
    @PropertyName("edited_by_uid")
    val editedByUid: String = "",

    @JvmField
    @PropertyName("service_orders")
    val serviceOrders: List<String> = emptyList(),

    @JvmField
    @PropertyName("patient_uid")
    val patientUid: String = "",
    @JvmField
    @PropertyName("patient_document")
    val patientDocument: String = "",
    @JvmField
    @PropertyName("patient_name")
    val patientName: String = "",

    @JvmField
    @PropertyName("base_left")
    val baseLeft: Double = (if (eye == Eye.Left) 0.47 else 0.53) * screenWidth,
    @JvmField
    @PropertyName("base_left_rotation")
    val baseLeftRotation: Double = 0.0,

    @JvmField
    @PropertyName("base_right")
    val baseRight: Double = (if (eye == Eye.Left) 0.65 else 0.35) * screenWidth,
    @JvmField
    @PropertyName("base_right_rotation")
    val baseRightRotation: Double = 0.0,

    @JvmField
    @PropertyName("top_point_length")
    val topPointLength: Double = 245.0,
    @JvmField
    @PropertyName("top_point_rotation")
    val topPointRotation: Double = if (eye == Eye.Left) -45.0 else -135.0,

    @JvmField
    @PropertyName("bottom_point_length")
    val bottomPointLength: Double = 245.0,
    @JvmField
    @PropertyName("bottom_point_rotation")
    val bottomPointRotation: Double = if (eye == Eye.Left) 45.0 else 135.0,

    @JvmField
    @PropertyName("bridge_pivot")
    val bridgePivot: Double = (if (eye == Eye.Left) 0.48 else 0.52) * screenWidth,

    @JvmField
    @PropertyName("check_bottom")
    val checkBottom: Double = 0.44 * screenHeight,
    @JvmField
    @PropertyName("check_top")
    val checkTop: Double = 0.42 * screenHeight,
    @JvmField
    @PropertyName("check_left")
    val checkLeft: Double = (if (eye == Eye.Left) 0.43 else 0.57) * screenWidth,
    @JvmField
    @PropertyName("check_left_rotation")
    val checkLeftRotation: Double = 0.0,
    @JvmField
    @PropertyName("check_middle")
    val checkMiddle: Double = 0.5 * screenWidth,
    @JvmField
    @PropertyName("check_right")
    val checkRight: Double = (if (eye == Eye.Left) 0.38 else 0.62) * screenWidth,
    @JvmField
    @PropertyName("check_right_rotation")
    val checkRightRotation: Double = 0.0,

    @JvmField
    @PropertyName("frames_bottom")
    val framesBottom: Double = 0.52 * screenHeight,
    @JvmField
    @PropertyName("frames_left")
    val framesLeft: Double = (if (eye == Eye.Left) 0.74 else 0.26) * screenWidth,
    @JvmField
    @PropertyName("frames_right")
    val framesRight: Double = (if (eye == Eye.Left) 0.55 else 0.45) * screenWidth,
    @JvmField
    @PropertyName("frames_top")
    val framesTop: Double = 0.41 * screenHeight,

    @JvmField
    @PropertyName("optic_center_radius")
    val opticCenterRadius: Double = 281.0,
    @JvmField
    @PropertyName("optic_center_x")
    val opticCenterX: Double = (if (eye == Eye.Left) 0.63 else 0.37) * screenWidth,
    @JvmField
    @PropertyName("optic_center_y")
    val opticCenterY: Double = 0.44 * screenHeight,

    @JvmField
    @PropertyName("height")
    val realParamHeight: Double = 40.0,
    @JvmField
    @PropertyName("width")
    val realParamWidth: Double = 30.0,

    @JvmField
    @PropertyName("proportion_to_picture_horizontal")
    val proportionToPictureHorizontal: Double = 0.0,
    @JvmField
    @PropertyName("proportion_to_picture_vertical")
    val proportionToPictureVertical: Double = 0.0,

    @JvmField
    @PropertyName("is_editable")
    val isEditable: Boolean = false,

//    val created: Date = Date(),
//    val updated: Date = Date(),

    @JvmField
    @PropertyName("created_by")
    val createdBy: String = "",

    @JvmField
    @PropertyName("updated_by")
    val updatedBy: String = "",

    @JvmField
    @PropertyName("create_allowed_by")
    val createAllowedBy: String = "",

    @JvmField
    @PropertyName("update_allowed_by")
    val updateAllowedBy: String = "",
)

fun Positioning.updateInitialPositioningState(): Positioning {
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