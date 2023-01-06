package com.peyess.salesapp.feature.sale.frames_measure.state

import android.net.Uri
import androidx.core.net.toFile
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.frames_measure.updateInitialPositioningState
import com.peyess.salesapp.dao.sale.frames_measure.updateProportion
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.AnimationParametersFactory
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.PositioningAnimationState
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.nextStateEyeLeft
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.nextStateRightEye
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.previousStateLeftEye
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.previousStateRightEye
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import timber.log.Timber
import java.io.File
import java.io.IOException
import kotlin.math.abs

//const val slow = 1L
//const val speedUnit = 1L
//const val fastest = 100L

class FramesMeasureViewModel @AssistedInject constructor(
    @Assisted initialState: FramesMeasureState,
    animationParametersFactory: AnimationParametersFactory,
    private val saleRepository: SaleRepository,
): MavericksViewModel<FramesMeasureState>(initialState) {
    private val animationParameters = animationParametersFactory.makeMeasuringParameters(
        coroutineScope = viewModelScope,
        viewModel = this@FramesMeasureViewModel,
    )

    private val jobs: MutableList<Job> = mutableListOf()

    private val timeThreshold = 2000000
    private val checkMiddleThreshold = 7

    init {
        setState { copy(measuringParameters = animationParameters) }

        onEach(FramesMeasureState::eye) {
            Timber.i("Measuring for eye $it")

            for (job in jobs) {
                Timber.i("Trying to cancel job at $job")

                if (job.isActive) {
                    Timber.i("Cancelling job at $job")
                    job.cancel()
                }
            }

            jobs.add(
                saleRepository.currentPositioning(it)
                    .execute { positioning ->
                        Timber.i("Current positioning: $positioning")
                        copy(positioningAsync = positioning)
                    }
            )
        }
    }

    private fun updateSpeedMovement() = withState {
        // https://github.com/airbnb/mavericks/issues/381
        val firstTouch = it.firstTouch
        val secondTouch = System.nanoTime()

        Timber.i("Current diff: ${(secondTouch - firstTouch)}")
        Timber.i("Current diff as Int: ${(secondTouch - firstTouch).toInt()}")
        val speed = if ((secondTouch - firstTouch) < timeThreshold) {
            (it.currentSpeed + speedUnit).coerceAtMost(fastest)
        } else if ((secondTouch - firstTouch) >= timeThreshold) {
            (it.currentSpeed - speedUnit).coerceAtLeast(slow)
        } else {
            it.currentSpeed
        }

        setState {
            copy(
                currentSpeed = speed,
                firstTouch = secondTouch,
            )
        }
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

            PositioningAnimationState.PositioningBaseTop ->
                Parameter.BaseTop

            PositioningAnimationState.PositioningBaseBottom ->
                Parameter.BaseBottom

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
            intermediateState = state.copy(isCheckMiddleAttached = false)

            saleRepository.updatePositioning(
                state.positioning.copy(
                    checkMiddle = bridgeHelper + (if (state.eye == Eye.Left) 7 else -7)
                )
            )
        }
        
        return intermediateState
    }

    private fun isCheckMiddleInvalid(positioning: PositioningEntity, eye: Eye): Boolean {
        val framesRight = positioning.framesRight
        val framesLeft = positioning.framesLeft
        val bridgePivot = positioning.bridgePivot
        val bridgeHelper = (if (eye == Eye.Right) framesRight else bridgePivot) +
                abs(bridgePivot - (if (eye == Eye.Right) framesRight else framesLeft)) / 2.0

        return abs(positioning.checkMiddle - bridgeHelper) > checkMiddleThreshold
    }

    fun onFinishMeasure() = setState {
        val positioning = positioning
        saleRepository.updatePositioning(positioning.updateProportion())

        val parameters = copy().measuringParameters
        for (parameter in parameters.values) {
            parameter.isActive = false
            parameter.isVisible = false
        }

        copy(
            positioningAnimationState = PositioningAnimationState.Idle,
            movableParameter = animationParameterFromState(PositioningAnimationState.Idle),

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

            isCheckMiddleInvalid = false,
            measuringParameters = parameters,

            isMeasuringDone = true,
        )
    }

    fun updateEye(eye: Eye) = setState {
        copy(eye = eye)
    }

    fun onImageCaptured(uri: Uri, rotation: Double, deviceInfo: String) = setState {
        saleRepository.updatePositioning(
            positioning.copy(
                picture = uri,
                rotation = rotation,
                device = deviceInfo,
            )
        )

        copy(
            cameraSetUpState = CameraSetUpState.Idle,
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
        saleRepository.updatePositioning(
            positioning.copy(
                eulerAngleX = eulerX,
                eulerAngleY = eulerY,
                eulerAngleZ = eulerZ,
            )
        )

        copy(
            facesFound = faces,
            latestEulerX = eulerX,
            latestEulerY = eulerY,
            latestEulerZ = eulerZ,
        )
    }

    fun onBackFromCheckMiddle() = setState {
        copy(
            positioningAnimationState = PositioningAnimationState.PositioningCheckMiddle,
            isCheckMiddleInvalid = false,
        )
    }

    fun onNextState() = setState {
        val previousAnimationState = positioningAnimationState
        val nextAnimationState = when (eye) {
            Eye.Left -> nextStateEyeLeft(positioningAnimationState)
            else -> nextStateRightEye(positioningAnimationState)
        }

        val previousParameter = movableParameter
        val nextParameter = animationParameterFromState(nextAnimationState)

        if (previousParameter is Parameter.CheckMiddle && isCheckMiddleInvalid(positioning, eye)) {
            copy(
                positioningAnimationState = PositioningAnimationState.CheckError,
                isCheckMiddleInvalid = true
            )
        } else {
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
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.moveUp()

            copy(measuringParameters = params)
        }
    }

    fun onMoveDown() = setState {
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.moveDown()

            copy(measuringParameters = params)
        }
    }

    fun onMoveLeft() = setState {
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.moveLeft()

            Timber.i("Moving parameter left: ${params[movableParameter]}")

            copy(measuringParameters = params)
        }
    }

    fun onMoveRight() = setState {
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.moveRight()

            Timber.i("Moving parameter right: ${params[movableParameter]}")

            copy(measuringParameters = params)
        }
    }

    fun onExpand() = setState {
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.expand()

            copy(measuringParameters = params)
        }
    }

    fun onShrink() = setState {
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.shrink()

            copy(measuringParameters = params)
        }
    }

    fun onRotateLeft() = setState {
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.rotateLeft()

            copy(measuringParameters = params)
        }
    }

    fun onRotateRight() = setState {
        if (isCheckMiddleInvalid) {
            copy()
        } else {
            val params = measuringParameters
            params[movableParameter]?.rotateRight()

            copy(measuringParameters = params)
        }
    }

    fun onCancelMeasure() = setState {
        val intermediateState = copy(
            positioningAnimationState = PositioningAnimationState.Idle,
            movableParameter = animationParameterFromState(PositioningAnimationState.Idle),

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

            isCheckMiddleInvalid = false,
        )

        val parameters = intermediateState.measuringParameters
        for (parameter in parameters.values) {
            parameter.isActive = false
            parameter.isVisible = false
        }

        // TODO: move this task to a worker
        try {
            val file = try {
                positioning.picture.toFile()
            } catch (e: Exception) {
                Timber.e("Failed to get file from picture: ${positioning.picture}", e)
                null
            }

            if (file?.exists() == true) {
                file.delete()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        saleRepository.updatePositioning(
            positioning.updateInitialPositioningState()
        )

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

    fun moveOpticCenterLeft() = withState {
        updateSpeedMovement()

        val opticCenterX = it.positioning.opticCenterX - it.currentSpeed
        val framesRight = it.positioning.framesRight
        val framesLeft = it.positioning.framesLeft

        val innerFramesDistance = 180f

        val positioning = it.positioning.copy(opticCenterX = opticCenterX)
        if (it.eye == Eye.Right) {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesRight = if (it.isInnerFramesAttached)
                        opticCenterX + innerFramesDistance
                    else
                        framesRight
                )
            )
        } else {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesLeft = if (it.isInnerFramesAttached)
                        opticCenterX - innerFramesDistance
                    else
                        framesLeft
                )
            )
        }
    }

    fun moveOpticCenterRight() = withState {
        updateSpeedMovement()

        val opticCenterX = it.positioning.opticCenterX + it.currentSpeed
        val framesRight = it.positioning.framesRight
        val framesLeft = it.positioning.framesLeft

        val innerFramesDistance = 180f

        val positioning = it.positioning.copy(opticCenterX = opticCenterX)

        if (it.eye == Eye.Right) {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesRight = if (it.isInnerFramesAttached) {
                        opticCenterX + innerFramesDistance
                    } else {
                        framesRight
                    }
                )
            )
        } else {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesLeft = if (it.isInnerFramesAttached) {
                        opticCenterX - innerFramesDistance
                    } else {
                        framesLeft
                    }
                )
            )
        }
    }

    fun moveOpticCenterUp() = withState {
        updateSpeedMovement()

        val opticCenterY = it.positioning.opticCenterY - it.currentSpeed
        val checkTop = it.positioning.checkTop
        val framesBottom = it.positioning.framesBottom

        val checkTopDistance = -98f
        val framesBottomDistance = 175f

        saleRepository.updatePositioning(
            it.positioning.copy(
                opticCenterY = opticCenterY,

                checkTop = if (it.isCheckTopAttached) {
                    opticCenterY + checkTopDistance
                } else {
                    checkTop
                },
                framesBottom = if (it.isFramesBottomAttached) {
                    opticCenterY + framesBottomDistance
                }
                else {
                    framesBottom
                },
            )
        )
    }

    fun moveOpticCenterDown() = withState {
        updateSpeedMovement()

        val opticCenterY = it.positioning.opticCenterY + it.currentSpeed
        val checkTop = it.positioning.checkTop
        val framesBottom = it.positioning.framesBottom

        val checkTopDistance = -98f
        val framesBottomDistance = 175f

        saleRepository.updatePositioning(
            it.positioning.copy(
                opticCenterY = opticCenterY,

                checkTop = if (it.isCheckTopAttached) {
                    opticCenterY + checkTopDistance
                } else {
                    checkTop
                },
                framesBottom = if (it.isFramesBottomAttached) {
                    opticCenterY + framesBottomDistance
                } else {
                    framesBottom
                },
            )
        )
    }

    fun moveOpticCenterExpand() = withState {
        updateSpeedMovement()

        val opticCenterX = it.positioning.opticCenterX
        val radius = it.positioning.opticCenterRadius + it.currentSpeed
        val topLength = it.positioning.topPointLength
        val bottomLength = it.positioning.bottomPointLength
        val framesRight = it.positioning.framesRight
        val framesLeft = it.positioning.framesLeft

        val positioning = it.positioning.copy(
            opticCenterRadius = radius,
            topPointLength = if (it.isPointTopAttached) radius else topLength,
            bottomPointLength = if (it.isPointBottomAttached) radius else bottomLength,
        )

        if (it.eye == Eye.Left) {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesRight = if (it.isOuterFramesAttached)
                        opticCenterX + radius
                    else
                        framesRight,
                )
            )
        } else {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesLeft = if (it.isOuterFramesAttached)
                        opticCenterX - radius
                    else
                        framesLeft,
                )
            )
        }
    }

    fun moveOpticCenterShrink() = withState {
        updateSpeedMovement()

        val opticCenterX = it.positioning.opticCenterX
        val radius = it.positioning.opticCenterRadius - it.currentSpeed
        val topLength = it.positioning.topPointLength
        val bottomLength = it.positioning.bottomPointLength
        val framesRight = it.positioning.framesRight
        val framesLeft = it.positioning.framesLeft

        val positioning = it.positioning.copy(
            opticCenterRadius = radius,
            topPointLength = if (it.isPointTopAttached) radius else topLength,
            bottomPointLength = if (it.isPointBottomAttached) radius else bottomLength,
        )

        if (it.eye == Eye.Left) {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesRight = if (it.isOuterFramesAttached) opticCenterX + radius else framesRight,
                )
            )
        } else {
            saleRepository.updatePositioning(
                positioning.copy(
                    framesLeft = if (it.isOuterFramesAttached) opticCenterX - radius else framesLeft,
                )
            )
        }
    }

    fun moveFramesRightRight() = withState {
        updateSpeedMovement()

        val isAttached = it.eye == Eye.Right && it.isBridgeAttached
        val framesRight = it.positioning.framesRight + it.currentSpeed
        val bridgePivot = it.positioning.bridgePivot

        // TODO: Remove magic number from here
        val bridgeDistance = 160

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesRight = framesRight,
                bridgePivot = if (isAttached) framesRight + bridgeDistance else bridgePivot
            )
        )
    }

    fun moveFramesRightLeft() = withState {
        updateSpeedMovement()

        val isAttached = it.eye == Eye.Right && it.isBridgeAttached
        val framesRight = it.positioning.framesRight - it.currentSpeed
        val bridgePivot = it.positioning.bridgePivot

        // TODO: Remove magic number from here
        val bridgeDistance = 160

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesRight = framesRight,

                bridgePivot = if (isAttached) framesRight + bridgeDistance else bridgePivot
            )
        )
    }

    fun moveFramesLeftRight() = withState {
        updateSpeedMovement()

        val isAttached = it.eye == Eye.Left && it.isBridgeAttached
        val framesLeft = it.positioning.framesLeft + it.currentSpeed
        val bridgePivot = it.positioning.bridgePivot

        // TODO: Remove magic number from here
        val bridgeDistance = 160

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesLeft = framesLeft,

                bridgePivot = if (isAttached) framesLeft - bridgeDistance else bridgePivot
            )
        )
    }

    fun moveFramesLeftLeft() = withState {
        updateSpeedMovement()

        val isAttached = it.eye == Eye.Left && it.isBridgeAttached
        val framesLeft = it.positioning.framesLeft - it.currentSpeed
        val bridgePivot = it.positioning.bridgePivot

        // TODO: Remove magic number from here
        val bridgeDistance = 160

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesLeft = framesLeft,

                bridgePivot = if (isAttached) framesLeft - bridgeDistance else bridgePivot
            )
        )
    }

    fun moveBridgePivotRight() = withState {
        updateSpeedMovement()

        val bridgePivot = it.positioning.bridgePivot + it.currentSpeed
        val baseLeft = it.positioning.baseLeft

        val positioning = it.positioning.copy(bridgePivot = bridgePivot)

        // TODO: Remove magic number from here
        val baseLeftDistance = if (it.eye == Eye.Right) 20.0 else -295.0

        saleRepository.updatePositioning(
            positioning.copy(
                baseLeft = if (it.isBaseLeftAttached) {
                    bridgePivot + baseLeftDistance
                } else {
                    baseLeft
                }
            )
        )
    }

    fun moveBridgePivotLeft() = withState {
        updateSpeedMovement()

        val bridgePivot = it.positioning.bridgePivot - it.currentSpeed
        val baseLeft = it.positioning.baseLeft

        val positioning = it.positioning.copy(bridgePivot = bridgePivot)

        // TODO: Remove magic number from here
        val baseLeftDistance = if (it.eye == Eye.Right) 20.0 else -295.0

        saleRepository.updatePositioning(
            positioning.copy(
                baseLeft = if (it.isBaseLeftAttached) {
                    bridgePivot + baseLeftDistance
                } else {
                    baseLeft
                }
            )
        )
    }

    fun moveCheckMiddleRight() = withState {
        updateSpeedMovement()

        Timber.i("Moving check middle right to ${it.positioning.checkMiddle + it.currentSpeed}")
        saleRepository.updatePositioning(
            it.positioning.copy(
                checkMiddle = it.positioning.checkMiddle + it.currentSpeed
            )
        )
    }

    fun moveCheckMiddleLeft() = withState {
        updateSpeedMovement()

        Timber.i("Moving check middle left to ${it.positioning.checkMiddle - it.currentSpeed}")
        saleRepository.updatePositioning(
            it.positioning.copy(
                checkMiddle = it.positioning.checkMiddle - it.currentSpeed
            )
        )
    }

    fun moveBaseLeftRight() = withState {
        updateSpeedMovement()

        val baseLeft = it.positioning.baseLeft + it.currentSpeed
        val checkLeft = it.positioning.checkLeft
        val checkRight = it.positioning.checkRight
        val baseRight = it.positioning.baseRight

        // TODO: Remove magic numbers from here
        val checkLeftDistance = if (it.eye == Eye.Right) 100 else 70
        val checkRightDistance = if (it.eye == Eye.Right) 195 else 179
        val baseRightDistance = if (it.eye == Eye.Right) 235 else 262

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseLeft = baseLeft,

                checkLeft = if (it.isCheckLeftAttached) baseLeft + checkLeftDistance else checkLeft,
                checkRight = if (it.isCheckRightAttached) baseLeft + checkRightDistance else checkRight,
                baseRight = if (it.isBaseRightAttached) baseLeft + baseRightDistance else baseRight,
            )
        )
    }

    fun moveBaseLeftLeft() = withState {
        updateSpeedMovement()

        val baseLeft = it.positioning.baseLeft - it.currentSpeed
        val checkLeft = it.positioning.checkLeft
        val checkRight = it.positioning.checkRight
        val baseRight = it.positioning.baseRight

        // TODO: Remove magic numbers from here
        val checkLeftDistance = if (it.eye == Eye.Right) 100 else 70
        val checkRightDistance = if (it.eye == Eye.Right) 195 else 179
        val baseRightDistance = if (it.eye == Eye.Right) 235 else 262

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseLeft = baseLeft,

                checkLeft = if (it.isCheckLeftAttached) baseLeft + checkLeftDistance else checkLeft,
                checkRight = if (it.isCheckRightAttached) baseLeft + checkRightDistance else checkRight,
                baseRight = if (it.isBaseRightAttached) baseLeft + baseRightDistance else baseRight,
            )
        )
    }

    fun rotateBaseLeftRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseLeftRotation = it.positioning.baseLeftRotation +
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateBaseLeftLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseLeftRotation = it.positioning.baseLeftRotation -
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveBaseRightRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseRight = it.positioning.baseRight + it.currentSpeed
            )
        )
    }

    fun moveBaseRightLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseRight = it.positioning.baseRight - it.currentSpeed
            )
        )
    }

    fun rotateBaseRightRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseRightRotation = it.positioning.baseRightRotation +
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateBaseRightLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseRightRotation = it.positioning.baseRightRotation -
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveBaseTopUp() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseTop = it.positioning.baseTop - it.currentSpeed
            )
        )
