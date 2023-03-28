package com.peyess.salesapp.screen.sale.anamnesis.first_step_first_time.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState

data class FirstTimeState(
    @PersistState val isFirstTime: Boolean? = null,
    @PersistState val usageScore: Float = 0f,

    @PersistState val mikeMessage: String = "",
): MavericksState {
    val showUsageScore = !(isFirstTime ?: true)
}
