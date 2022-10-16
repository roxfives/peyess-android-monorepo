package com.peyess.salesapp.feature.sale.service_order.utils

import android.net.Uri
import com.peyess.salesapp.dao.client.firestore.ClientDao
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.data.repository.positioning.PositioningRepository
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.adapter.measuring.toMeasuringDocument
import com.peyess.salesapp.data.adapter.payment.toPaymentDocument
import com.peyess.salesapp.data.adapter.positioning.toPositioningDocument
import com.peyess.salesapp.data.adapter.prescription.prescriptionFrom
import com.peyess.salesapp.data.adapter.products.toDescription
import com.peyess.salesapp.data.adapter.service_order.toPreview
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.data.model.sale.service_order.discount_description.DiscountDescriptionDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold.ProductsSoldDocument
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.ProductSoldDescriptionDocument
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.payment.PurchaseRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.database.room.ActiveSalesDatabase
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.lens_pick.model.toMeasuring
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.users.Collaborator
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import com.peyess.salesapp.repository.service_order.ServiceOrderRepository
import com.peyess.salesapp.typing.sale.PurchaseState
import com.peyess.salesapp.typing.sale.SOState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.time.ZonedDateTime

private data class ProductSet(
    val lens: LocalLensEntity = LocalLensEntity(),
    val coloring: LocalColoringEntity = LocalColoringEntity(),
    val treatment: LocalTreatmentEntity = LocalTreatmentEntity(),
    val frames: FramesEntity = FramesEntity(),
)

