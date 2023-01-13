package com.peyess.salesapp.feature.sale.service_order.state

import android.content.Context
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.client.firestore.ClientDao
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.database.room.ActiveSalesDatabase
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.service_order.adapter.toColoring
import com.peyess.salesapp.feature.sale.service_order.adapter.toFrames
import com.peyess.salesapp.feature.sale.service_order.adapter.toLens
import com.peyess.salesapp.feature.sale.service_order.adapter.toOverallDiscount
import com.peyess.salesapp.feature.sale.service_order.adapter.toPaymentFee
import com.peyess.salesapp.feature.sale.service_order.adapter.toTreatment
import com.peyess.salesapp.feature.sale.service_order.model.Coloring
import com.peyess.salesapp.feature.sale.service_order.model.Frames
import com.peyess.salesapp.feature.sale.service_order.model.Lens
import com.peyess.salesapp.feature.sale.service_order.model.Treatment
import com.peyess.salesapp.feature.sale.service_order.utils.ServiceOrderUploader
import com.peyess.salesapp.features.pdf.service_order.buildHtml
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.utils.file.createPrintFile
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
import kotlinx.coroutines.withContext
import org.nvest.html_to_pdf.HtmlToPdfConvertor
import timber.log.Timber
import java.io.File
import java.time.ZonedDateTime
import kotlin.random.Random
import kotlin.random.asJavaRandom

