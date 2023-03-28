package com.peyess.salesapp.screen.sale.frames_measure.animation.mediator

import com.peyess.salesapp.screen.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.screen.sale.frames_measure.state.FramesMeasureViewModel

interface MediatorFactory {
    fun mediatorFor(
        parameter: Parameter,
        framesMeasureViewModel: FramesMeasureViewModel,
    ): PositioningMediator
}
