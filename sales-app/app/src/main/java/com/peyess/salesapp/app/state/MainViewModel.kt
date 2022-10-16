package com.peyess.salesapp.app.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.auth.StoreAuthState
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.database.room.gambeta.GambetaDao
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import timber.log.Timber


class MainViewModel @AssistedInject constructor(
    @Assisted initialState: MainAppState,
    val authenticationRepository: AuthenticationRepository,
    val saleRepository: SaleRepository,
    private val clientRepository: ClientRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val gambetaDao: GambetaDao,
): MavericksViewModel<MainAppState>(initialState) {

    init {
        setState {
            copy(
                createNewSale = Success(false),
            )
        }

        withState {
            gambetaDao.getGambeta(0).execute {
                copy(isUpdatingProductsAsync = it)
            }
        }

        onEach(MainAppState::authState) {
            if (it is AppAuthenticationState.Authenticated) {
                loadClients()
                loadServiceOrders()
            }
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

        loadCurrentCollaborator()
        loadStore()
    }

    private fun loadStore() = withState {
        authenticationRepository.currentStore.execute {
            Timber.i("Current store $it")
            copy(currentStoreAsync = it)
        }
    }

    private fun loadCurrentCollaborator() = withState {
        authenticationRepository.currentUser().execute(Dispatchers.IO) {
            Timber.i("Current colalborator is $it")
            copy(currentCollaboratorAsync = it)
        }
    }

    private fun loadClients() = withState {
        clientRepository.clients()
            .execute {
                copy(clientListAsync = it)
            }
    }

    private fun loadServiceOrders() = withState {
        serviceOrderRepository
            .serviceOrders()
            .execute(Dispatchers.IO) {
                copy(serviceOrderListAsync = it)
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