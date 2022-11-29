package com.peyess.salesapp.feature.sale.frames_measure.animation.utils

import com.airbnb.lottie.compose.LottieCompositionSpec
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.frames_measure.state.HeadState

fun lottieCompositionFor(state: PositioningAnimationState, eye: Eye):
        LottieCompositionSpec.RawRes {
    if (eye == Eye.None) {
        return LottieCompositionSpec.RawRes(R.raw.lottie_measuring_adjust_left)
    }

    return when (state) {
        PositioningAnimationState.DrawAll ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_adjust_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_adjust_right)
            }
        PositioningAnimationState.Idle ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_adjust_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_adjust_right)
            }

        PositioningAnimationState.PositioningBottomPointAngle ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_bottom_angle_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_bottom_angle_positioning_right)
            }
        PositioningAnimationState.PositioningBottomPointSize ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_bottom_size_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_bottom_size_positioning_right)
            }
        PositioningAnimationState.PositioningCheckMiddle ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_center_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_center_positioning_right)
            }
        PositioningAnimationState.PositioningCheckLeft ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_left_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_left_positioning_right)
            }
        PositioningAnimationState.PositioningCheckRight ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_right_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_right_positioning_right)
            }
        PositioningAnimationState.PositioningCheckTop ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_top_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_top_positioning_right)
            }
        PositioningAnimationState.PositioningCheckBottom ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_bottom_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_bottom_positioning_right)
            }
        PositioningAnimationState.PositioningBridgePivot ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_bridge_pivot_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_bridge_pivot_positioning_right)
            }
        PositioningAnimationState.PositioningDiameter ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_diameter_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_diameter_positioning_right)
            }
        PositioningAnimationState.PositioningFramesBottom ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_bottom_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_bottom_positioning_right)
            }
        PositioningAnimationState.PositioningFramesLeft ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_left_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_left_positioning_right)
            }
        PositioningAnimationState.PositioningFramesRight ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_right_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_right_positioning_right)
            }
        PositioningAnimationState.PositioningFramesTop ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_top_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_frames_top_positioning_right)
            }
        PositioningAnimationState.PositioningOpticCenter ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_optic_center_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_optic_center_positioning_right)
            }
        PositioningAnimationState.PositioningBaseLeft ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_parameter_left_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_parameter_left_positioning_right)
            }
        PositioningAnimationState.PositioningBaseRight ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_parameter_right_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_parameter_right_positioning_right)
            }
        PositioningAnimationState.PositioningBaseTop ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_base_top_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_base_top_positioning_right)
            }
        PositioningAnimationState.PositioningBaseBottom ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_base_bottom_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_base_bottom_positioning_right)
            }
        PositioningAnimationState.PositioningTopPointAngle ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_top_angle_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_top_angle_positioning_right)
            }
        PositioningAnimationState.PositioningTopPointSize ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_top_size_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_point_top_size_positioning_right)
            }
        PositioningAnimationState.PositioningCheckMiddle ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_center_positioning_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_check_center_positioning_right)
            }
        PositioningAnimationState.CheckError ->
            if (eye == Eye.Left) {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_check_error_left)
            } else {
                LottieCompositionSpec.RawRes(R.raw.lottie_measuring_icon_check_error_right)
            }
    }
}

fun headCompositionFor(state: HeadState):
        LottieCompositionSpec.RawRes {
    return when (state) {
        HeadState.Idle ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_looking_for_someone)

        HeadState.NoPerson ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_looking_for_someone)
        HeadState.TooManyPeople ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_found_too_many)

        HeadState.Down ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_move_head_up)
        HeadState.Lifted ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_move_head_down)

        HeadState.TiltedLeft ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_tilt_head_right)
        HeadState.TiltedRight ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_tilt_head_left)

        HeadState.TurnedLeft ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_move_head_right)
        HeadState.TurnedRight ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_move_head_left)

        HeadState.LookingForward ->
            LottieCompositionSpec.RawRes(R.raw.lottie_measuring_looking_foward)
    }
}
