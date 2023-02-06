package com.peyess.salesapp.screen.sale.frames_measure.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter.MeasuringParameter
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.PositioningAnimationState

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
const val fastest = 10.0

data class FramesMeasureState(
//    val picture: Uri = Uri.EMPTY,

    val eye: Eye = Eye.None,

    val positioningAsync: Async<PositioningEntity> = Uninitialized,

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

    val isCheckMiddleInvalid: Boolean = false,
    val isCheckMiddleAttached: Boolean = true,

    val isCheckTopAttached: Boolean = true,
    val isCheckBottomAttached: Boolean = true,
    val isFramesTopAttached: Boolean = true,
    val isFramesBottomAttached: Boolean = true,

    val standardMiddleY: Double = 0.44 * screenHeight,
    val standardMiddleX: Double = 1224.0,
    val standardHeight: Double = 720.0,
    val standardLength: Double = 1800.0,
    val standardRotation: Double = 0.0,

    val personInputErrorCode: String = "",
    val framesInputErrorCode: String = "",
    val measureInputErrorCode: String = "",

    val canAddMeasure: Boolean = false,

    val currentSpeed: Double = slow,
    val firstTouch: Long = 0L,

    val isMeasuringDone: Boolean = false,
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

    val positioning: PositioningEntity = if (positioningAsync is Success) {
        positioningAsync.invoke()
    } else {
        PositioningEntity()
    }
    val picture = positioning.picture

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
