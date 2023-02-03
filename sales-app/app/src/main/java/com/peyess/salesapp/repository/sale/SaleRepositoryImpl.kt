package com.peyess.salesapp.repository.sale

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import arrow.core.valid
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.local_sale.client_picked.ClientPickedDao
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.dao.local_sale.positioning.PositioningDao
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.positioning.updateInitialPositioningState
import com.peyess.salesapp.data.dao.local_sale.payment.SalePaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.SalePaymentEntity
import com.peyess.salesapp.data.dao.local_sale.local_prescription.LocalPrescriptionDao
import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedDao
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.adapter.toProductPickedDocument
import com.peyess.salesapp.repository.sale.error.ActiveSaleError
import com.peyess.salesapp.repository.sale.error.ActiveSaleNotCanceled
import com.peyess.salesapp.repository.sale.error.ActiveSaleNotFound
import com.peyess.salesapp.repository.sale.error.ActiveSaleNotRegistered
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderError
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderNotFound
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderNotRegistered
import com.peyess.salesapp.repository.sale.error.ProductPickedNotFound
import com.peyess.salesapp.repository.sale.error.Unexpected
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import timber.log.Timber
import javax.inject.Inject

private val currentSOThreshold = 10

class SaleRepositoryImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
    val authenticationRepository: AuthenticationRepository,
    private val activeSalesDao: ActiveSalesDao,
    private val activeSODao: ActiveSODao,
    private val lensTypeCategoryDao: LensTypeCategoryDao,
    private val prescriptionPictureDao: LocalPrescriptionDao,
    private val framesDataDao: FramesDataDao,
    private val positioningDao: PositioningDao,
    private val productPickedDao: ProductPickedDao,
    private val clientPickedDao: ClientPickedDao,
    private val salePaymentDao: SalePaymentDao,
): SaleRepository {
    private val Context.dataStoreCurrentSale: DataStore<Preferences>
            by preferencesDataStore(currentSaleFileName)

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentSO by lazy {
        salesApplication
            .dataStoreCurrentSale
            .data
            .flatMapLatest { prefs ->
                val soId = prefs[currentSOKey]

                activeSODao.getById(soId ?: "")
            }.retryWhen { _ , attempt ->
                attempt < currentSOThreshold
            }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentSale by lazy {
        salesApplication
            .dataStoreCurrentSale
            .data
            .flatMapLatest { prefs ->
                val saleId = prefs[currentSaleKey]

                activeSalesDao.getById(saleId ?: "")
            }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentPrescriptionPicture by lazy {
        currentSO
            .filterNotNull()
            .flatMapLatest { so ->
                prescriptionPictureDao.getById(so.id).map {
                    it ?: PrescriptionEntity(soId = so.id)
            }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentFramesData by lazy {
        currentSO
            .filterNotNull()
            .flatMapLatest { so ->
                framesDataDao.getById(so.id).map {
                    it ?: FramesEntity(soId = so.id)
                }
            }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
    }

//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val currentPrescriptionData by lazy {
//        currentSO
//            .filterNotNull()
//            .flatMapLatest { so ->
//                prescriptionDataDao.getById(so.id).map {
//                    Timber.i("Emitting prescription data")
//                    it ?: PrescriptionDataEntity(soId = so.id)
//            }.shareIn(
//                scope = repositoryScope,
//                replay = 1,
//                started = SharingStarted.WhileSubscribed(),
//            )
//        }
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentPayments by lazy { emptyFlow<List<SalePaymentEntity>>() }

//    by lazy {
//        currentSO
//            .filterNotNull()
//            .flatMapLatest { salePaymentDao.getBySO(it.id) }.shareIn(
//                scope = repositoryScope,
//                replay = 1,
//                started = SharingStarted.WhileSubscribed(),
//            )
//    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentPositioningsByEye by lazy {
        val eyes = Eye.allOptions

        eyes.map { eye ->
            eye to currentSO
                .filterNotNull()
                .flatMapLatest { so ->
                    positioningDao.getById(so.id, eye).map { entity ->
                        val uniqueId = firebaseManager.uniqueId()

                        entity ?: PositioningEntity(id = uniqueId, soId = so.id, eye = eye)
                            .updateInitialPositioningState()
                    }
                }.shareIn(
                    scope = repositoryScope,
                    replay = 1,
                    started = SharingStarted.WhileSubscribed(),
                )
        }.toMap()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentClientsByRole by lazy {
        val roles = ClientRole.allOptions

        roles.map { role ->
            role to currentSO
                .filterNotNull()
                .flatMapLatest { so ->
                    Timber.i("Got so $so")
                    clientPickedDao.getClientForSO(role = role, soId = so.id)
                }.shareIn(
                    scope = repositoryScope,
                    replay = 1,
                    started = SharingStarted.WhileSubscribed(),
                )
        }.toMap()
    }

    override fun createSale(): Flow<Boolean> {
        return authenticationRepository.currentUser().filterNotNull().map {
            val activeSale = ActiveSalesEntity(
                id = firebaseManager.uniqueId(),
                collaboratorUid = it.id,
                active = true,
            )

            val activeSO = ActiveSOEntity(
                id = firebaseManager.uniqueId(),
                saleId = activeSale.id,
                lensTypeCategoryName = LensTypeCategoryName.Near,
            )

            Timber.i("Creating sale $activeSale")
            Timber.i("Creating so $activeSO")

            salesApplication.dataStoreCurrentSale.edit { prefs ->
                prefs[currentSaleKey] = activeSale.id
                prefs[currentSOKey] = activeSO.id
            }

            activeSalesDao.add(activeSale)
            activeSODao.add(activeSO)
            true
        }
    }

    override suspend fun cancelCurrentSale(): CancelSaleResponse {
        return currentSale().flatMap {
            val canceled = it.copy(active = false)

            try {
                activeSalesDao.update(canceled).right()
            } catch (e: Throwable) {
                ActiveSaleNotCanceled(
                    description = "Error cancelling sale ${it.id}"
                ).left()
            }
        }
    }

    override suspend fun cancelSale(
        sale: ActiveSalesEntity,
    ): CancelSaleResponse = Either.catch {
        activeSalesDao.update(sale.copy(active = false))
    }.mapLeft {
        ActiveSaleNotCanceled(description = "Error cancelling sale ${sale.id}")
    }

    override suspend fun findActiveSaleFor(
        collaboratorId: String,
    ): ActiveSalesResponse = Either.catch {
        var activeServiceOrder: ActiveSOEntity
        activeSalesDao.activeSalesFor(collaboratorId).map {
            activeServiceOrder = activeSODao.getServiceOrdersForSale(it.id).first()

            it.copy(clientName = activeServiceOrder.clientName)
        }
    }.mapLeft {
        Unexpected(description = "Error finding active sale for collaborator $collaboratorId")
    }

    override fun activeSalesStreamFor(
        collaboratorId: String,
    ): ActiveSalesStreamResponse = Either.catch {
        var activeServiceOrder: ActiveSOEntity
        activeSalesDao.activeSalesStreamFor(collaboratorId).map {
            it.map { sale ->
                activeServiceOrder = activeSODao.getServiceOrdersForSale(sale.id).first()

                sale.copy(clientName = activeServiceOrder.clientName)
            }
        }
    }.mapLeft {
        Unexpected(description = "Error finding active sale for collaborator $collaboratorId")
    }

    override suspend fun resumeSale(
        activeSale: ActiveSalesEntity,
    ): ResumeSaleResponse = Either.catch {
        val serviceOrders = activeSODao.getServiceOrdersForSale(activeSale.id)

        if (serviceOrders.isEmpty()) {
            error("No service order found for sale ${activeSale.id}")
        }

        val activeSO = serviceOrders.first()

        Timber.i("Resuming sale $activeSale")
        Timber.i("Resuming so $activeSO")

        salesApplication.dataStoreCurrentSale.edit { prefs ->
            prefs[currentSaleKey] = activeSale.id
            prefs[currentSOKey] = activeSO.id
        }

        Timber.i("Sale resumed")
    }.mapLeft {
        Unexpected(description = "Error resuming sale ${activeSale.id}", it)
    }

    override fun updateSO(so: ActiveSOEntity) {
        Timber.i("Updating sale to $so")
        activeSODao.update(so)
    }

    override fun activeSale(): Flow<ActiveSalesEntity?> {
        return currentSale
    }

    override suspend fun currentSale(): ActiveSaleResponse = Either.catch {
        salesApplication
            .dataStoreCurrentSale
            .data
            .map { it[currentSaleKey] }
            .first()
    }.leftIfNull {
        ActiveSaleNotRegistered(description = "No active sale registered on data source")
    }.map {
        activeSalesDao.getSaleById(it)
    }.leftIfNull {
        ActiveSaleNotFound(description = "No active sale found on database")
    }.mapLeft {
        when (it) {
            is ActiveSaleError -> {
                it
            }

            else -> {
                val error = if (it is Throwable) { it } else { null }

                Unexpected(
                    description = "Unexpected error while getting active sale: ${error?.message}",
                    error = error,
                )
            }
        }
    }

    override suspend fun serviceOrder(
        serviceOrderId: String,
    ): ActiveServiceOrderResponse = Either.catch {
        activeSODao.getServiceOrderById(serviceOrderId)
    }.mapLeft {
        Unexpected(
            description = "Error finding sale for service order $serviceOrderId",
            error = it,
        )
    }.leftIfNull {
        Unexpected(
            description = "Error finding sale for service order $serviceOrderId",
        )
    }

    override suspend fun saleForServiceOrder(serviceOrderId: String): ActiveSaleResponse = Either.catch {
        val serviceOrder = activeSODao.getServiceOrderById(serviceOrderId)

        serviceOrder?.let { activeSalesDao.getSaleById(serviceOrder.saleId) }
    }.mapLeft {
        Unexpected(
            description = "Error finding sale for service order $serviceOrderId",
            error = it,
        )
    }.leftIfNull {
        Unexpected(
            description = "Error finding sale for service order $serviceOrderId",
        )
    }

    override fun updateActiveSO(activeSOEntity: ActiveSOEntity) {
        activeSODao.update(activeSOEntity)
    }

    override fun activeSO(): Flow<ActiveSOEntity?> {
        return currentSO
    }

    override suspend fun currentServiceOrder(): ActiveServiceOrderResponse = Either.catch {
        salesApplication
            .dataStoreCurrentSale
            .data
            .map { it[currentSOKey] }
            .first()
    }.leftIfNull {
        ActiveServiceOrderNotRegistered(description = "No active service order registered on data source")
    }.map {
        activeSODao.getServiceOrderById(it)
    }.leftIfNull {
        ActiveServiceOrderNotFound(description = "No active sale found on database")
    }.mapLeft {
        when (it) {
            is ActiveServiceOrderError -> {
                it
            }

            else -> {
                val error = if (it is Throwable) { it } else { null }

                Unexpected(
                    description = "Unexpected error while getting active sale: ${error?.message}",
                    error = error,
                )
            }
        }
    }

    override fun currentFramesData(): Flow<FramesEntity> {
        return currentFramesData
    }

    override fun pickedProduct(): Flow<ProductPickedEntity?> {
        return emptyFlow()
    }

    override suspend fun productPicked(
        serviceOrderId: String,
    ): ProductPickedResponse = Either.catch {
        productPickedDao.getByServiceOrderId(serviceOrderId)
    }.map {
        it?.toProductPickedDocument()
    }.mapLeft {
        Unexpected(
            description = "Unexpected error while getting picked products: ${it.message}",
            error = it,
        )
    }.leftIfNull {
        ProductPickedNotFound(description = "No picked products found on database")
    }

    override fun payments(): Flow<List<SalePaymentEntity>> {
        return currentPayments
    }

    override fun currentPositioning(eye: Eye): Flow<PositioningEntity> {
        // TODO: possible null pointer exception
        return currentPositioningsByEye[eye]!!
    }

    override fun clientPicked(role: ClientRole): Flow<ClientPickedEntity?> {
        // TODO: possible null pointer exception
        return currentClientsByRole[role]!!
    }

    override fun updateFramesData(frames: FramesEntity) {
        framesDataDao.update(frames)
    }

    override suspend fun updateFrames(frames: FramesEntity) = Either.catch {
        framesDataDao.updateFrames(frames)
    }.fold(
        ifLeft = { Timber.i("Frames updated") },
        ifRight = { Timber.e("Error updating frames: $frames") }
    )

    override fun updatePositioning(positioning: PositioningEntity) {
        positioningDao.add(positioning)
    }

    override fun lensTypeCategories(): Flow<List<LensTypeCategoryDocument>> {
        return lensTypeCategoryDao.categories()
    }

    override fun pickProduct(productPicked: ProductPickedEntity) {
        productPickedDao.add(productPicked)
    }

    override fun pickClient(client: ClientPickedEntity) {
        Timber.i("Adding client $client")
        clientPickedDao.add(client)
    }

    override fun addPayment(payment: SalePaymentEntity): Long {
        return salePaymentDao.add(payment)
    }

    override fun updatePayment(payment: SalePaymentEntity) {
        salePaymentDao.update(payment)
    }

    override fun deletePayment(payment: SalePaymentEntity) {
        salePaymentDao.delete(payment)
    }

    override fun paymentById(paymentId: Long): Flow<SalePaymentEntity?> {
        return emptyFlow()
    }

    companion object {
        val currentSaleFileName =
            "com.peyess.salesapp.repository.sale.SaleRepositoryImpl__CurrnetSale"

        val currentSaleKey = stringPreferencesKey("current_sale")

        val currentSOKey = stringPreferencesKey("current_so")
    }
}