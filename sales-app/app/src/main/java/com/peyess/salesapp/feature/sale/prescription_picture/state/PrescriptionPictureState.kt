package com.peyess.salesapp.feature.sale.prescription_picture.state

import android.net.Uri
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.dao.local_sale.prescription_picture.PrescriptionPictureEntity
import java.time.LocalDate

data class PrescriptionPictureState(
    val currentPrescription: Async<PrescriptionPictureEntity> = Uninitialized,

    @PersistState val isCopy: Boolean = false,

    @PersistState val pictureUri: Uri = Uri.EMPTY,
    @PersistState val prescriptionDate: LocalDate = LocalDate.now(),

    @PersistState val professionalId: String = "",
    @PersistState val professionalName: String = "",
): MavericksState {
    private val canGoNextWithoutCopy = prescriptionDate.isBefore(LocalDate.now().plusDays(1))
            && pictureUri != Uri.EMPTY
            && professionalId.isNotEmpty()
            && professionalName.isNotEmpty()
            && !isCopy

    private val canGoNextWithCopy = pictureUri != Uri.EMPTY && isCopy

    val canGoNext = canGoNextWithCopy || canGoNextWithoutCopy

    val isLoading = currentPrescription is Loading
}