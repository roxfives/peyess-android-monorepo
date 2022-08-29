package com.peyess.salesapp.feature.sale.service_order.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.take
import timber.log.Timber

class ServiceOrderViewModel @AssistedInject constructor(
    @Assisted initialState: ServiceOrderState,
    val saleRepository: SaleRepository,
    val productRepository: ProductRepository,
): MavericksViewModel<ServiceOrderState>(initialState) {

    init {
        loadClients()
        loadPrescriptionPicture()
        loadPrescriptionData()
        loadProducts()
        loadFrames()
    }

    private fun loadClients() = withState {
        saleRepository.clientPicked(ClientRole.User).execute(Dispatchers.IO) {
            copy(userClientAsync = it)
        }

        saleRepository.clientPicked(ClientRole.Responsible).execute(Dispatchers.IO) {
            copy(responsibleClientAsync = it)
        }

        saleRepository.clientPicked(ClientRole.Witness).execute(Dispatchers.IO) {
            copy(witnessClientAsync = it)
        }
    }

    private fun loadPrescriptionPicture() = withState {
        saleRepository.currentPrescriptionPicture().execute(Dispatchers.IO) {
            copy(prescriptionPictureAsync = it)
        }
    }

    private fun loadPrescriptionData() = withState {
        saleRepository.currentPrescriptionData().execute(Dispatchers.IO) {
            copy(prescriptionDataAsync = it)
        }
    }

    private fun loadFrames() = withState {
        saleRepository.currentFramesData().execute(Dispatchers.IO) {
            copy(framesEntityAsync = it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadProducts() = withState {
        saleRepository
            .pickedProduct()
            .filterNotNull()
            .flatMapLatest {
                Timber.i("Getting ids lens: ${it.lensId}, coloring: ${it.coloringId}, treatment: ${it.treatmentId}")

                combine(
                    productRepository.lensById(it.lensId).filterNotNull().take(1),
                    productRepository.coloringById(it.coloringId).filterNotNull().take(1),
                    productRepository.treatmentById(it.treatmentId).filterNotNull().take(1),
                    ::Triple,
                )
            }.execute(Dispatchers.IO) {
                Timber.i("Got products $it")

                when(it) {
                    Uninitialized ->
                        copy(
                            lensEntityAsync = Uninitialized,
                            coloringEntityAsync = Uninitialized,
                            treatmentEntityAsync = Uninitialized,
                        )
                    is Loading ->
                        copy(
                            lensEntityAsync = Loading(),
                            coloringEntityAsync = Loading(),
                            treatmentEntityAsync = Loading(),
                        )
                    is Fail ->
                        copy(
                            lensEntityAsync = Fail(it.error),
                            coloringEntityAsync = Fail(it.error),
                            treatmentEntityAsync = Fail(it.error),
                        )
                    is Success ->
                        copy(
                            lensEntityAsync = Success(it.invoke().first!!),
                            coloringEntityAsync = Success(it.invoke().second!!),
                            treatmentEntityAsync = Success(it.invoke().third!!),
                        )
                }
            }

    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<ServiceOrderViewModel, ServiceOrderState> {
        override fun create(state: ServiceOrderState): ServiceOrderViewModel
    }

    companion object:
        MavericksViewModelFactory<ServiceOrderViewModel, ServiceOrderState> by hiltMavericksViewModelFactory()
}