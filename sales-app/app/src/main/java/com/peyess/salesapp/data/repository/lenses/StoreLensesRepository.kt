package com.peyess.salesapp.data.repository.lenses

import arrow.core.Either
import com.peyess.salesapp.data.model.lens.StoreLensDocument
import com.peyess.salesapp.data.repository.internal.firestore.ReadOnlyRepository
import com.peyess.salesapp.data.repository.internal.firestore.errors.RepositoryError
import com.peyess.salesapp.data.repository.lenses.internal.StoreLensesQueryFields

typealias StoreLensResponse = Either<RepositoryError, List<StoreLensDocument>>

interface StoreLensesRepository: ReadOnlyRepository<StoreLensDocument> {
    val queryFields: StoreLensesQueryFields
}
