package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_symptoms.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_data.state.EditPrescriptionDataState
import com.peyess.salesapp.screen.sale.prescription_data.adapter.toPrescriptionData
import com.peyess.salesapp.screen.sale.service_order.adapter.toPrescription
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditPrescriptionSymptomsViewModel, EditPrescriptionSymptomsState>
private typealias EditPrescriptionViewModelFactory =
        MavericksViewModelFactory<EditPrescriptionSymptomsViewModel, EditPrescriptionSymptomsState>

class EditPrescriptionSymptomsViewModel @AssistedInject constructor(
    @Assisted initialState: EditPrescriptionSymptomsState,
    private val salesApplication: SalesApplication,
    private val editServiceOrderRepository: EditServiceOrderRepository,
    private val editPrescriptionRepository: EditPrescriptionRepository,
): MavericksViewModel<EditPrescriptionSymptomsState>(initialState) {

    init {
        onEach(EditPrescriptionSymptomsState::serviceOrderId) { loadServiceOrder(it) }
        onEach(EditPrescriptionSymptomsState::prescriptionId) { loadPrescription(it) }

        onEach(
            EditPrescriptionSymptomsState::prescriptionResponse,
            EditPrescriptionSymptomsState::clientName,
        ) { _, _ ->
            mikeMessageAmetropie()
        }

        onAsync(EditPrescriptionSymptomsState::serviceOrderResponseAsync) {
            processServiceOrderResponse(it)
        }
        onAsync(EditPrescriptionSymptomsState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }
    }

    private fun loadServiceOrder(serviceOrderId: String) {
        suspend {
            editServiceOrderRepository.serviceOrderById(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(serviceOrderResponseAsync = it)
        }
    }

    private fun processServiceOrderResponse(
        response: EditServiceOrderFetchResponse
    ) = setState {
        response.fold(
            ifLeft = { copy(serviceOrderResponseAsync = Fail(it.error)) },
            ifRight = { copy(serviceOrderResponse = it) },
        )
    }

    private fun loadPrescription(prescriptionId: String) {
        suspend {
            editPrescriptionRepository.prescriptionById(prescriptionId)
        }.execute(Dispatchers.IO) {
            copy(prescriptionResponseAsync = it)
        }
    }

    private fun processPrescriptionResponse(
        response: EditPrescriptionFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = { copy(prescriptionResponseAsync = Fail(it.error)) },
            ifRight = { copy(prescriptionResponse = it) },
        )
    }

    private fun mikeMessageAmetropie() = setState {
        val SEPARATOR = " com "
        val AND = " e "

        var stringBuilder = StringBuilder()
        val baseMessage: String = salesApplication.getString(R.string.mike_refractive_errors_base)
        var leftEyeErrors = ""
        var rightEyeErrors = ""
        var eyeErrors = ""

        val leftErrors: MutableList<String> = ArrayList()
        val rightErrors: MutableList<String> = ArrayList()

        if (hasHypermetropiaLeft) {
            leftErrors.add(salesApplication.getString(R.string.hypermetropia))
        }
        if (hasMyopiaLeft) {
            leftErrors.add(salesApplication.getString(R.string.myopia))
        }
        if (hasAstigmatismLeft) {
            leftErrors.add(salesApplication.getString(R.string.astigmatism))
        }
        if (hasPresbyopiaLeft) {
            leftErrors.add(salesApplication.getString(R.string.presbyopia))
        }

        if (hasHypermetropiaRight) {
            rightErrors.add(salesApplication.getString(R.string.hypermetropia))
        }
        if (hasMyopiaRight) {
            rightErrors.add(salesApplication.getString(R.string.myopia))
        }
        if (hasAstigmatismRight) {
            rightErrors.add(salesApplication.getString(R.string.astigmatism))
        }
        if (hasPresbyopiaRight) {
            rightErrors.add(salesApplication.getString(R.string.presbyopia))
        }

        if (leftErrors.size > 0) {
            for (i in 0 until leftErrors.size - 1) {
                stringBuilder.append(leftErrors[i])
                stringBuilder.append(SEPARATOR)
            }
            stringBuilder.append(leftErrors[leftErrors.size - 1])
            leftEyeErrors = stringBuilder.toString()
            leftEyeErrors = salesApplication.getString(R.string.mike_refractive_errors_left_eye).format(leftEyeErrors)
            rightEyeErrors = AND
        }

        if (rightErrors.size > 0) {
            stringBuilder = StringBuilder()
            for (i in 0 until rightErrors.size - 1) {
                stringBuilder.append(rightErrors[i])
                stringBuilder.append(SEPARATOR)
            }
            stringBuilder.append(rightErrors[rightErrors.size - 1])
            rightEyeErrors += stringBuilder.toString()
            rightEyeErrors = salesApplication.getString(R.string.mike_refractive_errors_right_eye).format(rightEyeErrors)
        }

        eyeErrors = leftEyeErrors + rightEyeErrors
        // TODO: use string resource for fallback
        val greeting = clientName.ifBlank { "Olá" }
        val message = if (eyeErrors == "") {
            salesApplication.getString(R.string.mike_refractive_errors_none).format(greeting)
        } else {
            baseMessage.format(greeting, eyeErrors)
        }

        copy(mikeMessageAmetropies = message)
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

    @AssistedFactory
    interface Factory : ViewModelFactory {
        override fun create(state: EditPrescriptionSymptomsState): EditPrescriptionSymptomsViewModel
    }

    companion object: EditPrescriptionViewModelFactory by hiltMavericksViewModelFactory()
}