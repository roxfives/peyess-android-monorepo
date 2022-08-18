package com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter

import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.PositioningMediator
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.Parameter
import kotlinx.coroutines.CoroutineScope

interface MeasuringParameterFactory {
    fun parameterFor(
        parameter: Parameter,
        mediator: PositioningMediator,
        coroutineScope: CoroutineScope
    ): MeasuringParameter?
}
