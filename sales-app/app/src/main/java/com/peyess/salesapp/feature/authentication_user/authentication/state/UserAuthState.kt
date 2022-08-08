package com.peyess.salesapp.feature.authentication_user.authentication.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.LocalAuthorizationState
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.model.users.Collaborator

data class UserAuthState(
    val currentUser: Async<Collaborator> = Uninitialized,
    val currentUserAuthState: Async<UserAuthenticationState> = Uninitialized,
    val currentUserAuthorizationState: Async<LocalAuthorizationState> = Uninitialized,
    val authErrorMessage: String = "",

    val email: String = "",
    val password: String = "",
): MavericksState {
    val isAuthenticating = currentUserAuthState is Loading
    val hasError = currentUserAuthState is Fail

    val isLoadingLocalPassword = currentUserAuthorizationState is Loading

    val canSignIn = password.isNotEmpty() && email.isNotEmpty()
}