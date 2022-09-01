package com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FirstTimeViewModel @AssistedInject constructor(
    @Assisted initialState: FirstTimeState,
): MavericksViewModel<FirstTimeState>(initialState) {

    fun onUsageScoreChanged(score: Float) = setState {
        copy(usageScore = score)
    }

    fun onFirstTimeChanged(isFirstTime: Boolean) = setState {
        copy(isFirstTime = isFirstTime)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<FirstTimeViewModel, FirstTimeState> {
        override fun create(state: FirstTimeState): FirstTimeViewModel
    }

    companion object: MavericksViewModelFactory<FirstTimeViewModel, FirstTimeState>
        by hiltMavericksViewModelFactory()
}