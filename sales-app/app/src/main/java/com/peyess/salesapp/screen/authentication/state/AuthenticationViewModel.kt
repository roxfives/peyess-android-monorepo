package com.peyess.salesapp.screen.authentication.state

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.auth.authenticateStore
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AuthenticationViewModel @AssistedInject constructor(
    @Assisted initialState: AuthenticationState,
): MavericksViewModel<AuthenticationState>(initialState) {

    fun updatePassword(password: String) {
        setState {
            copy(password = password)
        }
    }

    fun updateUsername(username: String) {
        setState {
            copy(username = username)
        }
    }

    fun signIn() = withState {
        if (it.authState is Loading) {
            return@withState
        }

        authenticateStore(email = it.username, password = it.password).execute { authState ->
            copy(authState = authState)
        }
    }

    // DI - Hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<AuthenticationViewModel, AuthenticationState> {
        override fun create(state: AuthenticationState): AuthenticationViewModel
    }

    companion object:
        MavericksViewModelFactory<AuthenticationViewModel, AuthenticationState> by
        hiltMavericksViewModelFactory()
}