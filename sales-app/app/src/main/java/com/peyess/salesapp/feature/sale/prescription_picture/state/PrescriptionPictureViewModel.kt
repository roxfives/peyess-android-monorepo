package com.peyess.salesapp.feature.sale.prescription_picture.state

import android.net.Uri
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.time.LocalDate

class PrescriptionPictureViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionPictureState,
    private val saleRepository: SaleRepository,
): MavericksViewModel<PrescriptionPictureState>(initialState) {

    init {
        loadInitData()
    }

    private fun loadInitData() = withState {
        saleRepository.currentPrescriptionPicture().execute {
            if (it is Success) {
                overwriteScreenState(it.invoke())
            }

            copy(currentPrescription = it)
        }
    }

    private fun overwriteScreenState(prescription: PrescriptionPictureEntity) = setState {
        copy(
            pictureUri = prescription.pictureUri,
            prescriptionDate = prescription.prescriptionDate,

            professionalId = prescription.professionalId,
            professionalName = prescription.professionalName,
        )
    }


    fun onPictureTaken(uri: Uri?) = setState {
        val prescriptionPicture = this.currentPrescription.invoke()

        if (prescriptionPicture != null) {
            saleRepository.updatePrescriptionPicture(
                prescriptionPicture.copy(pictureUri = uri?: this.pictureUri)
            )
        }

        copy(pictureUri = uri ?: this.pictureUri)
    }

    fun onDatePicked(date: LocalDate) = setState {
        val prescriptionPicture = this.currentPrescription.invoke()

        if (prescriptionPicture != null) {
            saleRepository.updatePrescriptionPicture(
                prescriptionPicture.copy(prescriptionDate = date)
            )
        }

        copy(prescriptionDate = date)
    }

    fun onProfessionalNameChanged(professionalName: String) = setState {
        val prescriptionPicture = this.currentPrescription.invoke()

        if (prescriptionPicture != null) {
            saleRepository.updatePrescriptionPicture(
                prescriptionPicture.copy(professionalName = professionalName)
            )
        }

        copy(professionalName = professionalName)
    }

    fun onProfessionalIdChanged(professionalId: String) = setState {
        val prescriptionPicture = this.currentPrescription.invoke()

        if (prescriptionPicture != null) {
            saleRepository.updatePrescriptionPicture(
                prescriptionPicture.copy(professionalId = professionalId)
            )
        }

        copy(professionalId = professionalId)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PrescriptionPictureViewModel, PrescriptionPictureState> {
        override fun create(state: PrescriptionPictureState): PrescriptionPictureViewModel
    }

    companion object: MavericksViewModelFactory<PrescriptionPictureViewModel, PrescriptionPictureState>
        by hiltMavericksViewModelFactory()
}