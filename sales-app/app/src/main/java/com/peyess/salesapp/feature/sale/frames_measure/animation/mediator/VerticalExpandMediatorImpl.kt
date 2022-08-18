package com.peyess.salesapp.feature.sale.frames_measure.animation.mediator

import kotlinx.coroutines.flow.Flow

class VerticalExpandMediatorImpl(
    rotation: Flow<Double>,
    positionX: Flow<Double>,
    positionY: Flow<Double>,
    size: Flow<Double>,
    activeColorId: Int,
    inactiveColorId: Int,

    val onExpand: () -> Unit,
    val onShrink: () -> Unit,
    val onRotateLeft: () -> Unit,
    val onRotateRight: () -> Unit
) : PositioningMediator(rotation, positionX, positionY, size, activeColorId, inactiveColorId),
    RadiusMediator,
    RotateMediator {

    override fun expand() {
        onExpand()
    }

    override fun shrink() {
        onShrink()
    }

    override fun rotateLeft() {
        onRotateLeft()
    }

    override fun rotateRight() {
        onRotateRight()
    }
}
