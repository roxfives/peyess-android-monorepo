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
import timber.log.Timber

class OpticCenter(
    positioningMediator: PositioningMediator,
    private val coroutineScope: CoroutineScope,
) : MeasuringParameter(positioningMediator) {
    private val center: Point = Point()
    private var diameterRotation = 0
    private var outerRadius = 0f

    companion object {
        private const val diameterStrokeWidth = 1f
        private const val dashFillSize = 3f
        private const val dashSpaceSize = 3f
        private const val dashPhase = 0f
        private const val rotation90 = 90
        private const val minCanvasUnit = 1
    }

    init {
        setupObservers()
    }

    override fun drawInactive(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        Timber.i("Drawing inactive")

        paint.color = positioningMediator.inactiveColorId
        paint.strokeWidth = diameterStrokeWidth
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(
            floatArrayOf(dashFillSize, dashSpaceSize),
            dashPhase
        )
        drawPath(canvas, paint)
    }

    override fun drawActive(canvas: Canvas) {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        Timber.i("Drawing inactive")

        paint.color = positioningMediator.activeColorId
        paint.strokeWidth = diameterStrokeWidth
        paint.style = Paint.Style.STROKE
        paint.pathEffect = DashPathEffect(
            floatArrayOf(dashFillSize, dashSpaceSize),
            dashPhase
        )
        drawPath(canvas, paint)
    }

    private fun drawPath(canvas: Canvas, paint: Paint) {
        val path = Path()

        Timber.i("Drawing path")

        path.moveTo(center.x - outerRadius, center.y.toFloat())
        path.quadTo(
            center.x - outerRadius, center.y.toFloat(),
            center.x + outerRadius, center.y.toFloat()
        )
        val rect = Rect(
            center.x - outerRadius.toInt(), center.y - minCanvasUnit,
            center.x + outerRadius.toInt(), center.y + minCanvasUnit
        )

        // Draws first diameter line
        canvas.save()
        canvas.rotate(
            diameterRotation.toFloat(),
            center.x.toFloat(),
            center.y.toFloat()
        )
        canvas.drawRect(rect, paint)
        canvas.restore()

        // Draws a second diameter line to make an X
        canvas.save()
        canvas.rotate(
            (diameterRotation + rotation90).toFloat(),
            center.x.toFloat(),
            center.y.toFloat()
        )
        canvas.drawRect(rect, paint)
        canvas.restore()

        // Draws outer circle
        canvas.drawCircle(
            center.x.toFloat(), center.y.toFloat(),
            outerRadius - minCanvasUnit,
            paint
        )
        canvas.drawCircle(
            center.x.toFloat(), center.y.toFloat(),
            outerRadius + minCanvasUnit,
            paint
        )
    }

    private fun setupObservers() {
        coroutineScope.launch {
            try {
                positioningMediator.positionX.collect { position ->
                    center.x = position.toInt()
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        coroutineScope.launch {
            try {
                positioningMediator.positionY.collect { position ->
                    center.y = position.toInt()
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        coroutineScope.launch {
            try {
                positioningMediator.rotation.collect { rotation ->
                    diameterRotation = rotation.toInt()
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        coroutineScope.launch {
            try {
                positioningMediator.size.collect { length ->
                    outerRadius = length.toFloat()
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
