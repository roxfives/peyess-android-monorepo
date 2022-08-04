package com.peyess.salesapp.screen.authentication.state

import android.util.Log
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.AuthState
import com.peyess.salesapp.auth.AuthenticationError
import com.peyess.salesapp.auth.authenticateStore
import com.peyess.salesapp.utils.string.isEmailValid
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AuthenticationViewModel @AssistedInject constructor(
    @Assisted initialState: AuthenticationState,
): MavericksViewModel<AuthenticationState>(initialState) {

    init {
        setState {
            copy(
                errorMessage = SalesApplication.string(R.string.empty_string),
                passwordErrorMessage = SalesApplication.string(R.string.msg_error_empty_password),
            )
        }

//        onEach(AuthenticationState::authState) {
//            if (it is Fail) {
//                it.error.
//            }
//        }

        onEach(AuthenticationState::authError) {
            setState {
                val message = if (it is AuthenticationError.InvalidCredentials) {
                    SalesApplication.string(R.string.msg_error_invalid_credentials)
                } else {
                    SalesApplication.string(R.string.error_msg_default)
                }

                copy(errorMessage = message)
            }
        }
    }

    private fun errorMessageEmail(email: String): String {
        return if (email.isEmpty()) {
            SalesApplication.string(R.string.msg_error_empty_email)
        } else {
            SalesApplication.string(R.string.msg_error_invalid_email)
        }
    }

    fun updateUsername(username: String) = setState {
        copy(
            username = username,
            usernameErrorMessage = errorMessageEmail(username),
        )
    }

    fun updatePassword(password: String) = setState {
        copy(
            password = password,
        )
    }

    fun signIn() = withState {
        if (it.authState is Loading) {
            return@withState
        }

        setState {
            copy(
                signInAttempts = it.signInAttempts + 1,
                authError = AuthenticationError.None,
            )
        }

        if (it.username.isEmpty() || !isEmailValid(it.username) || it.password.isEmpty()) {
            setState {
                copy(
                    authError = AuthenticationError.InvalidCredentials,
                    authState = Fail(Exception("Invalid credentials"), AuthState.Unauthenticated)
                )
            }

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