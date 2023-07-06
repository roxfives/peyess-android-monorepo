package com.peyess.salesapp.features.edit_service_order.fetcher

import android.content.Context
import android.net.Uri
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.leftIfNull
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.sale.active_sale.LocalSaleDocument
import com.peyess.salesapp.dao.sale.active_so.LocalServiceOrderDocument
import com.peyess.salesapp.data.adapter.client.toLocalClientDocument
import com.peyess.salesapp.data.model.client.ClientDocument
import com.peyess.salesapp.data.model.discount.OverallDiscountDocument
import com.peyess.salesapp.data.model.edit_service_order.client_picked.EditClientPickedDocument
import com.peyess.salesapp.data.model.lens.room.repo.StoreLensWithDetailsDocument
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.local_sale.frames.LocalFramesDocument
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentDocument
import com.peyess.salesapp.data.model.local_sale.positioning.LocalPositioningDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.model.measuring.MeasuringDocument
import com.peyess.salesapp.data.model.payment_fee.PaymentFeeDocument
import com.peyess.salesapp.data.model.positioning.PositioningDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.client.error.ClientNotFound
import com.peyess.salesapp.data.repository.client.error.ClientRepositoryUnexpectedError
import com.peyess.salesapp.data.repository.client.error.ExistsClientRepositoryError
import com.peyess.salesapp.data.repository.client.error.UpdateClientRepositoryError
import com.peyess.salesapp.data.repository.client.error.UploadClientRepositoryError
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepository
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.EditLensComparisonRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepository
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepository
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.sale.EditSaleRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.data.repository.lenses.room.LensNotFound
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.Unexpected
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.data.repository.prescription.error.ReadPrescriptionRepositoryError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddAllClientsError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddFramesError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddLensComparisonError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddPaymentDiscountError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddPaymentFeeError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddPaymentsError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddPositioningError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddPrescriptionError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddProductPickedError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddSaleError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.AddServiceOrderError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.DeleteSaleError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.FindSaleError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.SaleFetcherReadClientError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.SaleFetcherReadLensError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.SaleFetcherReadMeasuringError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.SaleFetcherReadPositioningError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.SaleFetcherReadPrescriptionError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.SaleFetcherReadPurchaseError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.SaleFetcherReadServiceOrderError
import com.peyess.salesapp.features.edit_service_order.fetcher.error.ServiceOrderErrors
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.repository.service_order.error.ServiceOrderRepositoryFetchError
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.workmanager.edit_service_order.fetch_positioning_picture.enqueuePositioningPictureDownloadWorker
import com.peyess.salesapp.workmanager.edit_service_order.fetch_prescription_picture.enqueuePrescriptionPictureDownloadWorker
import javax.inject.Inject

private typealias FindLocalSaleResponse = Either<FindSaleError, Boolean>

private typealias FetchServiceOrderResponse =
        Either<SaleFetcherReadServiceOrderError, ServiceOrderDocument>
private typealias FetchPurchaseResponse =
        Either<SaleFetcherReadPurchaseError, PurchaseDocument>
private typealias FetchPrescriptionResponse =
        Either<SaleFetcherReadPrescriptionError, PrescriptionDocument>
private typealias FetchPositioningResponse =
        Either<SaleFetcherReadPositioningError, PositioningDocument>
private typealias FetchMeasuringResponse =
        Either<SaleFetcherReadMeasuringError, MeasuringDocument>
private typealias FetchClientResponse =
        Either<SaleFetcherReadClientError, ClientDocument>
private typealias FetchLensResponse =
        Either<SaleFetcherReadLensError, StoreLensWithDetailsDocument>

private typealias AddAllClientsResponse = Either<AddAllClientsError, Unit>
private typealias AddFramesResponse = Either<AddFramesError, Unit>
private typealias AddLensComparisonResponse = Either<AddLensComparisonError, Unit>
private typealias AddPaymentsResponse = Either<AddPaymentsError, Long>
private typealias AddPaymentDiscountResponse = Either<AddPaymentDiscountError, Unit>
private typealias AddPaymentFeeResponse = Either<AddPaymentFeeError, Unit>
private typealias AddPositioningResponse = Either<AddPositioningError, Unit>
private typealias AddPrescriptionResponse = Either<AddPrescriptionError, Unit>
private typealias AddProductPickedResponse = Either<AddProductPickedError, Unit>
private typealias AddSaleResponse = Either<AddSaleError, Unit>
private typealias AddServiceOrderResponse = Either<AddServiceOrderError, Unit>
private typealias DeleteSaleResponse = Either<DeleteSaleError, Unit>

