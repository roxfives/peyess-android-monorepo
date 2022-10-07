package com.peyess.salesapp.workmanager.sale

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
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
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.data.adapter.measuring.toMeasuringDocument
import com.peyess.salesapp.data.adapter.positioning.toPositioningDocument
import com.peyess.salesapp.data.adapter.prescription.prescriptionFrom
import com.peyess.salesapp.data.adapter.products.toDescription
import com.peyess.salesapp.data.model.sale.service_order.FSServiceOrder
import com.peyess.salesapp.data.model.sale.service_order.products_sold.FSProductsSold
import com.peyess.salesapp.data.model.sale.service_order.products_sold_desc.FSProductSoldDescription
import com.peyess.salesapp.data.repository.measuring.MeasuringRepository
import com.peyess.salesapp.data.repository.prescription.PrescriptionRepository
import com.peyess.salesapp.database.room.ActiveSalesDatabase
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.lens_pick.model.toMeasuring
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.users.Collaborator
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*

const val saleIdKey = "sale_id_key"
const val soIdKey = "so_id_key"

private data class ProductSet(
    val lens: LocalLensEntity = LocalLensEntity(),
    val coloring: LocalColoringEntity = LocalColoringEntity(),
    val treatment: LocalTreatmentEntity = LocalTreatmentEntity(),
    val frames: FramesEntity = FramesEntity(),
)

@HiltWorker
class GenerateServiceOrderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val salesDatabase: ActiveSalesDatabase,
    private val productRepository: ProductRepository,
    private val clientDao: ClientDao,
    private val salesApplication: SalesApplication,
    private val authenticationRepository: AuthenticationRepository,
    private val saleRepository: SaleRepository,
    private val positioningRepository: PositioningRepository,
    private val measuringRepository: MeasuringRepository,
    private val prescriptionRepository: PrescriptionRepository,
    private val firebaseManager: FirebaseManager,
): CoroutineWorker(context, workerParams) {

    private suspend fun addPrescriptionData(so: FSServiceOrder): FSServiceOrder {
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
            return so
        }

        val prescriptionId =
            uploadPrescription(prescriptionDataEntity!!, prescriptionPictureEntity!!, so)

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

        so: FSServiceOrder
    ): String {
        val prescriptionId = firebaseManager.uniqueId()
        val fsPrescription = prescriptionFrom(
            id = prescriptionId,

            clientDocument = so.clientDocument,
            clientName = so.clientName,
            salespersonUid = so.salespersonUid,

            dataEntity = prescriptionDataEntity,
            pictureEntity = prescriptionPictureEntity,
        )

        prescriptionRepository.add(fsPrescription)
        return prescriptionId
    }

    private suspend fun addPositioningData(
        so: FSServiceOrder,
    ): FSServiceOrder {
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
            return so
        }

        val positioningLeftId = firebaseManager.uniqueId()
        val positioningRightId = firebaseManager.uniqueId()

        val measuringLeftId = firebaseManager.uniqueId()
        val measuringRightId = firebaseManager.uniqueId()

        // TODO: update height and width
        val positioningDocLeft = positioningLeft!!.toPositioningDocument(
            id = positioningLeftId,

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
            id = positioningRightId,

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
                measuringLeftId,
                so.storeIds[0],
                so.prescriptionId,
                so.lPositioningId,
                so.salespersonUid,
                so.clientUid,
                so.clientDocument,
                so.clientName,
                so.id,
            )
        val measuringRight = positioningRight!!
            .toMeasuring()
            .toMeasuringDocument(
                measuringRightId,
                so.storeIds[0],
                so.prescriptionId,
                so.lPositioningId,
                so.salespersonUid,
                so.clientUid,
                so.clientDocument,
                so.clientName,
                so.id,
            )

        measuringRepository.add(measuringLeft)
        measuringRepository.add(measuringRight)

        return so.copy(
            lPositioningId = positioningLeftId,
            rPositioningId = positioningRightId,

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
    private suspend fun addClientData(so: FSServiceOrder): FSServiceOrder {
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
            return so
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

    private suspend fun addSalespersonData(so: FSServiceOrder): FSServiceOrder {
        var user: Collaborator? = null

        authenticationRepository
            .currentUser()
            .take(1)
            .collect {
                user = it
            }

        if (user == null) {
            return so
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
    private suspend fun addProductsData(so: FSServiceOrder): FSServiceOrder {
        var total = 0.0

        var lensDesc = FSProductSoldDescription()
        var coloringDesc = FSProductSoldDescription()
        var treatmentDesc = FSProductSoldDescription()
        var framesDesc = FSProductSoldDescription()

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

//                lensDesc = lens.toDescription()
//                coloringDesc = coloring.toDescription()
//                treatmentDesc = treatment.toDescription()
//                framesDesc = frames.toDescription()

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
            products = FSProductsSold(
                lenses = mapOf(lensDesc.id to lensDesc),
                colorings = mapOf(coloringDesc.id to coloringDesc),
                treatments = mapOf(treatmentDesc.id to treatmentDesc),

                frames = framesDesc,

                misc = emptyMap(),
            )
        )
    }

//    private suspend fun createPurchase(so: FSServiceOrder): FSPurchase {
//        val payments: MutableList<SalePaymentEntity> = mutableListOf()
//
//        saleRepository
//            .payments()
//            .collect {
//                payments.addAll(it)
//            }
//
//        val purchase = FSPurchase(
//
//        )
//
//        return
//    }

    override suspend fun doWork(): Result {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return Result.retry()
        }

        val soId = inputData.getString(soIdKey)
        val saleId = inputData.getString(saleIdKey)

        val storeId = firebaseManager.currentStore!!.uid
        val soPath = salesApplication
            .stringResource(id = R.string.fs_col_so)
            .format(storeId)

        withContext(Dispatchers.IO) {
            var sale: ActiveSalesEntity? = null
            runBlocking {
                salesDatabase.activeSalesDao().getById(saleId ?: "").take(1).collect {
                    sale = it
                }
            }

            if (sale != null) {
                salesDatabase
                    .activeSalesDao()
                    .update(sale!!.copy(isUploading = true))
            }

            val now = Date()

            var serviceOrder = FSServiceOrder(
                id = soId ?: "",
                storeIds = listOf(storeId),

                state = "confirmed",

//                created = now,
//                updated = now,
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

            runBlocking {
                Timber.i("Adding service order $soId to fb at $soPath")

                firestore
                    .collection(soPath)
                    .document(soId ?: "")
                    .set(serviceOrder)
                    .addOnCompleteListener {
                        Timber.i("Completed with $it (${it.isSuccessful})")

                        if (!it.isSuccessful) {
                            Timber.e(it.exception)
                        }
                    }
                    .await()
            }

            if (sale != null) {
                salesDatabase
                    .activeSalesDao()
                    .update(sale!!.copy(active = false, isUploading = false))
            }
        }

        return Result.success()
    }
}