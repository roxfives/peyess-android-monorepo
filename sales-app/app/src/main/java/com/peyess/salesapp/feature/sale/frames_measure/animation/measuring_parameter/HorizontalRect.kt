package com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter

import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.Rect
import com.peyess.salesapp.feature.sale.frames_measure.animation.mediator.PositioningMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class HorizontalRect(
    positioningMediator: PositioningMediator,
    private val coroutineScope: CoroutineScope,
) : MeasuringParameter(positioningMediator) {
    private val pointTop: Point = Point()
    private val pointBottom: Point = Point()
    private var length = 0.0
    private var rotation = 0.0

    private val middleX: Float
        get() = (pointBottom.x - pointTop.x) / 2.0f + pointTop.x
    private val middleY: Float
        get() = (pointBottom.y - pointTop.y) / 2.0f + pointTop.y

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

        Timber.i("Drawing active at pointTop: $pointTop\n" + "pointBottom: $pointBottom\n" + "length: $length\n" + "rotation: $rotation")

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

        path.moveTo(pointTop.x.toFloat(), pointTop.y.toFloat())
        path.quadTo(
            pointTop.x.toFloat(), pointTop.y.toFloat(),
            pointBottom.x.toFloat(), pointBottom.y.toFloat()
        )

        val rect = Rect(
            pointTop.x, pointTop.y,
            pointBottom.x, pointBottom.y
        )

        canvas.save()
        canvas.rotate(
            rotation.toFloat(),
            middleX,
            middleY
        )
        canvas.drawRect(rect, paint)
        canvas.restore()
    }

    private fun setupObservers() {
        this.coroutineScope.launch {
            try {
                positioningMediator.positionX.collect { position ->
                    if (length > 0) {
                        pointTop.x = (position - length / 2.0).toInt()
                        pointBottom.x = (position + length / 2.0).toInt()
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        this.coroutineScope.launch {
            try {
                positioningMediator.positionY.collect { position ->
                    pointTop.y = position.toInt() - minCanvasUnit
                    pointBottom.y = position.toInt() + minCanvasUnit
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        this.coroutineScope.launch {
            try {
                positioningMediator.rotation.collect { rotation = it }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        this.coroutineScope.launch {
            try {
                positioningMediator.size.collect { length = it }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }
}
