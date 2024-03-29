package com.peyess.salesapp.workmanager.products

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.getOrElse
import arrow.core.left
import arrow.core.right
import arrow.fx.coroutines.Schedule
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
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
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.model.lens.alt_height.StoreLensAltHeightDocument
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDao
import com.peyess.salesapp.data.model.lens.coloring.StoreLensColoringDocument
import com.peyess.salesapp.data.model.lens.treatment.StoreLensTreatmentDocument
import com.peyess.salesapp.data.repository.internal.firestore.errors.RepositoryError
import com.peyess.salesapp.data.repository.lenses.StoreLensResponse
import com.peyess.salesapp.data.repository.lenses.StoreLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.products_table_state.ProductsTableStateRepository
import com.peyess.salesapp.data.utils.query.PeyessOrderBy
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.data.utils.query.PeyessQueryOperation
import com.peyess.salesapp.data.utils.query.buildQueryField
import com.peyess.salesapp.data.utils.query.types.Order
import com.peyess.salesapp.data.room.database.ProductsDatabase
import com.peyess.salesapp.repository.stats.ProductStatsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.math.RoundingMode

const val forceUpdateKey = "UpdateProductsWorker_forceUpdate"

const val notificationId = 0

@HiltWorker
class UpdateProductsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val salesApplication: SalesApplication,
    private val productsDatabase: ProductsDatabase,
    private val productsTableStateRepository: ProductsTableStateRepository,
    private val storeLensesRepository: StoreLensesRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val productStatsRepository: ProductStatsRepository,
    // TODO: Create specific repository and remove this dependency
    private val lensTypeDao: LensTypeCategoryDao,
): CoroutineWorker(context, workerParams) {

    private suspend fun populateDatabase(docs: List<StoreLensDocument>) {
        Timber.i("populateDatabase: Populating database " +
                "with ${docs.size} documents...")

        docs.forEach {
            Timber.i("populateDatabase: Adding lens ${it.id}...", it)
            populateLensData(it)

            Timber.i("populateDatabase: Adding coloring data for lens ${it.id}...")
            populateColoringData(it.id, it.colorings)
            addColoringsToLens(it.id, it.colorings)

            Timber.i("populateDatabase: Adding treatment data for lens ${it.id}...")
            populateTreatmentData(it.id, it.treatments)
            addTreatmentsToLens(it.id, it.treatments)

            Timber.i("populateDatabase: Adding alt height data for lens ${it.id}...")
            populateAlternativeData(it.id, it.altHeights)
            addAlternativeHeightsToLens(it.id, it.altHeights)
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
        lens.typeCategories.forEach { lensTypeCategory ->
            localLensesRepository.lensTypeCategoryById(lensTypeCategory.id).fold(
                ifLeft = {
                    Timber.i("populateLensData: Adding category $lensTypeCategory... ")
                    lensTypeDao.typeCategoryById(lensTypeCategory.id).fold(
                        ifLeft = {
                            Timber.i("populateLensData: Failed while fetching category " +
                                    "${lensTypeCategory.id} from remove database... ")
                        },

                        ifRight = {
                            Timber.i("populateLensData: Using category from... ")
                            localLensesRepository.addLensTypeCategory(
                                lensTypeCategory.copy(explanation = it.explanation)
                            )
                        }
                    )
                },

                ifRight = {
                    Timber.i("populateLensData: Category $lensTypeCategory already exists... ")
                }
            )

            localLensesRepository.addCategoryToType(
                categoryId = lensTypeCategory.id,
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
        }
    }

    private suspend fun addColoringsToLens(lensId: String, colorings: List<StoreLensColoringDocument>) {
        Timber.i("addColoringsToLens: Adding ${colorings.size} for lens $lensId...")

        colorings.forEach {
            Timber.i("addColoringsToLens: Adding coloring ${it.id}")

            localLensesRepository.addColoringToLens(
                lensId = lensId,
                coloringId = it.id,
                price = it.price.setScale(2, RoundingMode.HALF_EVEN),
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
                price = it.price.setScale(2, RoundingMode.HALF_EVEN),
            )
        }
    }

    private suspend fun addTreatmentsToLens(
        lensId: String,
        treatments: List<StoreLensTreatmentDocument>,
    ) {
        Timber.i("addTreatmentsToLens: Adding ${treatments.size} for lens $lensId...")

        treatments.forEach {
            Timber.i("addTreatmentsToLens: Adding treatment ${it.id}")

            localLensesRepository.addTreatmentToLens(
                lensId = lensId,
                treatmentId = it.id,
                price = it.price.setScale(2, RoundingMode.HALF_EVEN),
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
        }
    }

    private suspend fun addAlternativeHeightsToLens(
        lensId: String,
        alternativeHeights: List<StoreLensAltHeightDocument>,
    ) {
        Timber.i("addAlternativesToLens: Adding ${alternativeHeights.size} for lens $lensId...")

        alternativeHeights.forEach {
            Timber.i("addAlternativesToLens: Adding alternative ${it.id}")

            localLensesRepository.addAlternativeHeightToLens(
                lensId = lensId,
                alternativeHeightId = it.id,
            )
        }
    }

    private fun clearAllProducts() {
        Timber.i("clearAllProducts: Clearing all current local products")
        Either.catch {
            productsDatabase.clearAllTables()
        }.mapLeft {
            Timber.e("clearAllProducts: Failed to clear all tables: ${it.message}", it)
        }
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
            notify(notificationId, builder.build())
        }
    }

    private fun dismissNotification() {
        val context = salesApplication as Context

        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
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

    private fun buildQuery(): PeyessQuery {
        val queryFields = storeLensesRepository.queryFields

        return PeyessQuery(
            queryFields = listOf(
                buildQueryField(
                    field = queryFields.isLocalEnabled,
                    op = PeyessQueryOperation.Equal,
                    value = true,
                )
            ),

            orderBy = listOf(
                PeyessOrderBy(
                    field = queryFields.priority,
                    order = Order.ASCENDING,
                ),
            )
        )
    }

    private suspend fun isLocalDownloadComplete() = either {
        Timber.i("verifyDownloadedProducts: Verifying downloaded products...")

        val totalLocalLenses = localLensesRepository.totalLenses().bind()
        val totalRemoteLenses = storeLensesRepository.totalLensesEnabled().bind()

        if (totalLocalLenses != totalRemoteLenses) {
            val errorMessage =
                "local lenses ($totalLocalLenses) and remote lenses (${totalRemoteLenses}) do not match"
            Timber.e("verifyDownloadedProducts: $errorMessage")
        }
        totalLocalLenses == totalRemoteLenses
    }

    override suspend fun doWork(): Result {
       Timber.i("doWork: Starting product update")

        val result = withContext(Dispatchers.IO) {
            val forceUpdate = inputData.getBoolean(forceUpdateKey, false)
            Timber.i("doWork: Starting Worker with forceUpdate = $forceUpdate")

            val productsTableStatus = productsTableStateRepository.getCurrentState()

            val isUpdating = productsTableStatus.isUpdating
            val needsUpdate = !productsTableStatus.hasUpdated
                    || productsTableStatus.hasUpdateFailed
            val avoidUpdate = isUpdating || (!forceUpdate && !needsUpdate)

            if (avoidUpdate) {
                Timber.i("doWork: Avoiding update: isUpdating = $isUpdating, needsUpdate = $needsUpdate")
                return@withContext Result.success()
            }

            Timber.i("doWork: Cleaning up first...")
            clearAllProducts()

            Timber.i("doWork: Starting product update...")
            productsTableStateRepository.update(
                productsTableStatus.copy(isUpdating = true, hasUpdateFailed = false)
            )
            createNotification()

            Timber.i("doWork: Building query...")
            val query = buildQuery()

            Timber.i("doWork: Downloading and updating products...")
            storeLensesRepository.resetPagination()
            val response = downloadAndPopulateProducts(query)

            Timber.i("doWork: Finished update with response $response")
            return@withContext if (response.isLeft()) {
                Timber.w("doWork: Failed to update products table: ${response.left()}")
                productsTableStateRepository.update(
                    productsTableStatus.copy(
                        hasUpdated = false,
                        hasUpdateFailed = true,
                        isUpdating = false,
                    )
                )

                dismissNotification()
                Result.failure()
            } else {
                val isDownloadComplete = isLocalDownloadComplete()

                isDownloadComplete.fold(
                    ifLeft = { Result.failure() },

                    ifRight = {
                        if (it) {
                            Timber.i("doWork: Update finished successfully: ${response.right()}")
                            productsTableStateRepository.update(
                                productsTableStatus.copy(
                                    hasUpdated = true,
                                    hasUpdateFailed = false,
                                    isUpdating = false,
                                )
                            )
                            dismissNotification()
                            Result.success()
                        } else {
                            Timber.w("doWork: Failed to update products table, the download is incomplete")
                            productsTableStateRepository.update(
                                productsTableStatus.copy(
                                    hasUpdated = false,
                                    hasUpdateFailed = true,
                                    isUpdating = false,
                                )
                            )
                            dismissNotification()
                            Result.failure()
                        }
                    }
                )
            }
        }

        return result
    }

    companion object {
        const val pageLimit = 50

        const val workerTag = "TAG_UpdateProductsWorker"
    }
}