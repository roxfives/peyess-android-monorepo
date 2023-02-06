package com.peyess.salesapp.screen.authentication_user.screen.user_list.model

import com.peyess.salesapp.auth.PeyessUser
import com.peyess.salesapp.auth.UserAuthenticationState

data class AuthenticationUser(
    val user: PeyessUser,
    val authState: UserAuthenticationState = UserAuthenticationState.Unauthenticated,
)