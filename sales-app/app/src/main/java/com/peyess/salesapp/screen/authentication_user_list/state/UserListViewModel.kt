package com.peyess.salesapp.screen.authentication_user_list.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.UserAuthState
import com.peyess.salesapp.auth.authenticateUser
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber

class UserListViewModel @AssistedInject constructor(
    @Assisted initialState: UserListState,
    private val application: SalesApplication,
    private val authenticationRepository: AuthenticationRepository
): MavericksViewModel<UserListState>(initialState) {

    init {
        loadStore()

        authenticationRepository.activeCollaborators().setOnEach {
            Timber.i("Got users: ${it.size}")

            copy(users = it)
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
            screenState = UserListState.ScreenState.SignIn,
            currentUser = user ?: this.currentUser,
        )
    }

    fun localSignIn() {}

    fun regularSignIn() = withState {
        if (it.currentUserAuthState is Loading) {
            return@withState
        }

        setState {
            copy(
                authErrorMessage = application.stringResource(R.string.empty_string),
            )
        }

        if (it.password.isEmpty()) {
            setState {
                copy(
                    currentUserAuthState =
                        Fail(Exception("Invalid credentials"), UserAuthState.Unauthenticated)
                )
            }

            return@withState
        }

        val firebaseApplication = authenticationRepository.userFirebaseApp(it.currentUser.id)
        if (firebaseApplication == null) {
            Timber.e("Firebase application is null for: ${it.currentUser.id}")

            setState {
                copy(
                    currentUserAuthState = Fail(
                        Exception("Firebase application is null"),
                        UserAuthState.Unauthenticated
                    )
                )
            }

            return@withState
        }

        authenticateUser(
            email = it.currentUser.email,
            password = it.password,
            firebaseApp = firebaseApplication,
        ).execute { authState ->
            Timber.i("Current auth state: ${authState}")
            copy(currentUserAuthState = authState)
        }
    }

    fun onPasswordChanged(password: String) = setState {
        copy(password = password)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<UserListViewModel, UserListState> {
        override fun create(state: UserListState): UserListViewModel
    }

    companion object:
        MavericksViewModelFactory<UserListViewModel, UserListState> by
        hiltMavericksViewModelFactory()
}