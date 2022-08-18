package com.peyess.salesapp.feature.sale.frames_measure.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.feature.sale.frames.state.Eye
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

const val slow = 1L
const val speedUnit = 1L
const val fastest = 100L

class FramesMeasureViewModel @AssistedInject constructor(
    @Assisted initialState: FramesMeasureState
): MavericksViewModel<FramesMeasureState>(initialState) {
    private var currentSpeed = slow
    private var lastTime = 0L
    private var timeThreshold = 2000000


    private fun updateSpeedMovement() {
        if (System.nanoTime() - lastTime < timeThreshold && currentSpeed < fastest) {
            currentSpeed = (currentSpeed + speedUnit).coerceAtMost(fastest)
        } else if (System.nanoTime() - lastTime >= timeThreshold) {
            currentSpeed = (currentSpeed - speedUnit).coerceAtLeast(slow)
        }

        lastTime = System.nanoTime()
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
                        opticCenterX + innerFramesDistance else framesRight
                )
            )
        } else {
            copy(
                positioning = positioning.copy(
                    framesLeft = if (isInnerFramesAttached)
                        opticCenterX - innerFramesDistance else framesLeft
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

        val intermidiateState = copy(
            positioning = positioning.copy(
                opticCenterRadius = radius,
                topPointLength = if (isPointTopAttached) radius else topLength,
                bottomPointLength = if (isPointBottomAttached) radius else bottomLength,
            )
        )

        if (eye == Eye.Left) {
            intermidiateState.copy(
                positioning = positioning.copy(
                    framesRight =
                    if (isOuterFramesAttached) opticCenterX + radius else framesRight,
                )
            )
        } else {
            intermidiateState.copy(
                positioning = positioning.copy(
                    framesLeft =
                    if (isOuterFramesAttached) opticCenterX - radius else framesLeft,
                )
            )
        }
    }

    fun moveOpticCenterShrink() = setState {
        updateSpeedMovement()

        val opticCenterX = positioning.opticCenterX
        val radius = positioning.opticCenterRadius - currentSpeed
        val topLength = positioning.topPointLength
        val bottomLength = positioning.bottomPointLength
        val framesRight = positioning.framesRight
        val framesLeft = positioning.framesLeft

        val intermidiateState = copy(
            positioning = positioning.copy(
                opticCenterRadius = radius,
                topPointLength = if (isPointTopAttached) radius else topLength,
                bottomPointLength = if (isPointBottomAttached) radius else bottomLength,
            )
        )

        if (eye == Eye.Left) {
            intermidiateState.copy(
                positioning = positioning.copy(
                    framesRight =
                    if (isOuterFramesAttached) opticCenterX + radius else framesRight,
                )
            )
        } else {
            intermidiateState.copy(
                positioning = positioning.copy(
                    framesLeft =
                    if (isOuterFramesAttached) opticCenterX - radius else framesLeft,
                )
            )
        }
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