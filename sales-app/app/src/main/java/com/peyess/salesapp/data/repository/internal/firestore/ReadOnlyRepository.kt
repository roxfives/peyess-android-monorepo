package com.peyess.salesapp.data.repository.internal.firestore

import arrow.core.Either
import com.peyess.salesapp.data.internal.firestore.simple_paginator.SimplePaginatorConfig
import com.peyess.salesapp.data.repository.internal.firestore.errors.ReadError
import com.peyess.salesapp.data.repository.internal.firestore.errors.RepositoryError
import com.peyess.salesapp.data.utils.query.PeyessQuery

interface ReadOnlyRepository<F: Any> {
    suspend fun paginateData(
        query: PeyessQuery,
        config: SimplePaginatorConfig,
    ): Either<RepositoryError, List<F>>

    suspend fun getById(id: String): Either<ReadError, F>
}