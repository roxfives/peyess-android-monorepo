package com.peyess.salesapp.features.edit_service_order.updater

import android.content.Context
import android.net.Uri
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.products.toDescription
import com.peyess.salesapp.data.model.local_client.LocalClientDocument
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.model.measuring.MeasuringUpdateDocument
import com.peyess.salesapp.data.model.positioning.PositioningUpdateDocument
import com.peyess.salesapp.data.model.prescription.PrescriptionUpdateDocument
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseUpdateDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.purchase.fee.FeeDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderUpdateDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.EditClientPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.client_picked.error.ReadClientPickedError
import com.peyess.salesapp.data.repository.edit_service_order.frames.EditFramesDataRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment.EditLocalPaymentRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_discount.EditPaymentDiscountRepository
import com.peyess.salesapp.data.repository.edit_service_order.payment_fee.EditPaymentFeeRepository
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepository
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepository
import com.peyess.salesapp.data.repository.edit_service_order.service_order.EditServiceOrderRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.management_picture_upload.PictureUploadRepository
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.features.edit_service_order.updater.adapter.toMeasuringUpdateDocument
import com.peyess.salesapp.features.edit_service_order.updater.adapter.toPictureUploadDocument
import com.peyess.salesapp.features.edit_service_order.updater.adapter.toPositioningUpdateDocument
import com.peyess.salesapp.features.edit_service_order.updater.adapter.toPrescriptionUpdateDocument
import com.peyess.salesapp.features.edit_service_order.updater.adapter.toPreview
import com.peyess.salesapp.features.edit_service_order.updater.adapter.toPurchase
import com.peyess.salesapp.features.edit_service_order.updater.adapter.toServiceOrder
import com.peyess.salesapp.features.edit_service_order.updater.error.AddPictureToUploadError
import com.peyess.salesapp.features.edit_service_order.updater.error.GenerateMeasuringDataError
import com.peyess.salesapp.features.edit_service_order.updater.error.GeneratePositioningDataError
import com.peyess.salesapp.features.edit_service_order.updater.error.GeneratePrescriptionDataError
import com.peyess.salesapp.features.edit_service_order.updater.error.GenerateSaleDataError
import com.peyess.salesapp.features.edit_service_order.updater.error.UpdateSaleError
import com.peyess.salesapp.features.pdf.service_order.buildHtml
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toMeasuring
import com.peyess.salesapp.screen.sale.service_order.adapter.toPaymentDocument
import com.peyess.salesapp.screen.sale.service_order.adapter.toDescription
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.typing.sale.PurchaseReasonSyncFailure
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.PurchaseSyncState
import com.peyess.salesapp.utils.file.isLocalFile
import com.peyess.salesapp.utils.string.removeDiacritics
import com.peyess.salesapp.workmanager.picture_upload.enqueuePictureUploadManagerWorker
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.ZonedDateTime
import javax.inject.Inject
import kotlin.math.max

private typealias FetchClientDataResponse = Either<GenerateSaleDataError, LocalClientDocument>

private typealias PartialSaleDataUpdateResponse =
        Either<GenerateSaleDataError, ServiceOrderUpdateDocument>

private typealias UpdatePurchaseResponse = Either<GenerateSaleDataError, PurchaseUpdateDocument>

private typealias CalculateValueResponse = Either<GenerateSaleDataError, BigDecimal>

private typealias AddPictureToUploadResponse = Either<AddPictureToUploadError, Long>

typealias UpdateSaleResponse = Either<UpdateSaleError, Unit>

typealias GeneratePrescriptionDataResponse =
        Either<GeneratePrescriptionDataError, Pair<Uri, PrescriptionUpdateDocument>>

typealias GeneratePositioningDataResponse =
        Either<GeneratePositioningDataError, Pair<Pair<Uri, PositioningUpdateDocument>, Pair<Uri, PositioningUpdateDocument>>>

typealias GenerateMeasuringDataResponse =
        Either<GenerateMeasuringDataError, Pair<MeasuringUpdateDocument, MeasuringUpdateDocument>>

typealias SaleDataResponse =
        Either<GenerateSaleDataError, Pair<PurchaseUpdateDocument, ServiceOrderUpdateDocument>>


