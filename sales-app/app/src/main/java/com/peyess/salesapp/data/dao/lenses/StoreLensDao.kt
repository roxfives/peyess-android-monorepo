package com.peyess.salesapp.data.dao.lenses

import arrow.core.Either
import com.peyess.salesapp.data.model.lens.FSStoreLocalLens
import com.peyess.salesapp.data.dao.internal.firestore.ReadOnlyFirestoreDao
import com.peyess.salesapp.data.dao.lenses.error.StoreLensDaoError
import com.peyess.salesapp.data.dao.lenses.error.TotalLensesEnabledError

typealias TotalLensesEnabledResponse = Either<TotalLensesEnabledError, Int>

interface StoreLensesDao: ReadOnlyFirestoreDao<FSStoreLocalLens> {
    suspend fun totalLensesEnabled(): TotalLensesEnabledResponse
}
