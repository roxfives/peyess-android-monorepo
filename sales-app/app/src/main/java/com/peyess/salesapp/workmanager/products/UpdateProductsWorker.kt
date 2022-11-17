package com.peyess.salesapp.workmanager.products

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
import com.peyess.salesapp.app.MainActivity
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.products.firestore.lens.FSLocalLens
import com.peyess.salesapp.dao.products.firestore.lens.getExplanations
import com.peyess.salesapp.dao.products.firestore.lens.toFilterCategory
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensFamily
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensGroup
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensMaterial
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensSpecialty
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensSupplier
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensTech
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensType
import com.peyess.salesapp.dao.products.firestore.lens.toLocalLensEntity
import com.peyess.salesapp.dao.products.room.join_lens_coloring.JoinLensColoringEntity
import com.peyess.salesapp.dao.products.room.join_lens_material.JoinLensMaterialEntity
import com.peyess.salesapp.dao.products.room.join_lens_tech.JoinLensTechEntity
import com.peyess.salesapp.dao.products.room.join_lens_treatment.JoinLensTreatmentEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.data.model.products_table_state.ProductsTableStatus
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.database.room.ProductsDatabase
import com.peyess.salesapp.firebase.FirebaseManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber


const val forceUpdateKey = "UpdateProductsWorker_forceUpdate"

