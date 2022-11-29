package com.peyess.salesapp.repository.sale

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.client.room.ClientEntity
import com.peyess.salesapp.dao.client.room.ClientPickedDao
import com.peyess.salesapp.dao.client.room.ClientRole
import com.peyess.salesapp.dao.products.firestore.lens_categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesDao
import com.peyess.salesapp.dao.sale.active_sale.ActiveSalesEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSODao
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.active_so.LensTypeCategoryName
import com.peyess.salesapp.dao.sale.frames.FramesDataDao
import com.peyess.salesapp.dao.sale.frames.FramesEntity
import com.peyess.salesapp.dao.sale.frames_measure.PositioningDao
import com.peyess.salesapp.dao.sale.frames_measure.PositioningEntity
import com.peyess.salesapp.dao.sale.frames_measure.updateInitialPositioningState
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonDao
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.dao.sale.payment.SalePaymentDao
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataDao
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureDao
import com.peyess.salesapp.dao.sale.prescription_picture.PrescriptionPictureEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedDao
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.products.LensTypeCategory
import com.peyess.salesapp.repository.auth.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.take
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
    private val prescriptionPictureDao: PrescriptionPictureDao,
    private val prescriptionDataDao: PrescriptionDataDao,
    private val framesDataDao: FramesDataDao,
    private val positioningDao: PositioningDao,
    private val comparisonDao: LensComparisonDao,
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
                val saleId = prefs[currentSOKey]

                activeSODao.getById(saleId ?: "")
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
                    it ?: PrescriptionPictureEntity(soId = so.id)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentComparisons by lazy {
        currentSO
            .filterNotNull()
            .flatMapLatest {
                comparisonDao.getBySo(it.id)
            }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentPrescriptionData by lazy {
        currentSO
            .filterNotNull()
            .flatMapLatest { so ->
                prescriptionDataDao.getById(so.id).map {
                    Timber.i("Emitting prescription data")
                    it ?: PrescriptionDataEntity(soId = so.id)
            }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentPickedProducts by lazy {
        currentSO
            .filterNotNull()
            .flatMapLatest { productPickedDao.getById(it.id) }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentPayments by lazy {
        currentSO
            .filterNotNull()
            .flatMapLatest { salePaymentDao.getBySO(it.id) }.shareIn(
                scope = repositoryScope,
                replay = 1,
                started = SharingStarted.WhileSubscribed(),
            )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val currentPositioningsByEye by lazy {
        val eyes = Eye.allOptions

        eyes.map { eye ->
            eye to currentSO
                .filterNotNull()
                .flatMapLatest { so ->
                    positioningDao.getById(so.id, eye).map { entity ->
                        entity ?: PositioningEntity(soId = so.id, eye = eye)
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

    override fun updateSO(so: ActiveSOEntity) {
        Timber.i("Updating sale to $so")
        activeSODao.update(so)
    }

    override fun activeSale(): Flow<ActiveSalesEntity?> {
        return currentSale
    }

    override fun updateActiveSO(activeSOEntity: ActiveSOEntity) {
        activeSODao.update(activeSOEntity)
    }

    override fun activeSO(): Flow<ActiveSOEntity?> {
        return currentSO
    }

    override fun currentPrescriptionPicture(): Flow<PrescriptionPictureEntity> {
        return currentPrescriptionPicture
    }

    override fun currentPrescriptionData(): Flow<PrescriptionDataEntity> {
        return currentPrescriptionData
    }

    override fun currentFramesData(): Flow<FramesEntity> {
        return currentFramesData
    }

    override fun comparisons(): Flow<List<LensComparisonEntity>> {
        return currentComparisons
    }

    override fun pickedProduct(): Flow<ProductPickedEntity?> {
        return currentPickedProducts
    }

    override fun payments(): Flow<List<SalePaymentEntity>> {
        return currentPayments
    }

    override fun currentPositioning(eye: Eye): Flow<PositioningEntity> {
        return currentPositioningsByEye[eye]!!

//        activeSO().filterNotNull().flatMapLatest { so ->
//            positioningDao.getById(so.id, eye).map {
//                it ?: PositioningEntity(soId = so.id, eye = eye)
//                    .updateInitialPositioningState()
//            }
//        }
    }

    override fun clientPicked(role: ClientRole): Flow<ClientEntity?> {
        return currentClientsByRole[role]!!
    }

    override fun updateFramesData(frames: FramesEntity) {
        framesDataDao.update(frames)
    }

    override suspend fun clearProductComparison() {
        activeSO()
            .filterNotNull()
            .take(1)
            .collect {
                comparisonDao.deleteAllFromSO(it.id)
            }
    }

    override fun removeComparison(id: Int) {
        comparisonDao.deleteById(id)
    }

    override fun updatePositioning(positioning: PositioningEntity) {
        positioningDao.add(positioning)
    }

    override fun updatePrescriptionData(prescriptionDataEntity: PrescriptionDataEntity) {
        prescriptionDataDao.add(prescriptionDataEntity)
    }

    override fun updatePrescriptionPicture(prescriptionPictureEntity: PrescriptionPictureEntity) {
        prescriptionPictureDao.add(prescriptionPictureEntity)
    }

    override fun lensTypeCategories(): Flow<List<LensTypeCategory>> {
        return lensTypeCategoryDao.categories()
    }

    override fun addLensForComparison(comparisonEntity: LensComparisonEntity) {
        comparisonDao.add(comparisonEntity)
    }

    override fun updateSaleComparison(comparison: LensComparisonEntity) {
        comparisonDao.update(comparison)
    }

    override fun pickProduct(productPicked: ProductPickedEntity) {
        productPickedDao.add(productPicked)
    }

    override fun pickClient(client: ClientEntity) {
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
        return salePaymentDao.getById(paymentId)
    }

    companion object {
        val currentSaleFileName =
            "com.peyess.salesapp.repository.sale.SaleRepositoryImpl__CurrnetSale"

        val currentSaleKey = stringPreferencesKey("current_sale")

        val currentSOKey = stringPreferencesKey("current_so")
    }
}