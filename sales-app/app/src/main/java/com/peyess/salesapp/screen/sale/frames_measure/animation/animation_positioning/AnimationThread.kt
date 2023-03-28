package com.peyess.salesapp.screen.sale.frames_measure.animation.animation_positioning

import android.graphics.Canvas
import android.view.SurfaceHolder
import java.lang.InterruptedException
import java.lang.RuntimeException
import java.lang.Thread

class AnimationThread internal constructor(
    private val mSurfaceHolder: SurfaceHolder,
    private val mMainAnimationPanel: AnimationPanel
) : Thread() {
    private var mIsRunning = false

    companion object {
        private const val MAX_FPS = 30
        private const val TARGET_TIME = (1000 / MAX_FPS).toLong()
    }

    fun setRunning(running: Boolean) {
        mIsRunning = running
    }

    override fun run() {
        var canvas: Canvas?
        var startTime: Long
        var waitTime: Long
        var timeMillis: Long

        while (mIsRunning) {
            canvas = null
            startTime = System.nanoTime()

            try {
                canvas = mSurfaceHolder.lockCanvas()
                mMainAnimationPanel.draw(canvas)
            } catch (e: RuntimeException) {
                e.printStackTrace()
            } finally {
                try {
                    mSurfaceHolder.unlockCanvasAndPost(canvas)
                } catch (e: RuntimeException) {
                    e.printStackTrace()
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = TARGET_TIME - timeMillis

            if (waitTime > 0) {
                try {
                    sleep(waitTime)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
