package com.peyess.salesapp.screen.sale.frames_measure.animation.mediator

import kotlinx.coroutines.flow.Flow

class HorizontalMediatorImpl(
    rotation: Flow<Double>,
    positionX: Flow<Double>,
    positionY: Flow<Double>,
    height: Flow<Double>,
    activeColorId: Int,
    inactiveColorId: Int,

    private val onMoveLeft: () -> Unit,
    private val onMoveRight: () -> Unit,
) : HorizontalMediator,
    PositioningMediator(rotation, positionX, positionY, height, activeColorId, inactiveColorId) {
    override fun moveLeft() {
        onMoveLeft()
    }

    override fun moveRight() {
        onMoveRight()
    }
}
