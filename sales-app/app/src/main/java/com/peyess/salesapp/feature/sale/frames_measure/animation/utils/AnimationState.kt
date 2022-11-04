package com.peyess.salesapp.feature.sale.frames_measure.animation.utils

sealed class PositioningAnimationState {
    object PositioningOpticCenter : PositioningAnimationState()
    object PositioningDiameter : PositioningAnimationState()
    object PositioningFramesRight : PositioningAnimationState()
    object PositioningBridgePivot : PositioningAnimationState()
    object PositioningCheckMiddle : PositioningAnimationState()
    object PositioningBaseLeft : PositioningAnimationState()
    object PositioningBaseRight : PositioningAnimationState()
    object PositioningBaseTop : PositioningAnimationState()
    object PositioningBaseBottom : PositioningAnimationState()
    object PositioningCheckLeft : PositioningAnimationState()
    object PositioningCheckRight : PositioningAnimationState()
    object PositioningCheckTop : PositioningAnimationState()
    object PositioningCheckBottom : PositioningAnimationState()
    object PositioningFramesLeft : PositioningAnimationState()
    object PositioningFramesTop : PositioningAnimationState()
    object PositioningFramesBottom : PositioningAnimationState()
    object PositioningTopPointAngle : PositioningAnimationState()
    object PositioningTopPointSize : PositioningAnimationState()
    object PositioningBottomPointAngle : PositioningAnimationState()
    object PositioningBottomPointSize : PositioningAnimationState()

    object Idle : PositioningAnimationState()
    object DrawAll : PositioningAnimationState()

    object CheckError : PositioningAnimationState()
}

fun nextStateEyeLeft(curState: PositioningAnimationState): PositioningAnimationState {
    return when (curState) {
        PositioningAnimationState.DrawAll ->
            PositioningAnimationState.PositioningOpticCenter
        PositioningAnimationState.Idle ->
            PositioningAnimationState.PositioningOpticCenter

        PositioningAnimationState.PositioningOpticCenter ->
            PositioningAnimationState.PositioningDiameter

        PositioningAnimationState.PositioningDiameter ->
            PositioningAnimationState.PositioningFramesLeft

        PositioningAnimationState.PositioningFramesLeft ->
            PositioningAnimationState.PositioningBridgePivot

        PositioningAnimationState.PositioningBridgePivot ->
            PositioningAnimationState.PositioningCheckMiddle

        PositioningAnimationState.PositioningCheckMiddle ->
            PositioningAnimationState.PositioningBaseLeft

        PositioningAnimationState.PositioningBaseLeft ->
            PositioningAnimationState.PositioningBaseRight

        PositioningAnimationState.PositioningBaseRight ->
            PositioningAnimationState.PositioningBaseTop

        PositioningAnimationState.PositioningBaseTop ->
            PositioningAnimationState.PositioningBaseBottom

        PositioningAnimationState.PositioningBaseBottom ->
            PositioningAnimationState.PositioningCheckLeft

        PositioningAnimationState.PositioningCheckLeft ->
            PositioningAnimationState.PositioningCheckRight

        PositioningAnimationState.PositioningCheckRight ->
            PositioningAnimationState.PositioningCheckTop

        PositioningAnimationState.PositioningCheckTop ->
            PositioningAnimationState.PositioningCheckBottom

        PositioningAnimationState.PositioningCheckBottom ->
            PositioningAnimationState.PositioningFramesRight

        PositioningAnimationState.PositioningFramesRight ->
            PositioningAnimationState.PositioningFramesTop

        PositioningAnimationState.PositioningFramesTop ->
            PositioningAnimationState.PositioningFramesBottom

        PositioningAnimationState.PositioningFramesBottom ->
            PositioningAnimationState.PositioningTopPointAngle

        PositioningAnimationState.PositioningTopPointAngle ->
            PositioningAnimationState.PositioningTopPointSize

        PositioningAnimationState.PositioningTopPointSize ->
            PositioningAnimationState.PositioningBottomPointAngle

        PositioningAnimationState.PositioningBottomPointAngle ->
            PositioningAnimationState.PositioningBottomPointSize

        PositioningAnimationState.PositioningBottomPointSize ->
            PositioningAnimationState.DrawAll

        PositioningAnimationState.CheckError ->
            PositioningAnimationState.Idle
    }
}

