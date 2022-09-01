package com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state

import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState

data class FirstTimeState(
    @PersistState val isFirstTime: Boolean? = null,
    @PersistState val usageScore: Float = 0f,
): MavericksState {
    val showUsageScore = !(isFirstTime ?: true)
}