typealias ServiceOrderFetchResponse =
        Either<ServiceOrderErrors, Pair<PurchaseDocument, ServiceOrderDocument>>

class ServiceOrderFetcher @Inject constructor(
    private val salesApplication: SalesApplication,

    private val serviceOrderRepository: ServiceOrderRepository,
    private val purchaseRepository: PurchaseRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val positioningRepository: PositioningRepository,
    private val measuringRepository: MeasuringRepository,
    private val clientRepository: ClientRepository,
    private val localLensRepository: LocalLensesRepository,

    private val localClientRepository: LocalClientRepository,
    private val localFramesRepository: EditFramesDataRepository,
    private val localLensComparisonRepository: EditLensComparisonRepository,
    private val localPaymentRepository: EditLocalPaymentRepository,
    private val localPaymentFeeRepository: EditPaymentFeeRepository,
    private val localPaymentDiscountRepository: EditPaymentDiscountRepository,
    private val localPositioningRepository: EditPositioningRepository,
    private val localPrescriptionRepository: EditPrescriptionRepository,
    private val localProductPickedRepository: EditProductPickedRepository,
    private val localServiceOrderRepository: EditServiceOrderRepository,
    private val localSaleRepository: EditSaleRepository,
    private val localClientPickedRepository: EditClientPickedRepository,
) {
    private suspend fun saleExistsLocally(saleId: String): FindLocalSaleResponse {
        return localSaleRepository.saleExits(saleId).mapLeft {
            FindSaleError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    private suspend fun fetchServiceOrder(
        serviceOrderId: String,
    ): FetchServiceOrderResponse = either {
        serviceOrderRepository.serviceOrderById(serviceOrderId).mapLeft {
            when (it) {
                is ServiceOrderRepositoryFetchError.ServiceOrderNotFound ->
                    SaleFetcherReadServiceOrderError.NotFound(
                        description = it.description,
                        throwable = it.error,
                    )
                is ServiceOrderRepositoryFetchError.Unexpected ->
                    SaleFetcherReadServiceOrderError.Unexpected(
                        description = it.description,
                        throwable = it.error,
                    )
            }
        }.bind()
    }

    private suspend fun fetchPurchase(
        purchaseId: String,
    ): FetchPurchaseResponse = Either.catch {
        purchaseRepository.getById(purchaseId)
    }.mapLeft {
        SaleFetcherReadPurchaseError.Unexpected(
            description = it.message ?: "Unexpected error",
            throwable = it,
        )
    }.leftIfNull {
        SaleFetcherReadPurchaseError.NotFound(
            description = "Purchase not found",
        )
    }

    private suspend fun fetchPrescription(
        prescriptionId: String,
    ): FetchPrescriptionResponse = either {
        prescriptionRepository.prescriptionById(prescriptionId).mapLeft {
            when(it) {
                is ReadPrescriptionRepositoryError.NotFound ->
                    SaleFetcherReadPrescriptionError.NotFound(
                        description = it.description,
                        throwable = it.error,
                    )
                is ReadPrescriptionRepositoryError.Unexpected ->
                    SaleFetcherReadPrescriptionError.Unexpected(
                        description = it.description,
                        throwable = it.error,
                    )
            }
        }.bind()
    }

    private suspend fun fetchPositioning(
        positioningId: String,
    ): FetchPositioningResponse = either {
        positioningRepository.positioningById(positioningId).mapLeft {
            SaleFetcherReadPositioningError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }.bind()
    }

    private suspend fun fetchMeasuring(
        measuringId: String,
    ): FetchMeasuringResponse = either {
        measuringRepository.measuringById(measuringId).mapLeft {
            SaleFetcherReadMeasuringError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }.bind()
    }

    private suspend fun fetchClient(
        clientId: String,
    ): FetchClientResponse = either {
        clientRepository.clientById(clientId).mapLeft {
            when (it) {
                is ClientNotFound ->
                    SaleFetcherReadClientError.NotFound(
                        description = it.description,
                        throwable = it.error,
                    )

                is ClientRepositoryUnexpectedError,
                is UploadClientRepositoryError,
                is ExistsClientRepositoryError,
                is UpdateClientRepositoryError ->
                    SaleFetcherReadClientError.Unexpected(
                        description = it.description,
                        throwable = it.error,
                    )
            }
        }.bind()
    }

    private suspend fun fetchLocalLens(
        lensId: String,
    ): FetchLensResponse = either {
        localLensRepository.getLensById(lensId).mapLeft {
            when(it) {
                is LensNotFound ->
                    SaleFetcherReadLensError.NotFound(
                        description = it.description,
                        throwable = it.error,
                    )
                is Unexpected ->
                    SaleFetcherReadLensError.Unexpected(
                        description = it.description,
                        throwable = it.error,
                    )
            }
        }.bind()
    }

    private fun buildLocalFrames(
        serviceOrderDocument: ServiceOrderDocument
    ): LocalFramesDocument {
        val frames = serviceOrderDocument.framesProducts

        return LocalFramesDocument(
            soId = serviceOrderDocument.id,
            areFramesNew = !serviceOrderDocument.hasOwnFrames,
            design = frames.design,
            reference = frames.reference,
            value = frames.price,
            tagCode = frames.code,
            type = frames.type,
            framesInfo = frames.info,
        )
    }

    private fun buildLocalLensComparison(
        serviceOrderDocument: ServiceOrderDocument
    ): LensComparisonDocument {
        val lens = serviceOrderDocument.leftProducts.lenses
        val treatment = serviceOrderDocument.leftProducts.treatments
        val coloring = serviceOrderDocument.leftProducts.colorings

        return LensComparisonDocument(
            soId = serviceOrderDocument.id,
            originalLensId = lens.id,
            originalColoringId = coloring.id,
            originalTreatmentId = treatment.id,
            comparisonLensId = lens.id,
            comparisonColoringId = coloring.id,
            comparisonTreatmentId = treatment.id,
        )
    }

    private fun buildLocalPayment(
        purchaseDocument: PurchaseDocument,
    ): List<LocalPaymentDocument> {
        return purchaseDocument.payments.map {
            LocalPaymentDocument(
                uuid = it.uuid,
                saleId = purchaseDocument.id,
                value = it.amount,
                clientId = it.payerUid,
                methodId = it.methodId,
                methodName = it.methodName.toNameDisplay(),
                methodType = it.methodName.toName(),
                installments = it.installments,
                document = it.document,

                hasLegalId = it.hasLegalId,
                legalId = it.legalId,

                cardFlagName = it.cardFlagName,
                cardFlagIcon = Uri.parse(it.cardFlagIcon),

                dueDate = it.dueDate,
                dueDateMode = it.dueDateMode,
                dueDatePeriod = it.dueDatePeriod,
            )
        }
    }

    private fun buildLocalPaymentDiscount(
        purchaseDocument: PurchaseDocument
    ): OverallDiscountDocument {
        return OverallDiscountDocument(
            saleId = purchaseDocument.id,
            overallDiscountValue = purchaseDocument.overallDiscount.value.toDouble(),
            discountMethod = purchaseDocument.overallDiscount.method,
        )
    }

    private fun buildLocalPaymentFee(
        purchaseDocument: PurchaseDocument,
    ): PaymentFeeDocument {
        return PaymentFeeDocument(
            saleId = purchaseDocument.id,
            method = PaymentFeeCalcMethod.Percentage,
            value = purchaseDocument.totalFee,
        )
    }

    private fun buildLocalPositioning(
        serviceOrderDocument: ServiceOrderDocument,
        positioningDocument: PositioningDocument,
        measuringDocument: MeasuringDocument,
    ): LocalPositioningDocument {
        return LocalPositioningDocument(
            id = positioningDocument.id,
            soId = serviceOrderDocument.id,
            eye = Eye.toEye(positioningDocument.eye),
            picture = Uri.EMPTY,
//            device = measuringDocument.,
            baseLeft = positioningDocument.baseLeft,
            baseLeftRotation = positioningDocument.baseLeftRotation,
            baseRight = positioningDocument.baseRight,
            baseRightRotation = positioningDocument.baseRightRotation,
            baseTop = positioningDocument.baseTop,
            baseBottom = positioningDocument.baseBottom,
            topPointLength = positioningDocument.topPointLength,
            topPointRotation = positioningDocument.topPointRotation,
            bottomPointLength = positioningDocument.bottomPointLength,
            bottomPointRotation = positioningDocument.bottomPointRotation,
            bridgePivot = positioningDocument.bridgePivot,
            checkBottom = positioningDocument.checkBottom,
            checkTop = positioningDocument.checkTop,
            checkLeft = positioningDocument.checkLeft,
            checkLeftRotation = positioningDocument.checkLeftRotation,
            checkMiddle = positioningDocument.checkMiddle,
            checkRight = positioningDocument.checkRight,
            checkRightRotation = positioningDocument.checkRightRotation,
            framesBottom = positioningDocument.framesBottom,
            framesLeft = positioningDocument.framesLeft,
            framesRight = positioningDocument.framesRight,
            framesTop = positioningDocument.framesTop,
            opticCenterRadius = positioningDocument.opticCenterRadius,
            opticCenterX = positioningDocument.opticCenterX,
            opticCenterY = positioningDocument.opticCenterY,
            proportionToPictureHorizontal = positioningDocument.proportionToPictureHorizontal,
            proportionToPictureVertical = positioningDocument.proportionToPictureVertical,
            eulerAngleX = measuringDocument.eulerAngleX,
            eulerAngleY = measuringDocument.eulerAngleY,
            eulerAngleZ = measuringDocument.eulerAngleZ,
        )
    }

    private fun buildLocalPrescription(
        serviceOrderDocument: ServiceOrderDocument,
        prescriptionDocument: PrescriptionDocument,
    ): LocalPrescriptionDocument {
        return LocalPrescriptionDocument(
            id = prescriptionDocument.id,
            soId = serviceOrderDocument.id,
            pictureUri = Uri.EMPTY,
            professionalName = prescriptionDocument.professionalName,
            professionalId = prescriptionDocument.professionalDocument,
            isCopy = prescriptionDocument.isCopy,
            lensTypeCategoryId = prescriptionDocument.lensTypeCategoryId,
            lensTypeCategory = prescriptionDocument.lensTypeCategory,
            prescriptionDate = prescriptionDocument.emitted,
            sphericalLeft = prescriptionDocument.lSpherical,
            sphericalRight = prescriptionDocument.rSpherical,
            cylindricalLeft = prescriptionDocument.lCylinder,
            cylindricalRight = prescriptionDocument.rCylinder,
            axisLeft = prescriptionDocument.lAxisDegree,
            axisRight = prescriptionDocument.rAxisDegree,
            hasAddition = prescriptionDocument.hasAddition,
            additionLeft = prescriptionDocument.lAddition,
            additionRight = prescriptionDocument.rAddition,
            hasPrism = prescriptionDocument.hasPrism,
            prismDegreeLeft = prescriptionDocument.lPrismDegree,
            prismDegreeRight = prescriptionDocument.rPrismDegree,
            prismAxisLeft = prescriptionDocument.lPrismAxis,
            prismAxisRight = prescriptionDocument.rPrismAxis,
            prismPositionLeft = prescriptionDocument.lPrismPos,
            prismPositionRight = prescriptionDocument.rPrismPos,
            observation = prescriptionDocument.observation,
        )
    }

    private fun buildLocalProductPicked(
        serviceOrderDocument: ServiceOrderDocument
    ): ProductPickedDocument {
        val lens = serviceOrderDocument.leftProducts.lenses
        val coloring = serviceOrderDocument.leftProducts.colorings
        val treatment = serviceOrderDocument.leftProducts.treatments

        return ProductPickedDocument(
            soId = serviceOrderDocument.id,
            lensId = lens.id,
            treatmentId = treatment.id,
            coloringId = coloring.id,
        )
    }

    private fun buildLocalServiceOrder(
        serviceOrderDocument: ServiceOrderDocument,
        lens: StoreLensWithDetailsDocument,
    ): LocalServiceOrderDocument {
        return LocalServiceOrderDocument(
            id = serviceOrderDocument.id,
            hasPrescription = true,
            saleId = serviceOrderDocument.purchaseId,
            clientName = serviceOrderDocument.clientName,
        )
    }

    private fun buildLocalSale(purchase: PurchaseDocument): LocalSaleDocument {
        return LocalSaleDocument(
            id = purchase.id,
            collaboratorUid = purchase.createdBy,
            active = false,
            isUploading = false,
        )
    }

    private fun buildClientPicked(
        serviceOrderId: String,
        role: ClientRole,
        localClient: LocalClientDocument,
    ): EditClientPickedDocument {
        return EditClientPickedDocument(
            id = localClient.id,
            soId = serviceOrderId,
            clientRole = role,
            nameDisplay = localClient.nameDisplay,
            name = localClient.name,
            sex = localClient.sex,
            email = localClient.email,
            document = localClient.document,
            shortAddress = localClient.shortAddress,
        )
    }

    private suspend fun addClient(
        client: ClientDocument,
    ): AddAllClientsResponse {
        return localClientRepository
            .insertClient(client.toLocalClientDocument())
            .mapLeft {
                AddAllClientsError.Unexpected(
                    description = "Error adding client to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun addFrames(
        frames: LocalFramesDocument,
    ): AddFramesResponse {
        return localFramesRepository.addFrame(frames).mapLeft {
            AddFramesError.Unexpected(
                description = "Error adding frames to local database",
                throwable = it.error,
            )
        }
    }

    private suspend fun addLensComparison(
        lensComparison: LensComparisonDocument,
    ): AddLensComparisonResponse {
        return localLensComparisonRepository
            .addLensComparison(lensComparison)
            .mapLeft {
                AddLensComparisonError.Unexpected(
                    description = "Error adding lens comparison to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun addPayment(
        payment: LocalPaymentDocument,
    ): AddPaymentsResponse {
        return localPaymentRepository.addPayment(payment).mapLeft {
            AddPaymentsError.Unexpected(
                description = "Error adding payment to local database",
                throwable = it.error,
            )
        }
    }

    private suspend fun addPaymentDiscount(
        discount: OverallDiscountDocument,
    ): AddPaymentDiscountResponse {
        return localPaymentDiscountRepository
            .addPaymentDiscount(discount)
            .mapLeft {
                AddPaymentDiscountError.Unexpected(
                    description = "Error adding payment discount to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun addPaymentFee(
        fee: PaymentFeeDocument,
    ): AddPaymentFeeResponse {
        return localPaymentFeeRepository.addPaymentFee(fee).mapLeft {
            AddPaymentFeeError.Unexpected(
                description = "Error adding payment error to local database",
                throwable = it.error,
            )
        }
    }

    private suspend fun addPositioning(
        positioningDocument: LocalPositioningDocument,
    ): AddPositioningResponse {
        return localPositioningRepository
            .addPositioning(positioningDocument)
            .mapLeft {
                AddPositioningError.Unexpected(
                    description = "Error adding positioning to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun addPrescription(
        prescription: LocalPrescriptionDocument,
    ): AddPrescriptionResponse {
        return localPrescriptionRepository
            .addPrescription(prescription)
            .mapLeft {
                AddPrescriptionError.Unexpected(
                    description = "Error adding prescription to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun addProductPicked(
        productPicked: ProductPickedDocument,
    ): AddProductPickedResponse {
        return localProductPickedRepository
            .addProductPicked(productPicked)
            .mapLeft {
                AddProductPickedError.Unexpected(
                    description = "Error adding product picked to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun addSale(
        sale: LocalSaleDocument,
    ): AddSaleResponse {
        return localSaleRepository.addSale(sale).mapLeft {
            AddSaleError.Unexpected(
                description = "Error adding sale to local database",
                throwable = it.error,
            )
        }
    }

    private suspend fun addServiceOrder(
        serviceOrder: LocalServiceOrderDocument,
    ): AddServiceOrderResponse {
        return localServiceOrderRepository
            .addServiceOrder(serviceOrder)
            .mapLeft {
                AddServiceOrderError.Unexpected(
                    description = "Error adding service order to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun addClientPicked(
        pickedClient: EditClientPickedDocument,
    ): AddServiceOrderResponse {
        return localClientPickedRepository
            .insertClientPicked(pickedClient)
            .mapLeft {
                AddServiceOrderError.Unexpected(
                    description = "Error adding service order to local database",
                    throwable = it.error,
                )
            }
    }

    private suspend fun deletePaymentFeeLocally(saleId: String): DeleteSaleResponse {
        return localPaymentFeeRepository.deletePaymentFeeForSale(saleId).mapLeft {
            DeleteSaleError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    private suspend fun deletePaymentDiscountLocally(saleId: String): DeleteSaleResponse {
        return localPaymentDiscountRepository.deletePaymentDiscountForSale(saleId).mapLeft {
            DeleteSaleError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    private suspend fun deleteProductPickedLocally(serviceOrderId: String): DeleteSaleResponse {
        return localProductPickedRepository
            .deleteProductPickedForServiceOrder(serviceOrderId).mapLeft {
                DeleteSaleError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }
    }

    private suspend fun deletePrescriptionLocally(serviceOrderId: String): DeleteSaleResponse {
        return localPrescriptionRepository
            .deletePrescriptionForServiceOrder(serviceOrderId).mapLeft {
                DeleteSaleError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }
    }

    private suspend fun deletePositioningsLocally(
        serviceOrderId: String,
    ): DeleteSaleResponse {
        return localPositioningRepository
            .deletePositioningsForServiceOrder(serviceOrderId).mapLeft {
                DeleteSaleError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }
    }

    private suspend fun deletePaymentsLocally(
        purchaseId: String,
    ): DeleteSaleResponse {
        return localPaymentRepository
            .deletePaymentsForSale(purchaseId).mapLeft {
                DeleteSaleError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }
    }

    private suspend fun deleteLensComparisonsLocally(
        serviceOrderId: String,
    ): DeleteSaleResponse {
        return localLensComparisonRepository
            .deleteComparisonsForServiceOrder(serviceOrderId).mapLeft {
                DeleteSaleError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }
    }

    private suspend fun deleteFramesLocally(
        serviceOrderId: String,
    ): DeleteSaleResponse {
        return localFramesRepository.deleteFramesForServiceOrder(serviceOrderId).mapLeft {
            DeleteSaleError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    private suspend fun deleteClientsPickedLocally(
        serviceOrderId: String,
    ): DeleteSaleResponse {
        return localClientPickedRepository
            .deleteClientsPickedForServiceOrder(serviceOrderId).mapLeft {
                DeleteSaleError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }
    }

    private suspend fun deleteServiceOrderLocally(
        serviceOrderId: String,
    ): DeleteSaleResponse {
        return localServiceOrderRepository.deleteServiceOrderById(serviceOrderId).mapLeft {
            DeleteSaleError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    private suspend fun deletePurchaseLocally(
        saleId: String,
    ): DeleteSaleResponse {
        return localSaleRepository.deleteSaleById(saleId).mapLeft {
            DeleteSaleError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    private suspend fun deleteSaleLocally(
        saleId: String,
        serviceOrderId: String,
    ): DeleteSaleResponse = either {
        deletePaymentFeeLocally(saleId).bind()
        deletePaymentDiscountLocally(saleId).bind()
        deletePaymentsLocally(saleId).bind()
        deleteProductPickedLocally(serviceOrderId).bind()
        deletePrescriptionLocally(serviceOrderId).bind()
        deletePositioningsLocally(serviceOrderId).bind()
        deleteLensComparisonsLocally(serviceOrderId).bind()
        deleteFramesLocally(serviceOrderId).bind()
        deleteClientsPickedLocally(serviceOrderId).bind()
        deleteServiceOrderLocally(serviceOrderId).bind()
        deletePurchaseLocally(saleId).bind()
    }

    suspend fun fetchFullSale(
        serviceOrderId: String,
        purchaseId: String,
        forceReload: Boolean = true,
    ): ServiceOrderFetchResponse = either {
        val saleExists = saleExistsLocally(purchaseId).bind()

        val serviceOrder = fetchServiceOrder(serviceOrderId).bind()
        val purchase = fetchPurchase(purchaseId).bind()

        if (saleExists && !forceReload) {
            return@either Pair(purchase, serviceOrder)
        } else if (forceReload) {
            deleteSaleLocally(
                saleId = purchaseId,
                serviceOrderId = serviceOrderId,
            ).bind()
        }

        val client = fetchClient(serviceOrder.clientUid).bind()
        val responsible = if (serviceOrder.clientUid == serviceOrder.responsibleUid) {
            null
        } else {
            fetchClient(serviceOrder.responsibleUid).bind()
        }
        val witness = serviceOrder.witnessUid.ifBlank { null }?.let { fetchClient(it).bind() }
        val payers = purchase.payerUids.filter {
            it.isNotBlank() && it !in listOfNotNull(client, responsible, witness).map { c -> c.id }
        }.map { fetchClient(it).bind() }

        val prescription = fetchPrescription(serviceOrder.prescriptionId).bind()
        val leftPositioning = fetchPositioning(serviceOrder.lPositioningId).bind()
        val rightPositioning = fetchPositioning(serviceOrder.rPositioningId).bind()
        val leftMeasuring = fetchMeasuring(serviceOrder.lMeasuringId).bind()
        val rightMeasuring = fetchMeasuring(serviceOrder.rMeasuringId).bind()

        val lens = fetchLocalLens(serviceOrder.leftProducts.lenses.id).bind()

        val localFrames = buildLocalFrames(serviceOrder)
        val localComparison = buildLocalLensComparison(serviceOrder)
        val localPayments = buildLocalPayment(purchase)
        val localPaymentFee = buildLocalPaymentFee(purchase)
        val localPaymentDiscount = buildLocalPaymentDiscount(purchase)

        val localProductPicked = buildLocalProductPicked(serviceOrder)

        enqueuePrescriptionPictureDownloadWorker(
            context = salesApplication as Context,
            prescriptionId = prescription.id,
        )
        enqueuePositioningPictureDownloadWorker(
            context = salesApplication as Context,
            positioningId = leftPositioning.id,
        )
        enqueuePositioningPictureDownloadWorker(
            context = salesApplication as Context,
            positioningId = rightPositioning.id,
        )

        val localPrescription = buildLocalPrescription(serviceOrder, prescription)
        val localLeftPositioning = buildLocalPositioning(serviceOrder, leftPositioning, leftMeasuring)
        val localRightPositioning = buildLocalPositioning(serviceOrder, rightPositioning, rightMeasuring)

        val localServiceOrder = buildLocalServiceOrder(serviceOrder, lens)
        val localSale = buildLocalSale(purchase)
        
        val clientPicked = buildClientPicked(
            serviceOrderId = serviceOrderId, 
            role = ClientRole.User, 
            localClient = client.toLocalClientDocument(), 
        )
        val responsiblePicked = buildClientPicked(
            serviceOrderId = serviceOrderId, 
            role = ClientRole.Responsible, 
            localClient = responsible?.toLocalClientDocument() ?: client.toLocalClientDocument(), 
        )
        val witnessPicked = witness?.let { 
            buildClientPicked(
                serviceOrderId = serviceOrderId, 
                role = ClientRole.Witness, 
                localClient = it.toLocalClientDocument(), 
            )
        }
        
        addClientPicked(clientPicked).bind()
        addClientPicked(responsiblePicked).bind()
        witnessPicked?.let { addClientPicked(it).bind() }

        addSale(localSale).bind()
        addServiceOrder(localServiceOrder).bind()

        (listOfNotNull(client, responsible, witness) + payers).forEach {
            addClient(it).bind()
        }

        addFrames(localFrames).bind()
        addLensComparison(localComparison).bind()

        localPayments.forEach {
            addPayment(it).bind()
        }

        addPaymentFee(localPaymentFee).bind()
        addPaymentDiscount(localPaymentDiscount).bind()
        addProductPicked(localProductPicked).bind()
        addPrescription(localPrescription).bind()
        addPositioning(localLeftPositioning).bind()
        addPositioning(localRightPositioning).bind()

        Pair(purchase, serviceOrder)
    }
}