class ServiceOrderUpdater @Inject constructor(
    private val salesApplication: SalesApplication,
    private val authenticationRepository: AuthenticationRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val positioningRepository: PositioningRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val measuringRepository: MeasuringRepository,
    private val localClientRepository: LocalClientRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val editFramesRepository: EditFramesDataRepository,
    private val editPrescriptionRepository: EditPrescriptionRepository,
    private val editPositioningRepository: EditPositioningRepository,
    private val editClientPickedRepository: EditClientPickedRepository,
    private val editProductPickedRepository: EditProductPickedRepository,
    private val editPaymentRepository: EditLocalPaymentRepository,
    private val discountRepository: EditPaymentDiscountRepository,
    private val paymentFeeRepository: EditPaymentFeeRepository,
    private val editServiceOrderRepository: EditServiceOrderRepository,
    private val pictureUploadRepository: PictureUploadRepository,
) {
    private suspend fun addClientData(
        serviceOrderId: String,
        update: ServiceOrderUpdateDocument,
    ): PartialSaleDataUpdateResponse = either {
        val client = findClientForRole(serviceOrderId, ClientRole.User).bind()
        val responsible = findClientForRole(serviceOrderId, ClientRole.Responsible).bind()
        val witnessResponse = findClientForRole(serviceOrderId, ClientRole.Witness)
        val witness = witnessResponse.fold(
            ifLeft = {
                if (it is GenerateSaleDataError.ClientNotFound) {
                    Either.Right(null)
                } else {
                    it.left()
                }
            },

            ifRight = { it.right() },
        ).bind()

        update.copy(
            clientUid = client.id,
            clientDocument = client.document,
            clientName = client.name,
            clientBirthday = client.birthday,
            clientPhone = client.phone,
            clientCellphone = client.cellphone,
            clientNeighborhood = client.neighborhood,
            clientStreet = client.street,
            clientCity = client.city,
            clientState = client.state,
            clientHouseNumber = client.houseNumber,
            clientZipcode = client.zipCode,

            responsibleUid = responsible.id,
            responsibleDocument = responsible.document,
            responsibleName = responsible.name,
            responsibleBirthday = responsible.birthday,
            responsiblePhone = responsible.phone,
            responsibleCellphone = responsible.cellphone,
            responsibleNeighborhood = responsible.neighborhood,
            responsibleStreet = responsible.street,
            responsibleCity = responsible.city,
            responsibleState = responsible.state,
            responsibleHouseNumber = responsible.houseNumber,
            responsibleZipcode = responsible.zipCode,

            hasWitness = witness != null,
            witnessUid = witness?.id ?: "",
            witnessDocument = witness?.document ?: "",
            witnessName = witness?.name ?: "",
            witnessBirthday = witness?.birthday ?: ZonedDateTime.now(),
            witnessPhone = witness?.phone ?: "",
            witnessCellphone = witness?.cellphone ?: "",
            witnessNeighborhood = witness?.neighborhood ?: "",
            witnessStreet = witness?.street ?: "",
            witnessCity = witness?.city ?: "",
            witnessState = witness?.state ?: "",
            witnessHouseNumber = witness?.houseNumber ?: "",
            witnessZipcode = witness?.zipCode ?: "",
        )
    }

    private suspend fun findClientForRole(
        serviceOrderId: String,
        role: ClientRole,
    ): FetchClientDataResponse {
        return editClientPickedRepository.findClientPickedForServiceOrder(serviceOrderId, role)
            .mapLeft { error ->
                when (error) {
                    is ReadClientPickedError.ClientPickedNotFound -> GenerateSaleDataError.ClientNotFound(
                        description = error.description,
                        throwable = error.error,
                    )
                    is ReadClientPickedError.Unexpected -> GenerateSaleDataError.Unexpected(
                        description = error.description,
                        throwable = error.error,
                    )
                }
            }.flatMap {
                localClientRepository.clientById(it.id).mapLeft { error ->
                    GenerateSaleDataError.Unexpected(
                        description = error.description,
                        throwable = error.error,
                    )
                }
            }
    }

    private suspend fun addPrescriptionData(
        serviceOrderId: String,
        update: ServiceOrderUpdateDocument,
    ): PartialSaleDataUpdateResponse = either {
        val prescription = editPrescriptionRepository
            .prescriptionByServiceOrder(serviceOrderId)
            .mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()

        update.copy(
            prescriptionId = prescription.id,
            isCopy = prescription.isCopy,
            professionalName = prescription.professionalName,
            professionalId = prescription.professionalId,
            prescriptionDate = prescription.prescriptionDate,

            hasAddition = prescription.hasAddition,
            hasPrism = prescription.hasPrism,
            lCylinder = prescription.cylindricalLeft,
            lSpheric = prescription.sphericalLeft,
            lAxisDegree = prescription.axisLeft,
            lAddition = prescription.additionLeft,
            lPrismAxis = prescription.prismAxisLeft,
            lPrismDegree = prescription.prismDegreeLeft,
            lPrismPos = prescription.prismPositionLeft.toName(),
            rCylinder = prescription.cylindricalRight,
            rSpheric = prescription.sphericalRight,
            rAxisDegree = prescription.axisRight,
            rAddition = prescription.additionRight,
            rPrismAxis = prescription.prismAxisRight,
            rPrismDegree = prescription.prismDegreeRight,
            rPrismPos = prescription.prismPositionRight.toName(),
        )
    }

    private suspend fun addPositioningData(
        serviceOrderId: String,
        update: ServiceOrderUpdateDocument,
    ): PartialSaleDataUpdateResponse = either {
        val positionings =
            editPositioningRepository.bothPositioningForServiceOrder(serviceOrderId).mapLeft {
                    GenerateSaleDataError.Unexpected(
                        description = it.description,
                        throwable = it.error,
                    )
                }.bind()

        val measurings = Pair(
            positionings.left.toMeasuring(),
            positionings.right.toMeasuring(),
        )

        update.copy(
            lIpd = measurings.first.fixedIpd,
            lBridge = measurings.first.fixedBridge,
            lBridgeHoop = measurings.first.fixedHorizontalBridgeHoop,
            lDiameter = measurings.first.fixedDiameter,
            lHe = measurings.first.fixedHe,
            lHorizontalBridgeHoop = measurings.first.fixedHorizontalBridgeHoop,
            lHorizontalHoop = measurings.first.fixedHHoop,
            lVerticalHoop = measurings.first.fixedVHoop,

            rIpd = measurings.second.fixedIpd,
            rBridge = measurings.second.fixedBridge,
            rBridgeHoop = measurings.second.fixedHorizontalBridgeHoop,
            rDiameter = measurings.second.fixedDiameter,
            rHe = measurings.second.fixedHe,
            rHorizontalBridgeHoop = measurings.second.fixedHorizontalBridgeHoop,
            rHorizontalHoop = measurings.second.fixedHHoop,
            rVerticalHoop = measurings.second.fixedVHoop,
        )
    }

    private suspend fun addProductsData(
        serviceOrderId: String,
        update: ServiceOrderUpdateDocument,
    ): PartialSaleDataUpdateResponse = either {
        val productPicked = editProductPickedRepository
            .productPickedForServiceOrder(serviceOrderId)
            .mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        val lens = localLensesRepository.getLensById(productPicked.lensId).mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        var coloring = localLensesRepository.getColoringById(
            productPicked.lensId,
            productPicked.coloringId,
        ).mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        var treatment = localLensesRepository.getTreatmentById(
            productPicked.lensId,
            productPicked.treatmentId,
        ).mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        val frames = editFramesRepository.findFramesForServiceOrder(serviceOrderId).mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

//        val lensAccessories = mutableListOf<AccessoryItemDocument>()

        val framesValue = if (frames.areFramesNew) {
            frames.value
        } else {
            BigDecimal.ZERO
        }

        var total = lens.price + framesValue

        if (!lens.isColoringIncluded && !lens.isColoringDiscounted) {
            total += coloring.price

            // TODO: refactor to remove identification by name
            if (
                lens.priceAddColoring > BigDecimal.ZERO
                && coloring.name.trim().lowercase().removeDiacritics() != "incolor"
                && coloring.name.trim().lowercase().removeDiacritics() != "indisponivel"
            ) {
                total += lens.priceAddColoring
                coloring = coloring.copy(price = coloring.price + lens.priceAddColoring)

//                lensAccessories.add(
//                    AccessoryItemDocument(
//                        nameDisplay = "Adicional por coloração",
//                        price = BigDecimal(lens.priceAddColoring).divide(
//                            BigDecimal(2), 2, RoundingMode.HALF_EVEN
//                        ).toDouble()
//                    )
//                )
            }
        }

        // TODO: refactor to remove identification by name
        if (!lens.isTreatmentIncluded && !lens.isTreatmentDiscounted) {
            total += treatment.price

            if (
                lens.priceAddTreatment > BigDecimal.ZERO
                && treatment.name.trim().lowercase().removeDiacritics() != "incolor"
                && treatment.name.trim().lowercase().removeDiacritics() != "indisponivel"
            ) {
                total += lens.priceAddTreatment
                treatment = treatment.copy(price = treatment.price + lens.priceAddTreatment)

//                lensAccessories.add(
//                    AccessoryItemDocument(
//                        nameDisplay = "Adicional por tratamento",
//                        price = BigDecimal(lens.priceAddTreatment).divide(
//                            BigDecimal(2), 2, RoundingMode.HALF_EVEN
//                        ).toDouble(),
//                    )
//                )
            }
        }

        val withHeight = max(update.lHe, update.rHe)
        update.copy(
            samePurchaseSo = listOf(serviceOrderId),

            fullPrice = total,
            hasOwnFrames = !frames.areFramesNew,

            leftProducts = ProductSoldEyeSetDocument(
                lenses = lens.toDescription(
                    withTreatment = treatment.name.trim().lowercase().removeDiacritics() != "incolor"
                            && treatment.name.trim().lowercase().removeDiacritics() != "indisponivel",
                    withColoring = coloring.name.trim().lowercase().removeDiacritics() != "incolor"
                            && coloring.name.trim().lowercase().removeDiacritics() != "indisponivel",
                    accessoriesPerUnit = emptyList(),
                    withHeight = withHeight,
                ),
                colorings = coloring.toDescription(
                    isDiscounted = lens.isColoringDiscounted,
                    isIncluded = lens.isColoringIncluded,
                ),
                treatments = treatment.toDescription(
                    isDiscounted = lens.isTreatmentDiscounted,
                    isIncluded = lens.isTreatmentIncluded,
                ),
            ),

            rightProducts = ProductSoldEyeSetDocument(
                lenses = lens.toDescription(
                    withTreatment = treatment.name.trim().lowercase().removeDiacritics() != "incolor"
                            && treatment.name.trim().lowercase().removeDiacritics() != "indisponivel",
                    withColoring = coloring.name.trim().lowercase().removeDiacritics() != "incolor"
                            && coloring.name.trim().lowercase().removeDiacritics() != "indisponivel",
                    accessoriesPerUnit = emptyList(),
                    withHeight = withHeight,
                ),
                colorings = coloring.toDescription(
                    isDiscounted = lens.isColoringDiscounted,
                    isIncluded = lens.isColoringIncluded,
                ),
                treatments = treatment.toDescription(
                    isDiscounted = lens.isTreatmentDiscounted,
                    isIncluded = lens.isTreatmentIncluded,
                ),
            ),

            framesProducts = frames.toDescription(),
            miscProducts = emptyList(),
        )
    }

    private suspend fun calculateDiscountAsWhole(
        saleId: String,
        serviceOrder: ServiceOrderUpdateDocument,
    ): CalculateValueResponse = either {
        val discount = discountRepository.discountForSale(saleId).mapLeft {
            GenerateSaleDataError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()

        when (discount.discountMethod) {
            DiscountCalcMethod.Percentage -> discount.overallDiscountValue * serviceOrder.fullPrice
            DiscountCalcMethod.Whole -> discount.overallDiscountValue
            DiscountCalcMethod.None -> BigDecimal.ZERO
        }
    }

    private suspend fun calculateFeeAsWhole(
        saleId: String,
        discountAsWhole: BigDecimal,
        serviceOrder: ServiceOrderUpdateDocument,
    ): CalculateValueResponse = either {
        val priceWithDiscount = serviceOrder.fullPrice - discountAsWhole
        val fee = paymentFeeRepository.paymentFeeForSale(saleId).mapLeft {
            GenerateSaleDataError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()

        when (fee.method) {
            PaymentFeeCalcMethod.Percentage -> fee.value / priceWithDiscount
            PaymentFeeCalcMethod.Whole -> fee.value
            PaymentFeeCalcMethod.None -> BigDecimal.ZERO
        }
    }

    private suspend fun createPurchase(
        serviceOrderId: String,
        saleId: String,
        discountAsWhole: BigDecimal,
        feeAsWhole: BigDecimal,
        serviceOrder: ServiceOrderUpdateDocument,
    ): UpdatePurchaseResponse = either {
        val discountDocument = discountRepository.discountForSale(saleId).mapLeft {
            GenerateSaleDataError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()
        val feeDocument = paymentFeeRepository.paymentFeeForSale(saleId).mapLeft {
            GenerateSaleDataError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()

        val fullPrice = serviceOrder.fullPrice
        val finalPrice = fullPrice - discountAsWhole + feeAsWhole

        val payments = editPaymentRepository.paymentsForSale(saleId).mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = "Payments for sale $saleId not found",
                    throwable = it.error,
                )
            }.bind()

        val totalPaid = if (payments.isEmpty()) {
            BigDecimal.ZERO
        } else {
            payments.map { it.value }
                .ifEmpty { listOf(BigDecimal.ZERO) }
                .reduce(BigDecimal::add)
                .setScale(2, RoundingMode.HALF_EVEN)
        }
        val totalLeft = (finalPrice - totalPaid).abs()
            .setScale(2, RoundingMode.HALF_EVEN)

        val store = authenticationRepository.loadCurrentStore().mapLeft {
            GenerateSaleDataError.Unexpected(
                description = "Store not found",
                throwable = it.error,
            )
        }.bind()

        val productPicked =
            editProductPickedRepository.productPickedForServiceOrder(serviceOrderId).mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        val lens = localLensesRepository.getLensById(productPicked.lensId).mapLeft {
            GenerateSaleDataError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()

        val daysToTakeFromStore = if (lens.needsCheck) {
            store.daysToTakeFromStore + store.additionalCheckDays
        } else {
            store.daysToTakeFromStore
        }

        PurchaseUpdateDocument(
            clientUids = listOf(serviceOrder.clientUid),
            clients = mapOf(
                serviceOrderId to DenormalizedClientDocument(
                    uid = serviceOrder.clientUid,
                    document = serviceOrder.clientDocument,
                    name = serviceOrder.clientName,
                )
            ),

            responsibleDocument = serviceOrder.responsibleDocument,
            responsibleName = serviceOrder.responsibleName,
            responsibleUid = serviceOrder.responsibleUid,
            responsibleBirthday = serviceOrder.responsibleBirthday,
            responsiblePhone = serviceOrder.responsiblePhone,
            responsibleCellphone = serviceOrder.responsibleCellphone,
            responsibleNeighborhood = serviceOrder.responsibleNeighborhood,
            responsibleStreet = serviceOrder.responsibleStreet,
            responsibleCity = serviceOrder.responsibleCity,
            responsibleState = serviceOrder.responsibleState,
            responsibleHouseNumber = serviceOrder.responsibleHouseNumber,
            responsibleZipcode = serviceOrder.responsibleZipcode,

            hasWitness = serviceOrder.hasWitness,
            witnessDocument = serviceOrder.witnessDocument,
            witnessName = serviceOrder.witnessName,
            witnessUid = serviceOrder.witnessUid,
            witnessBirthday = serviceOrder.witnessBirthday,
            witnessPhone = serviceOrder.witnessPhone,
            witnessCellphone = serviceOrder.witnessCellphone,
            witnessNeighborhood = serviceOrder.witnessNeighborhood,
            witnessStreet = serviceOrder.witnessStreet,
            witnessCity = serviceOrder.witnessCity,
            witnessState = serviceOrder.witnessState,
            witnessHouseNumber = serviceOrder.witnessHouseNumber,
            witnessZipcode = serviceOrder.witnessZipcode,

            isDiscountOverall = true,
            overallDiscount = DiscountDescriptionDocument(
                method = discountDocument.discountMethod,
                value = discountDocument.overallDiscountValue,
            ),
            paymentFee = FeeDescriptionDocument(
                method = feeDocument.method,
                value = feeDocument.value,
            ),

            fullPrice = fullPrice,
            finalPrice = finalPrice,

            leftToPay = totalLeft,
            totalPaid = totalPaid,

            totalDiscount = discountAsWhole,
            totalFee = feeAsWhole,

            payerUids = payments.map { it.clientId }.distinct(),
            payerDocuments = payments.map { it.clientDocument }.distinct(),
            payments = payments.map { it.toPaymentDocument() },

            soPreviews = mapOf(
                serviceOrderId to serviceOrder.toPreview()
            ),

            state = PurchaseState.PendingConfirmation,

            syncState = PurchaseSyncState.NotSynced,
            reasonSyncFailed = PurchaseReasonSyncFailure.None,

            finishedAt = serviceOrder.updated,
            daysToTakeFromStore = daysToTakeFromStore,
            hasProductWithPendingCheck = lens.needsCheck,

            updated = serviceOrder.updated,
            updatedBy = serviceOrder.updatedBy,
            updateAllowedBy = serviceOrder.updateAllowedBy,
        )
    }

    suspend fun generateSaleData(
        context: Context,
        serviceOrderId: String,
    ): SaleDataResponse = either {
        val existingServiceOrder = serviceOrderRepository.serviceOrderById(serviceOrderId).mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        val existingPurchase = purchaseRepository.getById(existingServiceOrder.purchaseId)

        val collaboratorName = authenticationRepository.fetchCurrentUserName()
        val collaboratorUid = authenticationRepository.fetchCurrentUserId()
        val now = ZonedDateTime.now()

        val localServiceOrder =
            editServiceOrderRepository.serviceOrderById(serviceOrderId).mapLeft {
                    GenerateSaleDataError.FetchLocalServiceOrderError(
                        description = it.description,
                        throwable = it.error,
                    )
                }.bind()

        var serviceOrderUpdate = ServiceOrderUpdateDocument(
            measureConfirmedBy = collaboratorUid,
            discountAllowedBy = collaboratorUid,

            updated = now,
            updatedBy = collaboratorUid,
            updateAllowedBy = collaboratorUid,
        )

        val prescription = editPrescriptionRepository
            .prescriptionByServiceOrder(serviceOrderId)
            .mapLeft {
                GenerateSaleDataError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()

        serviceOrderUpdate = addClientData(serviceOrderId, serviceOrderUpdate).bind()
        serviceOrderUpdate = addPrescriptionData(serviceOrderId, serviceOrderUpdate).bind()
        serviceOrderUpdate = addPositioningData(serviceOrderId, serviceOrderUpdate).bind()
        serviceOrderUpdate = addProductsData(serviceOrderId, serviceOrderUpdate).bind()

        val discount = calculateDiscountAsWhole(localServiceOrder.saleId, serviceOrderUpdate).bind()
        val fee = calculateFeeAsWhole(localServiceOrder.saleId, discount, serviceOrderUpdate).bind()

        var purchaseUpdate = createPurchase(
            saleId = localServiceOrder.saleId,
            serviceOrderId = serviceOrderId,
            discountAsWhole = discount,
            feeAsWhole = fee,
            serviceOrder = serviceOrderUpdate,
        ).mapLeft {
            GenerateSaleDataError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()

        serviceOrderUpdate = serviceOrderUpdate.copy(
            payerUids = purchaseUpdate.payerUids,
            payerDocuments = purchaseUpdate.payerDocuments,
        )

        val legalText = buildHtml(
            context = context,
            collaboratorName = collaboratorName,
            serviceOrder = serviceOrderUpdate.toServiceOrder(
                hid = existingServiceOrder.hid,
                created = existingServiceOrder.created,
            ),
            purchase = purchaseUpdate.toPurchase(
                hid = existingPurchase?.hid ?: "",
                soIds = existingPurchase?.soIds ?: listOf(existingServiceOrder.id),
            ),
        )
        purchaseUpdate = purchaseUpdate.copy(legalText = legalText)

        Pair(purchaseUpdate, serviceOrderUpdate)
    }

    private suspend fun generatePrescriptionData(
        serviceOrderId: String,
        serviceOrder: ServiceOrderDocument,
    ): GeneratePrescriptionDataResponse = either {
        val collaboratorUid = authenticationRepository.fetchCurrentUserId()

        val localPrescription = editPrescriptionRepository
            .prescriptionByServiceOrder(serviceOrderId).mapLeft {
                GeneratePrescriptionDataError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()

        val user = editClientPickedRepository.findClientPickedForServiceOrder(
                serviceOrderId,
                ClientRole.User
            ).mapLeft { err ->
                GeneratePrescriptionDataError.Unexpected(
                    description = err.description,
                    throwable = err.error,
                )
            }.flatMap {
                localClientRepository.clientById(it.id).mapLeft { err ->
                    GeneratePrescriptionDataError.Unexpected(
                        description = err.description,
                        throwable = err.error,
                    )
                }
            }.bind()

        Pair(
            localPrescription.pictureUri,
            localPrescription.toPrescriptionUpdateDocument(
                client = user,
                collaboratorUid = collaboratorUid,
                serviceOrder = serviceOrder,
                updated = ZonedDateTime.now(),
            ),
        )
    }

    private suspend fun generatePositioningData(
        serviceOrderId: String,
    ): GeneratePositioningDataResponse = either {
        val collaboratorUid = authenticationRepository.fetchCurrentUserId()

        val user = editClientPickedRepository.findClientPickedForServiceOrder(
            serviceOrderId,
            ClientRole.User
        ).mapLeft { err ->
            GeneratePositioningDataError.Unexpected(
                description = err.description,
                throwable = err.error,
            )
        }.flatMap {
            localClientRepository.clientById(it.id).mapLeft { err ->
                GeneratePositioningDataError.Unexpected(
                    description = err.description,
                    throwable = err.error,
                )
            }
        }.bind()

        val (localLeft, localRight) = editPositioningRepository
            .bothPositioningForServiceOrder(serviceOrderId)
            .mapLeft {
                GeneratePositioningDataError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()

        val now = ZonedDateTime.now()
        Pair(
            Pair(
                localLeft.picture,
                localLeft.toPositioningUpdateDocument(
                    collaboratorUid = collaboratorUid,
                    patientUid = user.id,
                    patientDocument = user.document,
                    patientName = user.name,
                    updated = now,
                ),
            ),
            Pair(
                localRight.picture,
                localRight.toPositioningUpdateDocument(
                    collaboratorUid = collaboratorUid,
                    patientUid = user.id,
                    patientDocument = user.document,
                    patientName = user.name,
                    updated = now,
                )
            ),
        )
    }

    private suspend fun generateMeasuringData(
        serviceOrderId: String,
    ): GenerateMeasuringDataResponse = either {
        val collaboratorUid = authenticationRepository.fetchCurrentUserId()

        val user = editClientPickedRepository.findClientPickedForServiceOrder(
            serviceOrderId,
            ClientRole.User
        ).mapLeft { err ->
            GenerateMeasuringDataError.Unexpected(
                description = err.description,
                throwable = err.error,
            )
        }.flatMap {
            localClientRepository.clientById(it.id).mapLeft { err ->
                GenerateMeasuringDataError.Unexpected(
                    description = err.description,
                    throwable = err.error,
                )
            }
        }.bind()

        val (localLeft, localRight) = editPositioningRepository
            .bothPositioningForServiceOrder(serviceOrderId)
            .mapLeft {
                GenerateMeasuringDataError.Unexpected(
                    description = it.description,
                    throwable = it.error,
                )
            }.bind()

        val now = ZonedDateTime.now()

        val measuringLeft = localLeft
            .toMeasuring()
            .toMeasuringUpdateDocument(
                client = user,
                collaboratorUid = collaboratorUid,
                updated = now,
            )
        val measuringRight = localRight
            .toMeasuring()
            .toMeasuringUpdateDocument(
                client = user,
                collaboratorUid = collaboratorUid,
                updated = now,
            )

        Pair(measuringLeft, measuringRight)
    }

    private suspend fun updatePurchase(
        purchaseId: String,
        purchaseUpdate: PurchaseUpdateDocument,
    ): UpdateSaleResponse = either {
        purchaseRepository.updatePurchase(purchaseId, purchaseUpdate).mapLeft {
            UpdateSaleError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()
    }

    private suspend fun updateServiceOrder(
        serviceOrderId: String,
        serviceOrderUpdate: ServiceOrderUpdateDocument,
    ): UpdateSaleResponse = either {
        serviceOrderRepository.update(serviceOrderId, serviceOrderUpdate).mapLeft {
            UpdateSaleError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()
    }

    private suspend fun updatePrescription(
        prescriptionId: String,
        prescriptionUpdate: PrescriptionUpdateDocument,
    ): UpdateSaleResponse = either {
        prescriptionRepository.updatePrescription(prescriptionId, prescriptionUpdate).mapLeft {
            UpdateSaleError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()
    }

    private suspend fun updatePositioning(
        positioningId: String,
        positioningUpdate: PositioningUpdateDocument,
    ): UpdateSaleResponse = either {
        positioningRepository.updatePositioning(positioningId, positioningUpdate).mapLeft {
            UpdateSaleError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()
    }

    private suspend fun updateMeasuring(
        measuringId: String,
        measuringUpdate: MeasuringUpdateDocument,
    ): UpdateSaleResponse = either {
        measuringRepository.updateMeasuring(measuringId, measuringUpdate).mapLeft {
            UpdateSaleError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()
    }

    private suspend fun addPictureToUpload(
        pictureUploadDocument: PictureUploadDocument,
    ): AddPictureToUploadResponse {
        return pictureUploadRepository.addPicture(pictureUploadDocument).mapLeft {
            AddPictureToUploadError.Unexpected(
                description = it.description,
                throwable = it.error,
            )
        }
    }

    suspend fun updateServiceOrder(
        context: Context,
        serviceOrderId: String,
    ): UpdateSaleResponse = either {
        val currentServiceOrder = serviceOrderRepository
            .serviceOrderById(serviceOrderId)
            .mapLeft {
                UpdateSaleError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        val (purchaseUpdate, serviceOrderUpdate) = generateSaleData(
            context = context,
            serviceOrderId = serviceOrderId,
        ).mapLeft {
            UpdateSaleError.Unexpected(
                description = it.description,
                it.error,
            )
        }.bind()

        val (prescriptionPicture, prescriptionUpdate) = generatePrescriptionData(serviceOrderId, currentServiceOrder)
            .mapLeft {
                UpdateSaleError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        val (positioningLeftResponse, positioningRightResponse) = generatePositioningData(serviceOrderId)
            .mapLeft {
                UpdateSaleError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()
        val (positioningPictureLeft, positioningLeftUpdate) = positioningLeftResponse
        val (positioningPictureRight, positioningRightUpdate) = positioningRightResponse

        val (measuringLeftUpdate, measuringRightUpdate) = generateMeasuringData(serviceOrderId)
            .mapLeft {
                UpdateSaleError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

        updatePurchase(
            purchaseId = currentServiceOrder.purchaseId,
            purchaseUpdate = purchaseUpdate,
        ).bind()

        updateServiceOrder(
            serviceOrderId = serviceOrderId,
            serviceOrderUpdate = serviceOrderUpdate,
        ).bind()

        updatePrescription(
            prescriptionId = currentServiceOrder.prescriptionId,
            prescriptionUpdate = prescriptionUpdate,
        ).bind()

        updatePositioning(
            positioningId = currentServiceOrder.lPositioningId,
            positioningUpdate = positioningLeftUpdate,
        ).bind()
        updatePositioning(
            positioningId = currentServiceOrder.rPositioningId,
            positioningUpdate = positioningRightUpdate,
        ).bind()

        updateMeasuring(
            measuringId = currentServiceOrder.lMeasuringId,
            measuringUpdate = measuringLeftUpdate,
        ).bind()
        updateMeasuring(
            measuringId = currentServiceOrder.rMeasuringId,
            measuringUpdate = measuringRightUpdate,
        ).bind()

        if (prescriptionPicture.isLocalFile()) {
            val prescriptionPictureEntryId = addPictureToUpload(
                pictureUploadDocument = prescriptionUpdate.toPictureUploadDocument(
                    storeId = currentServiceOrder.storeId,
                    prescriptionId = currentServiceOrder.prescriptionId,
                    pictureUri = prescriptionPicture,
                    salesApplication = salesApplication,
                )
            ).mapLeft {
                UpdateSaleError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

            enqueuePictureUploadManagerWorker(
                context = salesApplication as Context,
                uploadEntryId = prescriptionPictureEntryId,
            )
        }

        if (positioningPictureLeft.isLocalFile()) {
            val positioningPictureEntryId = addPictureToUpload(
                pictureUploadDocument = positioningLeftUpdate.toPictureUploadDocument(
                    storeId = currentServiceOrder.storeId,
                    positioningId = currentServiceOrder.lPositioningId,
                    pictureUri = positioningPictureLeft,
                    salesApplication = salesApplication,
                )
            ).mapLeft {
                UpdateSaleError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

            enqueuePictureUploadManagerWorker(
                context = salesApplication as Context,
                uploadEntryId = positioningPictureEntryId,
            )
        }

        if (positioningPictureRight.isLocalFile()) {
            val positioningPictureEntryId = addPictureToUpload(
                pictureUploadDocument = positioningRightUpdate.toPictureUploadDocument(
                    storeId = currentServiceOrder.storeId,
                    positioningId = currentServiceOrder.rPositioningId,
                    pictureUri = positioningPictureRight,
                    salesApplication = salesApplication,
                )
            ).mapLeft {
                UpdateSaleError.Unexpected(
                    description = it.description,
                    it.error,
                )
            }.bind()

            enqueuePictureUploadManagerWorker(
                context = salesApplication as Context,
                uploadEntryId = positioningPictureEntryId,
            )
        }
    }
}
