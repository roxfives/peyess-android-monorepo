package com.peyess.salesapp.screen.sale.frames.data.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.frames.UpdateFramesResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.screen.sale.frames.data.model.Frames
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.typing.frames.FramesType
import java.math.BigDecimal

data class FramesDataState(
    val serviceOrderId: String = "",

    val loadPrescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescription: LocalPrescriptionDocument = LocalPrescriptionDocument(),

    val activeServiceOrderResponseAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrder: ActiveSOEntity = ActiveSOEntity(),

    val mikeMessage: String = "",
    val showMike: Boolean = false,

    val loadFramesResponseAsync: Async<LocalFramesRepositoryResponse> = Uninitialized,
    val frames: Frames = Frames(),

    val areFramesNewInput: Boolean = false,
    val designInput: String = "",
    val infoInput: String = "",
    val referenceInput: String = "",
    val valueInput: BigDecimal = BigDecimal.ZERO,
    val tagCodeInput: String = "",
    val framesTypeInput: FramesType = FramesType.None,

    val finishSettingFramesAsync: Async<UpdateFramesResponse> = Uninitialized,
    val finishedSettingFrames: Boolean = false,
): MavericksState {
    val isUpdatingFrames = finishSettingFramesAsync is Loading
}
