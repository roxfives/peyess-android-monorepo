package com.peyess.salesapp.data.dao.card_flag

import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.card_flag.toCardFlagDocument
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import com.peyess.salesapp.data.model.sale.card_flags.FSCardFlag
import com.peyess.salesapp.firebase.FirebaseManager
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class CardFlagDaoImpl @Inject constructor(
    val firebaseManager: FirebaseManager,
    val salesApplication: SalesApplication,
) : CardFlagDao {
    override fun listCards(): Flow<List<CardFlagDocument>> = callbackFlow {
        val firestore = firebaseManager.storeFirestore
        if (firestore == null) {
            Timber.e("Firestore instance is null")
            error("Firestore instance is null")
        }

        firestore.collection(salesApplication.stringResource(R.string.fs_col_card_flags))
            .orderBy(salesApplication.stringResource(R.string.fs_field_card_flag_name))
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Timber.e(exception, "Failed while fetching clients")
                }

                val cardFlags = snapshot?.mapNotNull {
                    try {
                        it.toObject(FSCardFlag::class.java).toCardFlagDocument(it.id)
                    } catch (error: Throwable) {
                        Timber.e(error, "Failed to convert card flag document ${it.id}")
                        null
                    }
                }

                Timber.i("Sending card flags $cardFlags")
                trySend(cardFlags ?: emptyList())
            }

        awaitClose()
    }

}