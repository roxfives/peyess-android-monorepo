package com.peyess.salesapp.feature.sale.anamnesis.fifth_step_sports.state

import androidx.annotation.StringRes
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.peyess.salesapp.R

val mikeMessageMapping = mapOf(
    Pair(0, R.string.mike_user_profile_message_sport_nauseating_yes_multi),
    Pair(1, R.string.mike_user_profile_message_sport_nauseating_yes_multi),
    Pair(2, R.string.empty_string),
)

data class FifthStepState(
    @PersistState val selected: Int? = null,
): MavericksState {
    val showMike = selected != null && selected != 2

    @StringRes val mikeMessageId = mikeMessageMapping
        .getOrDefault(selected ?: 4, R.string.empty_string)
}
