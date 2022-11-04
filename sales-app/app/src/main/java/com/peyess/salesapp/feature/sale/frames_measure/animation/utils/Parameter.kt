package com.peyess.salesapp.feature.sale.frames_measure.animation.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

sealed class Parameter {
    object AngleBottom : Parameter()
    object AngleTop : Parameter()
    object BaseLeft : Parameter()
    object BaseRight : Parameter()
    object BaseTop : Parameter()
    object BaseBottom : Parameter()
    object CheckLeft : Parameter()
    object CheckRight : Parameter()
    object CheckTop : Parameter()
    object CheckMiddle : Parameter()
    object CheckBottom : Parameter()
    object FramesBottom : Parameter()
    object FramesBridgePivot : Parameter()
    object FramesBridgeHelper : Parameter()
    object FramesLeft : Parameter()
    object FramesRight : Parameter()
    object FramesTop : Parameter()
    object OpticCenter : Parameter()

    companion object {
        fun activeColor(parameter: Parameter): Int {
            return when (parameter) {
                AngleBottom ->
                    Color.Yellow.toArgb()
                AngleTop ->
                    Color.Yellow.toArgb()
                BaseLeft ->
                    Color.Yellow.toArgb()
                BaseRight ->
                    Color.Yellow.toArgb()
                BaseTop ->
                    Color.Yellow.toArgb()
                BaseBottom ->
                    Color.Yellow.toArgb()
                CheckBottom ->
                    Color.Yellow.toArgb()
                CheckLeft ->
                    Color.Yellow.toArgb()
                CheckMiddle ->
                    Color.Yellow.toArgb()
                CheckRight ->
                    Color.Yellow.toArgb()
                CheckTop ->
                    Color.Yellow.toArgb()
                FramesBottom ->
                    Color.Yellow.toArgb()
                FramesBridgeHelper ->
                    Color.Red.toArgb()
                FramesBridgePivot ->
                    Color.Yellow.toArgb()
                FramesLeft ->
                    Color.Yellow.toArgb()
                FramesRight ->
                    Color.Yellow.toArgb()
                FramesTop ->
                    Color.Yellow.toArgb()
                OpticCenter ->
                    Color.Yellow.toArgb()
            }
        }

        fun inactiveColor(parameter: Parameter): Int {
            return when (parameter) {
                AngleBottom ->
                    Color.Yellow.toArgb()
                AngleTop ->
                    Color.Yellow.toArgb()
                BaseLeft ->
                    Color.Yellow.toArgb()
                BaseRight ->
                    Color.Yellow.toArgb()
                BaseTop ->
                    Color.Yellow.toArgb()
                BaseBottom ->
                    Color.Yellow.toArgb()
                CheckBottom ->
                    Color.Yellow.toArgb()
                CheckLeft ->
                    Color.Yellow.toArgb()
                CheckMiddle ->
                    Color.Red.toArgb()
                CheckRight ->
                    Color.Yellow.toArgb()
                CheckTop ->
                    Color.Yellow.toArgb()
                FramesBottom ->
                    Color.Yellow.toArgb()
                FramesBridgeHelper ->
                    Color.Red.toArgb()
                FramesBridgePivot ->
                    Color.Yellow.toArgb()
                FramesLeft ->
                    Color.Yellow.toArgb()
                FramesRight ->
                    Color.Yellow.toArgb()
                FramesTop ->
                    Color.Yellow.toArgb()
                OpticCenter ->
                    Color.Yellow.toArgb()
            }
        }
    }
}