@HiltWorker
class UpdateProductsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val salesApplication: SalesApplication,
    val productsDatabase: ProductsDatabase,
    val productsTableStateRepository: ProductsTableStateRepository,
    val firebaseManager: FirebaseManager,
): CoroutineWorker(context, workerParams) {

    private fun addAllToLocalProducts(docs: List<DocumentSnapshot>) {
        val lenses = docs.mapNotNull {
            try {
                it.toObject(FSLocalLens::class.java)
            } catch (err: Throwable) {
                Timber.e(err, "Failed for lens ${it.id}")
                null
            }
        }

        Timber.i("Found ${lenses.size} lenses")

        lenses.forEach {
            Timber.i("Got lens ${it.id}")

            val lens = it.toLocalLensEntity()
            Timber.i("Adding lens with price ${lens.price}")
            try {
                productsDatabase.localLensDao().add(lens)
            } catch (e: Throwable) {
                Timber.e("Error while inserting lens ${lens.id}: \n\t $lens")
            }

            try {
                val explanations = it.getExplanations()

                for (exp in explanations) {
                    Timber.i("Adding product exp dao for lens ${it.id}: $exp")
                    productsDatabase.localProdExpDao().add(exp)
                    Timber.i("Successfully added exp dao for lens ${it.id}: $exp")

                }
            } catch (e: SQLiteConstraintException) {
                // Just ignore this error, collisions will happen
                Timber.e(e, "Error while inserting explanation")
            } catch (e: Throwable) {
                Timber.e(e, "Error while inserting explanation")
            }

            populateFilters(it)
            populateTreatments(it)
            populateColorings(it)

            Timber.i("With disps ${it.disponibilities}")
            it.disponibilities.forEach { disp ->
                productsDatabase.localLensDispEntityDao().add(
                    LocalLensDispEntity(
                        lensId = it.id,
                        diam = disp.diam,
                        maxCyl = disp.maxCyl,
                        minCyl = disp.minCyl,
                        maxSph = disp.maxSph,
                        minSph = disp.minSph,
                        maxAdd = disp.maxAdd,
                        minAdd = disp.minAdd,
                        hasPrism = disp.hasPrism,
                        prism = disp.prism,
                        prismPrice = disp.prismPrice,
                        prismCost = disp.prismCost,
                        separatePrism = disp.separatePrism,
                        needsCheck = disp.needsCheck,
                        sumRule = disp.sumRule,
                    )
                )
            }
        }
    }

    private fun populateFilters(lens: FSLocalLens) {
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

    private fun populateTreatments(lens: FSLocalLens) {
        lens.treatments.forEach { (id, fsTreatment) ->

            productsDatabase.localTreatmentDao().add(
                LocalTreatmentEntity(
                    id = id,
                    specialty = fsTreatment.specialty,
                    isColoringRequired = fsTreatment.isColoringRequired,
                    priority = fsTreatment.priority,
                    isGeneric = fsTreatment.isGeneric,
                    cost = fsTreatment.cost,
                    price = fsTreatment.price,
                    suggestedPrice = fsTreatment.suggestedPrice,
                    shippingTime = fsTreatment.shippingTime,
                    observation = fsTreatment.observation,
                    warning = fsTreatment.warning,
                    brand = fsTreatment.brand,
                    brandId = fsTreatment.brandId,
                    design = fsTreatment.design,
                    designId = fsTreatment.designId,
                    supplierPicture = fsTreatment.supplierPicture,
                    supplier = fsTreatment.supplier,
                    supplierId = fsTreatment.supplierId,
                    isManufacturingLocal = fsTreatment.isManufacturingLocal,
                    isEnabled = fsTreatment.isEnabled,
                    reasonDisabled = fsTreatment.reasonDisabled,
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

    private fun populateColorings(lens: FSLocalLens) {
        lens.colorings.forEach { (id, fsColoring) ->

            productsDatabase.localColoringDao().add(
                LocalColoringEntity(
                    id = id,
                    specialty = fsColoring.specialty,
                    isColoringRequired = fsColoring.isColoringRequired,
                    priority = fsColoring.priority,
                    isGeneric = fsColoring.isGeneric,
                    cost = fsColoring.cost,
                    price = fsColoring.price,
                    suggestedPrice = fsColoring.suggestedPrice,
                    shippingTime = fsColoring.shippingTime,
                    observation = fsColoring.observation,
                    warning = fsColoring.warning,
                    brand = fsColoring.brand,
                    brandId = fsColoring.brandId,
                    design = fsColoring.design,
                    designId = fsColoring.designId,
                    supplierPicture = fsColoring.supplierPicture,
                    supplier = fsColoring.supplier,
                    supplierId = fsColoring.supplierId,
                    isManufacturingLocal = fsColoring.isManufacturingLocal,
                    isEnabled = fsColoring.isEnabled,
                    reasonDisabled = fsColoring.reasonDisabled,
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

    override suspend fun doWork(): Result {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
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
        runBlocking(Dispatchers.IO) {
            val forceUpdate = inputData
                .getBoolean(forceUpdateKey, false)
            Timber.i("Starting Worker with forceUpdate = $forceUpdate")

            val productsTableStatus = productsTableStateRepository.getCurrentState()

            val isUpdating = productsTableStatus.isUpdating
            val needsUpdate = !productsTableStatus.hasUpdated
                    || productsTableStatus.hasUpdateFailed

            val avoidUpdate = isUpdating || (!forceUpdate && !needsUpdate)

            if (avoidUpdate) {
                return@runBlocking Result.success()
            }

            productsTableStateRepository.update(
                productsTableStatus.copy(isUpdating = true),
            )

            createNotification()

            Timber.i("Starting products update with path $lensPath")

            // TODO: use string resource
            // TODO: update enabled to is_local_enabled
            val initQuery = firestore
                .collection(lensPath)
                .whereEqualTo("is_enabled", true)
                .orderBy("priority")
                .limit(1)
                .get()
                .await()

            if (initQuery.size() != 1) {
                Timber.e("Initial query has to be of size 1, but it is ${initQuery.size()}")
                productsTableStateRepository
                    .update(
                        ProductsTableStatus(
                            hasUpdated = false,
                            hasUpdateFailed = true,
                            isUpdating = false,
                        )
                    )

                return@runBlocking Result.retry()
            }

            clearAllProducts()

            var snaps: QuerySnapshot
            var currSnapshot = initQuery.documents[0]
            var totalDownloaded = 0
            do {
                Timber.i("Starting page download")
                // TODO: use string resource
                // TODO: update enabled to is_local_enabled
                snaps = firestore
                    .collection(lensPath)
                    .whereEqualTo("is_enabled", true)
                    .orderBy("priority")
                    .startAfter(currSnapshot)
                    .limit(pageLimit)
                    .get()
                    .await()
                Timber.i("Downloaded total of ${snaps.size()}")

                if (snaps.isEmpty) {
                    currSnapshot = null
                } else {
                    totalDownloaded += snaps.size()
                    currSnapshot = snaps.last()
                    addAllToLocalProducts(snaps.documents)
                }
            } while (!snaps.isEmpty)

            Timber.i("Downloaded total of $totalDownloaded")
            productsTableStateRepository
                .update(
                    ProductsTableStatus(
                        hasUpdated = true,
                        hasUpdateFailed = false,
                        isUpdating = false,
                    )
                )
            dismissNotification()
        }
        Timber.i("Finished product update")

        return Result.success()
    }

    companion object {
        const val pageLimit = 100L

        const val workerTag = "TAG_UpdateProductsWorker"
    }
}