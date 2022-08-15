package com.peyess.salesapp.app.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import timber.log.Timber


class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
    val authenticationRepository: AuthenticationRepository,
    val saleRepository: SaleRepository,
): MavericksViewModel<MainAppState>(initialState) {

    init {
        setState {
            copy(
                createNewSale = Success(false),
            )
        }

        authenticationRepository.storeAuthState.setOnEach {
            when (it) {
                is StoreAuthState.Authenticated ->
                    copy(authState = AppAuthenticationState.Authenticated)
                is StoreAuthState.Unauthenticated ->
                    copy(authState = AppAuthenticationState.Unauthenticated)
//                else ->
//                    copy(authState = AppAuthenticationState.Away)

            }
        }
    }

    fun startNewSale() = withState {
        saleRepository.createSale().execute(Dispatchers.IO) {
            Timber.i("Creating a new sale $it")
            copy(createNewSale = it)
        }
    }

    fun newSaleStarted() = setState {
        copy(createNewSale = Success(false))
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<MainViewModel, MainAppState> {
        override fun create(state: MainAppState): MainViewModel
    }

    companion object:
        MavericksViewModelFactory<MainViewModel, MainAppState> by hiltMavericksViewModelFactory()
}