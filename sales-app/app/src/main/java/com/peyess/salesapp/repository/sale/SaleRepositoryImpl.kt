package com.peyess.salesapp.repository.sale

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.leftIfNull
import arrow.core.right
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.dao.local_sale.client_picked.ClientPickedDao
import com.peyess.salesapp.typing.sale.ClientRole
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.typing.lens.LensTypeCategoryName
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.data.dao.local_sale.positioning.PositioningDao
import com.peyess.salesapp.data.model.local_sale.positioning.PositioningEntity
import com.peyess.salesapp.data.model.local_sale.positioning.updateInitialPositioningState
import com.peyess.salesapp.data.dao.local_sale.payment.LocalPaymentDao
import com.peyess.salesapp.data.model.local_sale.payment.LocalPaymentEntity
import com.peyess.salesapp.data.dao.local_sale.local_prescription.LocalPrescriptionDao
import com.peyess.salesapp.data.dao.local_sale.local_prescription.PrescriptionEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedDao
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.data.model.local_sale.client_picked.ClientPickedEntity
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import com.peyess.salesapp.repository.sale.adapter.toProductPickedDocument
import com.peyess.salesapp.repository.sale.adapter.toProductPickedEntity
import com.peyess.salesapp.repository.sale.error.ActiveSaleError
import com.peyess.salesapp.repository.sale.error.ActiveSaleNotCanceled
import com.peyess.salesapp.repository.sale.error.ActiveSaleNotFound
import com.peyess.salesapp.repository.sale.error.ActiveSaleNotRegistered
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderError
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderNotFound
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderNotRegistered
import com.peyess.salesapp.repository.sale.error.ActiveServiceOrderUpdateError
import com.peyess.salesapp.repository.sale.error.CreateSaleError
import com.peyess.salesapp.repository.sale.error.DeactivateSaleError
import com.peyess.salesapp.repository.sale.error.ProductPickedNotFound
import com.peyess.salesapp.repository.sale.error.Unexpected
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
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
    private val localPaymentDao: LocalPaymentDao,
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

                activeSODao.streamServiceOrderById(soId ?: "")
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
                framesDataDao.streamFramesForServiceOrder(so.id).map {
                    it ?: FramesEntity(soId = so.id)
                }
            }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
    }

    private val currentPayments by lazy { emptyFlow<List<LocalPaymentEntity>>() }

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

    override suspend fun deactivateSales(): DeactivateSaleResponse = Either.catch {
        activeSalesDao.deactivateAllSales()
    }.mapLeft {
        DeactivateSaleError(
            description = "Error deactivating sales",
            error = it,
        )
    }

    override suspend fun createSale(): CreateSaleResponse = Either.catch {
        val collaboratorId = authenticationRepository.fetchCurrentUserId()
        val sale = ActiveSalesEntity(
            id = firebaseManager.uniqueId(),
            collaboratorUid = collaboratorId,
            active = true,
        )
        val serviceOrder = ActiveSOEntity(
            id = firebaseManager.uniqueId(),
            saleId = sale.id,
        )

        salesApplication.dataStoreCurrentSale.edit { prefs ->
            prefs[currentSaleKey] = sale.id
            prefs[currentSOKey] = serviceOrder.id
        }


        activeSalesDao.add(sale)
        activeSODao.add(serviceOrder)

        Pair(sale.id, serviceOrder.id)
    }.mapLeft {
        CreateSaleError(
            description = "Error creating sale",
            error = it,
        )
    }

    override suspend fun cancelCurrentSale(): CancelSaleResponse {
        return currentSale().flatMap {
            val canceled = it.copy(active = false, finished = true, canceled = true)

            try {
                activeSalesDao.update(canceled).right()
            } catch (e: Throwable) {
                ActiveSaleNotCanceled(
                    description = "Error cancelling sale ${it.id}"
                ).left()
            }
        }
    }

    override suspend fun cancelSale(saleId: String): CancelSaleResponse = Either.catch {
        activeSalesDao.updateSaleStatus(saleId, 0, 1, 1)
    }.mapLeft {
        ActiveSaleNotCanceled(description = "Error cancelling sale $saleId")
    }

    override suspend fun findActiveSaleFor(
        collaboratorId: String,
    ): ActiveSalesResponse = Either.catch {
        var activeServiceOrder: ActiveSOEntity
        activeSalesDao.activeSalesFor(collaboratorId).map {
            activeServiceOrder = activeSODao.getServiceOrdersForSale(it.id).first()

//            it.copy(clientName = activeServiceOrder.clientName)
            it
        }
    }.mapLeft {
        Unexpected(description = "Error finding active sale for collaborator $collaboratorId")
    }

    override fun unfinishedSalesStreamFor(
        collaboratorId: String,
    ): ActiveSalesStreamResponse = Either.catch {
        activeSODao.streamUnfinishedServiceOrdersForUser(collaboratorId)
    }.mapLeft {
        Unexpected(description = "Error finding active sale for collaborator $collaboratorId")
    }

    override suspend fun resumeSale(
        saleId: String,
        serviceOrderId: String,
    ): ResumeSaleResponse = Either.catch {
        Timber.i("Resuming sale $saleId")
        Timber.i("Resuming so $serviceOrderId")

        salesApplication.dataStoreCurrentSale.edit { prefs ->
            prefs[currentSaleKey] = saleId
            prefs[currentSOKey] = serviceOrderId
        }

        Timber.i("Sale resumed")
    }.mapLeft {
        Unexpected(description = "Error resuming sale $saleId", it)
    }

    override fun updateSO(so: ActiveSOEntity) {
        Timber.i("Updating sale to $so")
        activeSODao.update(so)
    }

    override suspend fun updateClientName(
        serviceOrderId: String,
        name: String
    ): ServiceOrderUpdateResponse = Either.catch {
        activeSODao.updateClientName(serviceOrderId, name)
    }.mapLeft {
        ActiveServiceOrderUpdateError(
            description = "Error updating client name for service order $serviceOrderId",
            error = it,
        )
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

    override fun streamServiceOrder(serviceOrderId: String): ActiveServiceOrderStreamResponse {
        return activeSODao.streamServiceOrderById(serviceOrderId).map {
            if (it == null) {
                Unexpected(
                    description = "Error finding sale for service order $serviceOrderId",
                ).left()
            } else {
                it.right()
            }
        }
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

    override fun payments(): Flow<List<LocalPaymentEntity>> {
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

    override suspend fun updateFrames(frames: FramesEntity) = Either.catch {
        framesDataDao.updateFrames(frames)
    }.fold(
        ifLeft = { Timber.i("Frames updated") },
        ifRight = { Timber.e("Error updating frames: $frames") }
    )

    override fun updatePositioning(positioning: PositioningEntity) {
        positioningDao.add(positioning)
    }

    override suspend fun lensTypeCategories(): LensTypeCategoriesResponse = either {
        lensTypeCategoryDao.typeCategories().mapLeft {
            Unexpected(
                description = it.description,
                error = it.error,
            )
        }.bind()
    }

    override suspend fun pickProduct(productPicked: ProductPickedDocument) {
        productPickedDao.add(productPicked.toProductPickedEntity())
    }

    override fun pickProduct(productPicked: ProductPickedEntity) {
        productPickedDao.add(productPicked)
    }

    override fun pickClient(client: ClientPickedEntity) {
        Timber.i("Adding client $client")
        clientPickedDao.add(client)
    }

    override fun addPayment(payment: LocalPaymentEntity): Long {
        return localPaymentDao.add(payment)
    }

    override fun updatePayment(payment: LocalPaymentEntity) {
        localPaymentDao.update(payment)
    }

    override fun deletePayment(payment: LocalPaymentEntity) {
        localPaymentDao.delete(payment)
    }

    override fun paymentById(paymentId: Long): Flow<LocalPaymentEntity?> {
        return emptyFlow()
    }

    companion object {
        val currentSaleFileName =
            "com.peyess.salesapp.repository.sale.SaleRepositoryImpl__CurrnetSale"

        val currentSaleKey = stringPreferencesKey("current_sale")

        val currentSOKey = stringPreferencesKey("current_so")
    }
}