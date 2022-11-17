package com.peyess.salesapp.feature.sale.frames.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity

sealed class Eye {
    object Left: Eye()
    object Right: Eye()
    object None: Eye()

    fun toName() = toName(this)

    companion object {
        val allOptions by lazy {
            listOf(Left, Right, None)
        }

        fun toName(eye: Eye?): String {
            return when(eye) {
                is Right -> "right"
                is Left -> "left"
                else -> "none"
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

    val positioningDataLeft: Async<PositioningEntity> = Uninitialized,
    val positioningDataRight: Async<PositioningEntity> = Uninitialized,

    val idealBaseMessageAsync: Async<String> = Uninitialized,
    val idealBaseAnimationResource: Async<Int> = Uninitialized,

    val showMike: Boolean = false,
    val mikeMessage: String = "",

    val landingMikeMessage: String = "",
    val hasSetFrames: Boolean = false
): MavericksState {
    val _currentFramesData: FramesEntity? = currentFramesData.invoke()

    val idealBaseMessage = idealBaseMessageAsync.invoke() ?: ""

    val areFramesNew = _currentFramesData?.areFramesNew ?: true
    val description = _currentFramesData?.description ?: ""
    val info = _currentFramesData?.framesInfo ?: ""
    val reference = _currentFramesData?.reference ?: ""
    val value = _currentFramesData?.value ?: 0.0
    val tagCode = _currentFramesData?.tagCode ?: ""
    val framesType = _currentFramesData?.type

    val pictureUriLeftEye = if (positioningDataLeft is Success) {
        positioningDataLeft.invoke().picture
    } else {
        Uri.EMPTY
    }
    val pictureUriRightEye = if (positioningDataRight is Success) {
        positioningDataRight.invoke().picture
    } else {
        Uri.EMPTY
    }
}
