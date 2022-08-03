package com.peyess.salesapp.app.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.peyess.salesapp.auth.AuthState

data class MainAppState(
    val authState: AuthState = AuthState.Unauthenticated,
): MavericksState
