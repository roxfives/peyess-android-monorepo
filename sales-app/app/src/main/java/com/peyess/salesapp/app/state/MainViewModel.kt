package com.peyess.salesapp.app.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
): MavericksViewModel<MainAppState>(initialState) {
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<MainViewModel, MainAppState> {
        override fun create(state: MainAppState): MainViewModel
    }

    companion object:
        MavericksViewModelFactory<MainViewModel, MainAppState> by hiltMavericksViewModelFactory()

    fun incrementCount() {
//        setState { copy(myTest = myTest + 1) }
    }
}