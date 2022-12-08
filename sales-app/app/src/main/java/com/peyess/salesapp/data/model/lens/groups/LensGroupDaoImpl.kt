package com.peyess.salesapp.data.model.lens.groups

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LensGroupDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): LensGroupDao {
    override fun groups(): Flow<List<LensGroupDocument>> = flow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            return@flow
        }

        val snaps = firestore
            .collection(salesApplication.stringResource(R.string.fs_col_lenses_groups))
            .orderBy(salesApplication.stringResource(R.string.fs_field_lenses_group_priority))
            .get()
            .await()

        val groups = snaps.mapNotNull {
                it.toObject(FSLensGroup::class.java)
                    .toDocument(it.id)
            }

        emit(groups)
    }
}