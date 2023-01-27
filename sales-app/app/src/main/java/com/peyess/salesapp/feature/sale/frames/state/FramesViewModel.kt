package com.peyess.salesapp.feature.sale.frames.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.typing.frames.FramesType
import com.peyess.salesapp.dao.sale.frames.hasPotentialProblemsWith
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.take
import timber.log.Timber

class FramesViewModel @AssistedInject constructor(
    @Assisted initialState: FramesState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
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
            FramesState::currentFramesData,
        ) { prescription, frames ->
            val framesType: FramesType
            val hasPotentialProblems: Boolean

            if (frames is Success) {
                hasPotentialProblems = frames.invoke()
                    .hasPotentialProblemsWith(prescription)
                framesType = frames.invoke().type ?: FramesType.None

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

    private fun loadCurrentFramesData() = withState {
        saleRepository
            .currentFramesData()
            .execute(Dispatchers.IO) {
                copy(currentFramesData = it)
            }
    }

    fun onFramesInfoChanged(value: String) = withState {
        if (it._currentFramesData != null) {
            saleRepository.updateFramesData(it._currentFramesData.copy(framesInfo = value))
        }
    }

    fun onFramesDescriptionChanged(value: String) = withState {
        if (it._currentFramesData != null) {
            saleRepository.updateFramesData(it._currentFramesData.copy(description = value))
        }
    }

    fun onFramesReferenceChanged(value: String) = withState {
        if (it._currentFramesData != null) {
            saleRepository.updateFramesData(it._currentFramesData.copy(reference = value))
        }
    }

    fun onFramesValueChanged(value: Double) = withState {
        if (it._currentFramesData != null) {
            saleRepository.updateFramesData(it._currentFramesData.copy(value = value))
        }
    }

    fun onFramesTagCodeChanged(value: String) = withState {
        if (it._currentFramesData != null) {
            saleRepository.updateFramesData(it._currentFramesData.copy(tagCode = value))
        }
    }

    fun onFramesTypeChanged(name: String) = withState {
        if (it._currentFramesData != null) {
            saleRepository.updateFramesData(
                it._currentFramesData.copy(type = FramesType.toFramesType(name)!!)
            )
        }
    }

    fun onFramesNewChanged(areNew: Boolean) = withState {
        if (it._currentFramesData != null) {
            saleRepository.updateFramesData(it._currentFramesData.copy(areFramesNew = areNew))

            setState { copy(hasSetFrames = true) }
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