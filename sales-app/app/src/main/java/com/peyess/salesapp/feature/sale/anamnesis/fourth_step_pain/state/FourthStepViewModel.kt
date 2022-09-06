package com.peyess.salesapp.feature.sale.anamnesis.fourth_step_pain.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.state.SecondStepState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FourthStepViewModel @AssistedInject constructor(
    @Assisted initialState: FourthStepState,
    salesApplication: SalesApplication,
): MavericksViewModel<FourthStepState>(initialState) {

    val options = mapOf(
        Pair(0, salesApplication.stringResource(R.string.anamnesis_first_step_yes)),
        Pair(1, salesApplication.stringResource(R.string.anamnesis_first_step_a_little)),
        Pair(2, salesApplication.stringResource(R.string.anamnesis_first_step_no)),
    )

    fun onOptionSelected(selected: Int) = setState {
        copy(selected = selected)
    }

    fun resetState() = setState {
        FourthStepState()
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<FourthStepViewModel, FourthStepState> {
        override fun create(state: FourthStepState): FourthStepViewModel
    }

    companion object: MavericksViewModelFactory<FourthStepViewModel, FourthStepState>
        by hiltMavericksViewModelFactory()
}