package com.peyess.salesapp.feature.sale.frames.landing.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.frames.UpdateFramesNewResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.typing.general.Eye
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

class FramesLandingViewModel @AssistedInject constructor(
    @Assisted initialState: FramesLandingState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
    private val localFramesRepository: LocalFramesRepository,
): MavericksViewModel<FramesLandingState>(initialState) {

    init {
        loadPositioningData()
        loadServiceOrderData()

        onEach(FramesLandingState::serviceOrderId) {
            streamCurrentFramesData(it)
            loadPrescriptionData(it)
        }

        onEach(FramesLandingState::prescriptionResponse) {
            updateFramesCurvatureAnimation(it)
        }

        onAsync(FramesLandingState::prescriptionResponseAsync) {
            processPrescriptionResponse(it)
        }

        onAsync(FramesLandingState::activeServiceOrderResponseAsync) {
            processServiceOrderResponse(it)
        }

        onAsync(FramesLandingState::loadFramesResponseAsync) {
            processLoadFramesResponse(it)
        }

        onAsync(FramesLandingState::setFramesNewResponseAsync) {
            processOnSetFramesNewResponse(it)
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
            },
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

    private fun updateFramesCurvatureAnimation(
        prescription: LocalPrescriptionDocument,
    ) = setState {
        val ib = prescription.prevalentIdealBase

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

        copy(
            idealBaseMessage = idealBaseMessage,
            idealBaseAnimationResource = animationId,
        )
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

    private fun processLoadFramesResponse(
        response: LocalFramesRepositoryResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(loadFramesResponseAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(frames = it)
            }
        )
    }

    private fun streamCurrentFramesData(serviceOrderId: String) = withState {
        localFramesRepository.streamFramesForServiceOrder(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(loadFramesResponseAsync = it)
            }
    }

    private fun processOnSetFramesNewResponse(
        response: UpdateFramesNewResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(setFramesNewResponseAsync = Fail(it.error ?: Throwable(it.description)))
            },

            ifRight = {
                copy(hasFinishedSettingFramesType = true)
            }
        )
    }

    fun onFramesNewChanged(areNew: Boolean) = withState {
        suspend {
            localFramesRepository.createFramesIfNotExists(it.serviceOrderId)
            localFramesRepository.updateFramesNew(it.serviceOrderId, areNew)
        }.execute(Dispatchers.IO) {
            copy(setFramesNewResponseAsync = it)
        }
    }

    fun onNavigateToSetFrames() = setState {
        copy(hasFinishedSettingFramesType = false)
    }

    private data class MikeMessageResult(
        val clientName: String = "",
        val collaboratorName: String = "",
    )

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<FramesLandingViewModel, FramesLandingState> {
        override fun create(state: FramesLandingState): FramesLandingViewModel
    }

    companion object: MavericksViewModelFactory<FramesLandingViewModel, FramesLandingState>
        by hiltMavericksViewModelFactory()
}