package com.peyess.salesapp.feature.sale.frames.landing.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.frames.UpdateFramesNewResponse
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.typing.frames.FramesType

data class FramesLandingState(
    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrderId: String = "",

    val prescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescriptionResponse: LocalPrescriptionDocument = LocalPrescriptionDocument(),

    val loadFramesResponseAsync: Async<LocalFramesRepositoryResponse> = Uninitialized,
    val frames: FramesDocument = FramesDocument(),

    val positioningDataLeft: Async<PositioningEntity> = Uninitialized,
    val positioningDataRight: Async<PositioningEntity> = Uninitialized,

    val idealBaseMessage: String = "",
    val idealBaseAnimationResource: Int = 0,

    val landingMikeMessage: String = "",

    val setFramesNewResponseAsync: Async<UpdateFramesNewResponse> = Uninitialized,
    val hasFinishedSettingFramesType: Boolean = false,
    val finishedSettingFrames: Boolean = false,
): MavericksState {
    val hasSetFrames = frames.type != FramesType.None
    val areFramesNew = frames.areFramesNew

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
