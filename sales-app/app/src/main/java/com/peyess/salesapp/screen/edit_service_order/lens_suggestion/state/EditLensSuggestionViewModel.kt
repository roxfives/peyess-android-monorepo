package com.peyess.salesapp.screen.edit_service_order.lens_suggestion.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditLensSuggestionViewModel, EditLensSuggestionState>
private typealias EditLensSuggestionViewModelFactory =
        MavericksViewModelFactory<EditLensSuggestionViewModel, EditLensSuggestionState>

class EditLensSuggestionViewModel @AssistedInject constructor(
    @Assisted initialState: EditLensSuggestionState,
): MavericksViewModel<EditLensSuggestionState>(initialState) {

    init {

    }

    fun setSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun setServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: EditLensSuggestionState): EditLensSuggestionViewModel
    }

    companion object: EditLensSuggestionViewModelFactory by hiltMavericksViewModelFactory()
}