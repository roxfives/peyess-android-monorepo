package com.peyess.salesapp.data.dao.internal.firestore

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.FirestoreError
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.NotFound
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.Unexpected
import com.peyess.salesapp.data.internal.firestore.simple_paginator.error.toFirestoreError
import com.peyess.salesapp.data.utils.query.PeyessQuery
import kotlinx.coroutines.tasks.await
import kotlin.reflect.KClass

typealias FetchCollectionResponse<T> = Either<FirestoreError, List<Pair<String, T>>>

interface ReadOnlyFirestoreDao<F: Any> {

    private suspend fun fetch(docRef: DocumentReference): Either<FirestoreError, DocumentSnapshot> =
        Either.catch { docRef.get().await() }
            .mapLeft {
                if (it is FirebaseFirestoreException) {
                    it.toFirestoreError()
                } else {
                    Unexpected("Failed while fetching document at ${docRef.path}", it)
                }
            }

    private suspend fun toObject(
        docType: KClass<F>,
        snap: DocumentSnapshot,
    ): Either<NotFound, F> =
        either {
            val obj = snap.toObject(docType.javaObjectType)

            ensureNotNull(obj) { NotFound("Document doesn't exist", null) }
        }

    suspend fun getById(id: String): Either<FirestoreError, F>

    suspend fun fetchDocument(
        docType: KClass<F>,
        docRef: DocumentReference,
    ): Either<FirestoreError, F> =
        either {
            val snap = fetch(docRef).bind()
            val obj = toObject(docType, snap).bind()

            obj
        }

    suspend fun simpleCollectionPaginator(
        query: PeyessQuery,
        config: SimplePaginatorConfig,
    ): Either<Unexpected, SimpleCollectionPaginator<F>>

    suspend fun fetchCollection(
        query: PeyessQuery,
    ): FetchCollectionResponse<F>
}