package com.peyess.salesapp.screen.sale.welcome.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.ActiveServiceOrderResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WelcomeViewModel @AssistedInject constructor(
    @Assisted initialState: WelcomeState,
    private val authenticationRepository: AuthenticationRepository,
    private val salesRepository: SaleRepository,
): MavericksViewModel<WelcomeState>(initialState) {

    init {
        loadCurrentSO()
        loadCurrentCollaborator()

        onAsync(WelcomeState::serviceOrderResponse) {
            processCurrentServiceOrderResponse(it)
        }
        onAsync(WelcomeState::collaboratorNameAsync) {
            processCollaboratorNameResponse(it)
        }
    }

    private fun loadCurrentCollaborator() {
        suspend {
            authenticationRepository.fetchCurrentUserName()
        }.execute(Dispatchers.IO) {
            copy(collaboratorNameAsync = it)
        }
    }

    private fun processCollaboratorNameResponse(response: String) = setState {
        copy(collaboratorName = response)
    }

    private fun loadCurrentSO() {
        suspend {
            salesRepository.currentServiceOrder()
        }.execute(Dispatchers.IO) {
            copy(serviceOrderResponse = it)
        }
    }

    private fun processCurrentServiceOrderResponse(
        response: ActiveServiceOrderResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    serviceOrderResponse = Fail(
                        it.error ?: Throwable(it.description)
                    )
                )
            },

            ifRight = {
                copy(
                    serviceOrderId = it.id,
                    clientNameInput = it.clientName,
                )
            }
        )
    }

    fun onClientNameChanged(name: String) = setState {
        viewModelScope.launch(Dispatchers.IO) {
            salesRepository.updateClientName(serviceOrderId, name)
        }

        copy(clientNameInput = name)
    }

    fun onCancelSale(onCanceled: () -> Unit) = withState {
        suspend {
            salesRepository.cancelCurrentSale().tap {
                withContext(Dispatchers.Main) {
                    onCanceled()
                }
            }
        }.execute(Dispatchers.IO) {
            copy(cancelCurrentSaleAsync = it)
        }
    }

    fun onFinished() = withState {
        suspend {
            salesRepository.updateClientName(it.serviceOrderId, it.clientNameInput)
        }.execute(Dispatchers.IO) {
            copy(hasFinished = it is Success)
        }
    }

    fun onNavigate() = setState {
        copy(hasFinished = false)
    }

    @AssistedFactory
    interface Factory: AssistedViewModelFactory<WelcomeViewModel, WelcomeState> {
        override fun create(state: WelcomeState): WelcomeViewModel
    }

    companion object: MavericksViewModelFactory<WelcomeViewModel, WelcomeState> by hiltMavericksViewModelFactory()
}
