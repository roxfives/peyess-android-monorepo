package com.peyess.salesapp.screen.sale.frames_measure.animation.mediator

import kotlinx.coroutines.flow.Flow

abstract class PositioningMediator(
    var rotation: Flow<Double>,
    var positionX: Flow<Double>,
    var positionY: Flow<Double>,
    var size: Flow<Double>,
    var activeColorId: Int,
    var inactiveColorId: Int
)
