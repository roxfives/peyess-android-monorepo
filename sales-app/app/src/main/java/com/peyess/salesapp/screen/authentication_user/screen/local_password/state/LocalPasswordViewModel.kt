package com.peyess.salesapp.screen.authentication_user.screen.local_password.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.screen.authentication_user.error.DifferentPasscodesError
import com.peyess.salesapp.screen.authentication_user.error.EmptyPasscodeError
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class LocalPasswordViewModel @AssistedInject constructor(
    @Assisted initialState: LocalPasswordState,
    val application: SalesApplication,
    val authenticationRepository: AuthenticationRepository,
): MavericksViewModel<LocalPasswordState>(initialState) {

    init {
        onEach(LocalPasswordState::setPasscodeProcess) {
            if (it is Fail) {
                setState { copy(passwordErrorMessage = errorMessage(it.error)) }
            }
        }
    }

    private fun errorMessage(error: Throwable): String {
        return when (error) {
            is DifferentPasscodesError ->
                application.stringResource(R.string.error_diff_passcode)
            is EmptyPasscodeError ->
                application.stringResource(R.string.error_empty_passcode)
            else ->
                application.stringResource(R.string.error_msg_default)
        }
    }

    fun onPasscodeChanged(passcode: String) = setState {
        copy(password = passcode)
    }

    fun onPasscodeConfirmationChanged(passcode: String) = setState {
        copy(passwordConfirmation = passcode)
    }

    fun onConfirmPasscode() = withState {
        if (it.password.isEmpty() || it.passwordConfirmation.isEmpty()) {
            setState { copy(setPasscodeProcess = Fail(EmptyPasscodeError())) }
            return@withState
        }

        if (it.password != it.passwordConfirmation) {
            setState { copy(setPasscodeProcess = Fail(DifferentPasscodesError())) }
            return@withState
        }

        authenticationRepository.setUserPasscode(it.password).execute { hasSet ->
            copy(setPasscodeProcess = hasSet)
        }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LocalPasswordViewModel, LocalPasswordState> {
        override fun create(state: LocalPasswordState): LocalPasswordViewModel
    }

    companion object: MavericksViewModelFactory<LocalPasswordViewModel, LocalPasswordState>
        by hiltMavericksViewModelFactory()
}