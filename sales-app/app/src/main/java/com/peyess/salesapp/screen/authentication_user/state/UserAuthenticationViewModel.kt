package com.peyess.salesapp.screen.authentication_user.state

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.UserAuthState
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class UserAuthenticationViewModel @AssistedInject constructor(
    @Assisted initialState: UserAuthenticationState,
    private val application: SalesApplication,
    private val authenticationRepository: AuthenticationRepository
): MavericksViewModel<UserAuthenticationState>(initialState) {

    init {
        loadStore()

        authenticationRepository.activeCollaborators().setOnEach {
            Timber.i("Got users: ${it.size}")
            copy(users = listOf(it[0], it[1],it[0], it[1],it[0], it[1],it[0], it[1],it[0], it[1],it[0], it[1],it[0], it[1],it[0], it[1],it[0], it[1],it[0], it[1],))
        }
    }

    fun loadStore() = withState {
        authenticationRepository.currentStore.execute {
            Timber.i("Current store is $it", it)
            copy(currentStore = it)
        }
    }

    fun enterStore(userId: String) = setState {
        val user = this.users.find {
            it.id == userId
        }

        copy(
            screenState = UserAuthenticationState.ScreenState.SignIn,
            currentUser = user ?: this.currentUser,
        )
    }

    fun localSignIn() {}

    fun fuck (): Flow<UserAuthState> = flow {
        delay(2000)
        emit(UserAuthState.Away)
    }

    fun regularSignIn() = withState {
        Timber.i("Signing in")
        setState { copy(currentUserAuthState = Loading()) }
        fuck().execute {
            copy(currentUserAuthState = it)
        }

    }

    fun onPasswordChanged(password: String) = setState {
        copy(password = password)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<UserAuthenticationViewModel, UserAuthenticationState> {
        override fun create(state: UserAuthenticationState): UserAuthenticationViewModel
    }

    companion object:
        MavericksViewModelFactory<UserAuthenticationViewModel, UserAuthenticationState> by
        hiltMavericksViewModelFactory()
}