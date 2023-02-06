package com.peyess.salesapp.screen.sale.frames_measure.animation.utils

import com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter.MeasuringParameter
import com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter.MeasuringParameterFactory
import com.peyess.salesapp.screen.sale.frames_measure.animation.mediator.MediatorFactory
import com.peyess.salesapp.screen.sale.frames_measure.state.FramesMeasureViewModel
import kotlinx.coroutines.CoroutineScope

class AnimationParametersFactoryImpl(
    private val measuringParameterFactory: MeasuringParameterFactory,
    private val mediatorParameterFactory: MediatorFactory,
) : AnimationParametersFactory {

    private val parametersLeftEye = listOf(
        Parameter.AngleBottom,
        Parameter.AngleTop,
        Parameter.BaseLeft,
        Parameter.BaseRight,
        Parameter.BaseTop,
        Parameter.BaseBottom,
        Parameter.CheckLeft,
        Parameter.CheckRight,
        Parameter.CheckTop,
        Parameter.CheckMiddle,
        Parameter.CheckBottom,
        Parameter.FramesBottom,
        Parameter.FramesBridgePivot,
//        Parameter.FramesBridgeHelper,
        Parameter.FramesLeft,
        Parameter.FramesRight,
        Parameter.FramesTop,
        Parameter.OpticCenter,
    )

    override fun makeMeasuringParameters(
        coroutineScope: CoroutineScope,
        viewModel: FramesMeasureViewModel,
    ): Map<Parameter, MeasuringParameter> {
        val mapping = mutableMapOf<Parameter, MeasuringParameter>()

        for (parameter in parametersLeftEye) {
            mapping[parameter] = measuringParameterFactory.parameterFor(
                parameter,
                mediatorParameterFactory.mediatorFor(parameter, viewModel),
                coroutineScope
            )!!
        }

        return mapping
    }
}
