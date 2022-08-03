package com.peyess.salesapp.app.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MvRxViewModel
import dagger.Binds
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

//class MainViewModel(initialState: MainAppState): MvRxViewModel<MainAppState>(initialState) {
//
//    fun incrementCount() {
//        setState { copy(myTest = myTest + 1) }
//    }
//}

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun mainViewModelFactory(factory: MainViewModel.Factory): AssistedViewModelFactory<*, *>
}


class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
): MvRxViewModel<MainAppState>(initialState) {
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