fun previousStateLeftEye(curState: PositioningAnimationState): PositioningAnimationState {
    return when (curState) {
        PositioningAnimationState.PositioningOpticCenter ->
            PositioningAnimationState.DrawAll
        PositioningAnimationState.PositioningOpticCenter ->
            PositioningAnimationState.Idle

        PositioningAnimationState.PositioningDiameter ->
            PositioningAnimationState.PositioningOpticCenter

        PositioningAnimationState.PositioningFramesLeft ->
            PositioningAnimationState.PositioningDiameter

        PositioningAnimationState.PositioningBridgePivot ->
            PositioningAnimationState.PositioningFramesLeft

        PositioningAnimationState.PositioningCheckMiddle ->
            PositioningAnimationState.PositioningBridgePivot

        PositioningAnimationState.PositioningBaseLeft ->
            PositioningAnimationState.PositioningCheckMiddle

        PositioningAnimationState.PositioningBaseRight ->
            PositioningAnimationState.PositioningBaseLeft

        PositioningAnimationState.PositioningBaseTop ->
            PositioningAnimationState.PositioningBaseRight

        PositioningAnimationState.PositioningBaseBottom ->
            PositioningAnimationState.PositioningBaseTop

        PositioningAnimationState.PositioningCheckLeft ->
            PositioningAnimationState.PositioningBaseBottom

        PositioningAnimationState.PositioningCheckRight ->
            PositioningAnimationState.PositioningCheckLeft

        PositioningAnimationState.PositioningCheckTop ->
            PositioningAnimationState.PositioningCheckRight

        PositioningAnimationState.PositioningCheckBottom ->
            PositioningAnimationState.PositioningCheckTop

        PositioningAnimationState.PositioningFramesRight ->
            PositioningAnimationState.PositioningCheckBottom

        PositioningAnimationState.PositioningFramesTop ->
            PositioningAnimationState.PositioningFramesRight

        PositioningAnimationState.PositioningFramesBottom ->
            PositioningAnimationState.PositioningFramesTop

        PositioningAnimationState.PositioningTopPointAngle ->
            PositioningAnimationState.PositioningFramesBottom

        PositioningAnimationState.PositioningTopPointSize ->
            PositioningAnimationState.PositioningTopPointAngle

        PositioningAnimationState.PositioningBottomPointAngle ->
            PositioningAnimationState.PositioningTopPointSize

        PositioningAnimationState.PositioningBottomPointSize ->
            PositioningAnimationState.PositioningBottomPointAngle

        PositioningAnimationState.DrawAll ->
            PositioningAnimationState.PositioningBottomPointSize

        PositioningAnimationState.Idle ->
            PositioningAnimationState.DrawAll

        PositioningAnimationState.CheckError ->
            PositioningAnimationState.Idle
    }
}

