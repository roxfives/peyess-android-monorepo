package com.peyess.salesapp.screen.sale.prescription_data.state


import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
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
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.error.PrescriptionNotFound
import com.peyess.salesapp.screen.sale.prescription_data.adapter.toLocalPrescriptionDocument
import com.peyess.salesapp.screen.sale.prescription_data.adapter.toPrescriptionData
import com.peyess.salesapp.feature.prescription.model.PrescriptionData
import com.peyess.salesapp.feature.prescription.utils.animationFor
import com.peyess.salesapp.feature.prescription.utils.messageFor
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.typing.prescription.PrismPosition
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class PrescriptionDataViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionDataState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
): MavericksViewModel<PrescriptionDataState>(initialState) {

    init {
        loadServiceOrderData()

        onEach(PrescriptionDataState::lensTypeCategoryName) { loadMessage(it) }

        onAsync(PrescriptionDataState::activeServiceOrderResponseAsync) {
            processServiceOrderDataResponse(it)
        }

        onAsync(PrescriptionDataState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }

        onEach(PrescriptionDataState::activeServiceOrderResponse) {
            if (it.id.isNotBlank()) {
                loadPrescription(it.id)
            }
        }
        
        onEach(
            PrescriptionDataState::lensTypeCategoryName,
            PrescriptionDataState::clientName,
            PrescriptionDataState::prescriptionResponse,
        ) { category, _, _ ->
            updateAnimation(category)
            mikeMessageAmetropie()
        }
    }

    private fun processServiceOrderDataResponse(response: ActiveServiceOrderResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    activeServiceOrderResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(activeServiceOrderResponse = it) },
        )
    }

    private fun loadServiceOrderData() {
        suspend {
            saleRepository.currentServiceOrder()
        }.execute {
            copy(activeServiceOrderResponseAsync = it)
        }
    }

    private fun loadMessage(lensTypeCategoryName: LensTypeCategoryName) = setState {
        copy(generalMessage = messageFor(lensTypeCategoryName, salesApplication))
    }

    private fun updateAnimation(category: LensTypeCategoryName) = setState {
        copy(animationId = animationFor(category))
    }

    private fun mikeMessageAmetropie() = withState {
        Timber.i("Using type ${it.lensTypeCategoryName}")

        val SEPARATOR = " com "
        val AND = " e "

        var stringBuilder = StringBuilder()
        val baseMessage: String = salesApplication.getString(R.string.mike_refractive_errors_base)
        var leftEyeErrors = ""
        var rightEyeErrors = ""
        var eyeErrors = ""

        val leftErrors: MutableList<String> = ArrayList()
        val rightErrors: MutableList<String> = ArrayList()

        if (it.hasHypermetropiaLeft) {
            leftErrors.add(salesApplication.getString(R.string.hypermetropia))
        }
        if (it.hasMyopiaLeft) {
            leftErrors.add(salesApplication.getString(R.string.myopia))
        }
        if (it.hasAstigmatismLeft) {
            leftErrors.add(salesApplication.getString(R.string.astigmatism))
        }
        if (it.hasPresbyopiaLeft) {
            leftErrors.add(salesApplication.getString(R.string.presbyopia))
        }

        if (it.hasHypermetropiaRight) {
            rightErrors.add(salesApplication.getString(R.string.hypermetropia))
        }
        if (it.hasMyopiaRight) {
            rightErrors.add(salesApplication.getString(R.string.myopia))
        }
        if (it.hasAstigmatismRight) {
            rightErrors.add(salesApplication.getString(R.string.astigmatism))
        }
        if (it.hasPresbyopiaRight) {
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
        val clientName = it.clientName.ifBlank { "Olá" }
        val message = if (eyeErrors == "") {
            salesApplication
                .getString(R.string.mike_refractive_errors_none)
                .format(clientName)
        } else {
            baseMessage.format(clientName, eyeErrors)
        }

        setState { copy(mikeMessageAmetropies = message) }
    }

    private fun createPrescription() = withState {
        suspend {
            localPrescriptionRepository.createPrescriptionForServiceOrder(it.serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(createPrescriptionResponseAsync = it)
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
                copy(
                    prescriptionResponse = it.toPrescriptionData(),
                )
            }
        )
    }

    private fun loadPrescription(serviceOrderId: String) {
        localPrescriptionRepository
            .streamPrescriptionForServiceOrder(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(prescriptionResponseAsync = it)
            }
    }

    private fun updatePrescription(prescription: PrescriptionData) {
        viewModelScope.launch(Dispatchers.IO) {
            localPrescriptionRepository
                .updatePrescription(prescription.toLocalPrescriptionDocument())
        }
    }

    fun increaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)
        val prescriptionData = it.prescriptionResponse
        
        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(sphericalLeft = newValue)
            
            updatePrescription(prescription)
        }
    }

    fun decreaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)
        val prescriptionData = it.prescriptionResponse
        
        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(sphericalLeft = newValue)
            
            updatePrescription(prescription)
        }
    }

    fun increaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(sphericalRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(sphericalRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun increaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(cylindricalLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(cylindricalLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun increaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(cylindricalRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(cylindricalRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun increaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(axisLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(axisLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun increaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(axisRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(axisRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun increaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(additionLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(additionLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun increaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(additionRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(additionRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun increasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismDegreeLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismDegreeLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun increasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismDegreeRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismDegreeRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun increasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismAxisLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismAxisLeft = newValue)

            updatePrescription(prescription)
        }
    }

    fun increasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismAxisRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun decreasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismAxisRight = newValue)

            updatePrescription(prescription)
        }
    }

    fun setPrismPositionLeft(position: PrismPosition) = withState {
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismPositionLeft = position)

            updatePrescription(prescription)
        }
    }

    fun setPrismPositionRight(position: PrismPosition) = withState {
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(prismPositionRight = position)

            updatePrescription(prescription)
        }
    }

    fun toggleHasPrism() = withState {
        val prescriptionData = it.prescriptionResponse

        if (prescriptionData.id.isNotBlank()) {
            val prescription = it.prescriptionResponse
                .copy(hasPrism = !it.hasPrism)

            updatePrescription(prescription)
        }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PrescriptionDataViewModel, PrescriptionDataState> {
        override fun create(state: PrescriptionDataState): PrescriptionDataViewModel
    }

    companion object: MavericksViewModelFactory<PrescriptionDataViewModel, PrescriptionDataState>
        by hiltMavericksViewModelFactory()
}
