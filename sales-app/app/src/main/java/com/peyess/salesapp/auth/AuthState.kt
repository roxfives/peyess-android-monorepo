package com.peyess.salesapp.auth

sealed class AuthState {
    object Unauthenticated: AuthState()

    object Unauthorized: AuthState() // Authenticated, but not authorized
    object EmailNotVerified: AuthState() // Authenticated, but email has to be verified

    object Unknown: AuthState() // Unknown state should always be considered an unexpected state

//    object Authenticating: AuthState()
//    object Disabled: AuthState()
}