fun nextStateRightEye(curState: PositioningAnimationState): PositioningAnimationState {
    return when (curState) {
        PositioningAnimationState.DrawAll ->
            PositioningAnimationState.PositioningOpticCenter
        PositioningAnimationState.Idle ->
            PositioningAnimationState.PositioningOpticCenter

        PositioningAnimationState.PositioningOpticCenter ->
            PositioningAnimationState.PositioningDiameter

        PositioningAnimationState.PositioningDiameter ->
            PositioningAnimationState.PositioningFramesRight

        PositioningAnimationState.PositioningFramesRight ->
            PositioningAnimationState.PositioningBridgePivot

        PositioningAnimationState.PositioningBridgePivot ->
            PositioningAnimationState.PositioningCheckMiddle

        PositioningAnimationState.PositioningCheckMiddle ->
            PositioningAnimationState.PositioningBaseLeft

        PositioningAnimationState.PositioningBaseLeft ->
            PositioningAnimationState.PositioningBaseRight

        PositioningAnimationState.PositioningBaseRight ->
            PositioningAnimationState.PositioningBaseTop

        PositioningAnimationState.PositioningBaseTop ->
            PositioningAnimationState.PositioningBaseBottom

        PositioningAnimationState.PositioningBaseBottom->
            PositioningAnimationState.PositioningCheckLeft

        PositioningAnimationState.PositioningCheckLeft ->
            PositioningAnimationState.PositioningCheckRight

        PositioningAnimationState.PositioningCheckRight ->
            PositioningAnimationState.PositioningCheckTop

        PositioningAnimationState.PositioningCheckTop ->
            PositioningAnimationState.PositioningCheckBottom

        PositioningAnimationState.PositioningCheckBottom ->
            PositioningAnimationState.PositioningFramesLeft

        PositioningAnimationState.PositioningFramesLeft ->
            PositioningAnimationState.PositioningFramesTop

        PositioningAnimationState.PositioningFramesTop ->
            PositioningAnimationState.PositioningFramesBottom

        PositioningAnimationState.PositioningFramesBottom ->
            PositioningAnimationState.PositioningTopPointAngle

        PositioningAnimationState.PositioningTopPointAngle ->
            PositioningAnimationState.PositioningTopPointSize

        PositioningAnimationState.PositioningTopPointSize ->
            PositioningAnimationState.PositioningBottomPointAngle

        PositioningAnimationState.PositioningBottomPointAngle ->
            PositioningAnimationState.PositioningBottomPointSize

        PositioningAnimationState.PositioningBottomPointSize ->
            PositioningAnimationState.DrawAll

        PositioningAnimationState.CheckError ->
            PositioningAnimationState.Idle
    }
}

fun previousStateRightEye(curState: PositioningAnimationState): PositioningAnimationState {
    return when (curState) {
        PositioningAnimationState.PositioningOpticCenter ->
            PositioningAnimationState.DrawAll
        PositioningAnimationState.PositioningOpticCenter ->
            PositioningAnimationState.Idle

        PositioningAnimationState.PositioningDiameter ->
            PositioningAnimationState.PositioningOpticCenter

        PositioningAnimationState.PositioningFramesRight ->
            PositioningAnimationState.PositioningDiameter

        PositioningAnimationState.PositioningBridgePivot ->
            PositioningAnimationState.PositioningFramesRight

        PositioningAnimationState.PositioningCheckMiddle ->
            PositioningAnimationState.PositioningBridgePivot

        PositioningAnimationState.PositioningBaseLeft ->
            PositioningAnimationState.PositioningCheckMiddle

        PositioningAnimationState.PositioningBaseRight ->
            PositioningAnimationState.PositioningBaseLeft

        PositioningAnimationState.PositioningBaseTop ->
            PositioningAnimationState.PositioningBaseRight

        PositioningAnimationState.PositioningBaseBottom ->
            PositioningAnimationState.PositioningBaseTop

        PositioningAnimationState.PositioningCheckLeft ->
            PositioningAnimationState.PositioningBaseBottom

        PositioningAnimationState.PositioningCheckRight ->
            PositioningAnimationState.PositioningCheckLeft

        PositioningAnimationState.PositioningCheckTop ->
            PositioningAnimationState.PositioningCheckRight

        PositioningAnimationState.PositioningCheckBottom ->
            PositioningAnimationState.PositioningCheckTop

        PositioningAnimationState.PositioningFramesLeft ->
            PositioningAnimationState.PositioningCheckBottom

        PositioningAnimationState.PositioningFramesTop ->
            PositioningAnimationState.PositioningFramesLeft

        PositioningAnimationState.PositioningFramesBottom ->
            PositioningAnimationState.PositioningFramesTop

        PositioningAnimationState.PositioningTopPointAngle ->
            PositioningAnimationState.PositioningFramesBottom

        PositioningAnimationState.PositioningTopPointSize ->
            PositioningAnimationState.PositioningTopPointAngle

        PositioningAnimationState.PositioningBottomPointAngle ->
            PositioningAnimationState.PositioningTopPointSize

        PositioningAnimationState.PositioningBottomPointSize ->
            PositioningAnimationState.PositioningBottomPointAngle

        PositioningAnimationState.DrawAll ->
            PositioningAnimationState.PositioningBottomPointSize

        PositioningAnimationState.Idle ->
            PositioningAnimationState.DrawAll

        PositioningAnimationState.CheckError ->
            PositioningAnimationState.Idle
    }
}
