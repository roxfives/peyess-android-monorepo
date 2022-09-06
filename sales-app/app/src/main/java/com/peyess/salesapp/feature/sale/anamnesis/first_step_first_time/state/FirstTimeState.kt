package com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized

data class FirstTimeState(
    @PersistState val isFirstTime: Boolean? = null,
    @PersistState val usageScore: Float = 0f,

    @PersistState val mikeMessage: String = "",
): MavericksState {
    val showUsageScore = !(isFirstTime ?: true)
}
