package com.peyess.salesapp.feature.authentication_user.screen.user_list.state

import androidx.navigation.NavHostController
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
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
    private val authenticationRepository: AuthenticationRepository
): MavericksViewModel<UserListState>(initialState) {

    init {
        loadStore()
        resetCurrentUser()

        authenticationRepository.activeCollaborators().setOnEach {
            Timber.i("Got users: ${it.size}")

            copy(users = it)
        }
    }

    private fun resetCurrentUser() = withState {
        authenticationRepository.resetCurrentUser().execute {
            copy()
        }
    }

    private fun loadStore() = withState {
        authenticationRepository.currentStore.execute {
            Timber.i("Current store $it")
            copy(currentStore = it)
        }
    }

    fun pickUser(uid: String) = withState {
        if (it.updatingCurrentUser is Loading) {
            return@withState
        }

//        setState { copy(updatingCurrentUser = Loading()) }

        suspend {
            authenticationRepository.updateCurrentUser(uid)
        }.execute {
            copy(updatingCurrentUser = Success(false))
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