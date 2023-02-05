package com.peyess.salesapp.feature.sale.service_order.state

import android.content.Context
import android.net.Uri
import arrow.core.Either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepositoryResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.SingleColoringResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleLensResponse
import com.peyess.salesapp.data.repository.lenses.room.SingleTreatmentResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.local_sale.client_picked.ClientPickedRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepositoryResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentRepository
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentResponse
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentTotalResponse
import com.peyess.salesapp.data.repository.local_sale.positioning.LocalPositioningFetchBothResponse
import com.peyess.salesapp.data.repository.local_sale.positioning.LocalPositioningRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.data.repository.management_picture_upload.PictureAddResponse
import com.peyess.salesapp.data.repository.management_picture_upload.PictureUploadRepository
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepositoryResponse
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.data.room.database.ActiveSalesDatabase
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.feature.sale.service_order.adapter.toColoring
import com.peyess.salesapp.feature.sale.service_order.adapter.toFrames
import com.peyess.salesapp.feature.sale.service_order.adapter.toLens
import com.peyess.salesapp.feature.sale.service_order.adapter.toOverallDiscount
import com.peyess.salesapp.feature.sale.service_order.adapter.toPayment
import com.peyess.salesapp.feature.sale.service_order.adapter.toPaymentFee
import com.peyess.salesapp.feature.sale.service_order.adapter.toPictureUploadDocument
import com.peyess.salesapp.feature.sale.service_order.adapter.toPrescription
import com.peyess.salesapp.feature.sale.service_order.adapter.toSalePaymentDocument
import com.peyess.salesapp.feature.sale.service_order.adapter.toTreatment
import com.peyess.salesapp.feature.sale.service_order.model.Coloring
import com.peyess.salesapp.feature.sale.service_order.model.Frames
import com.peyess.salesapp.feature.sale.service_order.model.Lens
import com.peyess.salesapp.feature.sale.service_order.model.Payment
import com.peyess.salesapp.feature.sale.service_order.model.Treatment
import com.peyess.salesapp.feature.sale.service_order.utils.SaleDataGenerationResponse
import com.peyess.salesapp.feature.sale.service_order.utils.ServiceOrderUploader
import com.peyess.salesapp.features.pdf.service_order.buildHtml
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.ProductPickedResponse
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.utils.file.createPrintFile
import com.peyess.salesapp.workmanager.picture_upload.enqueuePictureUploadManagerWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
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
    private val salesApplication: SalesApplication,
    private val saleRepository: SaleRepository,
    private val salesDatabase: ActiveSalesDatabase,
    private val authenticationRepository: AuthenticationRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val framesRepository: LocalFramesRepository,
    private val positioningRepository: PositioningRepository,
    private val measuringRepository: MeasuringRepository,
    private val localPositioningRepository: LocalPositioningRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val salePaymentRepository: SalePaymentRepository,
    private val discountRepository: OverallDiscountRepository,
    private val paymentFeeRepository: PaymentFeeRepository,
    private val localClientRepository: LocalClientRepository,
    private val clientRepository: ClientRepository,
    private val clientPickedRepository: ClientPickedRepository,
    private val pictureUploadRepository: PictureUploadRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
    private val firebaseManager: FirebaseManager,
): MavericksViewModel<ServiceOrderState>(initialState) {

    private lateinit var serviceOrderUploader: ServiceOrderUploader
    private lateinit var serviceOrderHid: String

    init {
        loadCurrentStore()

        loadClients()
        loadPositioning()

        createUploader()
        createHid()

        onAsync(ServiceOrderState::activeStoreIdAsync) {
            setState { copy(activeStoreId = it) }
        }

        onAsync(ServiceOrderState::productPickedResponseAsync) { processProductPickedResponse(it) }
        onAsync(ServiceOrderState::lensResponseAsync) { processLensPickedResponse(it) }
        onAsync(ServiceOrderState::coloringResponseAsync) { processColoringPickedResponse(it) }
        onAsync(ServiceOrderState::treatmentResponseAsync) { processTreatmentPickedResponse(it) }
        onAsync(ServiceOrderState::framesResponseAsync) { processFramesResponse(it) }

        onAsync(ServiceOrderState::discountAsync) { processDiscountResponse(it) }
        onAsync(ServiceOrderState::paymentFeeAsync) { processPaymentFeeResponse(it) }
        onAsync(ServiceOrderState::totalPaidAsync) { processTotalPaidResponse(it) }
        onAsync(ServiceOrderState::paymentsResponseAsync) { processPaymentsResponse(it) }

        onAsync(ServiceOrderState::serviceOrderGenerationResponseAsync) {
            processGenerateSaleResponse(it)
        }

        onAsync(ServiceOrderState::localPositioningsAsync) { processLocalPositioningResponse(it) }
        onAsync(ServiceOrderState::localPrescriptionResponseAsync) {
            processPrescriptionPictureResponse(it)
        }

        onAsync(ServiceOrderState::addPrescriptionPictureResponseAsync) {
            processAddPrescriptionPictureResponse(it)
        }

        onAsync(ServiceOrderState::addPositioningLeftResponseAsync) {
            processAddPositioningLeftPictureResponse(it)
        }

        onAsync(ServiceOrderState::addPositioningRightResponseAsync) {
            processAddPositioningRightPictureResponse(it)
        }

        onEach(ServiceOrderState::serviceOrderId) {
            if (it.isNotBlank()) {
                loadPrescriptionForServiceOrder(it)
                loadProductPicked(it)
                loadFrames(it)
            }
        }
        onEach(ServiceOrderState::productPicked) { loadProducts(it) }
        onEach(ServiceOrderState::saleId) {
            if (it.isNotBlank()) {
                loadPayments(it)
                loadPaymentFee(it)
                loadDiscount(it)
                loadTotalPaid(it)
            }
        }

        onEach(
            ServiceOrderState::lens,
            ServiceOrderState::coloring,
            ServiceOrderState::treatment,
            ServiceOrderState::frames,
        ) { lens, coloring, treatment, frames ->
            updateTotalToPay(lens, coloring, treatment, frames)
        }

        onEach(ServiceOrderState::serviceOrderResponse) {
            if (
                it.first.lPositioningId.isNotBlank()
                    && it.first.rPositioningId.isNotBlank()
                    && it.first.prescriptionId.isNotBlank()
            ) {
                schedulePictureUpload()
            }
        }
    }

    private fun loadCurrentStore() {
        suspend {
            authenticationRepository.activeStoreId()
        }.execute(Dispatchers.IO) {
            copy(activeStoreIdAsync = it)
        }
    }

    private fun loadDiscount(saleId: String) {
        discountRepository.watchDiscount(saleId)
            .execute(Dispatchers.IO) {
                copy(discountAsync = it)
            }
    }

    private fun loadPaymentFee(saleId: String) {
        paymentFeeRepository.watchPaymentFee(saleId)
            .execute(Dispatchers.IO) {
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
            salesDatabase = salesDatabase,
            authenticationRepository = authenticationRepository,
            saleRepository = saleRepository,
            positioningRepository = positioningRepository,
            measuringRepository = measuringRepository,
            prescriptionRepository = prescriptionRepository,
            purchaseRepository = purchaseRepository,
            serviceOrderRepository = serviceOrderRepository,
            discountRepository = discountRepository,
            paymentFeeRepository = paymentFeeRepository,
            localLensesRepository = localLensesRepository,
            framesRepository = framesRepository,
            localClientRepository = localClientRepository,
            clientPickedRepository = clientPickedRepository,
            firebaseManager = firebaseManager,
            salePaymentRepository = salePaymentRepository,
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

    private fun loadPrescriptionForServiceOrder(serviceOrderId: String) {
        suspend {
            localPrescriptionRepository.getPrescriptionForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(localPrescriptionResponseAsync = it)
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

    private fun loadPayments(saleId: String) {
        salePaymentRepository.watchPaymentsForSale(saleId)
            .execute(Dispatchers.IO) {
                copy(paymentsResponseAsync = it)
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

    private fun processPaymentsResponse(response: SalePaymentResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    paymentsResponseAsync = Fail(
                        it.error ?: Throwable(it.description),
                    ),
                )
            },

            ifRight = {
                copy(payments = it.map { p -> p.toPayment() })
            }
        )
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

    private fun processTotalPaidResponse(response: SalePaymentTotalResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    totalPaidAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(totalPaid = it) }
        )
    }

    private fun loadTotalPaid(saleId: String) {
        salePaymentRepository.watchTotalPayment(saleId)
            .execute(Dispatchers.IO) { copy(totalPaidAsync = it) }
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

    private fun processGenerateSaleResponse(response: SaleDataGenerationResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    serviceOrderGenerationResponseAsync = Fail(
                        it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = {
                copy(serviceOrderResponse = it)
            },
        )
    }

    private fun schedulePrescriptionPictureUpload(soId: String) {
        suspend {
            localPrescriptionRepository.getPrescriptionForServiceOrder(soId)
        }.execute(Dispatchers.IO) {
            copy(localPrescriptionResponseAsync = it)
        }
    }

    private fun scheduleMeasuringPictureUpload(soId: String) {
        suspend {
            localPositioningRepository.bothPositioningsForServiceOrder(soId)
        }.execute(Dispatchers.IO) {
            copy(localPositioningsAsync = it)
        }
    }


    private fun processPrescriptionPictureResponse(response: LocalPrescriptionResponse) = withState { state ->
        response.fold(
            ifLeft = {
                setState {
                    copy(
                        localPrescriptionResponseAsync = Fail(
                            it.error ?: Throwable(it.description)
                        ),
                    )
                }
            },

            ifRight = {
                addPrescriptionPictureToUpload(
                    it.toPictureUploadDocument(
                        salesApplication,
                        state.activeStoreId,
                        state.userClient.id,
                        state.serviceOrderResponse.first.prescriptionId,
                    )
                )

                setState {
                    copy(prescription = it.toPrescription())
                }
            },
        )
    }

    private fun processLocalPositioningResponse(
        response: LocalPositioningFetchBothResponse,
    ) = withState { state ->
        response.fold(
            ifLeft = {
                setState {
                    copy(
                        localPositioningsAsync = Fail(
                            it.error ?: Throwable(it.description)
                        ),
                    )
                }
            },

            ifRight = {
                addPositioningLeftPictureToUpload(
                    it.left.toPictureUploadDocument(
                        salesApplication = salesApplication,
                        storeId = state.activeStoreId,
                        clientId = state.userClient.id,
                    )
                )

                addPositioningRightPictureToUpload(
                    it.right.toPictureUploadDocument(
                        salesApplication = salesApplication,
                        storeId = state.activeStoreId,
                        clientId = state.userClient.id,
                    )
                )
            },
        )
    }

    private fun addPrescriptionPictureToUpload(pictureUploadDocument: PictureUploadDocument) {
        suspend {
            pictureUploadRepository.addPicture(pictureUploadDocument)
        }.execute {
            copy(addPrescriptionPictureResponseAsync = it)
        }
    }

    private fun addPositioningLeftPictureToUpload(pictureUploadDocument: PictureUploadDocument) {
        suspend {
            pictureUploadRepository.addPicture(pictureUploadDocument)
        }.execute {
            copy(addPositioningLeftResponseAsync = it)
        }
    }

    private fun addPositioningRightPictureToUpload(pictureUploadDocument: PictureUploadDocument) {
        suspend {
            pictureUploadRepository.addPicture(pictureUploadDocument)
        }.execute {
            copy(addPositioningRightResponseAsync = it)
        }
    }

    private fun processAddPrescriptionPictureResponse(response: PictureAddResponse) {
        response.fold(
            ifLeft = {
                setState {
                    copy(
                        addPrescriptionPictureResponseAsync = Fail(
                            it.error ?: Throwable(it.description)
                        ),
                    )
                }
            },

            ifRight = {
                enqueuePictureUploadManagerWorker(
                    context = salesApplication as Context,
                    uploadEntryId = it,
                )
            },
        )
    }

    private fun processAddPositioningLeftPictureResponse(response: PictureAddResponse) {
        response.fold(
            ifLeft = {
                setState {
                    copy(
                        addPositioningLeftResponseAsync = Fail(
                            it.error ?: Throwable(it.description)
                        ),
                    )
                }
            },

            ifRight = {
                enqueuePictureUploadManagerWorker(
                    context = salesApplication as Context,
                    uploadEntryId = it,
                )
            },
        )
    }

    private fun processAddPositioningRightPictureResponse(response: PictureAddResponse) {
        response.fold(
            ifLeft = {
                setState {
                    copy(
                        addPositioningRightResponseAsync = Fail(
                            it.error ?: Throwable(it.description)
                        ),
                    )
                }
            },

            ifRight = {
                enqueuePictureUploadManagerWorker(
                    context = salesApplication as Context,
                    uploadEntryId = it,
                )
            },
        )
    }

    private fun schedulePictureUpload() = withState {
        schedulePrescriptionPictureUpload(it.serviceOrderId)
        scheduleMeasuringPictureUpload(it.serviceOrderId)
    }

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
        copy(serviceOrderGenerationResponseAsync = Uninitialized)
    }

    fun generateSale(context: Context) = withState { state ->
        suspend {
            val sale = serviceOrderUploader.generateSaleData(
                context = context,
                hid = state.hidServiceOrder,
                serviceOrderId = state.serviceOrderId,
                localSaleId = state.saleId,
                uploadPartialData = true,
            )

            sale.fold(
                ifLeft = { it },
                ifRight = {
                    serviceOrderUploader.emitServiceOrder(it.first, it.second, state.saleId)
                    it
                }
            )

            sale
        }.execute(Dispatchers.IO) {
            copy(serviceOrderGenerationResponseAsync = it)
        }
    }

    fun generateServiceOrderPdf(
        context: Context,
        onPdfGenerated: (File) -> Unit,
        onPdfGenerationFailed: () -> Unit,
    ) = withState {
        suspend {
            val htmlToPdfConverter = HtmlToPdfConvertor(context)

            val salePair = serviceOrderUploader
                .generateSaleData(
                    context = context,
                    hid = it.hidServiceOrder,
                    serviceOrderId = it.serviceOrderId,
                    localSaleId = it.saleId,
                    uploadPartialData = false,
                )
                .fold(
                    ifLeft = { null},
                    ifRight = { it },
                )

            val html = if (salePair != null) {
                buildHtml(context, salePair.first, salePair.second)
            } else {
                null
            }

            Timber.v("Using the html to print the document")
            Timber.v(html)

            if (html != null) {
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
            }
        }.execute(Dispatchers.IO) {
            Timber.i("Generated Service Order result is $it")

            copy(isSOPdfBeingGenerated = it is Loading)
        }
    }

    fun createPayment(onAdded: (id: Long) -> Unit) = withState {
        suspend {
            val payment = Payment(
                saleId = it.saleId,
            )

            salePaymentRepository
                .addPaymentToSale(payment.toSalePaymentDocument())
                .tap {
                    withContext(Dispatchers.Main) {
                        onAdded(it)
                    }
                }
        }.execute(Dispatchers.IO) {
            copy(creatingNewPaymentAsync = it)
        }
    }

    fun deletePayment(payment: Payment) {
        viewModelScope.launch(Dispatchers.IO) {
            salePaymentRepository.deletePayment(payment.toSalePaymentDocument())
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