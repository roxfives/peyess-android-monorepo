package com.peyess.salesapp.feature.sale.frames.state

import android.net.Uri
import androidx.annotation.RawRes
import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.typing.frames.FramesType

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
    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrderId: String = "",

    val prescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescriptionResponse: LocalPrescriptionDocument = LocalPrescriptionDocument(),

//    val currentFramesData: Async<FramesEntity> = Uninitialized,
    val loadFramesResponseAsync: Async<Either<Throwable, FramesDocument>> = Uninitialized,
    val currentFramesAsync: Async<FramesEntity> = Uninitialized,
    val currentFrames: FramesEntity = FramesEntity(),

    val areFramesNewInput: Boolean = false,
    val descriptionInput: String = "",
    val infoInput: String = "",
    val referenceInput: String = "",
    val valueInput: Double = 0.0,
    val tagCodeInput: String = "",
    val framesTypeInput: FramesType = FramesType.None,

    val positioningDataLeft: Async<PositioningEntity> = Uninitialized,
    val positioningDataRight: Async<PositioningEntity> = Uninitialized,

    val idealBaseMessage: String = "",
    val idealBaseAnimationResource: Int = 0,

    val showMike: Boolean = false,
    val mikeMessage: String = "",

    val landingMikeMessage: String = "",
    val hasSetFrames: Boolean = false,

    val finishedSettingFrames: Boolean = false,
): MavericksState {
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
