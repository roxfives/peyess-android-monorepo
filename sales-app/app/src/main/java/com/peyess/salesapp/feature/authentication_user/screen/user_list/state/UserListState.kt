package com.peyess.salesapp.feature.authentication_user.screen.user_list.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator

data class UserListState(
    val resettingCurrentUser: Async<Boolean> = Uninitialized,
    val currentStore: Async<OpticalStore> = Uninitialized,

    val users: List<Collaborator> = listOf(),

    val updatingCurrentUser: Async<Boolean> = Uninitialized,
    val currentUser: Collaborator = Collaborator(),
    val currentUserAuthState: Async<UserAuthenticationState> = Uninitialized,
    val authErrorMessage: String = "",

    val password: String = "",
): MavericksState {
    val isLoading = currentStore is Loading
        || resettingCurrentUser is Loading
        || currentUserAuthState is Loading
}