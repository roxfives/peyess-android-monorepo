package com.peyess.salesapp.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.screen.authentication_store.state.AuthenticationViewModel
import com.peyess.salesapp.screen.authentication_user_list.state.UserListViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModelFactory(
        factory: MainViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    fun bindStoreAuthenticationViewModelFactory(
        factory: AuthenticationViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    fun bindUserAuthenticationViewModelFactory(
        factory: UserListViewModel.Factory
    ): AssistedViewModelFactory<*, *>
}
