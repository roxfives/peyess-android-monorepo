package com.peyess.salesapp.feature.sale.anamnesis.third_step_sun_light.state

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

class ThirdStepViewModel @AssistedInject constructor(
    @Assisted initialState: ThirdStepState,
    salesApplication: SalesApplication,
): MavericksViewModel<ThirdStepState>(initialState) {

    val options = mapOf(
        Pair(0, salesApplication.stringResource(R.string.anamnesis_first_step_yes)),
        Pair(1, salesApplication.stringResource(R.string.anamnesis_first_step_a_little)),
        Pair(2, salesApplication.stringResource(R.string.anamnesis_first_step_no)),
    )

    fun onOptionSelected(selected: Int) = setState {
        copy(selected = selected)
    }

    fun resetState() = setState {
        ThirdStepState()
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<ThirdStepViewModel, ThirdStepState> {
        override fun create(state: ThirdStepState): ThirdStepViewModel
    }

    companion object: MavericksViewModelFactory<ThirdStepViewModel, ThirdStepState>
        by hiltMavericksViewModelFactory()
}