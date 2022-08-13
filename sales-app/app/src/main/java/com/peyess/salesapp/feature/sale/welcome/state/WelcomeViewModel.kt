package com.peyess.salesapp.feature.sale.welcome.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.take
import timber.log.Timber

class WelcomeViewModel @AssistedInject constructor(
    @Assisted initialState: WelcomeState,
    val application: SalesApplication,
    private val authenticationRepository: AuthenticationRepository,
    private val salesRepository: SaleRepository,
): MavericksViewModel<WelcomeState>(initialState) {

    init {
        loadCurrentCollaborator()


        withState {
            Timber.i("Filho de uma puta morfética: ${it.hasUpdatedSale}")
        }
    }

    private fun loadCurrentCollaborator() = withState {
        authenticationRepository.currentUser().execute(Dispatchers.IO) {
            copy(collaborator = it)
        }
    }

    fun updateSale() = withState { state ->
        salesRepository.activeSO()
            .take(1)
            .map { salesRepository.updateSO(it.copy(clientName = state.clientName)) }
            .execute(Dispatchers.IO) {
                val attemptedNext = if (it is Success) {
                    this.goNextAttempts + 1
                } else {
                    this.goNextAttempts
                }

                Timber.i("Current attempts ${attemptedNext}]")

                copy(
                    hasUpdatedSale = true,
                    goNextAttempts = attemptedNext
                )
            }
    }

    fun onNext() = setState {
        copy(hasUpdatedSale = false)
    }


    fun onClientNameChanged(name: String) = setState {
        copy(clientName = name)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<WelcomeViewModel, WelcomeState> {
        override fun create(state: WelcomeState): WelcomeViewModel
    }

    companion object: MavericksViewModelFactory<WelcomeViewModel, WelcomeState> by hiltMavericksViewModelFactory()
}
