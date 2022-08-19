package com.peyess.salesapp.feature.sale.frames_measure.animation.animation_positioning

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.RectF
import android.os.Build
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.compose.ui.graphics.toArgb
import com.otaliastudios.zoom.ZoomApi
import com.otaliastudios.zoom.ZoomEngine
import com.peyess.salesapp.feature.sale.frames_measure.animation.measuring_parameter.MeasuringParameter
import com.peyess.salesapp.feature.sale.frames_measure.animation.utils.Parameter
import com.peyess.salesapp.ui.theme.PeyessBlue
import timber.log.Timber
import java.lang.Exception

@SuppressLint("ViewConstructor")
class AnimationPanel(
    context: Context,
    imageBackground: Bitmap?,
    private val positioningObjects: Map<Parameter, MeasuringParameter>,
) : SurfaceView(context), SurfaceHolder.Callback {
    private val INITIAL_ZOOM = 1f
    private val MAX_ZOOM = 6f
    private var mAnimationThread: AnimationThread? = null
    private var mMatrix = Matrix()
    private var mZoomEngine: ZoomEngine? = null
    private var mImageBackground: Bitmap?

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        performClick()
        return mZoomEngine?.onTouchEvent(event) == true || super.onTouchEvent(event)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if (mImageBackground == null) {
            Timber.e("Image uri is null")
            return
        }

        canvas.setMatrix(mMatrix)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            canvas.drawColor(
                PeyessBlue.toArgb(), // resources.getColor(R., null),
                PorterDuff.Mode.SRC
            )
        }
        canvas.drawBitmap(
            mImageBackground!!,
            null,
            RectF(
                0f, 0f, mImageBackground!!.width.toFloat(),
                mImageBackground!!.height
                    .toFloat()
            ),
            null
        )

        try {
            for (obj in positioningObjects.values) {
                if (obj.isVisible) {
                    if (obj.isActive) {
                        obj.drawActive(canvas)
                    } else {
                        obj.drawInactive(canvas)
                    }
                }
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mAnimationThread = AnimationThread(holder, this)
        mAnimationThread!!.setRunning(true)
        mAnimationThread!!.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry: Boolean

        retry = true
        while (retry) {
            try {
                mAnimationThread!!.setRunning(false)
                mAnimationThread!!.join()
                retry = false
            } catch (e: Exception) {
                retry = true
                e.printStackTrace()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mZoomEngine?.setContainerSize(width.toFloat(), height.toFloat())
    }

    override fun computeHorizontalScrollExtent(): Int {
        return mZoomEngine?.computeHorizontalScrollOffset() ?: 0
    }

    override fun computeHorizontalScrollRange(): Int {
        return mZoomEngine?.computeHorizontalScrollRange() ?: 0
    }

    override fun computeVerticalScrollOffset(): Int {
        return mZoomEngine?.computeVerticalScrollOffset() ?: 0
    }

    override fun computeVerticalScrollRange(): Int {
        return mZoomEngine?.computeVerticalScrollRange() ?: 0
    }

    //    @Override
    fun updateImageBackground(imageBackground: Bitmap?) {
        mImageBackground = imageBackground
    }

    fun resetZoom() {
        mZoomEngine?.zoomTo(INITIAL_ZOOM, true)
    }

    private fun setupTreeObserver() {
        val treeObserver = viewTreeObserver
        if (treeObserver.isAlive) {
            treeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    this@AnimationPanel.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val displayMetrics = DisplayMetrics()
                    display.getMetrics(displayMetrics)
                    scaleViewToImage(this@AnimationPanel)
                    setupZoomLimit()
                }
            })
        }
    }

    private fun scaleViewToImage(view: AnimationPanel) {
        if (mImageBackground == null) {
            return
        }

        val xScale = view.measuredWidth.toFloat() / mImageBackground!!.width.toFloat()
        val yScale = view.measuredHeight.toFloat() / mImageBackground!!.height.toFloat()
        val scale = Math.min(xScale, yScale)
        val params = view.layoutParams

        params.width = (scale * mImageBackground!!.width.toFloat()).toInt()
        params.height = (scale * mImageBackground!!.height.toFloat()).toInt()

        view.layoutParams = params
        view.requestLayout()
    }

    private fun setupZoomLimit() {
        if (mImageBackground == null) {
            return
        }

        mZoomEngine?.setContentSize(
            mImageBackground!!.width.toFloat(),
            mImageBackground!!.height.toFloat()
        )
        mZoomEngine?.setContainerSize(measuredWidth.toFloat(), measuredHeight.toFloat())
        mZoomEngine?.setMaxZoom(MAX_ZOOM, ZoomApi.TYPE_ZOOM)
    }

    private fun setupZoomEngine(context: Context) {
        mZoomEngine = ZoomEngine(context, this)

        mZoomEngine?.addListener(object : ZoomEngine.Listener {
            override fun onUpdate(engine: ZoomEngine, matrix: Matrix) {
                mMatrix = matrix
            }

            override fun onIdle(engine: ZoomEngine) {}
        })

        mZoomEngine?.setScrollEnabled(true)
        mZoomEngine?.setOverScrollHorizontal(true)
        mZoomEngine?.setOverScrollVertical(true)
        mZoomEngine?.setFlingEnabled(true)
        mZoomEngine?.setAllowFlingInOverscroll(true)
        mZoomEngine?.setOneFingerScrollEnabled(true)
        mZoomEngine?.setTwoFingersScrollEnabled(true)
        mZoomEngine?.setThreeFingersScrollEnabled(true)
        mZoomEngine?.setZoomEnabled(true)
        mZoomEngine?.setMinZoom(1f)
    }

    private fun setupPanel() {
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)
        isFocusable = true
    }

    init {
        mImageBackground = imageBackground
        setupZoomEngine(context)
        setupTreeObserver()
        setupPanel()
    }
}
