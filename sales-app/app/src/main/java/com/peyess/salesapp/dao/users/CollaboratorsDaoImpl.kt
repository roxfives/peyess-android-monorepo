package com.peyess.salesapp.dao.users

import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.users.AccountStatus
import com.peyess.salesapp.model.users.Collaborator
import com.peyess.salesapp.model.users.FSCollaborator
import com.peyess.salesapp.model.users.toDocument
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class CollaboratorsDaoImpl @Inject constructor(
    val firebaseManager: FirebaseManager,
    val application: SalesApplication,
) : CollaboratorsDao {
    val firestore = firebaseManager.storeFirestore

    private fun toCollaborators(snap:  QuerySnapshot?): List<Collaborator> {
        return if (snap != null) {
            snap.documents.mapNotNull {
                Timber.i("Parsing collaborator", it)

                val fsCollaborator = it.toObject(FSCollaborator::class.java)

                fsCollaborator?.toDocument()
            }
        } else {
            Timber.d("Snapshot is null")
            listOf()
        }
    }

    override fun user(uid: String): Flow<Collaborator> = flow {
        if (firestore == null || firebaseManager.currentStore == null) {
            Timber.e("Firestore or firebase application is null")
            error("Firestore or firebase application is null")
        }

        val docRef = firestore
            .collection(
                application.stringResource(R.string.fs_col_collaborators)
                    .format(firebaseManager.currentStore!!.uid)
            )
            .document(uid)

        val fsCollaborator: FSCollaborator?
        val snap = docRef.get().await()

        if (snap.exists()) {
            fsCollaborator = snap.toObject(FSCollaborator::class.java)

            if (fsCollaborator != null) {
                emit(fsCollaborator.toDocument())
            } else {
                Timber.e("Failed while converting to document")
                error("Failed while converting to document")
            }
        } else {
            Timber.e("Collaborator $uid does not exist")
        }
    }

    override fun subscribeToActiveAccounts(): Flow<List<Collaborator>> = callbackFlow {
        val snapListener: ListenerRegistration?
        val storeId = firebaseManager.currentStore?.uid

        if (firestore != null) {
            val collectionPath = application
                .stringResource(R.string.fs_col_collaborators)
                .format(storeId)

            Timber.i("Adding callback to fetch collaborators at ${collectionPath}")

            snapListener = firestore.collection(
                application.stringResource(R.string.fs_col_collaborators).format(storeId)
            )
                .whereEqualTo(
                    application.stringResource(R.string.fs_field_collaborators_account_status),
                    AccountStatus.Active.name()
                )
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Timber.e("Listen failed: ${e.message}")
                        Timber.e(e)
                        error("Listen failed.")
                    }
                    Timber.i("Found ${snapshot?.documents?.size} documents")

                    trySend(toCollaborators(snapshot))
                }
        } else {
            Timber.e("Firestore instance is null")
            error("Firestore instance is null")
        }

        awaitClose {
            Timber.i("Removing listener")
            snapListener.remove()
        }
    }
}