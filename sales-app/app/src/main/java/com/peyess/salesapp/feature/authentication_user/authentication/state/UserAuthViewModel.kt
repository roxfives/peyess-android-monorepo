package com.peyess.salesapp.feature.authentication_user.authentication.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.auth.authenticateUser
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber

class UserAuthViewModel @AssistedInject constructor(
    @Assisted initialState: UserAuthState,
    private val application: SalesApplication,
    private val authenticationRepository: AuthenticationRepository
): MavericksViewModel<UserAuthState>(initialState) {

    init {
        loadActiveCollaborator()
    }

    private fun loadActiveCollaborator() = withState {
        authenticationRepository.currentUser().execute {
            Timber.i("Current user is: ${it}")

            copy(currentUser = it)
        }
    }

    fun localSignIn() {}

    fun regularSignIn() = withState {
        if (it.currentUserAuthState is Loading || it.currentUser is Loading) {
            return@withState
        }

        setState {
            copy(
                authErrorMessage = application.stringResource(R.string.empty_string),
            )
        }

        if (it.password.isEmpty()) {
            setState {
                copy(currentUserAuthState = Fail(
                    Exception("Invalid credentials"),
                    UserAuthenticationState.Unauthenticated,
                ))
            }

            return@withState
        }

        val currentUser = it.currentUser.invoke()
        if (it.currentUser is Fail || it.currentUser is Uninitialized || currentUser == null) {
            setState {
                copy(
                    currentUserAuthState = Fail(
                        Exception("User has not been loaded"),
                        UserAuthenticationState.Unauthenticated,
                    )
                )
            }

            return@withState
        }

        val firebaseApplication = authenticationRepository.userFirebaseApp(currentUser.id)
        if (firebaseApplication == null) {
            Timber.e("Firebase application is null for: ${currentUser.id}")

            setState {
                copy(
                    currentUserAuthState = Fail(
                        Exception("Firebase application is null"),
                        UserAuthenticationState.Unauthenticated
                    )
                )
            }

            return@withState
        }

        authenticateUser(
            email = it.email,
            password = it.password,
            firebaseApp = firebaseApplication,
        ).execute { authState ->
            Timber.i("Current auth state: ${authState}")
            copy(currentUserAuthState = authState)
        }
    }

    fun onEmailChanged(email: String) = setState {
        copy(email = email)
    }

    fun onPasswordChanged(password: String) = setState {
        copy(password = password)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<UserAuthViewModel, com.peyess.salesapp.feature.authentication_user.authentication.state.UserAuthState> {
        override fun create(state: com.peyess.salesapp.feature.authentication_user.authentication.state.UserAuthState): UserAuthViewModel
    }

    companion object:
        MavericksViewModelFactory<UserAuthViewModel, com.peyess.salesapp.feature.authentication_user.authentication.state.UserAuthState> by
        hiltMavericksViewModelFactory()
}