class ServiceOrderUploader constructor(
    private val salesDatabase: ActiveSalesDatabase,
    private val productRepository: ProductRepository,
    private val clientDao: ClientDao,
    private val authenticationRepository: AuthenticationRepository,
    private val saleRepository: SaleRepository,
    private val positioningRepository: PositioningRepository,
    private val measuringRepository: MeasuringRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val purchaseRepository: PurchaseRepository,
    private val serviceOrderRepository: ServiceOrderRepository,
    private val firebaseManager: FirebaseManager,
) {
    var purchaseId = ""

    var prescriptionId = ""

    var positioningLeftId = ""
    var positioningRightId = ""

    var measuringLeftId = ""
    var measuringRightId = ""

    private suspend fun addPrescriptionData(so: ServiceOrderDocument): ServiceOrderDocument {
        var prescriptionDataEntity: PrescriptionDataEntity? = null
        var prescriptionPictureEntity: PrescriptionPictureEntity? = null

        salesDatabase
            .prescriptionDataDao()
            .getById(so.id)
            .take(1)
            .collect { prescriptionDataEntity = it }

        salesDatabase
            .prescriptionPictureDao()
            .getById(so.id)
            .take(1)
            .collect { prescriptionPictureEntity = it }

        if (prescriptionDataEntity == null || prescriptionPictureEntity == null) {
            error("Prescription not found")
        }

        val prescriptionId = uploadPrescription(
            prescriptionDataEntity!!,
            prescriptionPictureEntity!!,
            so,
        )

        return so.copy(
            prescriptionId = prescriptionId,

            lCylinder = prescriptionDataEntity!!.cylindricalLeft,
            lSpheric = prescriptionDataEntity!!.sphericalLeft,
            lAxisDegree = prescriptionDataEntity!!.axisLeft,
            lAddition = prescriptionDataEntity!!.additionLeft,
            lPrismAxis = prescriptionDataEntity!!.prismAxisLeft,
            lPrismDegree = prescriptionDataEntity!!.prismDegreeLeft,
            lPrismPos = PrismPosition.toName(prescriptionDataEntity!!.prismPositionLeft),

            rCylinder = prescriptionDataEntity!!.cylindricalRight,
            rSpheric = prescriptionDataEntity!!.sphericalRight,
            rAxisDegree = prescriptionDataEntity!!.axisRight,
            rAddition = prescriptionDataEntity!!.additionRight,
            rPrismAxis = prescriptionDataEntity!!.prismAxisRight,
            rPrismDegree = prescriptionDataEntity!!.prismDegreeRight,
            rPrismPos = PrismPosition.toName(prescriptionDataEntity!!.prismPositionRight),
        )
    }

    private suspend fun uploadPrescription(
        prescriptionDataEntity: PrescriptionDataEntity,
        prescriptionPictureEntity: PrescriptionPictureEntity,

        so: ServiceOrderDocument
    ): String {
        val id = prescriptionId.ifBlank {
            val uniqueId = firebaseManager.uniqueId()
            prescriptionId = uniqueId

            uniqueId
        }
        val fsPrescription = prescriptionFrom(
            id = id,

            storeId = so.storeId,

            clientUid = so.clientUid,
            clientDocument = so.clientDocument,
            clientName = so.clientName,
            salespersonUid = so.salespersonUid,

            dataEntity = prescriptionDataEntity,
            pictureEntity = prescriptionPictureEntity,
        )

        prescriptionRepository.add(fsPrescription)
        return id
    }

    private suspend fun addPositioningData(
        so: ServiceOrderDocument,
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

        val posLeftId = positioningLeftId.ifBlank {
            val uniqueId = firebaseManager.uniqueId()
            positioningLeftId = uniqueId

            uniqueId
        }
        val posRightId = positioningRightId.ifBlank {
            val uniqueId = firebaseManager.uniqueId()
            positioningRightId = uniqueId

            uniqueId
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
            id = posLeftId,

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
            id = posRightId,

            storeId = currentStore,
            prescriptionId = so.prescriptionId,

            patientUid = so.clientUid,
            patientName = so.clientName,
            patientDocument = so.clientDocument,

            salesPersonId = so.salespersonUid,
            takenByUid = so.salespersonUid,
            soId = so.id,
        )

        positioningRepository.add(positioningDocLeft)
        positioningRepository.add(positioningDocRight)

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

        measuringRepository.add(measuringLeft)
        measuringRepository.add(measuringRight)

        return so.copy(
            lPositioningId = posLeftId,
            rPositioningId = posRightId,

            lMeasuringId = measLeftId,
            rMeasuringId = measRightId,

            lIpd = measuringLeft.ipd,
            lBridge = measuringLeft.bridge,
            lDiameter = measuringLeft.diameter,
            lHe = measuringLeft.he,
            lHorizontalBridgeHoop = measuringLeft.horizontalBridgeHoop,
            lHorizontalHoop = measuringLeft.horizontalHoop,
            lVerticalHoop = measuringLeft.verticalHoop,

            rIpd = measuringRight.ipd,
            rBridge = measuringRight.bridge,
            rDiameter = measuringRight.diameter,
            rHe = measuringRight.he,
            rHorizontalBridgeHoop = measuringRight.horizontalBridgeHoop,
            rHorizontalHoop = measuringRight.horizontalHoop,
            rVerticalHoop = measuringRight.verticalHoop,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun addClientData(so: ServiceOrderDocument): ServiceOrderDocument {
        var user: ClientDocument? = null
        var responsible: ClientDocument? = null
        var witness: ClientDocument? = null

        salesDatabase
            .clientPickedDao()
            .getClientForSO(ClientRole.User, so.id)
            .flatMapLatest { clientDao.clientById(it?.id ?: "") }
            .take(1)
            .collect { user = it }

        salesDatabase
            .clientPickedDao()
            .getClientForSO(ClientRole.Responsible, so.id)
            .flatMapLatest { clientDao.clientById(it?.id ?: "") }
            .take(1)
            .collect { responsible = it }

        salesDatabase
            .clientPickedDao()
            .getClientForSO(ClientRole.Witness, so.id)
            .flatMapLatest {
                if (it != null) {
                    clientDao.clientById(it.id)
                } else {
                    flowOf(null)
                }
            }
            .take(1)
            .collect { witness = it }

        if (user == null || responsible == null) {
            error("User or responsible is null: " +
                    "user == $user and responsible == $responsible")
        }

        return so.copy(
            clientDocument = user!!.document,
            clientName = user!!.name,
            clientPicture = user!!.picture.toString(),
            clientUid = user!!.id,

            responsibleDocument = responsible!!.document,
            responsibleName = responsible!!.name,
            responsiblePicture = responsible!!.picture.toString(),
            responsibleUid = responsible!!.id,

            witnessDocument = witness?.document ?: "",
            witnessName = witness?.name ?: "",
            witnessPicture = (witness?.picture ?: "").toString(),
            witnessUid = witness?.id ?: "",
        )
    }

    private suspend fun addSalespersonData(so: ServiceOrderDocument): ServiceOrderDocument {
        var user: Collaborator? = null

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
            soldBy = user!!.id,
            measureConfirmedBy = user!!.id,
            discountAllowedBy = user!!.id,

            createdBy =  user!!.id,
            createAllowedBy =  user!!.id,
            updatedBy =  user!!.id,
            updateAllowedBy =  user!!.id,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun addProductsData(so: ServiceOrderDocument): ServiceOrderDocument {
        var total = 0.0

        var lensDesc = ProductSoldDescriptionDocument()
        var coloringDesc = ProductSoldDescriptionDocument()
        var treatmentDesc = ProductSoldDescriptionDocument()
        var framesDesc = ProductSoldDescriptionDocument()

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

                lensDesc = lens.toDescription()
                coloringDesc = coloring.toDescription()
                treatmentDesc = treatment.toDescription()
                framesDesc = frames.toDescription()

                val framesValue = if (frames.areFramesNew) {
                    frames.value
                } else {
                    0.0
                }

                // TODO: Update coloring and treatment to use price instead of suggested price
                var totalToPay = lens.price + framesValue

                if (!lens.isColoringIncluded && !lens.isColoringDiscounted) {
                    totalToPay += coloringDesc.price
                }
                if (!lens.isTreatmentIncluded && !lens.isTreatmentDiscounted) {
                    totalToPay += treatmentDesc.price
                }

                if (coloring.suggestedPrice > 0) {
                    totalToPay += lens.suggestedPriceAddColoring
                }
                if (treatment.suggestedPrice > 0) {
                    totalToPay += lens.suggestedPriceAddTreatment
                }

                totalToPay
            }
            .take(1)
            .collect {
                total = it
            }

        return so.copy(
            samePurchaseSo = listOf(so.id),

            total = total,
            products = ProductsSoldDocument(
                lenses = mapOf(lensDesc.id to lensDesc),
                colorings = mapOf(coloringDesc.id to coloringDesc),
                treatments = mapOf(treatmentDesc.id to treatmentDesc),

                frames = framesDesc,

                misc = emptyMap(),
            )
        )
    }

    private suspend fun createPurchase(serviceOrder: ServiceOrderDocument): PurchaseDocument {
        val id = purchaseId.ifBlank {
            val uniqueId = firebaseManager.uniqueId()
            purchaseId = uniqueId

            uniqueId
        }

        val storeId = firebaseManager.currentStore?.uid
        if (storeId == null) {
            error("Could not find current store: store == ${firebaseManager.currentStore}")
        }

        val payments: MutableList<SalePaymentEntity> = mutableListOf()
        saleRepository
            .payments()
            .take(1)
            .collect {
                payments.addAll(it)
            }

        var frames: FramesEntity? = null
        saleRepository
            .currentFramesData()
            .take(1)
            .collect {
                frames = it
            }


        return PurchaseDocument(
            id = id,

            storeId = storeId,
            storeIds = listOf(storeId),
            clientUids = listOf(serviceOrder.id),
            clients = listOf(
                DenormalizedClientDocument(
                    uid = serviceOrder.clientUid,
                    document = serviceOrder.clientDocument,
                    name = serviceOrder.clientName,
                    picture = Uri.parse(serviceOrder.clientPicture),
                ),
            ),

            responsibleDocument = serviceOrder.responsibleDocument,
            responsibleName = serviceOrder.responsibleName,
            responsiblePicture = serviceOrder.responsiblePicture,
            responsibleUid = serviceOrder.responsibleUid,

            hasWitness = serviceOrder.hasWitness,
            witnessDocument = serviceOrder.witnessDocument,
            witnessName = serviceOrder.witnessName,
            witnessPicture = serviceOrder.witnessPicture,
            witnessUid = serviceOrder.witnessUid,

            salespersonUid = serviceOrder.salespersonUid,

            isDiscountPerProduct = false,// serviceOrder.is_discount_per_product,
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
                serviceOrder.id to serviceOrder.toPreview(frames?.areFramesNew == false)
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

    suspend fun emitServiceOrder(
        hid: String,
        serviceOrderId: String,
        localSaleId: String,
    ) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance for store is null")
        }

        Timber.i("Creating SO with id $serviceOrderId for sale $localSaleId")
        val storeId = firebaseManager.currentStore!!.uid
        var sale: ActiveSalesEntity? = null

        Timber.i("Getting current sale")
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

        serviceOrder = runBlocking {
            addClientData(serviceOrder)
        }

        serviceOrder = runBlocking {
            addSalespersonData(serviceOrder)
        }

        serviceOrder = runBlocking {
            addPrescriptionData(serviceOrder)
        }

        serviceOrder = runBlocking {
            addPositioningData(serviceOrder)
        }

        serviceOrder = runBlocking {
            addProductsData(serviceOrder)
        }

        val purchase = runBlocking {
            createPurchase(serviceOrder)
        }

        serviceOrder = serviceOrder.copy(
            purchaseId = purchaseId,
            payerUids = purchase.payerUids,
            payerDocuments = purchase.payerDocuments,
        )

        runBlocking {
            purchaseRepository.add(purchase)
            serviceOrderRepository.add(serviceOrder)
        }

        Timber.i("Finished creating SO with id $serviceOrderId for sale $localSaleId")
        if (sale != null) {
            salesDatabase
                .activeSalesDao()
                .update(sale!!.copy(active = false, isUploading = false))
        }
    }
}