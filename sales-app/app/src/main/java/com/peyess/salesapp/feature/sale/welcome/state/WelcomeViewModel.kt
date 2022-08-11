package com.peyess.salesapp.feature.sale.welcome.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class WelcomeViewModel @AssistedInject constructor(
    @Assisted initialState: WelcomeState,
    val application: SalesApplication,
    val authenticationRepository: AuthenticationRepository,
): MavericksViewModel<WelcomeState>(initialState) {

    init {
        loadCurrentCollaborator()
    }

    private fun loadCurrentCollaborator() = withState {
        authenticationRepository.currentUser().execute {
            copy(collaborator = it)
        }
    }

    fun attemptNext() = setState {
        copy(goNextAttempts = this.goNextAttempts + 1)
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


//class WelcomeViewModel @AssistedInject constructor(
//    @Assisted initialState: WelcomeState,
//    val authenticationRepository: AuthenticationRepository,
//): MavericksViewModel<WelcomeState>(initialState) {
//
//    init {
//        loadCurrentCollaborator()
//    }
//
//    private fun loadCurrentCollaborator() = withState {
//        authenticationRepository.currentUser().execute {
//            copy(collaborator = it)
//        }
//    }
//
//    @AssistedFactory
//    interface Factory: AssistedViewModelFactory<WelcomeViewModel, WelcomeState> {
//        override fun create(state: WelcomeState): WelcomeViewModel
//    }
//
//    companion object: MavericksViewModelFactory<WelcomeViewModel, WelcomeState>
//        by hiltMavericksViewModelFactory()
//}