package com.peyess.salesapp.data.model.lens.categories

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LensCategoryDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): LensTypeCategoryDao {
    override fun categories(): Flow<List<LensTypeCategoryDocument>> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val snaps = firestore
            .collection(salesApplication.stringResource(R.string.fs_col_lenses_types_category))
            .orderBy(salesApplication.stringResource(R.string.fs_field_lenses_types_category_priority))
            .get()
            .await()

        val categories = snaps.mapNotNull {
                it.toObject(FSLensCategory::class.java)
                    .toDocument(it.id)
            }

        emit(categories)
    }
}