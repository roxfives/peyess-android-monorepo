package com.peyess.salesapp.workmanager.products

import android.app.PendingIntent
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.getOrElse
import arrow.core.left
import arrow.fx.coroutines.Schedule
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.model.lens.toFilterCategory
import com.peyess.salesapp.data.model.lens.toFilterLensFamily
import com.peyess.salesapp.data.model.lens.toFilterLensGroup
import com.peyess.salesapp.data.model.lens.toFilterLensMaterial
import com.peyess.salesapp.data.model.lens.toFilterLensSpecialty
import com.peyess.salesapp.data.model.lens.toFilterLensSupplier
import com.peyess.salesapp.data.model.lens.toFilterLensTech
import com.peyess.salesapp.data.model.lens.toFilterLensType
import com.peyess.salesapp.dao.products.room.join_lens_coloring.JoinLensColoringEntity
import com.peyess.salesapp.dao.products.room.join_lens_material.JoinLensMaterialEntity
import com.peyess.salesapp.dao.products.room.join_lens_tech.JoinLensTechEntity
import com.peyess.salesapp.dao.products.room.join_lens_treatment.JoinLensTreatmentEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.data.adapter.lenses.colorings.extractColoring
import com.peyess.salesapp.data.adapter.lenses.colorings.extractTreatment
import com.peyess.salesapp.data.adapter.lenses.extractCategory
import com.peyess.salesapp.data.adapter.lenses.extractDescription
import com.peyess.salesapp.data.adapter.lenses.extractFamily
import com.peyess.salesapp.data.adapter.lenses.extractGroup
import com.peyess.salesapp.data.adapter.lenses.extractMaterial
import com.peyess.salesapp.data.adapter.lenses.extractMaterialCategory
import com.peyess.salesapp.data.adapter.lenses.extractSpecialty
import com.peyess.salesapp.data.adapter.lenses.extractSupplier
import com.peyess.salesapp.data.adapter.lenses.extractTech
import com.peyess.salesapp.data.adapter.lenses.extractType
import com.peyess.salesapp.data.adapter.lenses.room.toLocalLensTypeCategoryEntity
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.coloring.StoreLensColoringDocument
import com.peyess.salesapp.data.model.lens.treatment.StoreLensTreatmentDocument
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus
import com.peyess.salesapp.data.repository.lenses.StoreLensResponse
import com.peyess.salesapp.data.repository.lenses.StoreLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.database.room.ProductsDatabase
import com.peyess.salesapp.firebase.FirebaseManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber


const val forceUpdateKey = "UpdateProductsWorker_forceUpdate"

