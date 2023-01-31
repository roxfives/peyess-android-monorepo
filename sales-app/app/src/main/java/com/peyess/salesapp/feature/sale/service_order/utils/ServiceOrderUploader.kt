package com.peyess.salesapp.feature.sale.service_order.utils

import android.content.Context
import android.net.Uri
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import arrow.core.flatMap
import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.typing.prescription.PrismPosition
import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.data.adapter.measuring.toMeasuringDocument
import com.peyess.salesapp.data.adapter.positioning.toPositioningDocument
import com.peyess.salesapp.data.adapter.prescription.prescriptionFrom
import com.peyess.salesapp.data.adapter.products.toDescription
import com.peyess.salesapp.feature.sale.service_order.utils.adapter.toDescription
import com.peyess.salesapp.data.adapter.service_order.toPreview
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.sale.purchase.discount.description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductSoldEyeSetDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.repository.client.ClientRepository
import com.peyess.salesapp.data.repository.client.error.ClientNotFound
import com.peyess.salesapp.data.repository.discount.OverallDiscountRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.local_client.LocalClientReadSingleResponse
import com.peyess.salesapp.data.repository.local_client.LocalClientRepository
import com.peyess.salesapp.data.repository.local_sale.client_picked.ClientPickedRepository
import com.peyess.salesapp.data.repository.local_sale.frames.LocalFramesRepository
import com.peyess.salesapp.data.repository.local_sale.payment.SalePaymentRepository
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.payment_fee.PaymentFeeRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.data.room.database.ActiveSalesDatabase
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.lens_pick.model.toMeasuring
import com.peyess.salesapp.feature.sale.service_order.utils.adapter.toPaymentDocument
import com.peyess.salesapp.feature.sale.service_order.utils.error.AddClientError
import com.peyess.salesapp.feature.sale.service_order.utils.error.AddProductError
import com.peyess.salesapp.feature.sale.service_order.utils.error.ColoringNotFound
import com.peyess.salesapp.feature.sale.service_order.utils.error.CreatePurchaseError
import com.peyess.salesapp.feature.sale.service_order.utils.error.FramesNotFound
import com.peyess.salesapp.feature.sale.service_order.utils.error.LensNotFound
import com.peyess.salesapp.feature.sale.service_order.utils.error.PurchaseCreationFailed
import com.peyess.salesapp.feature.sale.service_order.utils.error.ResponsibleNotFound
import com.peyess.salesapp.feature.sale.service_order.utils.error.ServiceOrderUnexpected
import com.peyess.salesapp.feature.sale.service_order.utils.error.ServiceOrderUploaderError
import com.peyess.salesapp.feature.sale.service_order.utils.error.TreatmentNotFound
import com.peyess.salesapp.feature.sale.service_order.utils.error.UserNotFound
import com.peyess.salesapp.feature.sale.service_order.utils.error.WitnessNotFound
import com.peyess.salesapp.features.pdf.service_order.buildHtml
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.users.CollaboratorDocument
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.SOState
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.random.Random
import kotlin.random.asJavaRandom

typealias SaleData = Pair<ServiceOrderDocument, PurchaseDocument>
typealias SaleDataGenerationResponse = Either<ServiceOrderUploaderError, SaleData>
typealias PurchaseCreationResponse = Either<CreatePurchaseError, PurchaseDocument>

