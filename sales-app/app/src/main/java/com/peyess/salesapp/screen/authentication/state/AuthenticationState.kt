package com.peyess.salesapp.screen.authentication.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.AuthState
import com.peyess.salesapp.auth.AuthenticationError


data class AuthenticationState(
    val authState: Async<AuthState> = Uninitialized,

    val authError: AuthenticationError = AuthenticationError.None,
    val errorMessage: String = "",

    val username: String = "",
    val password: String = "",

): MavericksState {
    val isLoading = authState is Loading
    val hasError = authError !== AuthenticationError.None
}