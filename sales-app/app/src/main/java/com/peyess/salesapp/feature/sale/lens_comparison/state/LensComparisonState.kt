package com.peyess.salesapp.feature.sale.lens_comparison.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison

data class LensComparisonState(
    val comparisons: Async<List<IndividualComparison>> = Uninitialized,
): MavericksState
