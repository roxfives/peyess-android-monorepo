package com.peyess.salesapp.dao.auth.users

import android.net.Uri
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.firebase.FirebaseManager
import com.peyess.salesapp.model.users.AccountStatus
import com.peyess.salesapp.model.users.FSCollaborator
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class CollaboratorsDaoImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val firebaseManager: FirebaseManager,
) : CollaboratorsDao {
    val firestore = firebaseManager.storeFirestore

    private fun toCollaborators(snap:  QuerySnapshot?): List<FSCollaborator> {
        return if (snap != null) {
            snap.documents.mapNotNull {
                Timber.i("Parsing collaborator", it)

                it.toObject(FSCollaborator::class.java)
            }
        } else {
            Timber.e("Snapshot is null")
            listOf()
        }
    }

    override fun user(uid: String): Flow<FSCollaborator> = flow {
        if (firestore == null || firebaseManager.currentStore == null) {
            Timber.e("Firestore or firebase application is null")
            error("Firestore or firebase application is null")
        }

        val docRef = firestore
            .collection(
                salesApplication.stringResource(R.string.fs_col_collaborators)
                    .format(firebaseManager.currentStore!!.uid)
            )
            .document(uid)

        val fsCollaborator: FSCollaborator?
        val snap = docRef.get().await()

        if (snap.exists()) {
            fsCollaborator = snap.toObject(FSCollaborator::class.java)

            if (fsCollaborator != null) {
                emit(fsCollaborator)
            } else {
                Timber.e("Failed while converting to document")
                error("Failed while converting to document")
            }
        } else {
            Timber.e("Collaborator $uid does not exist")
        }
    }

    override fun streamActiveAccounts(): Flow<List<FSCollaborator>> = callbackFlow {
        val snapListener: ListenerRegistration?
        val storeId = firebaseManager.currentStore?.uid

        if (firestore != null) {
            val collectionPath = salesApplication
                .stringResource(R.string.fs_col_collaborators)
                .format(storeId)

            Timber.i("Adding callback to fetch collaborators at $collectionPath")

            snapListener = firestore.collection(
                salesApplication.stringResource(R.string.fs_col_collaborators).format(storeId)
            )
                .whereEqualTo(
                    salesApplication.stringResource(R.string.fs_field_collaborators_account_status),
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

    override suspend fun getById(uid: String): FSCollaborator? {
        if (firestore == null || firebaseManager.currentStore == null) {
            Timber.e("Firestore or firebase application is null")
            return null
        }

        val docRef = firestore.collection(
                salesApplication.stringResource(R.string.fs_col_collaborators)
                    .format(firebaseManager.currentStore!!.uid)
            )
            .document(uid)

        val fsCollaborator: FSCollaborator?
        val snap = docRef.get().await()

        return if (snap.exists()) {
            fsCollaborator = snap.toObject(FSCollaborator::class.java)

            fsCollaborator
        } else {
            Timber.e("Collaborator $uid does not exist")
            null
        }
    }

    override suspend fun pictureFor(uid: String): Uri {
        val storageRef = firebaseManager.storage?.reference
            ?: FirebaseStorage.getInstance().reference

        val storagePicturePath = salesApplication
            .stringResource(R.string.storage_collaborator_picture)
            .format(uid)

        val picture = storageRef.child(storagePicturePath)
            .downloadUrl
            .addOnFailureListener {
                Timber.e("Failed to download collaborator picture for $uid", it)
            }.addOnCompleteListener {
                Timber.i("Downloaded collaborator picture for $uid")
            }.await()

        if (picture == null) {
            Timber.e("Collaborator picture with id $uid does not exist")
            return Uri.EMPTY
        }

        return picture
    }
}