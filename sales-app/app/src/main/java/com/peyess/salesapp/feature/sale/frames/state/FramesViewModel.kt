package com.peyess.salesapp.feature.sale.frames.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.typing.frames.FramesType
import com.peyess.salesapp.dao.sale.frames.hasPotentialProblemsWith
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

class FramesViewModel @AssistedInject constructor(
    @Assisted initialState: FramesState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
    private val localFramesRepository: LocalFramesRepository,
): MavericksViewModel<FramesState>(initialState) {

    init {
        loadCurrentFramesData()
        loadPositioningData()
        loadMikeMessages()
        loadServiceOrderData()

        onEach(FramesState::serviceOrderId) {
            loadPrescriptionData(it)
        }

        onEach(
            FramesState::prescriptionResponse,
            FramesState::currentFrames,
        ) { prescription, frames ->
            val framesType = frames.type
            val hasPotentialProblems = frames.hasPotentialProblemsWith(prescription)

            setState {
                copy(
                    showMike = hasPotentialProblems
                            && (framesType is FramesType.AcetateScrewed
                            || framesType is FramesType.MetalScrewed
                            || framesType is FramesType.AcetateNylon
                            || framesType is FramesType.MetalNylon)
                )
            }
        }

        onEach(FramesState::prescriptionResponse) {
            val ib = it.prevalentIdealBase

            val messageId = if (ib <= 3.5) {
                R.string.ib_message_less_than_3_5
            } else if (ib <= 5) {
                R.string.ib_message_between_3_5_and_5
            } else if (ib <= 8) {
                R.string.ib_message_between_5_and_8
            } else {
                R.string.ib_message_greater_then_8
            }

            val idealBaseMessage = salesApplication.stringResource(messageId)
            loadLandingMikeMessage(idealBaseMessage.lowercase())

            val animationId = if (ib <= 4) {
                R.raw.lottie_frames_curvature_4
            } else if (ib <= 6) {
                R.raw.lottie_frames_curvature_6
            } else {
                R.raw.lottie_frames_curvature_8
            }

            setState {
                copy(
                    idealBaseMessage = idealBaseMessage,
                    idealBaseAnimationResource = animationId,
                )
            }
        }

        onAsync(FramesState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }

        onAsync(FramesState::activeServiceOrderResponseAsync) {
            processServiceOrderResponse(it)
        }

        onAsync(FramesState::loadFramesResponseAsync) {
            processLoadFramesResponse(it)
        }

        onAsync(FramesState::setFramesNewResponseAsync) {
            processOnSetFramesNewResponse(it)
        }


    }

    private fun updateFrames(frames: FramesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            if (frames.soId.isNotBlank()) {
                saleRepository.updateFramesData(frames)
            }
        }
    }

    private fun loadServiceOrderData() {
        suspend {
            saleRepository.currentServiceOrder()
        }.execute {
            copy(activeServiceOrderResponseAsync = it)
        }
    }

    private fun processServiceOrderResponse(response: ActiveServiceOrderResponse) = setState {
        response.fold(
            ifLeft = {
                copy(activeServiceOrderResponseAsync = Fail(
                    it.error ?: Throwable(it.description))
                )
            },

            ifRight = {
                copy(serviceOrderId = it.id)
            }
        )
    }

    private fun loadLandingMikeMessage(framesMessage: String) = withState {
        Timber.i("Loading mike message")
        combine(
            saleRepository.activeSO().retryWhen { _, attempt ->
                attempt < 10
            }.filterNotNull().map { it.clientName },
            authenticationRepository.currentUser().filterNotNull().map { it.name },
        ) { client, collaborator ->
            MikeMessageResult(client, collaborator)
        }.take(1).execute(Dispatchers.IO) {
            Timber.i("The message result: $it")

            if (it is Success) {
                val result = it.invoke()
                val message = salesApplication
                    .stringResource(R.string.mike_frames_landing_screen_message)
                    .format(result.clientName, framesMessage, result.collaboratorName)

                copy(landingMikeMessage = message)
            } else {
                copy()
            }
        }
    }

    private fun loadPrescriptionData(serviceOrderId: String) {
        suspend {
            localPrescriptionRepository.getPrescriptionForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(prescriptionResponseAsync = it)
        }
    }

    private fun processPrescriptionResponse(response: LocalPrescriptionResponse) = setState {
        response.fold(
            ifLeft = {
                copy(prescriptionResponseAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(prescriptionResponse = it)
            }
        )
    }

    private fun loadPositioningData() = withState {
        saleRepository
            .currentPositioning(Eye.Left)
            .execute(Dispatchers.IO) {
                copy(positioningDataLeft = it)
            }

        saleRepository
            .currentPositioning(Eye.Right)
            .execute(Dispatchers.IO) {
                copy(positioningDataRight = it)
            }
    }

    private fun loadMikeMessages() = withState {
        saleRepository
            .activeSO()
            .filterNotNull()
            .map { so -> so.clientName }
            .execute(Dispatchers.IO) { name ->
                val message = salesApplication.stringResource(R.string.mike_bad_lens_type)

                if (name is Success) {
                    copy(mikeMessage = message.format(name.invoke()))
                } else {
                    copy(mikeMessage = message.format("cliente"))
                }
            }
    }

    private fun processLoadFramesResponse(
      response: FramesEntity,
//        response: Either<Throwable, FramesDocument>
    ) = setState {
        copy(
            currentFrames = response,

            areFramesNewInput = response.areFramesNew,
            descriptionInput = response.description,
            referenceInput = response.reference,
            valueInput = response.value,
            tagCodeInput = response.tagCode,
            framesTypeInput = response.type,
            infoInput = response.framesInfo,
        )
    }

    fun loadCurrentFramesData() = withState {
        suspend {
            saleRepository.currentFramesData().first()
        }.execute(Dispatchers.IO) {
            copy(loadFramesResponseAsync = it)
        }
    }

    fun onFramesInfoChanged(value: String) = setState {
        val update = currentFrames.copy(framesInfo = value)

        updateFrames(update)
        copy(currentFrames = update, infoInput = value)
    }

    fun onFramesDescriptionChanged(value: String) = setState {
        val update = currentFrames.copy(description = value)

        updateFrames(update)
        copy(currentFrames = update, descriptionInput = value)
    }

    fun onFramesReferenceChanged(value: String) = setState {
        val update = currentFrames.copy(reference = value)

        updateFrames(update)
        copy(currentFrames = update, referenceInput = value)
    }

    fun onFramesValueChanged(value: Double) = setState {
        val update = currentFrames.copy(value = value)

        updateFrames(update)
        copy(currentFrames = update, valueInput = value)
    }

    fun onFramesTagCodeChanged(value: String) = setState {
        val update = currentFrames.copy(tagCode = value)

        updateFrames(update)
        copy(currentFrames = update, tagCodeInput = value)
    }

    fun onFramesTypeChanged(name: String) = setState {
        val type = FramesType.toFramesType(name)
        val update = currentFrames.copy(type = type)

        updateFrames(update)
        copy(currentFrames = update, framesTypeInput = type)
    }

    private fun processOnSetFramesNewResponse(frames: FramesEntity) = setState {
        copy(
            currentFrames = frames,
            areFramesNewInput = frames.areFramesNew,
            hasSetFrames = true,
            hasFinishedSettingFramesType = true,
        )
    }

    fun onFramesNewChanged(areNew: Boolean) = withState {
        suspend {
            val update = it.currentFrames.copy(areFramesNew = areNew)

            saleRepository.updateFrames(update)
            update
        }.execute(Dispatchers.IO) {
            copy(setFramesNewResponseAsync = it)
        }

    }

    fun onNavigateToSetFrames() = setState {
        copy(hasFinishedSettingFramesType = false)
    }

    fun onFinishSettingFrames() = withState {
        suspend {
            saleRepository.updateFrames(it.currentFrames)
        }.execute(Dispatchers.IO) {
            copy(finishedSettingFrames = it is Success)
        }
    }

    private data class MikeMessageResult(
        val clientName: String = "",
        val collaboratorName: String = "",
    )

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<FramesViewModel, FramesState> {
        override fun create(state: FramesState): FramesViewModel
    }

    companion object: MavericksViewModelFactory<FramesViewModel, FramesState>
        by hiltMavericksViewModelFactory()
}