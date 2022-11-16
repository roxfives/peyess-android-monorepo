package com.peyess.salesapp.feature.sale.frames_measure.animation.utils

fun isMoveUpVisible(animationState: PositioningAnimationState): Boolean {
    return when (animationState) {
        PositioningAnimationState.PositioningCheckBottom,
        PositioningAnimationState.PositioningCheckTop,
        PositioningAnimationState.PositioningBaseBottom,
        PositioningAnimationState.PositioningBaseTop,
        PositioningAnimationState.PositioningFramesBottom,
        PositioningAnimationState.PositioningFramesTop,
        PositioningAnimationState.PositioningOpticCenter ->
            true

        else ->
            false
    }
}

fun isMoveDownVisible(animationState: PositioningAnimationState): Boolean {
    return isMoveUpVisible(animationState)
}

fun isMoveLeftVisible(animationState: PositioningAnimationState): Boolean {
    return when (animationState) {
        PositioningAnimationState.PositioningOpticCenter,
        PositioningAnimationState.PositioningBaseLeft,
        PositioningAnimationState.PositioningBaseRight,
        PositioningAnimationState.PositioningBridgePivot,
        PositioningAnimationState.PositioningCheckLeft,
        PositioningAnimationState.PositioningCheckMiddle,
        PositioningAnimationState.PositioningCheckRight,
        PositioningAnimationState.PositioningFramesLeft,
        PositioningAnimationState.PositioningFramesRight ->
            true

        else ->
            false
    }
}

fun isMoveRightVisible(animationState: PositioningAnimationState): Boolean {
    return isMoveLeftVisible(animationState)
}

fun isRotateLeftVisible(animationState: PositioningAnimationState): Boolean {
    return when (animationState) {
        PositioningAnimationState.PositioningBottomPointAngle,
        PositioningAnimationState.PositioningTopPointAngle ->
            true

        else ->
            false
    }
}

fun isRotateRightVisible(animationState: PositioningAnimationState): Boolean {
    return isRotateLeftVisible(animationState)
}

fun isExpandVisible(animationState: PositioningAnimationState): Boolean {
    return when (animationState) {
        PositioningAnimationState.PositioningBottomPointSize,
        PositioningAnimationState.PositioningDiameter,
        PositioningAnimationState.PositioningTopPointSize ->
            true

        else ->
            false
    }
}

fun isShrinkVisible(animationState: PositioningAnimationState): Boolean {
    return isExpandVisible(animationState)
}

fun isPreviousVisible(animationState: PositioningAnimationState): Boolean {
    return animationState != PositioningAnimationState.Idle
}

fun isNextVisible(animationState: PositioningAnimationState): Boolean {
    return animationState != PositioningAnimationState.Idle
}

fun isPlayVisible(animationState: PositioningAnimationState): Boolean {
    return animationState == PositioningAnimationState.Idle
}

fun isDoneVisible(animationState: PositioningAnimationState): Boolean {
    return animationState == PositioningAnimationState.DrawAll
}
