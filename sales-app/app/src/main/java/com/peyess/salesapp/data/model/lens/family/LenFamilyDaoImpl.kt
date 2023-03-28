package com.peyess.salesapp.data.model.lens.family

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LenFamilyDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): LensFamilyDao {
    override fun families(): Flow<List<LensFamilyDocument>> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val snaps = firestore
            .collection(salesApplication.stringResource(R.string.fs_col_description))
//            .orderBy(salesApplication.stringResource(R.string.fs_field_lenses_types_category_priority))
            .get()
            .await()

        val descriptions = snaps.mapNotNull {
                it.toObject(FSLensFamily::class.java)
                    .toDocument(it.id)
            }

        emit(descriptions)
    }
}