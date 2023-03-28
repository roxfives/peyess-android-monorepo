package com.peyess.salesapp.screen.sale.frames_measure.animation.mediator

import kotlinx.coroutines.flow.Flow

class CircleExpandMediatorImpl(
    rotation: Flow<Double>,
    positionX: Flow<Double>,
    positionY: Flow<Double>,
    radius: Flow<Double>,
    activeColorId: Int,
    inactiveColorId: Int,

    private val onExpand: () -> Unit,
    private val onShrink: () -> Unit,

    private val onMoveUp: () -> Unit,
    private val onMoveDown: () -> Unit,
    private val onMoveLeft: () -> Unit,
    private val onMoveRight: () -> Unit,
) : PositioningMediator(rotation, positionX, positionY, radius, activeColorId, inactiveColorId),
    RadiusMediator,
    HorizontalMediator, VerticalMediator {

    override fun expand() {
        onExpand()
    }

    override fun shrink() {
        onShrink()
    }

    override fun moveUp() {
        onMoveUp()
    }

    override fun moveDown() {
        onMoveDown()
    }

    override fun moveLeft() {
        onMoveLeft()
    }

    override fun moveRight() {
        onMoveRight()
    }
}
