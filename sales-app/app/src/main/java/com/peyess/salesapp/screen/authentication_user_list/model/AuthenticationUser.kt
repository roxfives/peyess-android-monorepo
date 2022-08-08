package com.peyess.salesapp.screen.authentication_user_list.model

import com.peyess.salesapp.auth.PeyessUser
import com.peyess.salesapp.auth.UserAuthState

data class AuthenticationUser(
    val user: PeyessUser,
    val authState: UserAuthState = UserAuthState.Unauthenticated,
)