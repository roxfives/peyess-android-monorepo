package com.peyess.salesapp.screen.sale.anamnesis.sixth_step_time.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SixthStepViewModel @AssistedInject constructor(
    @Assisted initialState: SixthStepState,
    private val saleRepository: SaleRepository,
): MavericksViewModel<SixthStepState>(initialState) {

    init {
        onEach(SixthStepState::totalAvailable) {
            withState { state ->
                if (state.totalUsed > it) {
                    setState {
                        copy(
                            readingSlider = 0f,
                            phoneSlider = 0f,
                            computerSlider = 0f,
                            televisionSlider = 0f,
                            drivingSlider = 0f,
                            sportsSlider = 0f,
                            externalAreaSlider = 0f,
                            internalAreaSlider = 0f,
                        )
                    }
                }
            }
        }
    }

    private fun loadSaleData() {
        viewModelScope.launch(Dispatchers.IO) {
            saleRepository
                .currentSale()
                .fold(
                    ifLeft = { error ->
                        Timber.e("Error while fetching active sale", error)
                    },
                    ifRight = { sale ->
                        setState { copy(saleId = sale.id) }
                    }
                )
        }
    }

    private fun loadServiceOrderData() {
        viewModelScope.launch(Dispatchers.IO) {
            saleRepository
                .currentServiceOrder()
                .fold(
                    ifLeft = { error ->
                        Timber.e("Error while fetching active service order", error)
                    },
                    ifRight = { serviceOrder ->
                        setState { copy(serviceOrderId = serviceOrder.id) }
                    }
                )
        }
    }

    fun loadSale() {
        loadSaleData()
        loadServiceOrderData()
    }

    fun resetState() = setState {
        SixthStepState()
    }

    fun onReadingChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.readingSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.readingSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.readingSlider
        }

        copy(readingSlider = updateValue)
    }

    fun onPhoneChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.phoneSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.phoneSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.phoneSlider
        }

        copy(phoneSlider = updateValue)
    }

    fun onComputerChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.computerSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.computerSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.computerSlider
        }

        copy(computerSlider = updateValue)
    }

    fun onTelevisionChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.televisionSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.televisionSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.televisionSlider
        }

        copy(televisionSlider = updateValue)
    }

    fun onDrivingChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.drivingSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.drivingSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.drivingSlider
        }

        copy(drivingSlider = updateValue)
    }

    fun onSportsChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.sportsSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.sportsSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.sportsSlider
        }

        copy(sportsSlider = updateValue)
    }

    fun onExternalAreaChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.externalAreaSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.externalAreaSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.externalAreaSlider
        }

        copy(externalAreaSlider = updateValue)
    }

    fun onInternalAreaChanged(value: Float) = setState {
        val updateValue = if (this.totalLeft > 0 || this.internalAreaSlider > value) {
            value
        } else if (this.totalLeft < 0) {
            this.internalAreaSlider + (this.totalLeft / 30f).toInt()
        } else {
            this.internalAreaSlider
        }

        copy(internalAreaSlider = updateValue)
    }

    fun onWakeUpChanged(value: Float) = setState {
        copy(timeWakeUp = value)
    }

    fun onGoToBedChanged(value: Float) = setState {
        copy(timeGoToBed = value)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<SixthStepViewModel, SixthStepState> {
        override fun create(state: SixthStepState): SixthStepViewModel
    }

    companion object: MavericksViewModelFactory<SixthStepViewModel, SixthStepState>
        by hiltMavericksViewModelFactory()
}