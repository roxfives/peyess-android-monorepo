package com.peyess.salesapp.feature.sale.frames.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.frames.FramesType
import com.peyess.salesapp.dao.sale.frames.hasPotentialProblemsWith
import com.peyess.salesapp.dao.sale.prescription_data.prevalentIdealBase
import com.peyess.salesapp.repository.auth.AuthenticationRepository
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
): MavericksViewModel<FramesState>(initialState) {

    init {
        loadCurrentFramesData()
        loadPrescriptionData()
        loadPositioningData()
        loadMikeMessages()

        onEach(
            FramesState::currentPrescriptionData,
            FramesState::currentFramesData,
        ) { prescription, frames ->
            val framesType: FramesType
            val hasPotentialProblems: Boolean

            if (prescription is Success && frames is Success) {
                hasPotentialProblems = frames.invoke()
                    .hasPotentialProblemsWith(prescription.invoke())
                framesType = frames.invoke().type ?: FramesType.None

                setState {
                    copy(
                        showMike = hasPotentialProblems
                                && (framesType is FramesType.AcetateScrewed
                                    || framesType is FramesType.MetalScrewed)
                    )
                }
            }
        }

        onEach(FramesState::currentPrescriptionData) {
            val messageId: Int
            val animationId: Int
            var ib: Double

            val idealBaseMessage = when (it) {
                Uninitialized -> Uninitialized
                is Loading -> Loading()
                is Fail -> Fail(it.error)
                is Success -> {
                    ib = it.invoke().prevalentIdealBase()

                    messageId = if (ib <= 3.5) {
                        R.string.ib_message_less_than_3_5
                    } else if (ib <= 5) {
                        R.string.ib_message_between_3_5_and_5
                    } else if (ib <= 8) {
                        R.string.ib_message_between_5_and_8
                    } else {
                        R.string.ib_message_greater_then_8
                    }

                    // TODO: Refactor this out of here
                    loadLandingMikeMessage(salesApplication.stringResource(id = messageId).lowercase())
                    Success(salesApplication.stringResource(messageId))
                }
            }

            val idealBaseAnimation = when (it) {
                Uninitialized -> Uninitialized
                is Loading -> Loading()
                is Fail -> Fail(it.error)
                is Success -> {
                    ib = it.invoke().prevalentIdealBase()

                    animationId = if (ib <= 4) {
                        R.raw.lottie_frames_curvature_4
                    } else if (ib <= 6) {
                        R.raw.lottie_frames_curvature_6
                    } else {
                        R.raw.lottie_frames_curvature_8
                    }

                    Success(animationId)
                }
            }

            setState {
                copy(
                    idealBaseMessageAsync = idealBaseMessage,
                    idealBaseAnimationResource = idealBaseAnimation,
                )
            }
        }
    }

    private fun loadLandingMikeMessage(framesMessage: String) = withState {
        Timber.i("Loading mike message")
        combine(
            saleRepository.activeSO().retryWhen { _, attempt ->
                attempt < 10
            }.filterNotNull().map { it.clientName },
            authenticationRepository.currentUser().retryWhen { _, attempt ->
                attempt < 10
            }.map { it.name },
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

    private fun loadPrescriptionData() = withState {
        saleRepository
            .currentPrescriptionData()
            .execute(Dispatchers.IO) {
                copy(currentPrescriptionData = it)
            }
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
//        val ibMessage = it.idealBaseMessageAsync.invoke()
//        if (ibMessage != null) {
//            loadLandingMikeMessage(ibMessage.lowercase())
//        }

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