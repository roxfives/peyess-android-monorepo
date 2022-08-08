package com.peyess.salesapp.auth

sealed class UserAuthenticationState {
    object Unauthenticated: UserAuthenticationState()
    object Authenticated: UserAuthenticationState()
}

sealed class LocalAuthorizationState {
    object Unauthorized: UserAuthenticationState() // Has no local password set
    object Authorized: UserAuthenticationState()
    object Away: UserAuthenticationState() // Has a local password, but requires it again
}
