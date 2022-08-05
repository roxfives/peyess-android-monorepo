package com.peyess.salesapp.auth

sealed class StoreAuthState {
    object Unauthenticated: StoreAuthState()

    object Authenticated: StoreAuthState() // Authenticated, but not authorized
//    object Away: StoreAuthState()
//    object StoreEmailNotVerified: AuthState() // Authenticated, but email has to be verified
//    object UserEmailNotVerified: AuthState() // Authenticated, but email has to be verified

//    object Unknown: AuthState() // Unknown state should always be considered an unexpected state

//    object Authenticating: AuthState()
//    object Disabled: AuthState()
}
