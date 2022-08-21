package com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter

import android.graphics.Canvas
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.HorizontalMediator
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.PositioningMediator
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.RadiusMediator
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.RotateMediator
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.VerticalMediator

abstract class MeasuringParameter(
    protected val positioningMediator: PositioningMediator,
) {
    var isVisible: Boolean = false
    var isActive: Boolean = false

    abstract fun drawInactive(canvas: Canvas)
    abstract fun drawActive(canvas: Canvas)

    fun moveLeft() {
        when (positioningMediator) {
            is HorizontalMediator ->
                positioningMediator.moveLeft()
        }
    }

    fun moveRight() {
        when (positioningMediator) {
            is HorizontalMediator ->
                positioningMediator.moveRight()
        }
    }

    fun moveUp() {
        when (positioningMediator) {
            is VerticalMediator ->
                positioningMediator.moveUp()
        }
    }

    fun moveDown() {
        when (positioningMediator) {
            is VerticalMediator ->
                positioningMediator.moveDown()
        }
    }

    fun rotateLeft() {
        when (positioningMediator) {
            is RotateMediator ->
                positioningMediator.rotateLeft()
        }
    }

    fun rotateRight() {
        when (positioningMediator) {
            is RotateMediator ->
                positioningMediator.rotateRight()
        }
    }

    fun expand() {
        when (positioningMediator) {
            is RadiusMediator ->
                positioningMediator.expand()
        }
    }

    fun shrink() {
        when (positioningMediator) {
            is RadiusMediator ->
                positioningMediator.shrink()
        }
    }
}
