package com.peyess.salesapp.feature.authentication_user.user_list.state

import androidx.navigation.NavHostController
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class UserListViewModel @AssistedInject constructor(
    @Assisted initialState: UserListState,
    private val application: SalesApplication,
    private val authenticationRepository: AuthenticationRepository
): MavericksViewModel<UserListState>(initialState) {

    init {
        loadStore()

        authenticationRepository.activeCollaborators().setOnEach {
            Timber.i("Got users: ${it.size}")

            copy(users = it)
        }
    }

    fun loadStore() = withState {
        authenticationRepository.currentStore.execute {
            Timber.i("Current store is $it", it)
            copy(currentStore = it)
        }
    }

    fun enterStore(navHostController: NavHostController, uid: String) = withState {
        setState { copy(updatingCurrentUser = true) }

        runBlocking {
            authenticationRepository.updateCurrentUser(uid)
        }

        viewModelScope.launch {
            navHostController.navigate(SalesAppScreens.UserAuth.name)
        }
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<UserListViewModel, UserListState> {
        override fun create(state: UserListState): UserListViewModel
    }

    companion object:
        MavericksViewModelFactory<UserListViewModel, UserListState> by
        hiltMavericksViewModelFactory()
}