package com.peyess.salesapp.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.feature.authentication_store.state.AuthenticationViewModel
import com.peyess.salesapp.feature.authentication_user.screen.authentication.state.UserAuthViewModel
import com.peyess.salesapp.feature.authentication_user.screen.local_password.state.LocalPasswordViewModel
import com.peyess.salesapp.feature.authentication_user.screen.user_list.state.UserListViewModel
import com.peyess.salesapp.feature.sale.welcome.state.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    fun bindWelcomeViewModelFactory(
        factory: WelcomeViewModel.Factory
    ): AssistedViewModelFactory<*, *>
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
    fun bindUserListViewModelFactory(
        factory: UserListViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(UserAuthViewModel::class)
    fun bindUserAuthViewModelFactory(
        factory: UserAuthViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(LocalPasswordViewModel::class)
    fun bindLocalPasswordViewModelFactory(
        factory: LocalPasswordViewModel.Factory
    ): AssistedViewModelFactory<*, *>
}
