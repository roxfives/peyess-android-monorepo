package com.peyess.salesapp.screen.authentication_user.screen.local_password.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized

data class LocalPasswordState(
    val password: String = "",
    val passwordConfirmation: String = "",

    val attempts: Int = 0,
    val setPasscodeProcess: Async<Boolean> = Uninitialized,
    val passwordErrorMessage: String = "",
): MavericksState {
    val isSettingPasscode = setPasscodeProcess is Loading
    val hasFailed = setPasscodeProcess is Fail
    val arePasscodesDifferent = attempts > 0 && password != passwordConfirmation
}