//        viewState = viewState.copy(
//            positioning = viewState.positioning.copy(
//                baseTop = viewState.positioning.baseTop - currentSpeed
//            )
//        )
    }

    fun moveBaseTopDown() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseTop = it.positioning.baseTop + it.currentSpeed
            )
        )
//        viewState = viewState.copy(
//            positioning = viewState.positioning.copy(
//                baseTop = viewState.positioning.baseTop + currentSpeed
//            )
//        )
    }

    fun moveBaseBottomUp() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseBottom = it.positioning.baseBottom - it.currentSpeed
            )
        )
//        viewState = viewState.copy(
//            positioning = viewState.positioning.copy(
//                baseBottom = viewState.positioning.baseBottom - currentSpeed
//            )
//        )
    }

    fun moveBaseBottomDown() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                baseBottom = it.positioning.baseBottom + it.currentSpeed
            )
        )
//        viewState = viewState.copy(
//            positioning = viewState.positioning.copy(
//                baseBottom = viewState.positioning.baseBottom + currentSpeed
//            )
//        )
    }

    fun moveCheckLeftRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkLeft = it.positioning.checkLeft + it.currentSpeed
            )
        )
    }

    fun moveCheckLeftLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkLeft = it.positioning.checkLeft - it.currentSpeed
            )
        )
    }

    fun rotateCheckLeftRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkLeftRotation = it.positioning.checkLeftRotation +
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateCheckLeftLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkLeftRotation = it.positioning.checkLeftRotation -
                        (it.currentSpeed / 10).coerceAtLeast(slow)
        )
        )
    }

    fun moveCheckRightRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkRight = it.positioning.checkRight + it.currentSpeed
            )
        )
    }

    fun moveCheckRightLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkRight = it.positioning.checkRight - it.currentSpeed
            )
        )
    }

    fun rotateCheckRightRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkRightRotation = it.positioning.checkRightRotation +
                        (it.currentSpeed / 10).coerceAtLeast(slow)
        )
        )
    }

    fun rotateCheckRightLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkRightRotation = it.positioning.checkRightRotation -
                        (it.currentSpeed / 10).coerceAtLeast(slow)
        )
        )
    }

    fun moveCheckTopUp() = withState {
        updateSpeedMovement()

        val checkTop = it.positioning.checkTop - it.currentSpeed
        val framesTop = it.positioning.framesTop
        val checkBottom = it.positioning.checkBottom

        // TODO: Remove magic numbers from here
        val checkBottomDistance = 90f
        val framesTopDistance = -25f

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkTop = checkTop,

                checkBottom = if (it.isCheckBottomAttached) checkTop + checkBottomDistance else checkBottom,
                framesTop = if (it.isFramesTopAttached) checkTop + framesTopDistance else framesTop,
            )
        )
    }

    fun moveCheckTopDown() = withState {
        updateSpeedMovement()

        val checkTop = it.positioning.checkTop + it.currentSpeed
        val framesTop = it.positioning.framesTop
        val checkBottom = it.positioning.checkBottom

        // TODO: Remove magic numbers from here
        val checkBottomDistance = 90f
        val framesTopDistance = -25f

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkTop = it.positioning.checkTop + it.currentSpeed,

                checkBottom = if (it.isCheckBottomAttached) checkTop + checkBottomDistance else checkBottom,
                framesTop = if (it.isFramesTopAttached) checkTop + framesTopDistance else framesTop,
            )
        )
    }

    fun moveCheckBottomUp() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkBottom = it.positioning.checkBottom - it.currentSpeed
            )
        )
    }

    fun moveCheckBottomDown() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                checkBottom = it.positioning.checkBottom + it.currentSpeed
            )
        )
    }

    fun moveFramesTopUp() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesTop = it.positioning.framesTop - it.currentSpeed
            )
        )
    }

    fun moveFramesTopDown() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesTop = it.positioning.framesTop + it.currentSpeed
            )
        )
    }

    fun moveFramesBottomUp() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesBottom = it.positioning.framesBottom - it.currentSpeed
            )
        )
    }

    fun moveFramesBottomDown() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                framesBottom = it.positioning.framesBottom + it.currentSpeed
            )
        )
    }

    fun rotateTopPointRight() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                topPointRotation = it.positioning.topPointRotation +
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateTopPointLeft() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                topPointRotation = it.positioning.topPointRotation -
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveTopPointExpand() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                topPointLength = it.positioning.topPointLength + it.currentSpeed
            )
        )
    }

    fun moveTopPointShrink() = withState {
        updateSpeedMovement()

        saleRepository.updatePositioning(
            it.positioning.copy(
                topPointLength = it.positioning.topPointLength - it.currentSpeed
            )
        )
    }

    fun rotateBottomPointRight() = withState {
        updateSpeedMovement()


        saleRepository.updatePositioning(
            it.positioning.copy(
                bottomPointRotation = it.positioning.bottomPointRotation +
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun rotateBottomPointLeft() = withState {
        updateSpeedMovement()


        saleRepository.updatePositioning(
            it.positioning.copy(
                bottomPointRotation = it.positioning.bottomPointRotation -
                        (it.currentSpeed / 10).coerceAtLeast(slow)
            )
        )
    }

    fun moveBottomPointExpand() = withState {
        updateSpeedMovement()


        saleRepository.updatePositioning(
            it.positioning.copy(
                bottomPointLength = it.positioning.bottomPointLength + it.currentSpeed
            )
        )
    }

    fun moveBottomPointShrink() = withState {
        updateSpeedMovement()


        saleRepository.updatePositioning(
            it.positioning.copy(
                bottomPointLength = it.positioning.bottomPointLength - it.currentSpeed
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