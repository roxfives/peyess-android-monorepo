package com.peyess.salesapp.feature.sale.frames_measure.state

import android.net.Uri
import androidx.core.net.toFile
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.AnimationParametersFactory
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.PositioningAnimationState
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.nextStateEyeLeft
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.nextStateRightEye
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.previousStateLeftEye
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.previousStateRightEye
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber
import java.io.IOException
import kotlin.math.abs

//const val slow = 1L
//const val speedUnit = 1L
//const val fastest = 100L

class FramesMeasureViewModel @AssistedInject constructor(
    @Assisted initialState: FramesMeasureState,
    private val animationParametersFactory: AnimationParametersFactory,
): MavericksViewModel<FramesMeasureState>(initialState) {
    private val animationParameters = animationParametersFactory.makeMeasuringParameters(
        coroutineScope = viewModelScope,
        viewModel = this@FramesMeasureViewModel,
    )

    init {
        setState { copy(measuringParameters = animationParameters) }

        onEach(FramesMeasureState::eye) {
            setState {
                copy(
                    positioning = positioning
                        .copy(eye = eye)
                        .updateInitialPositioningState(),
                )
            }
        }
    }


    val lastTime: Long = 0L
    val timeThreshold: Long = 2000000L
    private fun updateSpeedMovement() = setState {
        val speed = if (System.nanoTime() - lastTime < timeThreshold && currentSpeed < fastest) {
            (currentSpeed + speedUnit).coerceAtMost(fastest)
        } else if (System.nanoTime() - lastTime >= timeThreshold) {
            (currentSpeed - speedUnit).coerceAtLeast(slow)
        } else {
            currentSpeed
        }

        copy(
            currentSpeed = speed,
        )
    }

    private fun animationParameterFromState(animationState: PositioningAnimationState): Parameter? {
        return when (animationState) {
            PositioningAnimationState.DrawAll,
            PositioningAnimationState.Idle ->
                null

            PositioningAnimationState.PositioningOpticCenter,
            PositioningAnimationState.PositioningDiameter ->
                Parameter.OpticCenter

            PositioningAnimationState.PositioningBottomPointSize,
            PositioningAnimationState.PositioningBottomPointAngle ->
                Parameter.AngleBottom

            PositioningAnimationState.PositioningTopPointAngle,
            PositioningAnimationState.PositioningTopPointSize ->
                Parameter.AngleTop

            PositioningAnimationState.PositioningBridgePivot ->
                Parameter.FramesBridgePivot

            PositioningAnimationState.PositioningCheckBottom ->
                Parameter.CheckBottom

            PositioningAnimationState.PositioningCheckMiddle ->
                Parameter.CheckMiddle

            PositioningAnimationState.PositioningCheckLeft ->
                Parameter.CheckLeft

            PositioningAnimationState.PositioningCheckRight ->
                Parameter.CheckRight

            PositioningAnimationState.PositioningCheckTop ->
                Parameter.CheckTop

            PositioningAnimationState.PositioningFramesBottom ->
                Parameter.FramesBottom

            PositioningAnimationState.PositioningFramesLeft ->
                Parameter.FramesLeft

            PositioningAnimationState.PositioningFramesRight ->
                Parameter.FramesRight

            PositioningAnimationState.PositioningFramesTop ->
                Parameter.FramesTop

            PositioningAnimationState.PositioningBaseLeft ->
                Parameter.BaseLeft

            PositioningAnimationState.PositioningBaseRight ->
                Parameter.BaseRight

            PositioningAnimationState.CheckError ->
                Parameter.CheckMiddle
        }
    }

    private fun handleDrawAll(
        state: FramesMeasureState,
        
        from: PositioningAnimationState,
        to: PositioningAnimationState
    ): FramesMeasureState {
        val intermediateState = state.copy()
        
        if (from == PositioningAnimationState.DrawAll) {
            val parameters = intermediateState.measuringParameters

            for (parameter in parameters.values) {
                parameter.isVisible = false
                parameter.isActive = false
            }
        }

        if (to == PositioningAnimationState.DrawAll) {
            val parameters = intermediateState.measuringParameters

            for (parameter in parameters.values) {
                parameter.isVisible = true
                parameter.isActive = true
            }
        }

        return intermediateState
    }

    private fun checkBridgeHelper(
        state: FramesMeasureState,
        
        parameter: Parameter?,
    ): FramesMeasureState {
        val isBridgeHelperActive = Parameter.CheckMiddle == parameter
        val intermediateState = state.copy()

        intermediateState.measuringParameters[Parameter.FramesBridgeHelper]?.isActive =
            isBridgeHelperActive
        intermediateState.measuringParameters[Parameter.FramesBridgeHelper]?.isVisible =
            isBridgeHelperActive
        
        return intermediateState
    }
    
    private fun handleAnimationChange(
        state: FramesMeasureState,
        
        from: PositioningAnimationState,
        to: PositioningAnimationState
    ): FramesMeasureState {
        var intermediateState = state.copy()
        
        val eye = state.eye
        val framesRight = state.positioning.framesRight
        val framesLeft = state.positioning.framesLeft
        val bridgePivot = state.positioning.bridgePivot
        val bridgeHelper = (if (eye == Eye.Right) framesRight else bridgePivot) +
                abs(bridgePivot - (if (eye == Eye.Right) framesRight else framesLeft)) / 2.0

        if (from == PositioningAnimationState.PositioningBottomPointAngle ||
            to == PositioningAnimationState.PositioningBottomPointAngle
        ) {
            intermediateState = state.copy(isPointBottomAttached = false)
        }

        if (from == PositioningAnimationState.PositioningTopPointAngle ||
            to == PositioningAnimationState.PositioningTopPointAngle
        ) {
            intermediateState = state.copy(isPointTopAttached = false)
        }

        if ((state.eye == Eye.Left && to == PositioningAnimationState.PositioningFramesRight) ||
            (state.eye == Eye.Right && to == PositioningAnimationState.PositioningFramesLeft)
        ) {
            intermediateState = state.copy(isOuterFramesAttached = false)
        }

        if ((state.eye == Eye.Right && to == PositioningAnimationState.PositioningFramesRight) ||
            (state.eye == Eye.Left && to == PositioningAnimationState.PositioningFramesLeft)
        ) {
            intermediateState = state.copy(isInnerFramesAttached = false)
        }

        if (to == PositioningAnimationState.PositioningCheckTop) {
            intermediateState = state.copy(isCheckTopAttached = false)
        }

        if (to == PositioningAnimationState.PositioningCheckBottom) {
            intermediateState = state.copy(isCheckBottomAttached = false)
        }

        if (to == PositioningAnimationState.PositioningFramesTop) {
            intermediateState = state.copy(isFramesTopAttached = false)
        }

        if (to == PositioningAnimationState.PositioningFramesBottom) {
            intermediateState = state.copy(isFramesBottomAttached = false)
        }

        if (to == PositioningAnimationState.PositioningBridgePivot) {
            intermediateState = state.copy(isBridgeAttached = false)
        }

        if (to == PositioningAnimationState.PositioningBaseLeft) {
            intermediateState = state.copy(isBaseLeftAttached = false)
        }

        if (to == PositioningAnimationState.PositioningBaseRight) {
            intermediateState = state.copy(isBaseRightAttached = false)
        }

        if (to == PositioningAnimationState.PositioningCheckLeft) {
            intermediateState = state.copy(isCheckLeftAttached = false)
        }

        if (to == PositioningAnimationState.PositioningCheckRight) {
            intermediateState = state.copy(isCheckRightAttached = false)
        }

        if (to == PositioningAnimationState.PositioningCheckMiddle) {
            intermediateState = state.copy(
                positioning = state.positioning.copy(
                    checkMiddle = bridgeHelper + (if (state.eye == Eye.Left) 7 else -7)
                ),
                isCheckMiddleAttached = false
            )
        }
        
        return intermediateState
    }

    fun updateEye(eye: Eye) = setState {
        copy(eye = eye)
    }

    fun onImageCaptured(uri: Uri, rotation: Double, deviceInfo: String) = setState {
        Timber.i("Updating state with uri $uri")

        copy(
            picture = uri,
            cameraSetUpState = CameraSetUpState.Idle,

            positioning = positioning.copy(
                picture = uri.path ?: "",
                rotation = rotation,
                device = deviceInfo,
            ),

            latestDeviceRotation = rotation,
        )
    }

    fun onTakingPictureHelperClicked() = setState {
        copy(
            takingHeadZoomState = when (takingHeadZoomState) {
                HelperZoomState.ZoomNormal -> HelperZoomState.ZoomOut
                HelperZoomState.ZoomOut -> HelperZoomState.ZoomNormal
            }
        )
    }

    fun onHeadTaken(
        faces: Int,
        eulerX: Double,
        eulerY: Double,
        eulerZ: Double,
    ) = setState {
        copy(
            facesFound = faces,
            latestEulerX = eulerX,
            latestEulerY = eulerY,
            latestEulerZ = eulerZ,
        )
    }

    fun onAcceptInvalidCheckMiddle() {
        onPreviousState()
    }
    
    fun onNextState() = setState {
        if (isCheckMiddleInvalid) {
            copy(positioningAnimationState = PositioningAnimationState.CheckError)
        } else {
            val previousAnimationState = positioningAnimationState
            val nextAnimationState = when (eye) {
                Eye.Left -> nextStateEyeLeft(positioningAnimationState)
                else -> nextStateRightEye(positioningAnimationState)
            }

            val previousParameter = movableParameter
            val nextParameter = animationParameterFromState(nextAnimationState)

            var intermediateState = copy(
                positioningAnimationState = nextAnimationState,
                movableParameter = nextParameter,
            )

            intermediateState.measuringParameters[previousParameter]?.isVisible = false
            intermediateState.measuringParameters[previousParameter]?.isActive = false

            intermediateState = handleDrawAll(
                intermediateState,

                from = previousAnimationState,
                to = nextAnimationState,
            )

            intermediateState.measuringParameters[nextParameter]?.isVisible = true
            intermediateState.measuringParameters[nextParameter]?.isActive = true

            intermediateState = checkBridgeHelper(intermediateState, nextParameter)

            handleAnimationChange(
                intermediateState,

                from = previousAnimationState,
                to = nextAnimationState,
            )

            intermediateState
        }
    }

    fun onPreviousState() = setState {
        val previousAnimationState = positioningAnimationState
        val nextAnimationState = when (eye) {
            Eye.Left -> previousStateLeftEye(positioningAnimationState)
            else -> previousStateRightEye(positioningAnimationState)
        }

        val previousParameter = movableParameter
        val nextParameter = animationParameterFromState(nextAnimationState)

        var intermediateState = copy(
            positioningAnimationState = nextAnimationState,
            movableParameter = nextParameter,
        )

        intermediateState.measuringParameters[previousParameter]?.isVisible = false
        intermediateState.measuringParameters[previousParameter]?.isActive = false

        intermediateState = handleDrawAll(
            intermediateState,

            from = previousAnimationState,
            to = nextAnimationState,
        )

        intermediateState.measuringParameters[nextParameter]?.isVisible = true
        intermediateState.measuringParameters[nextParameter]?.isActive = true

        intermediateState = checkBridgeHelper(intermediateState, nextParameter)

        intermediateState = handleAnimationChange(
            intermediateState,

            from = previousAnimationState,
            to = nextAnimationState,
        )

        intermediateState
    }

    fun onMoveUp() = setState {
        val params = measuringParameters
        params[movableParameter]?.moveUp()

        copy(measuringParameters = params)
    }

    fun onMoveDown() = setState {
        val params = measuringParameters
        params[movableParameter]?.moveDown()

        copy(measuringParameters = params)
    }

    fun onMoveLeft() = setState {
        val params = measuringParameters
        params[movableParameter]?.moveLeft()

        copy(measuringParameters = params)
    }

    fun onMoveRight() = setState {
        val params = measuringParameters
        params[movableParameter]?.moveRight()

        copy(measuringParameters = params)
    }

    fun onExpand() = setState {
        val params = measuringParameters
        params[movableParameter]?.expand()

        Timber.i("Current param: ${params[movableParameter]}")

        copy(measuringParameters = params)
    }

    fun onShrink() = setState {
        val params = measuringParameters
        params[movableParameter]?.shrink()

        copy(measuringParameters = params)
    }

    fun onRotateLeft() = setState {
        val params = measuringParameters
        params[movableParameter]?.rotateLeft()

        copy(measuringParameters = params)
    }

    fun onRotateRight() = setState {
        val params = measuringParameters
        params[movableParameter]?.rotateRight()

        copy(measuringParameters = params)
    }

    fun onCancelMeasure() = setState {
        val intermediateState = copy(
            positioningAnimationState = PositioningAnimationState.Idle,
            movableParameter = animationParameterFromState(PositioningAnimationState.Idle),

            positioning = Positioning(eye = eye),

            isPointTopAttached = true,
            isPointBottomAttached = true,

            isOuterFramesAttached = true,
            isInnerFramesAttached = true,
            isBridgeAttached = true,

            isBaseLeftAttached = true,
            isBaseRightAttached = true,
            isCheckLeftAttached = true,
            isCheckRightAttached = true,

            isCheckMiddleAttached = true,

            isCheckTopAttached = true,
            isCheckBottomAttached = true,
            isFramesTopAttached = true,
            isFramesBottomAttached = true,
        )

        val parameters = intermediateState.measuringParameters
        for (parameter in parameters.values) {
            parameter.isActive = false
            parameter.isVisible = false
        }

        try {
            val file = picture.toFile()

            if (file.exists()) {
                file.delete()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        intermediateState
    }

    fun onMeasuringHelperClicked() = setState {
        copy(
            zoomHelperState = when (zoomHelperState) {
                HelperZoomState.ZoomNormal -> HelperZoomState.ZoomOut
                HelperZoomState.ZoomOut -> HelperZoomState.ZoomNormal
            }
        )
    }

    fun moveOpticCenterLeft() = setState {
        updateSpeedMovement()

        val opticCenterX = positioning.opticCenterX - currentSpeed
        val framesRight = positioning.framesRight
        val framesLeft = positioning.framesLeft

        val innerFramesDistance = 180f

        val positioning = positioning.copy(opticCenterX = opticCenterX)

        if (eye == Eye.Right) {
            copy(
                positioning = positioning.copy(
                    framesRight = if (isInnerFramesAttached)
                        opticCenterX + innerFramesDistance
                    else
                        framesRight
                )
            )
        } else {
            copy(
                positioning = positioning.copy(
                    framesLeft = if (isInnerFramesAttached)
                        opticCenterX - innerFramesDistance
                    else
                        framesLeft
                )
            )
        }
    }

    fun moveOpticCenterRight() = setState {
        updateSpeedMovement()

        val opticCenterX = positioning.opticCenterX + currentSpeed
        val framesRight = positioning.framesRight
        val framesLeft = positioning.framesLeft

        val innerFramesDistance = 180f

        val positioning = positioning.copy(opticCenterX = opticCenterX)

        if (eye == Eye.Right) {
            copy(
                positioning = positioning.copy(
                    framesRight = if (isInnerFramesAttached) {
                        opticCenterX + innerFramesDistance
                    } else {
                        framesRight
                    }
                )
            )
        } else {
            copy(
                positioning = positioning.copy(
                    framesLeft = if (isInnerFramesAttached) {
                        opticCenterX - innerFramesDistance
                    } else {
                        framesLeft
                    }
                )
            )
        }
    }

    fun moveOpticCenterUp() = setState {
        updateSpeedMovement()

        val opticCenterY = positioning.opticCenterY - currentSpeed
        val checkTop = positioning.checkTop
        val framesBottom = positioning.framesBottom

        val checkTopDistance = -98f
        val framesBottomDistance = 175f

        copy(
            positioning = positioning.copy(
                opticCenterY = opticCenterY,

                checkTop =
                if (isCheckTopAttached) opticCenterY + checkTopDistance else checkTop,
                framesBottom =
                if (isFramesBottomAttached) opticCenterY + framesBottomDistance else framesBottom,
            )
        )
    }

    fun moveOpticCenterDown() = setState {
        updateSpeedMovement()

        val opticCenterY = positioning.opticCenterY + currentSpeed
        val checkTop = positioning.checkTop
        val framesBottom = positioning.framesBottom

        val checkTopDistance = -98f
        val framesBottomDistance = 175f

        copy(
            positioning = positioning.copy(
                opticCenterY = opticCenterY,

                checkTop =
                if (isCheckTopAttached) opticCenterY + checkTopDistance else checkTop,
                framesBottom =
                if (isFramesBottomAttached) opticCenterY + framesBottomDistance else framesBottom,
            )
        )
    }

    fun moveOpticCenterExpand() = setState {
        updateSpeedMovement()

        val opticCenterX = positioning.opticCenterX
        val radius = positioning.opticCenterRadius + currentSpeed
        val topLength = positioning.topPointLength
        val bottomLength = positioning.bottomPointLength
        val framesRight = positioning.framesRight
        val framesLeft = positioning.framesLeft

        var positioning = positioning.copy(
            opticCenterRadius = radius,
            topPointLength = if (isPointTopAttached) radius else topLength,
            bottomPointLength = if (isPointBottomAttached) radius else bottomLength,
        )

        positioning = if (eye == Eye.Left) {
            positioning.copy(
                framesRight = if (isOuterFramesAttached)
                    opticCenterX + radius
                else
                    framesRight,
            )
        } else {
            positioning.copy(
                framesLeft = if (isOuterFramesAttached)
                    opticCenterX - radius
                else
                    framesLeft,
            )
        }

        copy(positioning = positioning)
    }

    fun moveOpticCenterShrink() = setState {
        updateSpeedMovement()

        val opticCenterX = positioning.opticCenterX
        val radius = positioning.opticCenterRadius - currentSpeed
        val topLength = positioning.topPointLength
        val bottomLength = positioning.bottomPointLength
        val framesRight = positioning.framesRight
        val framesLeft = positioning.framesLeft

        var positioning = positioning.copy(
            opticCenterRadius = radius,
            topPointLength = if (isPointTopAttached) radius else topLength,
            bottomPointLength = if (isPointBottomAttached) radius else bottomLength,
        )

        positioning = if (eye == Eye.Left) {
            positioning.copy(
                framesRight = if (isOuterFramesAttached) opticCenterX + radius else framesRight,
            )
        } else {
            positioning.copy(
                framesLeft = if (isOuterFramesAttached) opticCenterX - radius else framesLeft,
            )
        }

        copy(positioning = positioning)
    }

    fun moveFramesRightRight() = setState {
        updateSpeedMovement()

        val isAttached = eye == Eye.Right && isBridgeAttached
        val framesRight = positioning.framesRight + currentSpeed
        val bridgePivot = positioning.bridgePivot

        val bridgeDistance = 160

        copy(
            positioning = positioning.copy(
                framesRight = framesRight,

                bridgePivot = if (isAttached) framesRight + bridgeDistance else bridgePivot
            )
        )
    }

    fun moveFramesRightLeft() = setState {
        updateSpeedMovement()

        val isAttached = eye == Eye.Right && isBridgeAttached
        val framesRight = positioning.framesRight - currentSpeed
        val bridgePivot = positioning.bridgePivot

        val bridgeDistance = 160

        copy(
            positioning = positioning.copy(
                framesRight = framesRight,

                bridgePivot = if (isAttached) framesRight + bridgeDistance else bridgePivot
            )
        )
    }

    fun moveFramesLeftRight() = setState {
        updateSpeedMovement()

        val isAttached = eye == Eye.Left && isBridgeAttached
        val framesLeft = positioning.framesLeft + currentSpeed
        val bridgePivot = positioning.bridgePivot

        val bridgeDistance = 160

        copy(
            positioning = positioning.copy(
                framesLeft = framesLeft,

                bridgePivot = if (isAttached) framesLeft - bridgeDistance else bridgePivot
            )
        )
    }

    fun moveFramesLeftLeft() = setState {
        updateSpeedMovement()

        val isAttached = eye == Eye.Left && isBridgeAttached
        val framesLeft = positioning.framesLeft - currentSpeed
        val bridgePivot = positioning.bridgePivot

        val bridgeDistance = 160

        copy(
            positioning = positioning.copy(
                framesLeft = framesLeft,

                bridgePivot = if (isAttached) framesLeft - bridgeDistance else bridgePivot
            )
        )
    }

    fun moveBridgePivotRight() = setState {
        updateSpeedMovement()

        val bridgePivot = positioning.bridgePivot + currentSpeed
        val baseLeft = positioning.baseLeft

        val positioning = positioning.copy(bridgePivot = bridgePivot)

        val baseLeftDistance = if (eye == Eye.Right) 20.0 else -295.0

        copy(
            positioning = positioning.copy(
                baseLeft = if (isBaseLeftAttached) {
                    bridgePivot + baseLeftDistance
                } else {
                    baseLeft
                }
            )
        )
    }

    fun moveBridgePivotLeft() = setState {
        updateSpeedMovement()

        val bridgePivot = positioning.bridgePivot - currentSpeed
        val baseLeft = positioning.baseLeft

        val positioning = positioning.copy(bridgePivot = bridgePivot)

        val baseLeftDistance = if (eye == Eye.Right) 20.0 else -295.0

        copy(
            positioning = positioning.copy(
                baseLeft = if (isBaseLeftAttached) {
                    bridgePivot + baseLeftDistance
                } else {
                    baseLeft
                }
            )
        )
    }

    fun moveCheckMiddleRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkMiddle = positioning.checkMiddle + currentSpeed
            )
        )
    }

    fun moveCheckMiddleLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkMiddle = positioning.checkMiddle - currentSpeed
            )
        )
    }

    fun moveBaseLeftRight() = setState {
        updateSpeedMovement()

        val baseLeft = positioning.baseLeft + currentSpeed
        val checkLeft = positioning.checkLeft
        val checkRight = positioning.checkRight
        val baseRight = positioning.baseRight

        val checkLeftDistance = if (eye == Eye.Right) 100 else 70
        val checkRightDistance = if (eye == Eye.Right) 195 else 179
        val baseRightDistance = if (eye == Eye.Right) 235 else 262

        copy(
            positioning = positioning.copy(
                baseLeft = baseLeft,

                checkLeft = if (isCheckLeftAttached) baseLeft + checkLeftDistance else checkLeft,
                checkRight = if (isCheckRightAttached) baseLeft + checkRightDistance else checkRight,
                baseRight = if (isBaseRightAttached) baseLeft + baseRightDistance else baseRight,
            )
        )
    }

    fun moveBaseLeftLeft() = setState {
        updateSpeedMovement()

        val baseLeft = positioning.baseLeft - currentSpeed
        val checkLeft = positioning.checkLeft
        val checkRight = positioning.checkRight
        val baseRight = positioning.baseRight

        val checkLeftDistance = if (eye == Eye.Right) 100 else 70
        val checkRightDistance = if (eye == Eye.Right) 195 else 179
        val baseRightDistance = if (eye == Eye.Right) 235 else 262

        copy(
            positioning = positioning.copy(
                baseLeft = baseLeft,

                checkLeft = if (isCheckLeftAttached) baseLeft + checkLeftDistance else checkLeft,
                checkRight = if (isCheckRightAttached) baseLeft + checkRightDistance else checkRight,
                baseRight = if (isBaseRightAttached) baseLeft + baseRightDistance else baseRight,
            )
        )
    }

    fun rotateBaseLeftRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                baseLeftRotation = positioning.baseLeftRotation +
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateBaseLeftLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                baseLeftRotation = positioning.baseLeftRotation -
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveBaseRightRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                baseRight = positioning.baseRight + currentSpeed
            )
        )
    }

    fun moveBaseRightLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                baseRight = positioning.baseRight - currentSpeed
            )
        )
    }

    fun rotateBaseRightRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                baseRightRotation = positioning.baseRightRotation +
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateBaseRightLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                baseRightRotation = positioning.baseRightRotation -
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveCheckLeftRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkLeft = positioning.checkLeft + currentSpeed
            )
        )
    }

    fun moveCheckLeftLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkLeft = positioning.checkLeft - currentSpeed
            )
        )
    }

    fun rotateCheckLeftRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkLeftRotation = positioning.checkLeftRotation +
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateCheckLeftLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkLeftRotation = positioning.checkLeftRotation -
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveCheckRightRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkRight = positioning.checkRight + currentSpeed
            )
        )
    }

    fun moveCheckRightLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkRight = positioning.checkRight - currentSpeed
            )
        )
    }

    fun rotateCheckRightRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkRightRotation = positioning.checkRightRotation +
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateCheckRightLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkRightRotation = positioning.checkRightRotation -
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveCheckTopUp() = setState {
        updateSpeedMovement()

        val checkTop = positioning.checkTop - currentSpeed
        val framesTop = positioning.framesTop
        val checkBottom = positioning.checkBottom

        val checkBottomDistance = 90f
        val framesTopDistance = -25f

        copy(
            positioning = positioning.copy(
                checkTop = checkTop,

                checkBottom = if (isCheckBottomAttached) checkTop + checkBottomDistance else checkBottom,
                framesTop = if (isFramesTopAttached) checkTop + framesTopDistance else framesTop,
            )
        )
    }

    fun moveCheckTopDown() = setState {
        updateSpeedMovement()

        val checkTop = positioning.checkTop + currentSpeed
        val framesTop = positioning.framesTop
        val checkBottom = positioning.checkBottom

        val checkBottomDistance = 90f
        val framesTopDistance = -25f

        copy(
            positioning = positioning.copy(
                checkTop = positioning.checkTop + currentSpeed,

                checkBottom =
                if (isCheckBottomAttached) checkTop + checkBottomDistance else checkBottom,
                framesTop =
                if (isFramesTopAttached) checkTop + framesTopDistance else framesTop,
            )
        )
    }

    fun moveCheckBottomUp() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkBottom = positioning.checkBottom - currentSpeed
            )
        )
    }

    fun moveCheckBottomDown() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                checkBottom = positioning.checkBottom + currentSpeed
            )
        )
    }

    fun moveFramesTopUp() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                framesTop = positioning.framesTop - currentSpeed
            )
        )
    }

    fun moveFramesTopDown() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                framesTop = positioning.framesTop + currentSpeed
            )
        )
    }

    fun moveFramesBottomUp() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                framesBottom = positioning.framesBottom - currentSpeed
            )
        )
    }

    fun moveFramesBottomDown() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                framesBottom = positioning.framesBottom + currentSpeed
            )
        )
    }

    fun rotateTopPointRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                topPointRotation = positioning.topPointRotation +
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateTopPointLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                topPointRotation = positioning.topPointRotation -
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveTopPointExpand() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                topPointLength = positioning.topPointLength + currentSpeed
            )
        )
    }

    fun moveTopPointShrink() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                topPointLength = positioning.topPointLength - currentSpeed
            )
        )
    }

    fun rotateBottomPointRight() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                bottomPointRotation = positioning.bottomPointRotation +
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateBottomPointLeft() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                bottomPointRotation = positioning.bottomPointRotation -
                        (currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveBottomPointExpand() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                bottomPointLength = positioning.bottomPointLength + currentSpeed
            )
        )
    }

    fun moveBottomPointShrink() = setState {
        updateSpeedMovement()

        copy(
            positioning = positioning.copy(
                bottomPointLength = positioning.bottomPointLength - currentSpeed
            )
        )
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<FramesMeasureViewModel, FramesMeasureState> {
        override fun create(state: FramesMeasureState): FramesMeasureViewModel
    }

    companion object: MavericksViewModelFactory<FramesMeasureViewModel, FramesMeasureState>
    by hiltMavericksViewModelFactory()
}