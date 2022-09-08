package com.peyess.salesapp.feature.demonstration.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DemonstrationViewModel @AssistedInject constructor(
    @Assisted initialState: DemonstrationState,
): MavericksViewModel<DemonstrationState>(initialState) {

    fun onSelectShown(shownName: String) = setState {
        val shown = DemonstrationShown.fromName(shownName)

        copy(demonstrationShown = shown)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<DemonstrationViewModel, DemonstrationState> {
        override fun create(state: DemonstrationState): DemonstrationViewModel
    }

    companion object: MavericksViewModelFactory<DemonstrationViewModel, DemonstrationState>
    by hiltMavericksViewModelFactory()
}