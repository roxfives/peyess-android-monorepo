package com.peyess.salesapp.screen.edit_service_order.payment.state

import android.net.Uri
import arrow.core.Either
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import timber.log.Timber

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditPaymentViewModel, EditPaymentState>
private typealias EditPaymentViewModelFactory =
        MavericksViewModelFactory<EditPaymentViewModel, EditPaymentState>

class EditPaymentViewModel @AssistedInject constructor(
    @Assisted initialState: EditPaymentState,

    private val clientRepository: ClientRepository,
): MavericksViewModel<EditPaymentState>(initialState) {

    suspend fun pictureForClient(clientId: String): Uri {
        return Either.catch {
            clientRepository.pictureForClient(clientId)
        }.mapLeft {
            Timber.e("Error getting picture for client $clientId: ${it.message}", it)
        }.fold(
            ifLeft = { Uri.EMPTY },
            ifRight = { it },
        )
    }

    fun onUpdatePaymentId(paymentId: Long) = setState {
        copy(paymentId = paymentId)
    }

    fun onUpdateClientId(clientId: String) = setState {
        copy(clientId = clientId)
    }

    fun onUpdateSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onUpdateServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: EditPaymentState): EditPaymentViewModel
    }

    companion object: EditPaymentViewModelFactory by hiltMavericksViewModelFactory()
}