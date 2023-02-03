package com.peyess.salesapp.feature.sale.frames.landing.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.typing.frames.FramesType

data class FramesState(
    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrderId: String = "",

    val prescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescriptionResponse: LocalPrescriptionDocument = LocalPrescriptionDocument(),

//    val currentFramesData: Async<FramesEntity> = Uninitialized,
//    val loadFramesResponseAsync: Async<Either<Throwable, FramesDocument>> = Uninitialized,
    val loadFramesResponseAsync: Async<FramesEntity> = Uninitialized,
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

    val setFramesNewResponseAsync: Async<FramesEntity> = Uninitialized,
    val hasFinishedSettingFramesType: Boolean = false,
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
