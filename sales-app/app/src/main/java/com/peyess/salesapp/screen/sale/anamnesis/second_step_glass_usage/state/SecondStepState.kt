package com.peyess.salesapp.screen.sale.anamnesis.second_step_glass_usage.state

import androidx.annotation.StringRes
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.peyess.salesapp.R

val mikeMessageMapping = mapOf(
    Pair(0, R.string.mike_user_profile_message_daily),
    Pair(1, R.string.mike_message_default),
    Pair(2, R.string.mike_user_profile_message_leisure),
    Pair(3, R.string.mike_user_profile_message_reading),
)

val workMessageMapping = mapOf(
    Pair(0, R.string.mike_user_profile_message_work_construction),
    Pair(1, R.string.mike_user_profile_message_work_office),
    Pair(2, R.string.mike_user_profile_message_work_housekeeping),
    Pair(3, R.string.mike_user_profile_message_work_chemicals),
    Pair(4, R.string.mike_user_profile_message_work_agriculture),
    Pair(5, R.string.mike_user_profile_message_work_driving),
)

data class SecondStepState(
    @PersistState val selected: Int? = null,
    @PersistState val workSelected: Int? = null,
): MavericksState {
    val showWorkList = selected == 1
    val showMike = (selected != null && selected != 1) || (workSelected != null && workSelected != 6)

    @StringRes val mikeMessageId = if (selected == 1) {
        workMessageMapping.getOrDefault(workSelected ?: 7, R.string.mike_message_default)
    } else {
        mikeMessageMapping.getOrDefault(selected ?: 4, R.string.mike_message_default)
    }
}
