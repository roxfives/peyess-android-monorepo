package com.peyess.salesapp.feature.authentication_user.screen.authentication.state

import android.net.Uri
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.auth.authenticateUser
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.feature.authentication_user.error.InvalidCredentialsError
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.utils.string.isEmailValid
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.filterNotNull
import timber.log.Timber

class UserAuthViewModel @AssistedInject constructor(
    @Assisted initialState: UserAuthState,
    private val application: SalesApplication,
    private val authenticationRepository: AuthenticationRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
): MavericksViewModel<UserAuthState>(initialState) {

    init {
        loadActiveCollaborator()

        setState {
            copy(
                authErrorMessage = application.stringResource(R.string.empty_string),
                passwordErrorMessage = application.stringResource(R.string.msg_error_empty_password),
                passcodeErrorMessage = application
                    .stringResource(R.string.error_wrong_passcode),
            )
        }

        onEach(UserAuthState::currentUser) {
            Timber.i("Current user is $it")

            checkCurrentAuthState()
        }

        onEach(UserAuthState::currentUserAuthState) {
            if (it is Fail) {
                Timber.e("User authentication failed", it.error)
                Timber.e(it.error)

                setState {
                    copy(
                        authErrorMessage = signInErrorMessage(it.error),
                        hasLocalPassword = Fail(it.error, false),
                    )
                }
            } else {
                checkLocalPasscode()
            }
        }
    }

    private fun loadActiveCollaborator() = withState {
        authenticationRepository.currentUser().filterNotNull().execute {
            Timber.i("Current user", it)

            copy(currentUser = it)
        }
    }

    private fun checkCurrentAuthState() = withState {
        authenticationRepository.userAuthenticationState().execute {
            Timber.i("Checking if user is authenticated", it)

            copy(
                currentUserAuthState = it,
                emailErrorMessage = "",
                passwordErrorMessage = "",
            )
        }
    }

    private fun checkLocalPasscode() = withState {
        authenticationRepository.userHasPasscode().execute {
            Timber.i("Checking if user has a passcode")

            copy(hasLocalPassword = it)
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
            is InvalidCredentialsError ->
                application.stringResource(R.string.msg_error_invalid_credentials)
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

    suspend fun pictureForUser(uid: String): Uri {
        return collaboratorsRepository.pictureFor(uid)
    }

    fun resetPasscode() = withState {
        authenticationRepository.userSignOut().execute {
            authenticationRepository.resetUserPasscode()
            authenticationRepository.resetCurrentUser()

            copy(
                confirmPasscodeReset = false,
                currentUserAuthState = it,
                currentUserLocalAuthState = Uninitialized,
            )
        }
    }


    fun confirmPasscodeReset() = setState {
        copy(confirmPasscodeReset = true)
    }

    fun cancelPasscodeReset() = setState {
        copy(confirmPasscodeReset = false)
    }

    fun onPasscodeChanged(passcode: String) = setState {
        copy(passcode = passcode)
    }

    fun onPasscodeConfirmed() = withState {
        if (it.currentUserLocalAuthState is Loading) {
            return@withState
        }

        setState {
            copy(localSignInAttempts = it.localSignInAttempts + 1)
        }

        authenticationRepository.authenticateLocalUser(it.passcode).execute { state ->
            Timber.i("Local authentication is $state")
            copy(currentUserLocalAuthState = state)
        }
    }

    fun signIn() = withState {
        if (it.currentUserAuthState is Loading || it.currentUser is Loading) {
            return@withState
        }

        setState {
            copy(
                signInAttempts = it.signInAttempts + 1,
                authErrorMessage = application.stringResource(R.string.empty_string),
            )
        }

        if (it.email.isEmpty() || !isEmailValid(it.email) || it.password.isEmpty()) {
            setState {
                copy(currentUserAuthState = Fail(
                        InvalidCredentialsError(),
                        UserAuthenticationState.Unauthenticated,
                    )
                )
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
            uid = it.currentUser.invoke()?.id ?: "",
            email = it.email,
            password = it.password,
            firebaseApp = firebaseApplication,
        ).execute { authState ->
            Timber.i("Current auth state: ${authState}")
            copy(currentUserAuthState = authState)
        }
    }

    fun onEmailChanged(email: String) = setState {
        copy(email = email, emailErrorMessage = errorMessageEmail(email))
    }

    fun onPasswordChanged(password: String) = setState {
        copy(password = password)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<UserAuthViewModel, UserAuthState> {
        override fun create(state: UserAuthState): UserAuthViewModel
    }

    companion object:
        MavericksViewModelFactory<UserAuthViewModel, UserAuthState> by hiltMavericksViewModelFactory()
}