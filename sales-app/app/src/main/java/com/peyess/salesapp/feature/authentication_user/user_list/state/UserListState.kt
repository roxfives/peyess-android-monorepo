package com.peyess.salesapp.feature.authentication_user.user_list.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator

data class UserListState(
    val currentStore: Async<OpticalStore> = Uninitialized,

    val users: List<Collaborator> = listOf(),

    val updatingCurrentUser: Boolean = false,
    val currentUser: Collaborator = Collaborator(),
    val currentUserAuthState: Async<UserAuthenticationState> = Uninitialized,
    val authErrorMessage: String = "",

    val password: String = "",
): MavericksState