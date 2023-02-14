package com.peyess.salesapp.screen.sale.frames.data.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.frames.model.FramesDocument
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.screen.sale.frames.data.adapter.toFrames
import com.peyess.salesapp.screen.sale.frames.data.adapter.toFramesCompatibility
import com.peyess.salesapp.screen.sale.frames.data.adapter.toFramesDocument
import com.peyess.salesapp.screen.sale.frames.data.adapter.toPrescriptionCompatibility
import com.peyess.salesapp.screen.sale.frames.data.model.Frames
import com.peyess.salesapp.features.frames.prescription_compatibility.isFramesInCompatible
import com.peyess.salesapp.features.frames.prescription_compatibility.model.FramesCompatibility
import com.peyess.salesapp.features.frames.prescription_compatibility.model.PrescriptionCompatibility
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.typing.frames.FramesType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private typealias  FramesDataMavericksAssistedVMFactory =
        AssistedViewModelFactory<FramesDataViewModel, FramesDataState>
private typealias FramesDataMavericksVMFactory =
        MavericksViewModelFactory<FramesDataViewModel, FramesDataState>

class FramesDataViewModel @AssistedInject constructor(
    @Assisted initialState: FramesDataState,
    private val salesApplication: SalesApplication,
    private val framesRepository: LocalFramesRepository,
    private val prescriptionRepository: LocalPrescriptionRepository,
    private val saleRepository: SaleRepository,
): MavericksViewModel<FramesDataState>(initialState) {

    private val defaultClientName = salesApplication
        .stringResource(R.string.mike_bad_lens_type_default_client_name)

    init {
        onEach(FramesDataState::serviceOrderId) {
            loadServiceOrder(it)
            loadFrames(it)
            loadPrescription(it)
        }

        onEach(
            FramesDataState::frames,
            FramesDataState::prescription,
        ) { frames, prescription ->
            checkFramesCompatibility(
                frames = frames.toFramesCompatibility(),
                prescription = prescription.toPrescriptionCompatibility(),
            )
        }

        onEach(FramesDataState::serviceOrder) { updateMikeMessage(it.clientName) }

        onAsync(FramesDataState::loadFramesResponseAsync) { processFramesResponse(it) }
        onAsync(FramesDataState::activeServiceOrderResponseAsync) { processServiceOrderResponse(it) }
        onAsync(FramesDataState::loadPrescriptionResponseAsync) { processPrescriptionResponse(it) }
    }

    private fun loadPrescription(serviceOrderId: String) {
        suspend {
            prescriptionRepository.getPrescriptionForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(loadPrescriptionResponseAsync = it)
        }
    }

    private fun processPrescriptionResponse(response: LocalPrescriptionResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    loadPrescriptionResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(prescription = it)
            },
        )
    }

    private fun loadFrames(serviceOrderId: String) {
        suspend {
            framesRepository.framesForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(loadFramesResponseAsync = it)
        }
    }

    private fun processFramesResponse(response: LocalFramesRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    loadFramesResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(
                    frames = it.toFrames(),

                    areFramesNewInput = it.areFramesNew,
                    designInput = it.design,
                    infoInput = it.framesInfo,
                    referenceInput = it.reference,
                    valueInput = it.value,
                    tagCodeInput = it.tagCode,
                    framesTypeInput = it.type,
                )
            },
        )
    }

    private fun updateFrames(frames: Frames) {
        viewModelScope.launch(Dispatchers.IO) {
            framesRepository.updateFrames(frames.toFramesDocument())
        }
    }

    private fun loadServiceOrder(serviceOrderId: String) {
        suspend {
            saleRepository.serviceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(activeServiceOrderResponseAsync = it)
        }
    }

    private fun processServiceOrderResponse(response: ActiveServiceOrderResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    activeServiceOrderResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(serviceOrder = it)
            },
        )
    }

    private fun updateMikeMessage(clientName: String) = setState {
        val message = salesApplication
            .stringResource(R.string.mike_bad_lens_type)
            .format(clientName.ifBlank { defaultClientName })

        copy(mikeMessage = message)
    }

    private fun checkFramesCompatibility(
        frames: FramesCompatibility,
        prescription: PrescriptionCompatibility,
    ) = setState {
        val isIncompatible = isFramesInCompatible(frames, prescription)

        copy(showMike = isIncompatible)
    }

    fun onUpdateServiceOrder(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun onFramesInfoChanged(value: String) = setState {
        val update = frames.copy(framesInfo = value)

        updateFrames(update)
        copy(frames = update, infoInput = value)
    }

    fun onFramesDescriptionChanged(value: String) = setState {
        val update = frames.copy(description = value)

        updateFrames(update)
        copy(frames = update, designInput = value)
    }

    fun onFramesReferenceChanged(value: String) = setState {
        val update = frames.copy(reference = value)

        updateFrames(update)
        copy(frames = update, referenceInput = value)
    }

    fun onFramesValueChanged(value: Double) = setState {
        val update = frames.copy(value = value)

        updateFrames(update)
        copy(frames = update, valueInput = value)
    }

    fun onFramesTagCodeChanged(value: String) = setState {
        val update = frames.copy(tagCode = value)

        updateFrames(update)
        copy(frames = update, tagCodeInput = value)
    }

    fun onFramesTypeChanged(name: String) = setState {
        val type = FramesType.toFramesType(name)
        val update = frames.copy(type = type)

        updateFrames(update)
        copy(frames = update, framesTypeInput = type)
    }

    fun onFinishSettingFrames() = withState {
        suspend {
            framesRepository.createFramesIfNotExists(it.serviceOrderId)
            framesRepository.updateFrames(it.frames.toFramesDocument())
        }.execute(Dispatchers.IO) {
            copy(
                finishSettingFramesAsync = it,
                finishedSettingFrames = it is Success,
            )
        }
    }

    fun onCancel() = withState {
        suspend {
            framesRepository.createFramesIfNotExists(it.serviceOrderId)
            framesRepository.updateFrames(FramesDocument(soId = it.serviceOrderId))
        }.execute(Dispatchers.IO) {
            copy(
                finishSettingFramesAsync = it,
                finishedSettingFrames = it is Success,
            )
        }
    }

    fun onNavigate() = setState {
        copy(finishedSettingFrames = false)
    }

    @AssistedFactory
    interface Factory: FramesDataMavericksAssistedVMFactory {
        override fun create(state: FramesDataState): FramesDataViewModel
    }

    companion object: FramesDataMavericksVMFactory by hiltMavericksViewModelFactory()
}