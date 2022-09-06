package com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class FirstTimeViewModel @AssistedInject constructor(
    @Assisted initialState: FirstTimeState,
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
    private val authenticationRepository: AuthenticationRepository,
): MavericksViewModel<FirstTimeState>(initialState) {

    private data class MikeMessageResult(
        val clientName: String = "",
        val collaboratorName: String = "",
    )

    init {
        loadMikeMessage()
    }

    private fun loadMikeMessage() = withState {
        combine(
            saleRepository.activeSO().filterNotNull().map { it.clientName },
            authenticationRepository.currentUser().filterNotNull().map { it.name },
        ) { client, collaborator ->
            MikeMessageResult(client, collaborator)
        }.execute(Dispatchers.IO) {
            if (it is Success) {
                val result = it.invoke()
                val message = salesApplication
                    .stringResource(R.string.mike_anamnesis_intro_message)
                    .format(result.clientName, result.collaboratorName)

                copy(mikeMessage = message)
            } else {
                copy()
            }
        }
    }

    fun onUsageScoreChanged(score: Float) = setState {
        copy(usageScore = score)
    }

    fun onFirstTimeChanged(isFirstTime: Boolean) = setState {
        copy(isFirstTime = isFirstTime)
    }

    fun resetState() = setState {
        FirstTimeState()
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<FirstTimeViewModel, FirstTimeState> {
        override fun create(state: FirstTimeState): FirstTimeViewModel
    }

    companion object: MavericksViewModelFactory<FirstTimeViewModel, FirstTimeState>
        by hiltMavericksViewModelFactory()
}