package com.peyess.salesapp.feature.sale.anamnesis.third_step_sun_light.state

import androidx.annotation.StringRes
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.PersistState
import com.peyess.salesapp.R

val mikeMessageMapping = mapOf(
    Pair(0, R.string.mike_user_profile_message_light_yes),
    Pair(1, R.string.mike_user_profile_message_light_alittle),
    Pair(2, R.string.mike_user_profile_message_light_no),
)

data class ThirdStepState(
    @PersistState val selected: Int? = null,
): MavericksState {
    val showMike = selected != null

    @StringRes val mikeMessageId = mikeMessageMapping
        .getOrDefault(selected ?: 4, R.string.mike_message_default)
}
