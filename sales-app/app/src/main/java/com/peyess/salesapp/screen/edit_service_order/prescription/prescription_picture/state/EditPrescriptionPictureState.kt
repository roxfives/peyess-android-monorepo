package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_picture.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.feature.prescription.model.PrescriptionPicture
import java.time.LocalDate
import java.time.ZonedDateTime

data class EditPrescriptionPictureState(
    val saleId: String = "",
    val serviceOrderId: String = "",
    val prescriptionId: String = "",

    val currentPrescription: Async<PrescriptionEntity> = Uninitialized,

    val prescriptionResponseAsync: Async<EditPrescriptionFetchResponse> = Uninitialized,
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