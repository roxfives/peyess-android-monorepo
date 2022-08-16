package com.peyess.salesapp.feature.sale.prescription_data.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import timber.log.Timber

private const val defaultStep = 0.25

const val stepSpherical = defaultStep
const val minSpherical = -30.0
const val maxSpherical = 25.0

const val stepCylindrical = defaultStep
const val minCylindrical = -9.0
const val maxCylindrical = -0.25

const val stepAxis = 10.0
const val minAxis = 0.0
const val maxAxis = 180.0

const val stepAddition = defaultStep
const val minAddition = 0.75
const val maxAddition = 4.5

const val stepPrismDegree = 1.0
const val minPrismDegree = 1.0
const val maxPrismDegree = 45.0

const val stepPrismAxis = 10.0
const val minPrismAxis = 1.0
const val maxPrismAxis = 360.0

class PrescriptionDataViewModel @AssistedInject constructor(
    @Assisted initialState: PrescriptionDataState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
): MavericksViewModel<PrescriptionDataState>(initialState) {

    init {
        loadHasAddition()
        loadMikeMessage()
        loadInitPrescriptionData()

        onEach(PrescriptionDataState::hasAddition) {
            updateHasAddition(it)
        }
    }

    private fun loadHasAddition() = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .map { !it.isLensTypeMono }
            .execute(Dispatchers.IO) {
                copy(hasAdditionAsync = it)
            }
    }

    private fun loadMikeMessage() = withState {
        saleRepository.activeSO()
            .filterNotNull()
            .map {
                mikeMessageFor(it.lensTypeCategoryName)
            }
            .execute(Dispatchers.IO) {
                copy(mikeMessage = it)
            }
    }

    private fun mikeMessageFor(categoryName: LensTypeCategoryName?): String {
        return when (categoryName) {
            LensTypeCategoryName.Far -> salesApplication.stringResource(R.string.mike_message_far)
            LensTypeCategoryName.Multi -> salesApplication.stringResource(R.string.mike_message_multi)
            LensTypeCategoryName.Near -> salesApplication.stringResource(R.string.mike_message_near)
            null -> salesApplication.stringResource(R.string.mike_message_default)
        }
    }

    private fun loadInitPrescriptionData() = withState {
        saleRepository.currentPrescriptionData().execute {
            copy(currentPrescriptionData = it)
        }
    }

    private fun updateHasAddition(hasAddition: Boolean) = withState {
        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(hasAddition = hasAddition)
            )
        }
    }


    fun increaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(sphericalLeft = newValue)
            )
        }
    }

    fun decreaseSphericalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(sphericalLeft = newValue)
            )
        }
    }

    fun increaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue + stepSpherical).coerceAtMost(maxSpherical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(sphericalRight = newValue)
            )
        }
    }

    fun decreaseSphericalRight(curValue: Double) = withState {
        val newValue = (curValue - stepSpherical).coerceAtLeast(minSpherical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(sphericalRight = newValue)
            )
        }
    }

    fun increaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(cylindricalLeft = newValue)
            )
        }
    }

    fun decreaseCylindricalLeft(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(cylindricalLeft = newValue)
            )
        }
    }

    fun increaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue + stepCylindrical).coerceAtMost(maxCylindrical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(cylindricalRight = newValue)
            )
        }
    }

    fun decreaseCylindricalRight(curValue: Double) = withState {
        val newValue = (curValue - stepCylindrical).coerceAtLeast(minCylindrical)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(cylindricalRight = newValue)
            )
        }
    }

    fun increaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(axisLeft = newValue)
            )
        }
    }

    fun decreaseAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(axisLeft = newValue)
            )
        }
    }

    fun increaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepAxis).coerceAtMost(maxAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(axisRight = newValue)
            )
        }
    }

    fun decreaseAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepAxis).coerceAtLeast(minAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(axisRight = newValue)
            )
        }
    }

    fun increaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(additionLeft = newValue)
            )
        }
    }

    fun decreaseAdditionLeft(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(additionLeft = newValue)
            )
        }
    }

    fun increaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue + stepAddition).coerceAtMost(maxAddition)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(additionRight = newValue)
            )
        }
    }

    fun decreaseAdditionRight(curValue: Double) = withState {
        val newValue = (curValue - stepAddition).coerceAtLeast(minAddition)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(additionRight = newValue)
            )
        }
    }

    fun increasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismDegreeLeft = newValue)
            )
        }
    }

    fun decreasePrismDegreeLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismDegreeLeft = newValue)
            )
        }
    }

    fun increasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismDegree).coerceAtMost(maxPrismDegree)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismDegreeRight = newValue)
            )
        }
    }

    fun decreasePrismDegreeRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismDegree).coerceAtLeast(minPrismDegree)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismDegreeRight = newValue)
            )
        }
    }

    fun increasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismAxisLeft = newValue)
            )
        }
    }

    fun decreasePrismAxisLeft(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismAxisLeft = newValue)
            )
        }
    }

    fun increasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue + stepPrismAxis).coerceAtMost(maxPrismAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismAxisRight = newValue)
            )
        }
    }

    fun decreasePrismAxisRight(curValue: Double) = withState {
        val newValue = (curValue - stepPrismAxis).coerceAtLeast(minPrismAxis)

        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismAxisRight = newValue)
            )
        }
    }

    fun setPrismPositionLeft(position: PrismPosition) = withState {
        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismPositionLeft = position)
            )
        }
    }

    fun setPrismPositionRight(position: PrismPosition) = withState {
        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(prismPositionRight = position)
            )
        }
    }

    fun toggleHasPrism() = withState {
        if (it._currentPrescriptionData != null) {
            saleRepository.updatePrescriptionData(
                it._currentPrescriptionData.copy(hasPrism = !it.hasPrism)
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