class ServiceOrderViewModel @AssistedInject constructor(
    @Assisted initialState: ServiceOrderState,
    private val saleRepository: SaleRepository,
    private val productRepository: ProductRepository,

    private val salesDatabase: ActiveSalesDatabase,
    private val clientDao: ClientDao,
    private val authenticationRepository: AuthenticationRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val framesRepository: LocalFramesRepository,
    private val positioningRepository: PositioningRepository,
    private val measuringRepository: MeasuringRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val discountRepository: OverallDiscountRepository,
    private val paymentFeeRepository: PaymentFeeRepository,
    private val firebaseManager: FirebaseManager,
): MavericksViewModel<ServiceOrderState>(initialState) {

    private lateinit var serviceOrderUploader: ServiceOrderUploader
    private lateinit var serviceOrderHid: String

    init {
        loadCurrentSale()

        loadClients()
        loadPrescriptionPicture()
        loadPrescriptionData()
        loadPositioning()
        loadPayments()
        loadTotalPaid()

        createUploader()
        createHid()

        onAsync(ServiceOrderState::productPickedResponseAsync) { processProductPickedResponse(it) }
        onAsync(ServiceOrderState::lensResponseAsync) { processLensPickedResponse(it) }
        onAsync(ServiceOrderState::coloringResponseAsync) { processColoringPickedResponse(it) }
        onAsync(ServiceOrderState::treatmentResponseAsync) { processTreatmentPickedResponse(it) }
        onAsync(ServiceOrderState::framesResponseAsync) { processFramesResponse(it) }

        onAsync(ServiceOrderState::discountAsync) { processDiscountResponse(it) }
        onAsync(ServiceOrderState::paymentFeeAsync) { processPaymentFeeResponse(it) }

        onEach(ServiceOrderState::serviceOrderId) {
            loadProductPicked(it)
            loadFrames(it)
        }
        onEach(ServiceOrderState::productPicked) { loadProducts(it) }
        onEach(ServiceOrderState::saleId) {
            loadPaymentFee(it)
            loadDiscount(it)
        }

        onEach(
            ServiceOrderState::lens,
            ServiceOrderState::coloring,
            ServiceOrderState::treatment,
            ServiceOrderState::frames,
        ) { lens, coloring, treatment, frames ->
            updateTotalToPay(lens, coloring, treatment, frames)
        }

        // TODO: refactor payments repository to use arrowKt and coroutines
        // TODO: load the payments and the total to be paid into the state
    }

    private fun loadCurrentSale() {
        saleRepository.activeSale().execute {
            copy(saleIdAsync = it)
        }
    }

    private fun loadDiscount(saleId: String) {
        suspend {
            discountRepository.discountForSale(saleId)
        }.execute {
            copy(discountAsync = it)
        }
    }

    private fun loadPaymentFee(saleId: String) {
        suspend {
            paymentFeeRepository.paymentFeeForSale(saleId)
        }.execute {
            copy(paymentFeeAsync = it)
        }
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
        copy(hidServiceOrder = serviceOrderHid)
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
            discountRepository,
            paymentFeeRepository,
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

    private fun loadPayments() = withState {
        saleRepository.payments().execute(Dispatchers.IO) {
            copy(paymentsAsync = it)
        }
    }

    private fun loadFrames(serviceOrderId: String) {
        suspend {
            framesRepository.framesForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(framesResponseAsync = it)
        }
    }

    private fun loadProductPicked(serviceOrderId: String) {
        suspend{
            saleRepository.productPicked(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(productPickedResponseAsync = it)
        }
    }

    private fun processProductPickedResponse(response: ProductPickedResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    productPickedResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(productPicked = it) }
        )
    }

    private fun loadLensPicked(lensId: String) {
        suspend {
            localLensesRepository.getLensById(lensId)
        }.execute(Dispatchers.IO) {
            copy(lensResponseAsync = it)
        }
    }

    private fun processLensPickedResponse(response: SingleLensResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    lensResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(lens = it.toLens()) }
        )
    }

    private fun loadColoringPicked(coloringId: String) {
        suspend {
            localLensesRepository.getColoringById(coloringId)
        }.execute(Dispatchers.IO) {
            copy(coloringResponseAsync = it)
        }
    }

    private fun processColoringPickedResponse(response: SingleColoringResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    coloringResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(coloring = it.toColoring()) }
        )
    }

    private fun loadTreatmentPicked(treatmentId: String) {
        suspend {
            localLensesRepository.getTreatmentById(treatmentId)
        }.execute(Dispatchers.IO) {
            copy(treatmentResponseAsync = it)
        }
    }

    private fun processTreatmentPickedResponse(response: SingleTreatmentResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    treatmentResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(treatment = it.toTreatment()) }
        )
    }

    private fun processFramesResponse(response: LocalFramesRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    framesResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(frames = it.toFrames()) }
        )
    }

    private fun processDiscountResponse(response: OverallDiscountRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    discountAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(discount = it.toOverallDiscount()) }
        )
    }

    private fun processPaymentFeeResponse(response: PaymentFeeRepositoryResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    paymentFeeAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(paymentFee = it.toPaymentFee()) }
        )
    }

    private fun loadProducts(productsPicked: ProductPickedDocument){
        viewModelScope.launch(Dispatchers.IO) {
            loadLensPicked(productsPicked.lensId)
            loadColoringPicked(productsPicked.coloringId)
            loadTreatmentPicked(productsPicked.treatmentId)
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

    private fun updateTotalToPay(
        lens: Lens,
        coloring: Coloring,
        treatment: Treatment,
        frames: Frames,
    ) = setState {
        var totalToPay = lens.price + frames.value

        if (!lens.isColoringIncluded && !lens.isColoringDiscounted) {
            totalToPay += coloring.price
        }
        if (!lens.isTreatmentIncluded && !lens.isTreatmentDiscounted) {
            totalToPay += treatment.price
        }

        if (coloring.price > 0) {
            totalToPay += lens.priceAddColoring
        }
        if (treatment.price > 0) {
            totalToPay += lens.priceAddTreatment
        }

        copy(totalToPay = totalToPay)
    }

    fun onUpdateIsCreating(isCreating: Boolean) = setState {
        copy(isCreating = isCreating)
    }

    fun onUpdateSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onUpdateServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun failedAnimationFinished() = setState {
        copy(hasSaleFailed = false)
    }

    fun generateSale(context: Context) = withState {
        suspend {
            var soId = ""
            var saleId = ""

            Timber.i("Getting so info")
            saleRepository
                .activeSO()
                .filterNotNull()
                .take(1)
                .collect{
                    soId = it.id
                    saleId = it.saleId
                }
            Timber.i("Got so info")

            Timber.i("Generating SO with $soId for sale $saleId")
            val salePair = serviceOrderUploader
                .generateSaleData(context, it.hidServiceOrder, soId, saleId, true)
            Timber.i("Generated SO with $soId for sale $saleId")

            Timber.i("Emitting SO with id $soId for sale $saleId")
            serviceOrderUploader.emitServiceOrder(salePair.first, salePair.second, saleId)
            Timber.i("Emitted SO with id $soId for sale $saleId")

        }.execute(Dispatchers.IO) {
            Timber.i("Generated Service Order result is $it")

            copy(
                isSaleDone = it is Success,
                isSaleLoading = it is Loading,
                hasSaleFailed = it is Fail,
            )
        }
    }

    fun generateServiceOrderPdf(
        context: Context,
        onPdfGenerated: (File) -> Unit,
        onPdfGenerationFailed: () -> Unit,
    ) = withState {
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

            Timber.i("Generating pdf for $soId with sale $saleId")

            val salePair = serviceOrderUploader
                .generateSaleData(context, it.hidServiceOrder, soId, saleId, false)
            val html = buildHtml(context, salePair.first, salePair.second)
            val htmlToPdfConverter = HtmlToPdfConvertor(context)

            Timber.v("Using the html to print the document")
            Timber.v(html)

            viewModelScope.launch(Dispatchers.Main) {
                val file = createPrintFile(context)
                if (file.exists()) {
                    file.delete()
                }

                htmlToPdfConverter.convert(
                    file,
                    html,
                    {
                        Timber.e("Failed to generate service order pdf: ${it.message}", it)
                        onPdfGenerationFailed()
                    },
                    { onPdfGenerated(it) },
                )
            }

            Timber.i("Generated PDF document for SO with id $soId for sale $saleId")
        }.execute(Dispatchers.IO) {
            Timber.i("Generated Service Order result is $it")

            copy(
                isSOPdfBeingGenerated = it is Loading,
            )
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

    fun onEditProducts() {}

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<ServiceOrderViewModel, ServiceOrderState> {
        override fun create(state: ServiceOrderState): ServiceOrderViewModel
    }

    companion object:
        MavericksViewModelFactory<ServiceOrderViewModel, ServiceOrderState> by hiltMavericksViewModelFactory()
}