package com.peyess.salesapp.feature.sale.frames_measure.animation.mediator

import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.feature.sale.frames_measure.state.FramesMeasureViewModel
import kotlinx.coroutines.flow.map
import timber.log.Timber
import kotlin.math.abs

class MediatorFactoryImpl : MediatorFactory {
    override fun mediatorFor(
        parameter: Parameter,
        framesMeasureViewModel: FramesMeasureViewModel,
    ): PositioningMediator {
        return when (parameter) {
            Parameter.AngleBottom ->
                VerticalExpandMediatorImpl(
                    framesMeasureViewModel.stateFlow
                        .map { it.positioning.bottomPointRotation },

                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterX },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.positioning.bottomPointLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveBottomPointExpand() },
                    { framesMeasureViewModel.moveBottomPointShrink() },
                    { framesMeasureViewModel.rotateBottomPointLeft() },
                    { framesMeasureViewModel.rotateBottomPointRight() },
                )

            Parameter.AngleTop ->
                VerticalExpandMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.positioning.topPointRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterX },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.positioning.topPointLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveTopPointExpand() },
                    { framesMeasureViewModel.moveTopPointShrink() },
                    { framesMeasureViewModel.rotateTopPointLeft() },
                    { framesMeasureViewModel.rotateTopPointRight() },
                )

            Parameter.BaseLeft ->
                HorizontalRotateMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.positioning.baseLeftRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.baseLeft },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.rotateBaseLeftLeft() },
                    { framesMeasureViewModel.rotateBaseLeftRight() },
                    { framesMeasureViewModel.moveBaseLeftLeft() },
                    { framesMeasureViewModel.moveBaseLeftRight() },
                )

            Parameter.BaseRight ->
                HorizontalRotateMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.positioning.baseRightRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.baseRight },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.rotateBaseRightLeft() },
                    { framesMeasureViewModel.rotateBaseRightRight() },
                    { framesMeasureViewModel.moveBaseRightLeft() },
                    { framesMeasureViewModel.moveBaseRightRight() },
                )

            Parameter.BaseTop ->
                VerticalMediatorImpl(
                    framesMeasureViewModel
                        .stateFlow.map { it.standardRotation },
                    framesMeasureViewModel
                        .stateFlow.map { it.standardMiddleX },
                    framesMeasureViewModel
                        .stateFlow.map { it.positioning.baseTop },
                    framesMeasureViewModel
                        .stateFlow.map { it.standardLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveBaseTopUp() },
                    { framesMeasureViewModel.moveBaseTopDown() },
                )
            Parameter.BaseBottom ->
                VerticalMediatorImpl(
                    framesMeasureViewModel
                        .stateFlow.map { it.standardRotation },
                    framesMeasureViewModel
                        .stateFlow.map { it.standardMiddleX },
                    framesMeasureViewModel
                        .stateFlow.map { it.positioning.baseBottom },
                    framesMeasureViewModel
                        .stateFlow.map { it.standardLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveBaseBottomUp() },
                    { framesMeasureViewModel.moveBaseBottomDown() },
                )

            Parameter.CheckBottom ->
                VerticalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.standardMiddleX },
                    framesMeasureViewModel.stateFlow.map { it.positioning.checkBottom },
                    framesMeasureViewModel.stateFlow.map { it.standardLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveCheckBottomUp() },
                    { framesMeasureViewModel.moveCheckBottomDown() },
                )

            Parameter.CheckTop ->
                VerticalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.standardMiddleX },
                    framesMeasureViewModel.stateFlow.map { it.positioning.checkTop },
                    framesMeasureViewModel.stateFlow.map { it.standardLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveCheckTopUp() },
                    { framesMeasureViewModel.moveCheckTopDown() },
                )

            Parameter.CheckLeft ->
                HorizontalRotateMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.positioning.checkLeftRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.checkLeft },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.rotateCheckLeftLeft() },
                    { framesMeasureViewModel.rotateCheckLeftRight() },
                    { framesMeasureViewModel.moveCheckLeftLeft() },
                    { framesMeasureViewModel.moveCheckLeftRight() },
                )

            Parameter.CheckMiddle ->
                HorizontalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.checkMiddle },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveCheckMiddleLeft() },
                    { framesMeasureViewModel.moveCheckMiddleRight() },
                )

            Parameter.CheckRight ->
                HorizontalRotateMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.positioning.checkRightRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.checkRight },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.rotateCheckRightLeft() },
                    { framesMeasureViewModel.rotateCheckRightRight() },
                    { framesMeasureViewModel.moveCheckRightLeft() },
                    { framesMeasureViewModel.moveCheckRightRight() },
                )

            Parameter.FramesBottom ->
                VerticalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.standardMiddleX },
                    framesMeasureViewModel.stateFlow.map { it.positioning.framesBottom },
                    framesMeasureViewModel.stateFlow.map { it.standardLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveFramesBottomUp() },
                    { framesMeasureViewModel.moveFramesBottomDown() },
                )

            Parameter.FramesTop ->
                VerticalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.standardMiddleX },
                    framesMeasureViewModel.stateFlow.map { it.positioning.framesTop },
                    framesMeasureViewModel.stateFlow.map { it.standardLength },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveFramesTopUp() },
                    { framesMeasureViewModel.moveFramesTopDown() },
                )

            Parameter.FramesBridgeHelper ->
                HorizontalFixedMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map {
                            if (it.eye == Eye.Right) {
                                (
                                    abs(
                                        it.positioning.bridgePivot - it.positioning.framesRight
                                    ) / 2.0
                                    ) + it.positioning.framesRight
                            } else {
                                (
                                    abs(
                                        it.positioning.bridgePivot - it.positioning.framesLeft
                                    ) / 2.0
                                    ) + it.positioning.bridgePivot
                            }
                        },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),
                )

            Parameter.FramesBridgePivot ->
                HorizontalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.bridgePivot },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveBridgePivotLeft() },
                    { framesMeasureViewModel.moveBridgePivotRight() },
                )

            Parameter.FramesLeft ->
                HorizontalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.framesLeft },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveFramesLeftLeft() },
                    { framesMeasureViewModel.moveFramesLeftRight() },
                )

            Parameter.FramesRight ->
                HorizontalMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.framesRight },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.standardHeight },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveFramesRightLeft() },
                    { framesMeasureViewModel.moveFramesRightRight() },
                )

            Parameter.OpticCenter ->
                CircleExpandMediatorImpl(
                    framesMeasureViewModel.stateFlow.map { it.standardRotation },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterX },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterY },
                    framesMeasureViewModel.stateFlow.map { it.positioning.opticCenterRadius },

                    Parameter.activeColor(parameter),
                    Parameter.inactiveColor(parameter),

                    { framesMeasureViewModel.moveOpticCenterExpand() },
                    { framesMeasureViewModel.moveOpticCenterShrink() },
                    { framesMeasureViewModel.moveOpticCenterUp() },
                    { framesMeasureViewModel.moveOpticCenterDown() },
                    { framesMeasureViewModel.moveOpticCenterLeft() },
                    { framesMeasureViewModel.moveOpticCenterRight() },
                )
        }
    }
}
