package com.peyess.salesapp.workmanager

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.products.firestore.lens.FSLocalLens
import com.peyess.salesapp.dao.products.firestore.lens.toFilterCategory
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensDescription
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensFamily
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensGroup
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensMaterial
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensSpecialty
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensSupplier
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensTech
import com.peyess.salesapp.dao.products.firestore.lens.toFilterLensType
import com.peyess.salesapp.dao.products.firestore.lens.toLocalLensEntity
import com.peyess.salesapp.dao.products.firestore.lens_coloring.FSLensColoring
import com.peyess.salesapp.database.room.ProductsDatabase
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.users.Collaborator
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class UpdateProductsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    val productsDatabase: ProductsDatabase,
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): CoroutineWorker(context, workerParams) {
    private suspend fun addAllToLocalProducts(docs: List<DocumentSnapshot>) {
        val lenses = docs.mapNotNull { it.toObject(FSLocalLens::class.java) }

        Timber.i("Found ${lenses.size} lenses")

        lenses.forEach {
            Timber.i("Got lens ${it.id}")

            val lens = it.toLocalLensEntity()
            productsDatabase.localLensDao().add(lens)

            populateFilters(it)


            Timber.i("With disps ${it.disponibilities}")
            it.disponibilities.forEach { disp ->
                Timber.i("With nested map ${disp.manufacturers}")
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
            val descriptionFilter = lens.toFilterLensDescription()

            productsDatabase.filterLensDescriptionEntity().add(descriptionFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for description")
        }

        try {
            val familyFilter = lens.toFilterLensFamily()

            productsDatabase.filterLensFamilyEntity().add(familyFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for family")
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

            productsDatabase.filterLensMaterialEntity().add(materialFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for material")
        }

        try {
            val specialtyFilter = lens.toFilterLensSpecialty()

            productsDatabase.filterLensSpecialtyEntity().add(specialtyFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for specialty")
        }

        try {
            val supplierFilter = lens.toFilterLensSupplier()

            productsDatabase.filterLensSupplierEntity().add(supplierFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for supplier")
        }

        try {
            val techFilter = lens.toFilterLensTech()

            productsDatabase.filterLensTechEntity().add(techFilter)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for tech")
        }

        try {
            val type = lens.toFilterLensType()

            productsDatabase.filterLensTypeEntity().add(type)
        } catch (e: SQLiteConstraintException) {
            // Just ignore this error, collisions will happen
        } catch (e: Throwable) {
            Timber.e(e, "Error while inserting lens filter for ")
        }
    }

    private fun clearAllProducts() {
        Timber.i("Clearing all current local products")
        productsDatabase.clearAllTables()
    }

    override suspend fun doWork(): Result {
        val firestore = firebaseManager.noCacheFirestore
        if (firestore == null) {
            return Result.retry()
        }

        val storeId = firebaseManager.currentStore!!.uid
        val lensPath = salesApplication
            .stringResource(id = R.string.fs_col_lenses)
            .format(storeId)

        withContext(Dispatchers.IO) {
            Timber.i("Starting products update")

            val initQuery = firestore
                .collection(lensPath)
                .orderBy("priority")
                .limit(1)
                .get()
                .await()

            if (initQuery.size() != 1) {
                Timber.e("Initial query has to be of size 1, but it is ${initQuery.size()}")
                return@withContext Result.retry()
            }

            clearAllProducts()

            var snaps: QuerySnapshot
            var currSnapshot = initQuery.documents[0]
            var totalDownloaded = 0
            do {
                Timber.i("Starting page download")
                snaps = firestore
                    .collection(lensPath)
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
        }

        return Result.success()
    }


    companion object {
        const val pageLimit = 100L
    }
}