@HiltWorker
class UpdateProductsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val salesApplication: SalesApplication,
    private val productsDatabase: ProductsDatabase,
    private val productsTableStateRepository: ProductsTableStateRepository,
    private val storeLensesRepository: StoreLensesRepository,
    private val firebaseManager: FirebaseManager,
    private val localLensesRepository: LocalLensesRepository,
): CoroutineWorker(context, workerParams) {

    private suspend fun populateDatabase(docs: List<StoreLensDocument>) {
        Timber.i("populateDatabase: Populating database " +
                "with ${docs.size} documents...")

        docs.forEach {
            populateLensData(it)
            populateColoringData(it.id, it.colorings)
            populateTreatmentData(it.id, it.treatments)
            populateAlternativeData(it.id, it.altHeights)
        }
    }

    private suspend fun populateLensData(lens: StoreLensDocument) {
        Timber.i("populateLens: Adding lens ${lens.id}...")

        val family = lens.extractFamily()
        val description = lens.extractDescription()
        val supplier = lens.extractSupplier()
        val group = lens.extractGroup()
        val specialty = lens.extractSpecialty()
        val tech = lens.extractTech()
        val type = lens.extractType()
        val category = lens.extractCategory()
        val material = lens.extractMaterial()
        val materialCategory = lens.extractMaterialCategory()

        localLensesRepository.addFamily(family)
        localLensesRepository.addDescription(description)
        localLensesRepository.addSupplier(supplier)
        localLensesRepository.addGroup(group)
        localLensesRepository.addSpecialty(specialty)
        localLensesRepository.addTech(tech)
        localLensesRepository.addCategory(category)

        localLensesRepository.addLensType(type)
        lens.typeCategories.forEach {
            localLensesRepository.addLensTypeCategory(it)

            localLensesRepository.addCategoryToType(
                categoryId = it.id,
                typeId = lens.typeId,
            )
        }

        localLensesRepository.addMaterialCategory(materialCategory)
        localLensesRepository.addMaterial(material)
        lens.materialTypes.forEach {
            localLensesRepository.addMaterialType(it)
            localLensesRepository.addTypeToMaterial(
                typeId = it.id,
                materialId = lens.materialId,
            )
        }

        localLensesRepository.addLens(lens)
    }

    private suspend fun populateColoringData(lensId: String, colorings: List<StoreLensColoringDocument>) {
        Timber.i("populateColoringData: Adding ${colorings.size} for lens $lensId...")

        colorings.forEach {
            Timber.i("populateColoringData: Adding coloring ${it.id}")

            localLensesRepository.addColoring(
                coloring = it.extractColoring(),
            )

            localLensesRepository.addColoringToLens(
                lensId = lensId,
                coloringId = it.id,
            )
        }
    }

    private suspend fun populateTreatmentData(lensId: String, treatments: List<StoreLensTreatmentDocument>) {
        Timber.i("populateTreatmentData: Adding ${treatments.size} for lens $lensId...")

        treatments.forEach {
            Timber.i("populateTreatmentData: Adding treatment ${it.id}")

            localLensesRepository.addTreatment(
                treatment = it.extractTreatment(),
            )

            localLensesRepository.addTreatmentToLens(
                lensId = lensId,
                treatmentId = it.id,
            )
        }
    }

    private suspend fun populateAlternativeData(
        lensId: String, alternatives: List<StoreLensAltHeightDocument>
    ) {
        Timber.i("populateAlternativeData: Adding ${alternatives.size} for lens $lensId...")

        alternatives.forEach {
            Timber.i("populateAlternativeData: Adding alternative ${it.id}")

            localLensesRepository.addAlternativeHeight(it)
            localLensesRepository.addAlternativeHeightToLens(
                lensId = lensId,
                alternativeHeightId = it.id,
            )
        }
    }

    private fun populateFilters(lens: FSStoreLocalLens) {
        Timber.i("Populating lens filters")

        try {
            val categoryFilter = lens.toFilterCategory()

            productsDatabase.filterLensCategoryEntity().add(categoryFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for category")
        }

        try {
            val groupFilter = lens.toFilterLensGroup()

            productsDatabase.filterLensGroupEntity().add(groupFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for group")
        }

        try {
            val materialFilter = lens.toFilterLensMaterial()

            productsDatabase.filterLensMaterialDao().add(materialFilter)
            productsDatabase.joinLensMaterialDao().add(
                JoinLensMaterialEntity(
                    lensId = lens.id,
                    materialId = materialFilter.id,
                )
            )
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for material")
        }

        try {
            val specialtyFilter = lens.toFilterLensSpecialty()

            productsDatabase.filterLensSpecialtyDao().add(specialtyFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for specialty")
        }

        try {
            val supplierFilter = lens.toFilterLensSupplier()

            productsDatabase.filterLensSupplierDao().add(supplierFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for supplier")
        }

        try {
            val techFilter = lens.toFilterLensTech()

            productsDatabase.filterLensTechDao().add(techFilter)
            productsDatabase.joinLensTechDao().add(
                JoinLensTechEntity(
                    lensId = lens.id,
                    techId = techFilter.id,
                )
            )
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for tech")
        }

        try {
            val type = lens.toFilterLensType()

            productsDatabase.filterLensTypeDao().add(type)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for ")
        }

        try {
            val family = lens.toFilterLensFamily()

            productsDatabase.filterLensFamilyDao().add(family)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for ")
        }
    }

    private fun populateTreatments(lens: FSStoreLocalLens) {
        lens.treatments.forEach { (id, fsTreatment) ->

            productsDatabase.localTreatmentDao().add(
                LocalTreatmentEntity(
//                    id = id,
//                    specialty = fsTreatment.specialty,
//                    isColoringRequired = fsTreatment.isColoringRequired,
//                    priority = fsTreatment.priority,
//                    isGeneric = fsTreatment.isGeneric,
//                    cost = fsTreatment.cost,
//                    price = fsTreatment.price,
//                    suggestedPrice = fsTreatment.suggestedPrice,
//                    shippingTime = fsTreatment.shippingTime,
//                    observation = fsTreatment.observation,
//                    warning = fsTreatment.warning,
//                    brand = fsTreatment.brand,
//                    brandId = fsTreatment.brandId,
//                    design = fsTreatment.design,
//                    designId = fsTreatment.designId,
//                    supplierPicture = fsTreatment.supplierPicture,
//                    supplier = fsTreatment.supplier,
//                    supplierId = fsTreatment.supplierId,
//                    isManufacturingLocal = fsTreatment.isManufacturingLocal,
//                    isEnabled = fsTreatment.isEnabled,
//                    reasonDisabled = fsTreatment.reasonDisabled,
                )
            )

            productsDatabase.joinLensTreatmentDao().add(
                JoinLensTreatmentEntity(
                    lensId = lens.id,
                    treatmentId = id
                )
            )
        }
    }

    private fun populateColoringData(lens: FSStoreLocalLens) {
        lens.colorings.forEach { (id, fsColoring) ->

            productsDatabase.localColoringDao().add(
                LocalColoringEntity(
//                    id = id,
//                    specialty = fsColoring.specialty,
//                    isColoringRequired = fsColoring.isColoringRequired,
//                    priority = fsColoring.priority,
//                    isGeneric = fsColoring.isGeneric,
//                    cost = fsColoring.cost,
//                    price = fsColoring.price,
//                    suggestedPrice = fsColoring.suggestedPrice,
//                    shippingTime = fsColoring.shippingTime,
//                    observation = fsColoring.observation,
//                    warning = fsColoring.warning,
//                    brand = fsColoring.brand,
//                    brandId = fsColoring.brandId,
//                    design = fsColoring.design,
//                    designId = fsColoring.designId,
//                    supplierPicture = fsColoring.supplierPicture,
//                    supplier = fsColoring.supplier,
//                    supplierId = fsColoring.supplierId,
//                    isManufacturingLocal = fsColoring.isManufacturingLocal,
//                    isEnabled = fsColoring.isEnabled,
//                    reasonDisabled = fsColoring.reasonDisabled,
                )
            )

            productsDatabase.joinLensColoringDao().add(
                JoinLensColoringEntity(
                    lensId = lens.id,
                    coloringId = id,
                )
            )
        }
    }

    private fun clearAllProducts() {
        Timber.i("Clearing all current local products")
        // TODO: fix foreign key problem
        productsDatabase.clearAllTables()
    }

    private fun createNotification() {
        val context = salesApplication as Context
        val packageManager = context.packageManager

        val launchIntent = packageManager.getLaunchIntentForPackage(BuildConfig.APPLICATION_ID)
        val pendingIntent = PendingIntent
            .getActivity(context, 0, launchIntent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(context, "products_table_channel_id")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(salesApplication.stringResource(R.string.notification_title_update_products_table))
            .setContentText(salesApplication.stringResource(R.string.notification_description_update_products_table))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(false)
            .setOngoing(true)
            .setProgress(0, 100, true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, builder.build())
        }
    }

    private fun dismissNotification() {
        val context = salesApplication as Context

        with(NotificationManagerCompat.from(context)) {
            cancel(0)
        }
    }

    private suspend fun downloadAndPopulateProducts(query: PeyessQuery) =
        Schedule.doWhile<StoreLensResponse> { it.getOrElse { emptyList() }.isNotEmpty() }
            .repeat {
                storeLensesRepository.paginateData(
                    query = query,
                    config = SimplePaginatorConfig(
                        initialPageSize = pageLimit,
                        pageSize = pageLimit,
                    ),
                ).tap {
                    Timber.i("Downloaded ${it.size} lenses: $it")

                    populateDatabase(it)
                }
            }

    override suspend fun doWork(): Result {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            Timber.e("Firestore instance is null")
            productsTableStateRepository
                .update(
                    ProductsTableStatus(
                        hasUpdated = false,
                        hasUpdateFailed = true,
                        isUpdating = false,
                    )
                )

            return Result.retry()
        }

        val storeId = firebaseManager.currentStore!!.uid
        val lensPath = salesApplication
            .stringResource(id = R.string.fs_col_lenses)
            .format(storeId)

        Timber.i("Starting product update")
        val result = runBlocking(Dispatchers.IO) {
            val forceUpdate = inputData.getBoolean(forceUpdateKey, false)
            Timber.i("Starting Worker with forceUpdate = $forceUpdate")

            val productsTableStatus = productsTableStateRepository.getCurrentState()

            val isUpdating = productsTableStatus.isUpdating
            val needsUpdate = !productsTableStatus.hasUpdated
                    || productsTableStatus.hasUpdateFailed
            val avoidUpdate = isUpdating || (!forceUpdate && !needsUpdate)

            if (false) {
                return@runBlocking Result.success()
            }

            productsTableStateRepository
                .update(productsTableStatus.copy(isUpdating = true))

            createNotification()

            Timber.i("Starting products update with path $lensPath")
            val queryFields = storeLensesRepository.queryFields

            val query = PeyessQuery(
                queryFields = listOf(
                    buildQueryField(
                        field = queryFields.isLocalEnabled,
                        op = PeyessQueryOperation.Equal,
                        value = true,
                    )
                ),
                orderBy = PeyessOrderBy(
                    field = queryFields.priority,
                    order = Order.ASCENDING,
                )
            )

            val response = downloadAndPopulateProducts(query)

            if (response.isLeft()) {
                Timber.w("Failed to update products table: ${response.left()}")
                return@runBlocking Result.retry()
            } else {
                return@runBlocking Result.success()
            }
        }

        return result

//        runBlocking(Dispatchers.IO) {
//            val forceUpdate = inputData
//                .getBoolean(forceUpdateKey, false)
//            Timber.i("Starting Worker with forceUpdate = $forceUpdate")
//
//            val productsTableStatus = productsTableStateRepository.getCurrentState()
//
//            val isUpdating = productsTableStatus.isUpdating
//            val needsUpdate = !productsTableStatus.hasUpdated
//                    || productsTableStatus.hasUpdateFailed
//
//            val avoidUpdate = isUpdating || (!forceUpdate && !needsUpdate)
//
//            if (false) {
//                return@runBlocking Result.success()
//            }
//
//            productsTableStateRepository.update(
//                productsTableStatus.copy(isUpdating = true),
//            )
//
//            createNotification()
//
//            Timber.i("Starting products update with path $lensPath")
//
//            // TODO: use string resource
//            // TODO: update enabled to is_local_enabled
//            val initQuery = firestore
//                .collection(lensPath)
//                .whereEqualTo("is_local_enabled", true)
//                .orderBy("priority")
//                .limit(20)
//                .get()
//                .await()

//            if (initQuery.size() != 1) {
//                Timber.e("Initial query has to be of size 1, but it is ${initQuery.size()}")
//                productsTableStateRepository
//                    .update(
//                        ProductsTableStatus(
//                            hasUpdated = false,
//                            hasUpdateFailed = true,
//                            isUpdating = false,
//                        )
//                    )
//
//                return@runBlocking Result.retry()
//            }

//            clearAllProducts()
//
//            var snaps: QuerySnapshot
//            var currSnapshot = initQuery.documents.last()
//            var totalDownloaded = 0
//            do {
//                Timber.i("Starting page download")
//                // TODO: use string resource
//                // TODO: update enabled to is_local_enabled
//                snaps = firestore
//                    .collection(lensPath)
//                    .whereEqualTo("is_local_enabled", true)
//                    .orderBy("priority")
//                    .startAfter(currSnapshot)
//                    .limit(20)
//                    .get()
//                    .await()
//                Timber.i("Downloaded total of ${snaps.size()}")
//
//                if (snaps.isEmpty) {
//                    currSnapshot = null
//                } else {
//                    totalDownloaded += snaps.size()
//                    currSnapshot = snaps.last()
//                    addAllToLocalProducts(snaps.documents)
//                }
//            } while (!snaps.isEmpty)
//
//            Timber.i("Downloaded total of $totalDownloaded")
//            productsTableStateRepository
//                .update(
//                    ProductsTableStatus(
//                        hasUpdated = true,
//                        hasUpdateFailed = false,
//                        isUpdating = false,
//                    )
//                )
//            dismissNotification()
//        }
//        Timber.i("Finished product update")
//
//        return Result.success()
    }

    companion object {
        const val pageLimit = 300

        const val workerTag = "TAG_UpdateProductsWorker"
    }
}