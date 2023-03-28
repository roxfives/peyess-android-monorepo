package com.peyess.salesapp.data.dao.client_legal

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import arrow.core.leftIfNull
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.client_legal.toMap
import com.peyess.salesapp.data.dao.client_legal.error.FetchClientLegalDaoError
import com.peyess.salesapp.data.dao.client_legal.error.UpdateClientLegalDaoError
import com.peyess.salesapp.data.model.client_legal.FSClientLegal
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ClientLegalDaoImpl @Inject constructor(
    val salesApplication: SalesApplication,
    val firebaseManager: FirebaseManager,
): ClientLegalDao {
    override suspend fun legalForClient(clientId: String): FetchClientLegalResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            FetchClientLegalDaoError.UnexpectedError(
                description = "Firestore instance is null",
            )
        }

        val legalDocPath = salesApplication
            .stringResource(R.string.fs_doc_client_legal)
            .format(clientId)
        ensure(legalDocPath.isNotBlank()) {
            FetchClientLegalDaoError.UnexpectedError(
                description = "Legal document path is blank",
            )
        }

        val snap = Either.catch {
            firestore
                .document(legalDocPath)
                .get()
                .await()
        }.mapLeft {
            FetchClientLegalDaoError.UnexpectedError(
                description = "Error while fetching client legal document",
                throwable = it,
            )
        }.bind()

        ensure(snap.exists()) {
            FetchClientLegalDaoError.NotFound(
                description = "Client legal document not found",
            )
        }

        Either.catch {
            snap.toObject(FSClientLegal::class.java)
        }.mapLeft {
            FetchClientLegalDaoError.UnexpectedError(
                description = "Error while converting client legal document to object",
                throwable = it,
            )
        }.leftIfNull {
            FetchClientLegalDaoError.UnexpectedError(
                description = "Error while converting client legal document to object",
            )
        }.bind()
    }

    override suspend fun addClientLegalFor(clientId: String, clientLegal: FSClientLegal) {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            error("Firestore instance is uninitialized")
        }

        try {
            firestore.document(
                salesApplication
                        .stringResource(R.string.fs_doc_client_legal)
                        .format(clientId)
                )
                .set(clientLegal)
                .await()
        } catch (err: Exception) {
            Timber.e(err, "Error while adding client")
            error(err)
        }
    }

    override suspend fun updateClientLegalFor(
        clientId: String,
        clientLegal: FSClientLegal,
    ): UpdateClientLegalResponse = either {
        val firestore = firebaseManager.storeFirestore
        ensureNotNull(firestore) {
            UpdateClientLegalDaoError.UnexpectedError(
                description = "Firestore instance is null",
            )
        }

        Either.catch {
            firestore
                .document(
                    salesApplication
                        .stringResource(R.string.fs_doc_client_legal)
                        .format(clientId)
                ).update(clientLegal.toMap())
                .await()
        }.mapLeft {
            UpdateClientLegalDaoError.UnexpectedError(
                description = "Error while updating client",
                throwable = it,
            )
        }.bind()
    }
}
