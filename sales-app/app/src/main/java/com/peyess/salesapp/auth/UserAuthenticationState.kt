package com.peyess.salesapp.auth

sealed class UserAuthenticationState {
    object Unauthenticated: UserAuthenticationState()
    object Authenticated: UserAuthenticationState()
}

sealed class LocalAuthorizationState {
    object Unauthorized: LocalAuthorizationState() // Has no local password set
    object Authorized: LocalAuthorizationState()
    object Away: LocalAuthorizationState() // Has a local password, but requires it again

    companion object {
        fun fromString(state: String): LocalAuthorizationState {
            return when(state) {
                "Authorized" -> Authorized
                "Away" -> Away
                else -> Unauthorized
            }
        }

        fun toString(state: LocalAuthorizationState): String {
            return when(state) {
                Authorized -> "Authorized"
                Away -> "Away"
                else -> "Unauthorized"
            }
        }
    }
}
