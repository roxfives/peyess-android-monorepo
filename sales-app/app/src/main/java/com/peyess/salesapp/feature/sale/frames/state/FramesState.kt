package com.peyess.salesapp.feature.sale.frames.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity

sealed class Eye {
    object Left: Eye()
    object Right: Eye()
    object None: Eye()

    companion object {
        fun toName(eye: Eye?): String {
            return when(eye) {
                is Right -> "Right"
                is Left -> "Left"
                else -> "None"
            }
        }

        fun toEye(name: String?): Eye {
            return when(name?.lowercase()) {
                "left" -> Left
                "right" -> Right
                else -> None
            }
        }
    }
}

data class FramesState(
    val currentPrescriptionData: Async<PrescriptionDataEntity> = Uninitialized,
    val currentFramesData: Async<FramesEntity> = Uninitialized,

    val idealBaseMessage: Async<String> = Uninitialized,
    val idealBaseAnimationResource: Async<Int> = Uninitialized,

    val showMike: Boolean = false,
    val mikeMessage: String = "",
): MavericksState {
    val _currentFramesData: FramesEntity? = currentFramesData.invoke()

    val areFramesNew = _currentFramesData?.areFramesNew ?: true
    val description = _currentFramesData?.description ?: ""
    val reference = _currentFramesData?.reference ?: ""
    val value = _currentFramesData?.value ?: 0.0
    val tagCode = _currentFramesData?.tagCode ?: ""
    val framesType = _currentFramesData?.type
}
