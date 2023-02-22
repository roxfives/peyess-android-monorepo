package com.peyess.salesapp.screen.sale.prescription_picture.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionInsertResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.feature.prescription.model.PrescriptionPicture
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import java.time.ZonedDateTime

data class PrescriptionPictureState(
    val activeServiceOrderAsync: Async<ActiveServiceOrderResponse> = Uninitialized,
    val serviceOrderId: String = "",

    val currentPrescription: Async<PrescriptionEntity> = Uninitialized,

    val createPrescriptionResponseAsync: Async<LocalPrescriptionInsertResponse> = Uninitialized,
    val prescriptionResponseAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescriptionResponse: PrescriptionPicture = PrescriptionPicture(),

    val professionalIdInput: String = "",
    val professionalNameInput: String = "",
): MavericksState {
    val isCopy = prescriptionResponse.isCopy
    val pictureUri = prescriptionResponse.pictureUri
    val prescriptionDate = prescriptionResponse.prescriptionDate

    private val tomorrow = ZonedDateTime.now().plusDays(1)
    private val canGoNextWithoutCopy = prescriptionDate.isBefore(tomorrow)
            && pictureUri != Uri.EMPTY
            && professionalIdInput.isNotEmpty()
            && professionalNameInput.isNotEmpty()
            && !isCopy

    private val canGoNextWithCopy = pictureUri != Uri.EMPTY && isCopy

    val canGoNext = canGoNextWithCopy || canGoNextWithoutCopy

    val isLoading = currentPrescription is Loading
}