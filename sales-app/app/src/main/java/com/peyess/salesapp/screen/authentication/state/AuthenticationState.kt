package com.peyess.salesapp.screen.authentication.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.auth.AuthenticationError
import com.peyess.salesapp.utils.string.isEmailValid


data class AuthenticationState(
    val authState: Async<StoreAuthState> = Uninitialized,

    val authError: AuthenticationError = AuthenticationError.None,
    val errorMessage: String = "",

    val signInAttempts: Int = 0,

    val username: String = "",
    val usernameErrorMessage: String = "",

    val password: String = "",
    val passwordErrorMessage: String = "",
): MavericksState {

    val isLoading = authState is Loading
    val hasError = authState is Fail

    val hasUsernameError = signInAttempts > 0 && !isEmailValid(email = username)
    val hasPasswordError = signInAttempts > 0 && password.isEmpty()

    val canSignIn = !hasUsernameError
            && !hasPasswordError
            && password.isNotEmpty()
            && username.isNotEmpty()

}