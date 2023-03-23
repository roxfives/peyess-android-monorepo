package com.peyess.salesapp.screen.edit_service_order.prescription.prescription_data.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.constants.maxAddition
import com.peyess.salesapp.constants.maxAxis
import com.peyess.salesapp.constants.maxCylindrical
import com.peyess.salesapp.constants.maxPrismAxis
import com.peyess.salesapp.constants.maxPrismDegree
import com.peyess.salesapp.constants.maxSpherical
import com.peyess.salesapp.constants.minAddition
import com.peyess.salesapp.constants.minAxis
import com.peyess.salesapp.constants.minCylindrical
import com.peyess.salesapp.constants.minPrismAxis
import com.peyess.salesapp.constants.minPrismDegree
import com.peyess.salesapp.constants.minSpherical
import com.peyess.salesapp.constants.stepAddition
import com.peyess.salesapp.constants.stepAxis
import com.peyess.salesapp.constants.stepCylindrical
import com.peyess.salesapp.constants.stepPrismAxis
import com.peyess.salesapp.constants.stepPrismDegree
import com.peyess.salesapp.constants.stepSpherical
import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.feature.prescription.utils.messageFor
import com.peyess.salesapp.screen.sale.prescription_data.adapter.toPrescriptionData
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.typing.prescription.PrismPosition
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditPrescriptionDataViewModel, EditPrescriptionDataState>
private typealias EditPrescriptionDataViewModelFactory =
        MavericksViewModelFactory<EditPrescriptionDataViewModel, EditPrescriptionDataState>

class EditPrescriptionDataViewModel @AssistedInject constructor(
    @Assisted initialState: EditPrescriptionDataState,
    private val salesApplication: SalesApplication,
    private val editServiceOrderRepository: EditServiceOrderRepository,
    private val editPrescriptionRepository: EditPrescriptionRepository,
): MavericksViewModel<EditPrescriptionDataState>(initialState) {

    init {
        onEach(EditPrescriptionDataState::serviceOrderId) { loadServiceOrder(it) }

        onEach(EditPrescriptionDataState::prescriptionId) { loadPrescription(it) }

        onEach(EditPrescriptionDataState::lensTypeCategoryName) { loadGeneralMessage(it) }

        onEach(
            EditPrescriptionDataState::prescriptionResponse,
            EditPrescriptionDataState::clientName,
        ) { _, _ ->
            mikeMessageAmetropie()
        }

        onAsync(EditPrescriptionDataState::serviceOrderResponseAsync) {
            processServiceOrderResponse(it)
        }

        onAsync(EditPrescriptionDataState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }
    }

    private fun loadServiceOrder(serviceOrderId: String) {
        editServiceOrderRepository
            .streamServiceOrderById(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(serviceOrderResponseAsync = it)
            }
    }

    private fun processServiceOrderResponse(
        response: EditServiceOrderFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = { copy(serviceOrderResponseAsync = Fail(it.error)) },
            ifRight = { copy(serviceOrderResponse = it) },
        )
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
            ifRight = { copy(prescriptionResponse = it.toPrescriptionData()) },
        )
    }

    private fun loadGeneralMessage(lensTypeCategoryName: LensTypeCategoryName) = setState {
        copy(generalMessage = messageFor(lensTypeCategoryName, salesApplication))
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

    fun increaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateSphericalLeft(it.prescriptionId, newValue)
        }
    }

    fun decreaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateSphericalLeft(it.prescriptionId, newValue)
        }
    }

    fun increaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateSphericalRight(it.prescriptionId, newValue)
        }
    }

    fun decreaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateSphericalRight(it.prescriptionId, newValue)
        }
    }

    fun increaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateCylindricalLeft(it.prescriptionId, newValue)
        }
    }

    fun decreaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateCylindricalLeft(it.prescriptionId, newValue)
        }
    }

    fun increaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateCylindricalRight(it.prescriptionId, newValue)
        }
    }

    fun decreaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateCylindricalRight(it.prescriptionId, newValue)
        }
    }

    fun increaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAxisLeft(it.prescriptionId, newValue)
        }
    }

    fun decreaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAxisLeft(it.prescriptionId, newValue)
        }
    }

    fun increaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAxisRight(it.prescriptionId, newValue)
        }
    }

    fun decreaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAxisRight(it.prescriptionId, newValue)
        }
    }

    fun increaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAdditionLeft(it.prescriptionId, newValue)
        }
    }

    fun decreaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAdditionLeft(it.prescriptionId, newValue)
        }
    }

    fun increaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAdditionRight(it.prescriptionId, newValue)
        }
    }

    fun decreaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateAdditionRight(it.prescriptionId, newValue)
        }
    }

    fun increasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismDegreeLeft(it.prescriptionId, newValue)
        }
    }

    fun decreasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismDegreeLeft(it.prescriptionId, newValue)
        }
    }

    fun increasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismDegreeRight(it.prescriptionId, newValue)
        }
    }

    fun decreasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismDegreeRight(it.prescriptionId, newValue)
        }
    }

    fun increasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismAxisLeft(it.prescriptionId, newValue)
        }
    }

    fun decreasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismAxisLeft(it.prescriptionId, newValue)
        }
    }

    fun increasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismAxisRight(it.prescriptionId, newValue)
        }
    }

    fun decreasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)

        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismAxisRight(it.prescriptionId, newValue)
        }
    }

    fun setPrismPositionLeft(position: PrismPosition) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismPositionLeft(it.prescriptionId, position)
        }
    }

    fun setPrismPositionRight(position: PrismPosition) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updatePrismPositionRight(it.prescriptionId, position)
        }
    }

    fun toggleHasPrism() = withState {
        viewModelScope.launch(Dispatchers.IO) {
            editPrescriptionRepository.updateHasPrism(
                it.prescriptionId,
                !it.prescriptionResponse.hasPrism,
            )
        }
    }

    @AssistedFactory
    interface Factory : ViewModelFactory {
        override fun create(state: EditPrescriptionDataState): EditPrescriptionDataViewModel
    }

    companion object: EditPrescriptionDataViewModelFactory by hiltMavericksViewModelFactory()
}