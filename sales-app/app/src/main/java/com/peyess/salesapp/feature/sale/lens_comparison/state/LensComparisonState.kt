package com.peyess.salesapp.feature.sale.lens_comparison.state

import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.repository.local_sale.lens_comparison.LocalComparisonReadError
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison

typealias LensComparisonResponse = Either<LocalComparisonReadError, List<LensComparisonDocument>>

data class LensComparisonState(
    val isEditing: Boolean = false,
    val serviceOrderId: String = "",
    val saleId: String = "",

    val comparisonListAsync: Async<LensComparisonResponse> = Uninitialized,
    val comparisonList: List<LensComparisonDocument> = emptyList(),

    val comparisonsAsync: Async<List<IndividualComparison>> = Uninitialized,
    val comparisons: List<IndividualComparison> = emptyList(),

//    val comparisons: Async<List<IndividualComparison>> = Uninitialized,

    val hasPickedProduct: Boolean = false,
): MavericksState {
    val hasLoaded = comparisonListAsync is Success && comparisonListAsync.invoke().isRight()
    val isLoading = comparisonListAsync is Loading
    val hasFailed = comparisonListAsync is Fail
}
