package com.peyess.salesapp.feature.sale.lens_pick.state

import androidx.paging.PagingData
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class LensPickState(
    val lenses: Flow<PagingData<LensSuggestionModel>> = flowOf(),
): MavericksState
