package com.peyess.salesapp.feature.sale.frames_measure.animation.mediator

import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.PositioningMediator
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
