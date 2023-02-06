package com.peyess.salesapp.data.model.lens.categories

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.model.lens.categories.error.LensCategoryDaoReadError
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LensCategoryDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): LensTypeCategoryDao {
    override fun streamCategories(): Flow<List<LensTypeCategoryDocument>> = flow {
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

    override suspend fun typeCategories(): LensTypeCategoriesResponse = either {
        val firestore = firebaseManager.storeFirestore


        ensureNotNull(firestore) {
            LensCategoryDaoReadError.Unexpected(
                description = "Firestore instance is null",
                throwable = null,
            )
        }

        Either.catch {
            val snaps = firestore
                .collection(salesApplication.stringResource(R.string.fs_col_lenses_types_category))
                .orderBy(salesApplication.stringResource(R.string.fs_field_lenses_types_category_priority))
                .get()
                .await()

            snaps.mapNotNull {
                it.toObject(FSLensCategory::class.java)
                    .toDocument(it.id)
            }
        }.mapLeft {
            LensCategoryDaoReadError.LensCategoryDaoFetchError(
                description = it.message ?: "Unknown error",
                throwable = it,
            )
        }.bind()
    }
}