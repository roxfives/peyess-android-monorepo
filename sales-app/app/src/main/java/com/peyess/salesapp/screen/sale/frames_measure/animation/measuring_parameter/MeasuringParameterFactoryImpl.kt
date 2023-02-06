package com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter

import com.peyess.salesapp.screen.sale.frames_measure.animation.mediator.PositioningMediator
import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.Parameter
import kotlinx.coroutines.CoroutineScope

class MeasuringParameterFactoryImpl() : MeasuringParameterFactory {
    override fun parameterFor(
        parameter: Parameter,
        mediator: PositioningMediator,
        coroutineScope: CoroutineScope
    ): MeasuringParameter {
        return when (parameter) {
            Parameter.AngleBottom ->
                MeasureAnglePoint(mediator, coroutineScope)

            Parameter.AngleTop ->
                MeasureAnglePoint(mediator, coroutineScope)

            Parameter.BaseLeft ->
                VerticalRect(mediator, coroutineScope)

            Parameter.BaseRight ->
                VerticalRect(mediator, coroutineScope)

            Parameter.BaseTop ->
                HorizontalRect(mediator, coroutineScope)

            Parameter.BaseBottom ->
                HorizontalRect(mediator, coroutineScope)

            Parameter.CheckBottom ->
                HorizontalRect(mediator, coroutineScope)

            Parameter.CheckLeft ->
                VerticalRect(mediator, coroutineScope)

            Parameter.CheckMiddle ->
                VerticalRect(mediator, coroutineScope)

            Parameter.CheckRight ->
                VerticalRect(mediator, coroutineScope)

            Parameter.CheckTop ->
                HorizontalRect(mediator, coroutineScope)

            Parameter.FramesBottom ->
                HorizontalRect(mediator, coroutineScope)

            Parameter.FramesBridgeHelper ->
                VerticalRect(mediator, coroutineScope)

            Parameter.FramesBridgePivot ->
                VerticalRect(mediator, coroutineScope)

            Parameter.FramesLeft ->
                VerticalRect(mediator, coroutineScope)

            Parameter.FramesRight ->
                VerticalRect(mediator, coroutineScope)

            Parameter.FramesTop ->
                HorizontalRect(mediator, coroutineScope)

            Parameter.OpticCenter ->
                OpticCenter(mediator, coroutineScope)
        }
    }
}
