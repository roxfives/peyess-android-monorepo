package com.peyess.salesapp.screen.authentication_user_list.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.auth.UserAuthState
import com.peyess.salesapp.model.store.OpticalStore
import com.peyess.salesapp.model.users.Collaborator



data class UserListState(
    val screenState: ScreenState = ScreenState.ChooseUser,

    val currentStore: Async<OpticalStore> = Uninitialized,

    val users: List<Collaborator> = listOf(),

    val currentUser: Collaborator = Collaborator(),
    val currentUserAuthState: Async<UserAuthState> = Uninitialized,
    val authErrorMessage: String = "",

    val password: String = "",
): MavericksState {
    val isAuthenticating = currentUserAuthState is Loading
    val hasError = currentUserAuthState is Fail

    val canSignIn = password.isNotEmpty()

    sealed class ScreenState {
        object ChooseUser: ScreenState()
        object SignIn: ScreenState()
    }
}