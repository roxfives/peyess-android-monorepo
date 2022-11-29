package com.peyess.salesapp.feature.authentication_user.screen.authentication.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.LocalAuthorizationState
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.utils.string.isEmailValid

data class UserAuthState(
    val currentUser: Async<CollaboratorDocument> = Uninitialized,
    val currentUserAuthState: Async<UserAuthenticationState> = Uninitialized,

    val hasLocalPassword: Async<Boolean> = Uninitialized,

    val authErrorMessage: String = "",
    val signInAttempts: Int = 0,

    val email: String = "",
    val emailErrorMessage: String = "",

    val password: String = "",
    val passwordErrorMessage: String = "",

    val currentUserLocalAuthState: Async<LocalAuthorizationState> = Uninitialized,
    val localSignInAttempts: Int = 0,
    val passcode: String = "",
    val passcodeErrorMessage: String = "",
    val confirmPasscodeReset: Boolean = false,
): MavericksState {
    val isAuthenticating = currentUserAuthState is Loading || hasLocalPassword is Loading
    val hasError = currentUserAuthState is Fail

    val isAuthenticated = currentUserAuthState is Success
            && currentUserAuthState.invoke() == UserAuthenticationState.Authenticated

    val hasUsernameError = signInAttempts > 0 && !isEmailValid(email = email)
    val hasPasswordError = signInAttempts > 0 && password.isEmpty()

    val hasPasscodeError = localSignInAttempts > 0
            && currentUserLocalAuthState is Fail

    val canSignIn = !hasUsernameError
            && !hasPasswordError
            && password.isNotEmpty()
            && email.isNotEmpty()
}