class ServiceOrderUploader constructor(
    private val salesDatabase: ActiveSalesDatabase,
    private val authenticationRepository: AuthenticationRepository,
    private val saleRepository: SaleRepository,
    private val positioningRepository: PositioningRepository,
    private val measuringRepository: MeasuringRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val discountRepository: OverallDiscountRepository,
    private val paymentFeeRepository: PaymentFeeRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val framesRepository: LocalFramesRepository,
    private val localClientRepository: LocalClientRepository,
    private val clientPickedRepository: ClientPickedRepository,
    private val salePaymentRepository: SalePaymentRepository,
    private val firebaseManager: FirebaseManager,
) {
    var purchaseId = ""

    var measuringLeftId = ""
    var measuringRightId = ""

    var purchaseHid = ""

    private suspend fun addPrescriptionData(
        so: ServiceOrderDocument,
        uploadPartialData: Boolean,
    ): ServiceOrderDocument {
        var prescriptionEntity: PrescriptionEntity? = null

        salesDatabase
            .prescriptionPictureDao()
            .getById(so.id)
            .take(1)
            .collect { prescriptionEntity = it }

        if (prescriptionEntity == null) {
            error("Prescription not found")
        }

        val prescriptionId = prescriptionEntity!!.id

        val instant: Instant = prescriptionEntity!!
            .prescriptionDate
            .toLocalDate()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())

        if (uploadPartialData) {
            uploadPrescription(prescriptionEntity!!, so)
        }

        return so.copy(
            prescriptionId = prescriptionId,

            isCopy = prescriptionEntity!!.isCopy,
            professionalName = prescriptionEntity!!.professionalName,
            professionalId = prescriptionEntity!!.professionalId,
            prescriptionDate = zonedDateTime,

            hasAddition = prescriptionEntity!!.hasAddition,
            hasPrism = prescriptionEntity!!.hasPrism,

            lCylinder = prescriptionEntity!!.cylindricalLeft,
            lSpheric = prescriptionEntity!!.sphericalLeft,
            lAxisDegree = prescriptionEntity!!.axisLeft,
            lAddition = prescriptionEntity!!.additionLeft,
            lPrismAxis = prescriptionEntity!!.prismAxisLeft,
            lPrismDegree = prescriptionEntity!!.prismDegreeLeft,
            lPrismPos = PrismPosition.toName(prescriptionEntity!!.prismPositionLeft),

            rCylinder = prescriptionEntity!!.cylindricalRight,
            rSpheric = prescriptionEntity!!.sphericalRight,
            rAxisDegree = prescriptionEntity!!.axisRight,
            rAddition = prescriptionEntity!!.additionRight,
            rPrismAxis = prescriptionEntity!!.prismAxisRight,
            rPrismDegree = prescriptionEntity!!.prismDegreeRight,
            rPrismPos = PrismPosition.toName(prescriptionEntity!!.prismPositionRight),
        )
    }

    private suspend fun uploadPrescription(
        prescriptionEntity: PrescriptionEntity,
        so: ServiceOrderDocument
    ) {
        val fsPrescription = prescriptionFrom(
            storeId = so.storeId,

            clientUid = so.clientUid,
            clientDocument = so.clientDocument,
            clientName = so.clientName,
            salespersonUid = so.salespersonUid,

            prescriptionEntity = prescriptionEntity,
        )

        prescriptionRepository.add(fsPrescription)
    }

    private suspend fun addPositioningData(
        so: ServiceOrderDocument,
        uploadPartialData: Boolean,
    ): ServiceOrderDocument {
        var positioningLeft: PositioningEntity? = null
        var positioningRight: PositioningEntity? = null

        val currentStore = firebaseManager.currentStore?.uid ?: ""

        salesDatabase
            .positioningDao()
            .getById(so.id, Eye.Left)
            .take(1)
            .collect { positioningLeft = it }

        salesDatabase
            .positioningDao()
            .getById(so.id, Eye.Right)
            .take(1)
            .collect { positioningRight = it }

        if (positioningLeft == null || positioningRight == null) {
            error("Could not find at least one of the positionings" +
                    "left == $positioningLeft " +
                    "right == $positioningRight")
        }

        val posLeftId = (positioningLeft?.id ?: "").ifBlank {
            firebaseManager.uniqueId()
        }

        val posRightId = (positioningRight?.id ?: "").ifBlank {
            firebaseManager.uniqueId()
        }

        val measLeftId = measuringLeftId.ifBlank {
            val uniqueId = firebaseManager.uniqueId()
            measuringLeftId = uniqueId

            uniqueId
        }
        val measRightId = measuringRightId.ifBlank {
            val uniqueId = firebaseManager.uniqueId()
            measuringRightId = uniqueId

            uniqueId
        }

        val positioningDocLeft = positioningLeft!!.toPositioningDocument(
            storeId = currentStore,
            prescriptionId = so.prescriptionId,

            patientUid = so.clientUid,
            patientName = so.clientName,
            patientDocument = so.clientDocument,

            salesPersonId = so.salespersonUid,
            takenByUid = so.salespersonUid,
            soId = so.id,
        )
        val positioningDocRight = positioningRight!!.toPositioningDocument(
            storeId = currentStore,
            prescriptionId = so.prescriptionId,

            patientUid = so.clientUid,
            patientName = so.clientName,
            patientDocument = so.clientDocument,

            salesPersonId = so.salespersonUid,
            takenByUid = so.salespersonUid,
            soId = so.id,
        )

        if (uploadPartialData) {
            positioningRepository.add(positioningDocLeft)
            positioningRepository.add(positioningDocRight)
        }

        val measuringLeft = positioningLeft!!
            .toMeasuring()
            .toMeasuringDocument(
                measLeftId,
                so.storeIds[0],
                so.prescriptionId,
                posLeftId,
                so.salespersonUid,
                so.clientUid,
                so.clientDocument,
                so.clientName,
                so.id,
            )
        val measuringRight = positioningRight!!
            .toMeasuring()
            .toMeasuringDocument(
                measRightId,
                so.storeIds[0],
                so.prescriptionId,
                posRightId,
                so.salespersonUid,
                so.clientUid,
                so.clientDocument,
                so.clientName,
                so.id,
            )

        if (uploadPartialData) {
            measuringRepository.add(measuringLeft)
            measuringRepository.add(measuringRight)
        }

        return so.copy(
            lPositioningId = posLeftId,
            rPositioningId = posRightId,

            lMeasuringId = measLeftId,
            rMeasuringId = measRightId,

            lIpd = measuringLeft.fixedIpd,
            lBridge = measuringLeft.fixedBridge,
            lDiameter = measuringLeft.fixedDiameter,
            lHe = measuringLeft.fixedHe,
            lHorizontalBridgeHoop = measuringLeft.fixedHorizontalBridgeHoop,
            lHorizontalHoop = measuringLeft.fixedHHoop,
            lVerticalHoop = measuringLeft.fixedVHoop,

            rIpd = measuringRight.fixedIpd,
            rBridge = measuringRight.fixedBridge,
            rDiameter = measuringRight.fixedDiameter,
            rHe = measuringRight.fixedHe,
            rHorizontalBridgeHoop = measuringRight.fixedHorizontalBridgeHoop,
            rHorizontalHoop = measuringRight.fixedHHoop,
            rVerticalHoop = measuringRight.fixedVHoop,
        )
    }

    private suspend fun addClientData(
        so: ServiceOrderDocument,
    ): Either<AddClientError, ServiceOrderDocument> = either {
        val user = clientPickedRepository
            .getClientForServiceOrder(role = ClientRole.User, soId = so.id)
            .mapLeft { ClientNotFound(it.description, it.error) }
            .flatMap {
                localClientRepository.clientById(it.id).mapLeft { e ->
                    ClientNotFound(e.description, e.error)
                }
            }.mapLeft { e -> UserNotFound(e.description, e.error) }
            .bind()

        val responsible = clientPickedRepository
            .getClientForServiceOrder(role = ClientRole.Responsible, soId = so.id)
            .mapLeft { ClientNotFound(it.description, it.error) }
            .flatMap {
                localClientRepository.clientById(it.id).mapLeft { e ->
                    ClientNotFound(e.description, e.error)
                }
            }.mapLeft { ResponsibleNotFound(it.description, it.error) }
            .bind()

        val witness = clientPickedRepository
            .getClientForServiceOrder(role = ClientRole.Witness, soId = so.id)
            .mapLeft { ClientNotFound(it.description, it.error) }
            .flatMap {
                localClientRepository.clientById(it.id).mapLeft { e ->
                    ClientNotFound(e.description, e.error)
                }
            }.mapLeft { WitnessNotFound(it.description, it.error) }
            .fold(
                ifLeft = { null },
                ifRight = { it },
            )

        so.copy(
            clientDocument = user.document,
            clientName = user.name,
            clientUid = user.id,
            clientBirthday = user.birthday,
            clientPhone = user.phone,
            clientCellphone = user.cellphone,
            clientNeighborhood = user.neighborhood,
            clientStreet = user.street,
            clientCity = user.city,
            clientState = user.state,
            clientHouseNumber = user.houseNumber,
            clientZipcode = user.zipCode,

            responsibleDocument = responsible.document,
            responsibleName = responsible.name,
            responsibleUid = responsible.id,
            responsibleBirthday = responsible.birthday,
            responsiblePhone = responsible.phone,
            responsibleCellphone = responsible.cellphone,
            responsibleNeighborhood = responsible.neighborhood,
            responsibleStreet = responsible.street,
            responsibleCity = responsible.city,
            responsibleState = responsible.state,
            responsibleHouseNumber = responsible.houseNumber,
            responsibleZipcode = responsible.zipCode,

            witnessDocument = witness?.document ?: "",
            witnessName = witness?.name ?: "",
            witnessUid = witness?.id ?: "",
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

    private suspend fun addSalespersonData(so: ServiceOrderDocument): ServiceOrderDocument {
        var user: CollaboratorDocument? = null

        authenticationRepository
            .currentUser()
            .filterNotNull()
            .take(1)
            .collect {
                user = it
            }

        if (user == null) {
            error("Could not find current collaborator: user == $user")
        }

        return so.copy(
            salespersonUid = user!!.id,
            salespersonName = user!!.name,

            soldBy = user!!.id,
            measureConfirmedBy = user!!.id,
            discountAllowedBy = user!!.id,

            createdBy =  user!!.id,
            createAllowedBy =  user!!.id,
            updatedBy =  user!!.id,
            updateAllowedBy =  user!!.id,
        )
    }

    private suspend fun addProductsData(
        so: ServiceOrderDocument,
    ): Either<AddProductError, ServiceOrderDocument> = either {
        val productPicked = saleRepository
            .productPicked(so.id)
            .mapLeft {
                ServiceOrderUnexpected("Comparison for so ${so.id} not found", it.error)
            }
            .bind()

        val lens = localLensesRepository
            .getLensById(productPicked.lensId)
            .mapLeft { LensNotFound(it.description, it.error) }
            .bind()

        val coloring = localLensesRepository
            .getColoringById(productPicked.coloringId)
            .mapLeft { ColoringNotFound(it.description, it.error) }
            .bind()

        val treatment = localLensesRepository
            .getTreatmentById(productPicked.treatmentId)
            .mapLeft { TreatmentNotFound(it.description, it.error) }
            .bind()

        val frames = framesRepository
            .framesForServiceOrder(so.id)
            .mapLeft { FramesNotFound(it.description, it.error) }
            .bind()

        val misc = mutableListOf<ProductSoldDescriptionDocument>()

        val framesValue = if (frames.areFramesNew) {
            frames.value
        } else {
            0.0
        }

        var total = lens.price + framesValue

        if (!lens.isColoringIncluded && !lens.isColoringDiscounted) {
            total += coloring.price

            if (lens.priceAddColoring > 0) {
                total += lens.priceAddColoring

                misc.add(
                    ProductSoldDescriptionDocument(
                        units = 1,
                        nameDisplay = "Adicional por coloração",
                        price = lens.priceAddColoring,
                    )
                )
            }
        }

        if (!lens.isTreatmentIncluded && !lens.isTreatmentDiscounted) {
            total += treatment.price

            if (lens.priceAddTreatment > 0) {
                total += lens.priceAddTreatment

                misc.add(
                    ProductSoldDescriptionDocument(
                        units = 1,
                        nameDisplay = "Adicional por tratamento",
                        price = lens.priceAddTreatment,
                    )
                )
            }
        }

        so.copy(
            samePurchaseSo = listOf(so.id),

            total = total,
            hasOwnFrames = !frames.areFramesNew,
            leftProducts = ProductSoldEyeSetDocument(
                lenses = lens.toDescription(),
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
                lenses = lens.toDescription(),
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
            miscProducts = misc,
        )
    }

    private suspend fun addDiscountData(
        saleId: String,
        serviceOrder: ServiceOrderDocument,
    ): ServiceOrderDocument {
        val discount = discountRepository.getDiscountForSale(saleId)
        val totalDiscount = when (discount?.discountMethod) {
            DiscountCalcMethod.Percentage -> discount.overallDiscountValue
            DiscountCalcMethod.Whole -> discount.overallDiscountValue / serviceOrder.total
            DiscountCalcMethod.None, null -> 0.0
        }

        return serviceOrder.copy(
            totalDiscount = totalDiscount,
            discountAllowedBy = serviceOrder.createAllowedBy,
        )
    }

    private suspend fun addFeeData(
        saleId: String,
        serviceOrder: ServiceOrderDocument,
    ): ServiceOrderDocument {

        val fee = paymentFeeRepository.getPaymentFeeForSale(saleId)
        val totalFee = when (fee?.method) {
            PaymentFeeCalcMethod.Percentage -> fee.value
            PaymentFeeCalcMethod.Whole ->  fee.value / serviceOrder.totalWithDiscount
            PaymentFeeCalcMethod.None, null -> 0.0
        }

        return serviceOrder.copy(totalFee = totalFee)
    }


    private fun createHid(): String {
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

        return "$suffix-$id"
    }

    private suspend fun createPurchase(
        context: Context,
        saleId: String,
        serviceOrder: ServiceOrderDocument,
    ): PurchaseCreationResponse = either {
//        val id = purchaseId.ifBlank {
//            val uniqueId = firebaseManager.uniqueId()
//            purchaseId = uniqueId
//
//            uniqueId
//        }

        purchaseId = saleId
        val id = purchaseId

        val storeId = firebaseManager.currentStore?.uid
        ensureNotNull(storeId) {
            PurchaseCreationFailed(
                description = "Store id $storeId not found",
            )
        }

        val payments = salePaymentRepository
            .paymentForSale(saleId)
            .mapLeft {
                PurchaseCreationFailed(
                    description = "Payments for sale $saleId not found",
                    error = it.error,
                )
            }.bind()

        PurchaseDocument(
            id = id,
            hid = purchaseHid.ifBlank { createHid() },

            storeId = storeId,
            storeIds = listOf(storeId),
            clientUids = listOf(serviceOrder.id),
            clients = listOf(
                DenormalizedClientDocument(
                    uid = serviceOrder.clientUid,
                    document = serviceOrder.clientDocument,
                    name = serviceOrder.clientName,
                ),
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

            salespersonUid = serviceOrder.salespersonUid,
            salespersonName = serviceOrder.salespersonName,

            isDiscountPerProduct = false, // serviceOrder.is_discount_per_product,
            overallDiscount = DiscountDescriptionDocument(), // serviceOrder.overall_discount,

            price = serviceOrder.total,
            priceWithDiscount = 0.0, // serviceOrder.price_with_discount,
            prodDiscount = emptyMap(),

            state = PurchaseState.PendingConfirmation,

            payerUids = payments.map { it.clientId }.distinct(),
            payerDocuments = payments.map { it.clientDocument }.distinct(),
            payments = payments.map { it.toPaymentDocument() },

            soStates = mapOf(serviceOrder.id to SOState.fromName(serviceOrder.state)),
            soIds = listOf(serviceOrder.id),
            soPreviews = mapOf(
                serviceOrder.id to serviceOrder.toPreview()
            ),
            soWithIssues = emptyList(),
            hasRectifiedSo = false,
            isLegalCustom = false,

            // TODO: load legal text + version and add it here
            legalText = "", // serviceOrder.legal_text,
            legalVersion = "", // serviceOrder.legal_version,

            created = serviceOrder.created,
            createdBy = serviceOrder.createdBy,
            createAllowedBy = serviceOrder.createAllowedBy,

            updated = serviceOrder.updated,
            updatedBy = serviceOrder.updatedBy,
            updateAllowedBy = serviceOrder.updateAllowedBy,
        )
    }

    suspend fun generateSaleData(
        context: Context,
        hid: String,
        serviceOrderId: String,
        localSaleId: String,
        uploadPartialData: Boolean,
    ): SaleDataGenerationResponse = either {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance for store is null")
        }

        Timber.i("Creating SO with id $serviceOrderId for sale $localSaleId")
        val storeId = firebaseManager.currentStore!!.uid

        val now = ZonedDateTime.now()
        var serviceOrder = ServiceOrderDocument(
            id = serviceOrderId,
            hid = hid,

            storeId = storeId,
            storeIds = listOf(storeId),

            state = SOState.PendingConfirmation.toName(),

            created = now,
            updated = now,
        )

        serviceOrder = addClientData(serviceOrder).bind()

        serviceOrder = addSalespersonData(serviceOrder)

        serviceOrder = addPrescriptionData(serviceOrder, uploadPartialData)

        serviceOrder = addPositioningData(serviceOrder, uploadPartialData)

        serviceOrder = addProductsData(serviceOrder).bind()

        serviceOrder = addDiscountData(localSaleId, serviceOrder)
        serviceOrder = addFeeData(localSaleId, serviceOrder)

        var purchase = createPurchase(context, localSaleId, serviceOrder).bind()

        val totalPaid = if (purchase.payments.isEmpty()) {
            0.0
        } else {
            purchase.payments
                .map { it.amount }
                .reduce { acc, payment -> acc + payment }
        }
        val totalLeft = BigDecimal(abs((serviceOrder.totalWithFee - totalPaid)))
            .setScale(2, RoundingMode.HALF_EVEN)
            .toDouble()

        serviceOrder = serviceOrder.copy(
            purchaseId = purchaseId,
            payerUids = purchase.payerUids,
            payerDocuments = purchase.payerDocuments,

            totalPaid = totalPaid,
            leftToPay = totalLeft,
        )

        val legalText = buildHtml(context, serviceOrder, purchase)
        purchase = purchase.copy(legalText = legalText)

        Pair(serviceOrder, purchase)
    }

    suspend fun emitServiceOrder(
        serviceOrder: ServiceOrderDocument,
        purchase: PurchaseDocument,
        localSaleId: String,
    ) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance for store is null")
        }

        var sale: ActiveSalesEntity? = null

        Timber.i("Getting current sale with id $localSaleId")
        salesDatabase
            .activeSalesDao()
            .getById(localSaleId)
            .take(1)
            .collect {
                sale = it
            }
        Timber.i("Current sale found: $sale")

        if (sale != null) {
            salesDatabase
                .activeSalesDao()
                .update(sale!!.copy(isUploading = true))
        } else {
            error("Sale not found")
        }

        runBlocking {
            purchaseRepository.add(purchase)
            serviceOrderRepository.add(serviceOrder)
        }

        if (sale != null) {
            salesDatabase
                .activeSalesDao()
                .update(sale!!.copy(active = false, isUploading = false))
        }
    }
}