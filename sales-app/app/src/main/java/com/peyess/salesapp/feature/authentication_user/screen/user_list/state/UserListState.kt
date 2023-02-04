package com.peyess.salesapp.feature.authentication_user.screen.user_list.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.UserAuthenticationState
import com.peyess.salesapp.dao.auth.store.OpticalStoreResponse
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.CollaboratorDocument

data class UserListState(
    val resettingCurrentUser: Async<Boolean> = Uninitialized,

    val storeResponseAsync: Async<OpticalStoreResponse> = Uninitialized,
    val store: OpticalStore = OpticalStore(),

    val users: List<CollaboratorDocument> = listOf(),

    val updatingCurrentUser: Async<Boolean> = Uninitialized,
    val currentUser: CollaboratorDocument = CollaboratorDocument(),
    val currentUserAuthState: Async<UserAuthenticationState> = Uninitialized,
    val authErrorMessage: String = "",

    val password: String = "",
): MavericksState {
    val isLoading = resettingCurrentUser is Loading
        || currentUserAuthState is Loading

    val isStoreLoading = storeResponseAsync is Loading
}