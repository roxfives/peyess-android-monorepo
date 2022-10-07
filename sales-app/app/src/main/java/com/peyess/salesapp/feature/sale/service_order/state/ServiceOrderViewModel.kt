package com.peyess.salesapp.feature.sale.service_order.state

import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.client.firestore.ClientDao
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.database.room.ActiveSalesDatabase
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.service_order.utils.ServiceOrderUploader
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.utils.products.ProductSet
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.ZonedDateTime
import kotlin.random.Random
import kotlin.random.asJavaRandom

class ServiceOrderViewModel @AssistedInject constructor(
    @Assisted initialState: ServiceOrderState,
    val salesApplication: SalesApplication,
    val saleRepository: SaleRepository,
    val productRepository: ProductRepository,

    private val salesDatabase: ActiveSalesDatabase,
    private val clientDao: ClientDao,
    private val authenticationRepository: AuthenticationRepository,
    private val positioningRepository: PositioningRepository,
    private val measuringRepository: MeasuringRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val firebaseManager: FirebaseManager,
): MavericksViewModel<ServiceOrderState>(initialState) {

    lateinit var serviceOrderUploader: ServiceOrderUploader
    lateinit var serviceOrderHid: String

    init {
        loadClients()
        loadPrescriptionPicture()
        loadPrescriptionData()
        loadPositioning()
        loadProducts()
        loadFrames()
        loadPayments()
        loadTotalPaid()
        loadTotalToPay()

        createUploader()
        createHid()
    }

    private fun createHid() {
        if (!this::serviceOrderHid.isInitialized) {
            val random = Random.asJavaRandom()
            val size = 9
            val alphabet = charArrayOf(
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
                'R', 'S', 'T', 'U', 'V', 'X', 'W', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9'
            )

            val zonedDateTime = ZonedDateTime.now()
            val year = zonedDateTime.year % 1000
            val month = zonedDateTime.month.value
            val day = zonedDateTime.dayOfMonth

            val suffix = "%02d%02d%02d".format(year, month, day)
            val id = NanoIdUtils.randomNanoId(random, alphabet, size)

            serviceOrderHid = "$suffix-$id"
            updateHid()
        }
    }

    private fun updateHid() = setState {
        copy(hid = serviceOrderHid)
    }

    private fun createUploader() {
        serviceOrderUploader = ServiceOrderUploader(
            salesDatabase,
            productRepository,
            clientDao,
            authenticationRepository,
            saleRepository,
            positioningRepository,
            measuringRepository,
            prescriptionRepository,
            purchaseRepository,
            serviceOrderRepository,
            firebaseManager,
        )
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

    private fun loadPositioning() = withState {
        saleRepository.currentPositioning(Eye.Left).execute(Dispatchers.IO) {
            copy(positioningLeftAsync = it)
        }

        saleRepository.currentPositioning(Eye.Right).execute(Dispatchers.IO) {
            copy(positioningRightAsync = it)
        }
    }

    private fun loadFrames() = withState {
        saleRepository.currentFramesData().execute(Dispatchers.IO) {
            copy(framesEntityAsync = it)
        }
    }

    private fun loadPayments() = withState {
        saleRepository.payments().execute(Dispatchers.IO) {
            copy(paymentsAsync = it)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadProducts() = withState {
        saleRepository
            .pickedProduct()
            .filterNotNull()
            .flatMapLatest {
                combine(
                    productRepository.lensById(it.lensId).filterNotNull().take(1),
                    productRepository.coloringById(it.coloringId).filterNotNull().take(1),
                    productRepository.treatmentById(it.treatmentId).filterNotNull().take(1),
                    ::Triple,
                )
            }.execute(Dispatchers.IO) {
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
                            lensEntityAsync = Success(it.invoke().first),
                            coloringEntityAsync = Success(it.invoke().second),
                            treatmentEntityAsync = Success(it.invoke().third),
                        )
                }
            }

    }

    private fun loadTotalPaid() = withState {
        saleRepository
            .payments()
            .map {
                var totalPaid = 0.0

                it.forEach { payment ->
                    totalPaid += payment.value
                }

                Timber.i("Loaded total paid: $totalPaid")
                totalPaid
            }
            .execute(Dispatchers.IO) {
                copy(totalPaidAsync = it)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadTotalToPay() = withState {
        saleRepository
            .pickedProduct()
            .filterNotNull()
            .flatMapLatest {
                combine(
                    productRepository.lensById(it.lensId).filterNotNull().take(1),
                    productRepository.coloringById(it.coloringId).filterNotNull().take(1),
                    productRepository.treatmentById(it.treatmentId).filterNotNull().take(1),
                    saleRepository.currentFramesData(),
                    ::ProductSet
                )
            }
            .map {
                val lens = it.lens
                val coloring = it.coloring
                val treatment = it.treatment
                val frames = it.frames

                val framesValue = if (frames.areFramesNew) {
                    frames.value
                } else {
                    0.0
                }

                // TODO: Update coloring and treatment to use price instead of suggested price
                var totalToPay = lens.price + framesValue

                if (!lens.isColoringIncluded && !lens.isColoringDiscounted) {
                    totalToPay += coloring.suggestedPrice
                }
                if (!lens.isTreatmentIncluded && !lens.isTreatmentDiscounted) {
                    totalToPay += treatment.suggestedPrice
                }

                if (coloring.suggestedPrice > 0) {
                    totalToPay += lens.suggestedPriceAddColoring
                }
                if (treatment.suggestedPrice > 0) {
                    totalToPay += lens.suggestedPriceAddTreatment
                }

                totalToPay
            }
            .execute(Dispatchers.IO) {
                copy(totalToPayAsync = it)
            }
    }

//    private suspend fun generateServiceOrder(hid: String) {
//
//
//        runBlocking {
//
//        }
//    }

//    private fun createSOWorker(soId: String, saleId: String) {
//        Timber.i("Creating worker")
//
//        val workerData = workDataOf(
//            soIdKey to soId,
//            saleIdKey to saleId,
//        )
//
//        val uploadWorkRequest: WorkRequest =
//            OneTimeWorkRequestBuilder<GenerateServiceOrderWorker>()
//                .setInputData(workerData)
//                .build()
//
//        WorkManager
//            .getInstance(salesApplication)
//            .enqueue(uploadWorkRequest)
//    }

    fun generateSale() = withState {
        suspend {
            var soId = ""
            var saleId = ""

            saleRepository
                .activeSO()
                .filterNotNull()
                .take(1)
                .collect{
                    soId = it.id
                    saleId = it.saleId
                }

            Timber.i("Emitting SO with id $soId for sale $saleId")
            serviceOrderUploader.emitServiceOrder(it.hid, soId, saleId)
            Timber.i("Emitted SO with id $soId for sale $saleId")

        }.execute(Dispatchers.IO) {
            Timber.i("Generated Service Order result is $it")

            copy(isSaleDone = it is Success)
        }
    }

    fun createPayment(onAdded: (id: Long) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            var id: Long = 0

            saleRepository
                .activeSO()
                .filterNotNull()
                .take(1)
                .map {
                    id = saleRepository.addPayment(SalePaymentEntity(soId = it.id))
                }
                .flowOn(Dispatchers.IO)
                .collect()

            withContext(Dispatchers.Main) {
                onAdded(id)
            }
        }
    }

    fun deletePayment(payment: SalePaymentEntity) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            saleRepository.deletePayment(payment)
        }
    }

    fun onEditProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            saleRepository.clearProductComparison()
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