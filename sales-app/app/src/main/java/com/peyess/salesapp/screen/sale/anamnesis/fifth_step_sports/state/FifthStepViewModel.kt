package com.peyess.salesapp.screen.sale.anamnesis.fifth_step_sports.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FifthStepViewModel @AssistedInject constructor(
    @Assisted initialState: FifthStepState,
    salesApplication: SalesApplication,
): MavericksViewModel<FifthStepState>(initialState) {

    val options = mapOf(
        Pair(0, salesApplication.stringResource(R.string.anamnesis_first_step_yes)),
        Pair(1, salesApplication.stringResource(R.string.anamnesis_first_step_a_little)),
        Pair(2, salesApplication.stringResource(R.string.anamnesis_first_step_no)),
    )

    fun onOptionSelected(selected: Int) = setState {
        copy(selected = selected)
    }

    fun resetState() = setState {
        FifthStepState()
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<FifthStepViewModel, FifthStepState> {
        override fun create(state: FifthStepState): FifthStepViewModel
    }

    companion object: MavericksViewModelFactory<FifthStepViewModel, FifthStepState>
        by hiltMavericksViewModelFactory()
}