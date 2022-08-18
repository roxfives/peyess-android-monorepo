package com.peyess.salesapp.feature.sale.welcome.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
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
        loadCurrentSO()
        loadCurrentCollaborator()
    }

    private fun loadCurrentCollaborator() = withState {
        authenticationRepository.currentUser().execute(Dispatchers.IO) {
            copy(collaborator = it)
        }
    }

    private fun loadCurrentSO() = withState {
        salesRepository.activeSO().execute(Dispatchers.IO) {
            copy(activeSO = it)
        }
    }

    fun onClientNameChanged(name: String) = withState {
        val activeSo: ActiveSOEntity

        if (it.activeSO is Success && it.activeSO.invoke() != null) {
            activeSo = it.activeSO.invoke()!!.copy(clientName = name)

            salesRepository.updateSO(activeSo)
        }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<WelcomeViewModel, WelcomeState> {
        override fun create(state: WelcomeState): WelcomeViewModel
    }

    companion object: MavericksViewModelFactory<WelcomeViewModel, WelcomeState> by hiltMavericksViewModelFactory()
}