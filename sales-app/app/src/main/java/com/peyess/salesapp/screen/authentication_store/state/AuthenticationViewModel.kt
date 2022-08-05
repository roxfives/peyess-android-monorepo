package com.peyess.salesapp.screen.authentication_store.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.auth.AuthenticationError
import com.peyess.salesapp.auth.authenticateStore
import com.peyess.salesapp.auth.exception.WrongAccountType
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.utils.string.isEmailValid
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber

class AuthenticationViewModel @AssistedInject constructor(
    @Assisted initialState: AuthenticationState,
    private val application: SalesApplication,
    private val authenticationRepository: AuthenticationRepository
): MavericksViewModel<AuthenticationState>(initialState) {
    private val storeFirebaseApp by lazy { authenticationRepository.storeFirebaseApp() }

    init {
        setState {
            copy(
                errorMessage = application.stringResource(R.string.empty_string),
                passwordErrorMessage = application.stringResource(R.string.msg_error_empty_password),
            )
        }

        onEach(AuthenticationState::authState) {
            if (it is Fail) {
                Timber.d("Store authentication failed", it.error)
                Timber.d(it.error)

                setState {
                    copy(
                        authError = AuthenticationError.SignInFailed,
                        errorMessage = signInErrorMessage(it.error),
                    )
                }
            }
        }
    }

    private fun errorMessageEmail(email: String): String {
        return if (email.isEmpty()) {
            application.stringResource(R.string.msg_error_empty_email)
        } else {
            application.stringResource(R.string.msg_error_invalid_email)
        }
    }

    private fun signInErrorMessage(error: Throwable): String {
        Timber.i("Getting message for $error")

        return when (error) {
            is WrongAccountType ->
                application.stringResource(R.string.msg_error_wrong_account_type)
            is FirebaseAuthInvalidCredentialsException ->
                application.stringResource(R.string.msg_error_invalid_credentials)
            is FirebaseAuthInvalidUserException ->
                if (error.errorCode == "ERROR_USER_DISABLED") {
                    application.stringResource(R.string.msg_error_account_disabled)
                } else {
                    application.stringResource(R.string.msg_error_invalid_credentials)
                }

            else ->
                application.stringResource(R.string.error_msg_default)

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
                    authState = Fail(Exception("Invalid credentials"), StoreAuthState.Unauthenticated)
                )
            }

            return@withState
        }

        authenticateStore(
            email = it.username,
            password = it.password,
            firebaseApp = storeFirebaseApp!!
        ).execute { authState ->
            Timber.i("Current auth state: ${authState}")
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