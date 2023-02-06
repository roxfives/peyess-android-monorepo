package com.peyess.salesapp.screen.sale.frames_measure.animation.mediator

import kotlinx.coroutines.flow.Flow

class VerticalMediatorImpl(
    rotation: Flow<Double>,
    positionX: Flow<Double>,
    positionY: Flow<Double>,
    height: Flow<Double>,
    activeColorId: Int,
    inactiveColorId: Int,

    private val onMoveUp: () -> Unit = {},
    private val onMoveDown: () -> Unit = {}
) : VerticalMediator,
    PositioningMediator(rotation, positionX, positionY, height, activeColorId, inactiveColorId) {

    override fun moveUp() {
        onMoveUp()
    }

    override fun moveDown() {
        onMoveDown()
    }
}
