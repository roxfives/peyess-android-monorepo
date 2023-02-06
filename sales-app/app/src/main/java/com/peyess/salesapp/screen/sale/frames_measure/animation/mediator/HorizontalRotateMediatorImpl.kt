package com.peyess.salesapp.screen.sale.frames_measure.animation.mediator

import kotlinx.coroutines.flow.Flow

class HorizontalRotateMediatorImpl(
    rotation: Flow<Double>,
    positionX: Flow<Double>,
    positionY: Flow<Double>,
    height: Flow<Double>,
    activeColorId: Int,
    inactiveColorId: Int,

    val onRotateLeft: () -> Unit,
    val onRotateRight: () -> Unit,
    val onMoveLeft: () -> Unit,
    val onMoveRight: () -> Unit
) : PositioningMediator(rotation, positionX, positionY, height, activeColorId, inactiveColorId),
    RotateMediator,
    HorizontalMediator {

    override fun rotateLeft() {
        onRotateLeft()
    }

    override fun rotateRight() {
        onRotateRight()
    }

    override fun moveLeft() {
        onMoveLeft()
    }

    override fun moveRight() {
        onMoveRight()
    }
}
