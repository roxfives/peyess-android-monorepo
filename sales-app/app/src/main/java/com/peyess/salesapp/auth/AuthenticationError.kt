package com.peyess.salesapp.auth

sealed class AuthenticationError {
    object SignInFailed: AuthenticationError()
    object InvalidCredentials: AuthenticationError()
    object None: AuthenticationError()
}