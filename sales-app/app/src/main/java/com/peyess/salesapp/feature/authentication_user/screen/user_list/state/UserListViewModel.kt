package com.peyess.salesapp.feature.authentication_user.screen.user_list.state

import android.net.Uri
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.collaborator.CollaboratorsRepository
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class UserListViewModel @AssistedInject constructor(
    @Assisted initialState: UserListState,
    private val authenticationRepository: AuthenticationRepository,
    private val collaboratorsRepository: CollaboratorsRepository,
): MavericksViewModel<UserListState>(initialState) {

    init {
        loadStore()
        resetCurrentUser()


        authenticationRepository.activeCollaborators().setOnEach {
            Timber.i("Got users: ${it.size}")

            copy(users = it)
        }
    }

    private fun resetCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            authenticationRepository.resetCurrentUser()
        }
    }

    private fun loadStore() = withState {
        authenticationRepository.currentStore.execute {
            Timber.i("Current store $it")
            copy(currentStore = it)
        }
    }

    suspend fun pictureForUser(uid: String): Uri {
        return collaboratorsRepository.pictureFor(uid)
    }

    suspend fun pictureForStore(storeId: String): Uri {
        return authenticationRepository.pictureForStore(storeId)
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