package com.peyess.salesapp.auth

sealed class UserAuthState {
    object Unauthenticated: UserAuthState()

    object Authenticated: UserAuthState()
    object Away: UserAuthState() // Authenticated, but requires local sign in
}
