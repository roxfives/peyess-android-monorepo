package com.peyess.salesapp.screen.edit_service_order.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

private typealias EditServiceOrderFactory =
        AssistedViewModelFactory<EditServiceOrderViewModel, EditServiceOrderState>
private typealias EditServiceOrderViewModelFactory =
        MavericksViewModelFactory<EditServiceOrderViewModel, EditServiceOrderState>

class EditServiceOrderViewModel @AssistedInject constructor(
    @Assisted initialState: EditServiceOrderState,
//    private val
): MavericksViewModel<EditServiceOrderState>(initialState) {}
