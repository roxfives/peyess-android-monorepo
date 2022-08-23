package com.peyess.salesapp.dao.products.firestore.lens_description

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class LensDescriptionDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): LensDescriptionDao {
    override fun descriptions(): Flow<List<LensDescription>> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val snaps = firestore
            .collection(salesApplication.stringResource(R.string.fs_col_description))
            .orderBy(salesApplication.stringResource(R.string.fs_field_lenses_types_category_priority))
            .get()
            .await()

        val descriptions = snaps.mapNotNull {
                it.toObject(FSLensDescription::class.java)
                    .toDocument(it.id)
            }

        emit(descriptions)
    }

    override fun descriptionsFor(familyId: String): Flow<List<LensDescription>> = flow {
        Timber.i("Getting description for $familyId")

        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        Timber.i("Getting desings")
        val snaps = firestore
            .collection(salesApplication.stringResource(R.string.fs_col_description))
            .whereArrayContains(
                salesApplication.stringResource(R.string.fs_field_description_families_ids),
                familyId,
            )
            .orderBy(salesApplication.stringResource(R.string.fs_field_lenses_types_category_priority))
            .get()
            .await()
        Timber.i("Got ${snaps.size()} designs")

        val descriptions = snaps.mapNotNull {
            it.toObject(FSLensDescription::class.java)
                .toDocument(it.id)
        }

        emit(descriptions)
    }
}