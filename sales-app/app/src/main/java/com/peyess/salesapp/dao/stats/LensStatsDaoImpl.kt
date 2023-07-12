package com.peyess.salesapp.dao.stats

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.google.firebase.firestore.AggregateSource
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.stats.error.FetchLensStatsDaoError
import com.peyess.salesapp.data.model.stats.FSLensStats
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LensStatsDaoImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
): LensStatsDao {
    override suspend fun fetchLensStats(): LensStatsResponse = either {
        val firestore = firebaseManager.storeFirestore

        ensureNotNull(firestore) {
            FetchLensStatsDaoError.DatabaseUnavailable("Firestore instance is null")
        }

        Either.catch {
           val snap = firestore
                .collection(salesApplication.stringResource(R.string.fs_col_stats))
                .document(salesApplication.stringResource(R.string.fs_doc_lens_stats))
                .get()
                .await()
            ensure(snap.exists()) {
                FetchLensStatsDaoError.NotFound("Lens stats not found")
            }

            val fsLensStats = snap.toObject(FSLensStats::class.java)
            ensureNotNull(fsLensStats) {
                FetchLensStatsDaoError.NotFound("Error while converting stats document to object")
            }

            fsLensStats
        }.mapLeft {
            FetchLensStatsDaoError.Unexpected("Unexpected error", it)
        }.bind()
    }
}
