package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_picture.state

import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.screen.sale.prescription_picture.adapter.toPrescriptionPicture
import com.peyess.salesapp.utils.time.toZonedDateTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditPrescriptionPictureViewModel, EditPrescriptionPictureState>
private typealias EditPrescriptionPictureViewModelFactory =
        MavericksViewModelFactory<EditPrescriptionPictureViewModel, EditPrescriptionPictureState>

class EditPrescriptionPictureViewModel @AssistedInject constructor(
    @Assisted initialState: EditPrescriptionPictureState,
    private val editPrescriptionRepository: EditPrescriptionRepository,
): MavericksViewModel<EditPrescriptionPictureState>(initialState) {

    init {
        onEach(EditPrescriptionPictureState::prescriptionId) { loadPrescription(it) }

        onAsync(EditPrescriptionPictureState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }
    }

    private fun loadPrescription(prescriptionId: String) {
        editPrescriptionRepository
            .streamPrescriptionById(prescriptionId)
            .execute(Dispatchers.IO) {
                copy(prescriptionResponseAsync = it)
            }
    }

    private fun processPrescriptionResponse(
        response: EditPrescriptionFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = { copy(prescriptionResponseAsync = Fail(it.error)) },
            ifRight = {
                copy(
                    prescriptionResponse = it.toPrescriptionPicture(),
                    professionalIdInput = professionalIdInput.ifBlank { it.professionalId },
                    professionalNameInput = professionalNameInput.ifBlank { it.professionalName },
                )
            },
        )
    }

    fun onSetSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onSetServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun onSetPrescriptionId(prescriptionId: String) = setState {
        copy(prescriptionId = prescriptionId)
    }

    fun onPictureTaken(uri: Uri?) = withState {
        if (uri == null) {
            return@withState
        }

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePicture(it.prescriptionId, uri)
        }
    }

    fun onCopyChanged(isCopy: Boolean) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateIsCopy(it.prescriptionId, isCopy)
        }
    }

    fun onDatePicked(date: LocalDate) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrescriptionDate(
                id = it.prescriptionId,
                prescriptionDate = date.toZonedDateTime(),
            )
        }
    }

    fun onProfessionalNameChanged(professionalName: String) = setState {
        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateProfessionalName(prescriptionId, professionalName)
        }

        copy(professionalNameInput = professionalName)
    }

    fun onProfessionalIdChanged(professionalId: String) = setState {
        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateProfessionalId(prescriptionId, professionalId)
        }

        copy(professionalIdInput = professionalId)
    }

    @AssistedFactory
    interface Factory : ViewModelFactory {
        override fun create(state: EditPrescriptionPictureState): EditPrescriptionPictureViewModel
    }

    companion object: EditPrescriptionPictureViewModelFactory by hiltMavericksViewModelFactory()
}
