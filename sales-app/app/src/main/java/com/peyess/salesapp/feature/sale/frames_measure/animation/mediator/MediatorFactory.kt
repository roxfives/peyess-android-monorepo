package com.peyess.salesapp.feature.sale.frames_measure.animation.mediator

import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.feature.sale.frames_measure.state.FramesMeasureViewModel

interface MediatorFactory {
    fun mediatorFor(
        parameter: Parameter,
        framesMeasureViewModel: FramesMeasureViewModel,
    ): PositioningMediator
}
