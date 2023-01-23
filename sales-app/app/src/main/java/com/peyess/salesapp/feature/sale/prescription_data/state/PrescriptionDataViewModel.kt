package com.peyess.salesapp.feature.sale.prescription_data.state


import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
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
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.typing.prescription.PrismPosition
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import timber.log.Timber

class PrescriptionDataViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionDataState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
): MavericksViewModel<PrescriptionDataState>(initialState) {

    init {
        loadHasAddition()
        loadAnimation()
        loadMessage()
        loadInitPrescriptionData()
        loadClientName()
        loadLensTypeCategory()
        loadServiceOrderData()

        onAsync(PrescriptionDataState::activeServiceOrderResponseAsync) {
            processServiceOrderDataResponse(it)
        }

        onEach(
            PrescriptionDataState::hasAdditionAsync,
            PrescriptionDataState::currentPrescriptionData,
        ) { hasAddition, currPrescription ->
            if (hasAddition is Success && currPrescription != null) {
                Timber.i("Updating hasAddition $hasAddition")
                updateHasAddition(hasAddition.invoke())
            }
        }

        onEach { mikeMessageAmetropie() }
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

            ifRight = { copy(activeServiceOrderResponse = it) }
        )
    }

    private fun loadServiceOrderData() {
        suspend {
            saleRepository.currentServiceOrder()
        }.execute {
            copy(activeServiceOrderResponseAsync = it)
        }
    }

    private fun loadMessage() = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .map {
                messageFor(it.lensTypeCategoryName)
            }
            .execute(Dispatchers.IO) {
                copy(generalMessage = it)
            }
    }

    private fun messageFor(categoryName: LensTypeCategoryName?): String {
        return when (categoryName) {
            LensTypeCategoryName.Far -> salesApplication.stringResource(R.string.mike_message_far)
            LensTypeCategoryName.Multi -> salesApplication.stringResource(R.string.mike_message_multi)
            LensTypeCategoryName.Near -> salesApplication.stringResource(R.string.mike_message_near)
            null -> salesApplication.stringResource(R.string.mike_message_default)
        }
    }

    private fun loadHasAddition() = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .map { !it.isLensTypeMono }
            .execute(Dispatchers.IO) {
                Timber.i("Loading has addition $it")
                copy(hasAdditionAsync = it)
            }
    }

    private fun loadLensTypeCategory() = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .map { it.lensTypeCategoryName }
            .execute(Dispatchers.IO) {
                copy(lensTypeCategoryName = it)
            }
    }

    private fun loadAnimation() = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .map {
                animationFor(it.lensTypeCategoryName)
            }
            .execute(Dispatchers.IO) {
                copy(animationId = it)
            }
    }

    private fun loadClientName() = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .map { it.clientName }
            .execute { copy(clientName = it) }
    }

    private fun animationFor(categoryName: LensTypeCategoryName?): Int {
        return when (categoryName) {
            LensTypeCategoryName.Far -> R.raw.lottie_lens_far
            LensTypeCategoryName.Multi -> R.raw.lottie_lens_multi
            LensTypeCategoryName.Near -> R.raw.lottie_lens_near
            null -> R.raw.lottie_lens_far
        }
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
        val clientName = if (it.clientName is Success) it.clientName.invoke() else "cliente"
        val message = if (eyeErrors == "") {
            salesApplication.getString(R.string.mike_refractive_errors_none).format(clientName)
        } else {
            baseMessage.format(clientName, eyeErrors)
        }

        setState { copy(mikeMessageAmetropies = message) }
    }

    private fun loadInitPrescriptionData() = withState {
        saleRepository.currentPrescriptionData().execute {
            copy(currentPrescriptionDataAsync = it)
        }
    }

    private fun updateHasAddition(hasAddition: Boolean) = withState {
        Timber.i("Updating has addition ($hasAddition) with prescription ${it.currentPrescriptionData}")
        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(hasAddition = hasAddition)
            )
        }
    }


    fun increaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(sphericalLeft = newValue)
            )
        }
    }

    fun decreaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(sphericalLeft = newValue)
            )
        }
    }

    fun increaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(sphericalRight = newValue)
            )
        }
    }

    fun decreaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(sphericalRight = newValue)
            )
        }
    }

    fun increaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(cylindricalLeft = newValue)
            )
        }
    }

    fun decreaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(cylindricalLeft = newValue)
            )
        }
    }

    fun increaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(cylindricalRight = newValue)
            )
        }
    }

    fun decreaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(cylindricalRight = newValue)
            )
        }
    }

    fun increaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(axisLeft = newValue)
            )
        }
    }

    fun decreaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(axisLeft = newValue)
            )
        }
    }

    fun increaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(axisRight = newValue)
            )
        }
    }

    fun decreaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(axisRight = newValue)
            )
        }
    }

    fun increaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(additionLeft = newValue)
            )
        }
    }

    fun decreaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(additionLeft = newValue)
            )
        }
    }

    fun increaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(additionRight = newValue)
            )
        }
    }

    fun decreaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(additionRight = newValue)
            )
        }
    }

    fun increasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismDegreeLeft = newValue)
            )
        }
    }

    fun decreasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismDegreeLeft = newValue)
            )
        }
    }

    fun increasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismDegreeRight = newValue)
            )
        }
    }

    fun decreasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismDegreeRight = newValue)
            )
        }
    }

    fun increasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismAxisLeft = newValue)
            )
        }
    }

    fun decreasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismAxisLeft = newValue)
            )
        }
    }

    fun increasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismAxisRight = newValue)
            )
        }
    }

    fun decreasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)

        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismAxisRight = newValue)
            )
        }
    }

    fun setPrismPositionLeft(position: PrismPosition) = withState {
        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismPositionLeft = position)
            )
        }
    }

    fun setPrismPositionRight(position: PrismPosition) = withState {
        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(prismPositionRight = position)
            )
        }
    }

    fun toggleHasPrism() = withState {
        if (it.currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it.currentPrescriptionData.copy(hasPrism = !it.hasPrism)
            )
        }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<PrescriptionDataViewModel, PrescriptionDataState> {
        override fun create(state: PrescriptionDataState): PrescriptionDataViewModel
    }

    companion object: MavericksViewModelFactory<PrescriptionDataViewModel, PrescriptionDataState>
        by hiltMavericksViewModelFactory()
}
