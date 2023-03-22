package com.peyess.salesapp.screen.sale.prescription_picture.state

import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.error.PrescriptionNotFound
import com.peyess.salesapp.screen.sale.prescription_picture.adapter.toLocalPrescriptionDocument
import com.peyess.salesapp.screen.sale.prescription_picture.adapter.toPrescriptionPicture
import com.peyess.salesapp.feature.prescription.model.PrescriptionPicture
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.utils.time.toZonedDateTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class PrescriptionPictureViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionPictureState,
    private val saleRepository: SaleRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
): MavericksViewModel<PrescriptionPictureState>(initialState) {

    init {
        loadServiceOrder()

        onEach(PrescriptionPictureState::serviceOrderId) {
            loadPrescription(it)
        }

        onAsync(PrescriptionPictureState::activeServiceOrderAsync) {
            processServiceOrderResponse(it)
        }

        onAsync(PrescriptionPictureState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }
    }

    private fun loadServiceOrder() = withState {
        suspend {
            saleRepository.currentServiceOrder()
        }.execute(Dispatchers.IO) {
            copy(activeServiceOrderAsync = it)
        }
    }

    private fun processServiceOrderResponse(response: ActiveServiceOrderResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    activeServiceOrderAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(serviceOrderId = it.id)
            }
        )
    }

    private fun loadPrescription(serviceOrderId: String) = withState {
        localPrescriptionRepository
            .streamPrescriptionForServiceOrder(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(prescriptionResponseAsync = it)
            }
    }

    private fun processPrescriptionResponse(response: LocalPrescriptionResponse) = setState {
        response.fold(
            ifLeft = {
                if (it is PrescriptionNotFound) {
                    createPrescription()
                    copy(prescriptionResponseAsync = Uninitialized)
                } else {
                    copy(
                        prescriptionResponseAsync = Fail(
                            it.error ?: Throwable(it.description)
                        )
                    )
                }
            },

            ifRight = {
                copy(prescriptionResponse = it.toPrescriptionPicture())
            }
        )
    }

    private fun createPrescription() = withState {
        suspend {
            localPrescriptionRepository.createPrescriptionForServiceOrder(it.serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(createPrescriptionResponseAsync = it)
        }
    }

    private fun updatePrescription(prescription: PrescriptionPicture) {
        viewModelScope.launch(Dispatchers.IO) {
            localPrescriptionRepository
                .updatePrescription(prescription.toLocalPrescriptionDocument())
        }
    }

    fun onPictureTaken(uri: Uri?) = withState {
        if (uri == null) {
            return@withState
        }

        val prescription = it.prescriptionResponse.copy(pictureUri = uri)
        if (prescription.id.isNotBlank()) {
            updatePrescription(prescription)
        }
    }

    fun onCopyChanged(isCopy: Boolean) = withState {
        val prescription = it.prescriptionResponse.copy(isCopy = isCopy)

        if (prescription.id.isNotBlank()) {
            updatePrescription(prescription)
        }
    }

    fun onDatePicked(date: LocalDate) = withState {
        val prescription = it.prescriptionResponse
            .copy(prescriptionDate = date.toZonedDateTime())

        if (prescription.id.isNotBlank()) {
            updatePrescription(prescription)
        }
    }

    fun onProfessionalNameChanged(professionalName: String) = withState {
        val prescription = it.prescriptionResponse
            .copy(professionalName = professionalName)

        if (prescription.id.isNotBlank()) {
            updatePrescription(prescription)
        }

        setState {
            copy(professionalNameInput = professionalName)
        }
    }

    fun onProfessionalIdChanged(professionalId: String) = withState {
        val prescription = it.prescriptionResponse
            .copy(professionalId = professionalId)

        if (prescription.id.isNotBlank()) {
            updatePrescription(prescription)
        }

        setState {
            copy(professionalIdInput = professionalId)
        }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PrescriptionPictureViewModel, PrescriptionPictureState> {
        override fun create(state: PrescriptionPictureState): PrescriptionPictureViewModel
    }

    companion object: MavericksViewModelFactory<PrescriptionPictureViewModel, PrescriptionPictureState>
        by hiltMavericksViewModelFactory()
}