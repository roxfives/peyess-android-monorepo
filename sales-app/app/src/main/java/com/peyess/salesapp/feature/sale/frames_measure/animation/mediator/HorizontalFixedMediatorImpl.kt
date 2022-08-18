package com.peyess.salesapp.feature.sale.frames_measure.animation.mediator

import kotlinx.coroutines.flow.Flow

class HorizontalFixedMediatorImpl(
    rotation: Flow<Double>,
    positionX: Flow<Double>,
    positionY: Flow<Double>,
    height: Flow<Double>,
    activeColorId: Int,
    inactiveColorId: Int
) : HorizontalMediator,
    PositioningMediator(rotation, positionX, positionY, height, activeColorId, inactiveColorId) {

    override fun moveLeft() {}
    override fun moveRight() {}
}
