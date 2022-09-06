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
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrismPosition
import com.peyess.salesapp.dao.service_order.FSServiceOrder
import com.peyess.salesapp.database.room.ActiveSalesDatabase
import com.peyess.salesapp.database.room.ProductsDatabase
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.isActive
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
    val productsDatabase: ProductsDatabase,
    val salesDatabase: ActiveSalesDatabase,
    val productRepository: ProductRepository,
    val clientDao: ClientDao,
    val salesApplication: SalesApplication,
    val authenticationRepository: AuthenticationRepository,
    val saleRepository: SaleRepository,
    val firebaseManager: FirebaseManager,
): CoroutineWorker(context, workerParams) {

    private suspend fun addPrescriptionData(so: FSServiceOrder): FSServiceOrder {
        var prescriptionDataEntity: PrescriptionDataEntity? = null

        salesDatabase
            .prescriptionDataDao()
            .getById(so.id)
            .take(1)
            .collect { prescriptionDataEntity = it }

        if (prescriptionDataEntity == null) {
            return so
        }

        return so.copy(
            prescriptionId = "",

            lCylinder = prescriptionDataEntity!!.cylindricalLeft,
            lSpheric = prescriptionDataEntity!!.sphericalLeft,
            lAxisDegree = prescriptionDataEntity!!.axisLeft,
            lAddition = prescriptionDataEntity!!.additionLeft,
            lPrismAxis = prescriptionDataEntity!!.prismAxisLeft,
            lPrismDegree = prescriptionDataEntity!!.prismDegreeLeft,
            lPrismPos = PrismPosition.toName(prescriptionDataEntity!!.prismPositionLeft),

            rIpd = prescriptionDataEntity!!.cylindricalRight,
            rCylinder = prescriptionDataEntity!!.sphericalRight,
            rSpheric = prescriptionDataEntity!!.axisRight,
            rAxisDegree = prescriptionDataEntity!!.additionRight,
            rAddition = prescriptionDataEntity!!.prismAxisRight,
            rPrismAxis = prescriptionDataEntity!!.prismDegreeRight,
            rPrismDegree = prescriptionDataEntity!!.prismDegreeRight,
            rPrismPos = PrismPosition.toName(prescriptionDataEntity!!.prismPositionRight),
        )
    }

    private suspend fun addPositioningData(so: FSServiceOrder): FSServiceOrder {
        var positioningLeft: PositioningEntity? = null
        var positioningRight: PositioningEntity? = null

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

        val measuringLeft = positioningLeft!!.toMeasuring()
        val measuringRight = positioningRight!!.toMeasuring()

        return so.copy(
            lMeasuringId = "",
            rMeasuringId = "",

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

        if (user == null || responsible == null) {
            return so
        }

        return so.copy(
            clientDocument = user!!.document,
            clientName = user!!.name,
            clientPicture = user!!.picture,
            clientUid = user!!.id,

            responsibleDocument = responsible!!.document,
            responsibleName = responsible!!.name,
            responsiblePicture = responsible!!.picture,
            responsibleUid = responsible!!.id,
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

            createdBy =  user!!.id,
            createAllowedBy =  user!!.id,
            updatedBy =  user!!.id,
            updateAllowedBy =  user!!.id,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun addProductsData(so: FSServiceOrder): FSServiceOrder {
        var total = 0.0

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
            .take(1)
            .collect {
                total = it
            }

        return so.copy(total = total)
    }

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
                storeId = storeId,

                state = "confirmed",

                created = now,
                updated = now,
            )

            serviceOrder = runBlocking {
                addSalespersonData(serviceOrder)
            }

            serviceOrder = runBlocking {
                addProductsData(serviceOrder)
            }

            serviceOrder = runBlocking {
                addPrescriptionData(serviceOrder)
            }

            serviceOrder = runBlocking {
                addPositioningData(serviceOrder)
            }

            serviceOrder = runBlocking {
                addClientData(serviceOrder)
            }

            runBlocking {
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