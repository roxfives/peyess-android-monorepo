package com.peyess.salesapp.data.repository.lenses

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.continuations.ensureNotNull
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.data.adapter.lenses.toStoreLensDocument
import com.peyess.salesapp.data.dao.lenses.StoreLensesDao
import com.peyess.salesapp.data.internal.firestore.SimpleCollectionPaginator
import com.peyess.salesapp.data.internal.firestore.SimplePaginatorConfig
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.repository.internal.firestore.errors.CreatePaginatorError
import com.peyess.salesapp.data.repository.internal.firestore.errors.FetchDataError
import com.peyess.salesapp.data.repository.internal.firestore.errors.FetchPageError
import com.peyess.salesapp.data.repository.internal.firestore.errors.PaginationError
import com.peyess.salesapp.data.repository.internal.firestore.errors.ReadError
import com.peyess.salesapp.data.repository.internal.firestore.errors.Unexpected
import com.peyess.salesapp.data.repository.lenses.internal.StoreLensesQueryFields
import com.peyess.salesapp.data.utils.query.PeyessQuery
import timber.log.Timber
import javax.inject.Inject

class StoreLensesRepositoryImpl @Inject constructor(
    private val storeLensesDao: StoreLensesDao,
): StoreLensesRepository {
    private var paginator: SimpleCollectionPaginator<FSStoreLocalLens>? = null

    override val queryFields = StoreLensesQueryFields()

    override suspend fun paginateData(
        query: PeyessQuery,
        config: SimplePaginatorConfig,
    ): Either<PaginationError, List<StoreLensDocument>> =
        either {
            val localPaginator = if (paginator == null) {
                storeLensesDao.simpleCollectionPaginator(query, config)
                    .mapLeft { CreatePaginatorError(it.description, it.error) }
                    .bind()
            } else {
                paginator
            }
            paginator = localPaginator

            ensureNotNull(localPaginator) {
                Unexpected("Failed to initialize paginator", null)
            }

            val lenses = localPaginator.page()
                .mapLeft { FetchPageError(it.description, it.error) }
                .bind()

            lenses.map { it.toStoreLensDocument() }
        }

    override suspend fun getById(id: String): Either<ReadError, StoreLensDocument> =
        either {
            val document = storeLensesDao.getById(id)
                .mapLeft { FetchDataError(it.description, it.error) }
                .bind()

            document.toStoreLensDocument()
        }
}