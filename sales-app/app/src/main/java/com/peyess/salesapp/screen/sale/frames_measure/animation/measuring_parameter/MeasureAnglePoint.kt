package com.peyess.salesapp.screen.sale.frames_measure.animation.measuring_parameter

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import com.peyess.salesapp.screen.sale.frames_measure.animation.mediator.PositioningMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MeasureAnglePoint(
    positioningMediator: PositioningMediator,
    private val coroutineScope: CoroutineScope,
) : MeasuringParameter(positioningMediator) {
    private val opticCenter: Point = Point()
    private val pointBorder: Point = Point()
    private var rotation = 0.0
    private var length = 0.0

    companion object {
        private const val dashFillSize = 3f
        private const val dashSpaceSize = 3f
        private const val dashPhase = 0f
        private const val minCanvasUnit = 1
    }

    init {
        setupObservers()
    }

    override fun drawInactive(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = positioningMediator.inactiveColorId
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(
            floatArrayOf(
                dashFillSize,
                dashSpaceSize
            ),
            dashPhase
        )

        drawPath(canvas, paint)
    }

    override fun drawActive(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = positioningMediator.activeColorId
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(
            floatArrayOf(
                dashFillSize,
                dashSpaceSize
            ),
            dashPhase
        )
        drawPath(canvas, paint)
    }

    private fun drawPath(canvas: Canvas, paint: Paint) {
        val path = Path()
        path.moveTo(opticCenter.x.toFloat(), opticCenter.y.toFloat())
        path.quadTo(
            opticCenter.x.toFloat(), opticCenter.y.toFloat(),
            pointBorder.x.toFloat(), pointBorder.y.toFloat()
        )
        val rect = Rect(
            opticCenter.x, opticCenter.y,
            pointBorder.x, pointBorder.y
        )
        canvas.save()
        canvas.rotate(rotation.toFloat(), opticCenter.x.toFloat(), opticCenter.y.toFloat())
        canvas.drawRect(rect, paint)
        canvas.restore()
    }

    private fun setupObservers() {
        coroutineScope.launch {
            try {
                positioningMediator.positionX.collect { position ->
                    opticCenter.x = position.toInt()

                    if (length >= 0) {
                        pointBorder.x = position.toInt() + length.toInt()
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        coroutineScope.launch {
            try {
                positioningMediator.positionY.collect { position ->
                    opticCenter.y = position.toInt()
                    pointBorder.y = position.toInt() + minCanvasUnit
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        coroutineScope.launch {
            try {
                positioningMediator.rotation.collect { rotation = it }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        coroutineScope.launch {
            try {
                positioningMediator.size.collect {
                    length = it

                    if (length >= 0) {
                        pointBorder.x = opticCenter.x + it.toInt()
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
