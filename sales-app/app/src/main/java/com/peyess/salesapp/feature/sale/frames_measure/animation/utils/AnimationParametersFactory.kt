package com.peyess.salesapp.feature.sale.frames_measure.animation.utils

import com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter.MeasuringParameter
import com.peyess.salesapp.feature.sale.frames_measure.state.FramesMeasureViewModel
import kotlinx.coroutines.CoroutineScope

interface AnimationParametersFactory {
    fun makeMeasuringParameters(
        coroutineScope: CoroutineScope,
        viewModel: FramesMeasureViewModel,
    ): Map<Parameter, MeasuringParameter>
}
