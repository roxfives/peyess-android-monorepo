package com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.base.MavericksViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SecondStepViewModel @AssistedInject constructor(
    @Assisted initialState: SecondStepState,
    salesApplication: SalesApplication,
): MavericksViewModel<SecondStepState>(initialState) {

    private val optionsMessage = mapOf(
        Pair(0, salesApplication.stringResource(R.string.anamnesis_daily)),
        Pair(1, salesApplication.stringResource(R.string.anamnesis_work)),
        Pair(2, salesApplication.stringResource(R.string.anamnesis_leisure)),
        Pair(3, salesApplication.stringResource(R.string.anamnesis_reading)),
    )

    private val workOptionsMessage = mapOf(
        Pair(0, salesApplication.stringResource(R.string.mike_user_profile_message_work_construction)),
        Pair(1, salesApplication.stringResource(R.string.mike_user_profile_message_work_office)),
        Pair(2, salesApplication.stringResource(R.string.mike_user_profile_message_work_housekeeping)),
        Pair(3, salesApplication.stringResource(R.string.mike_user_profile_message_work_chemicals)),
        Pair(3, salesApplication.stringResource(R.string.mike_user_profile_message_work_agriculture)),
        Pair(3, salesApplication.stringResource(R.string.mike_user_profile_message_work_driving)),
    )

    val options = mapOf(
        Pair(0, salesApplication.stringResource(R.string.anamnesis_daily)),
        Pair(1, salesApplication.stringResource(R.string.anamnesis_work)),
        Pair(2, salesApplication.stringResource(R.string.anamnesis_leisure)),
        Pair(3, salesApplication.stringResource(R.string.anamnesis_reading)),
    )

    val workOptions = mapOf(
        Pair(0, salesApplication.stringResource(R.string.construction)),
        Pair(1, salesApplication.stringResource(R.string.office)),
        Pair(2, salesApplication.stringResource(R.string.stay_home)),
        Pair(3, salesApplication.stringResource(R.string.chemicals)),
        Pair(4, salesApplication.stringResource(R.string.agriculture)),
        Pair(5, salesApplication.stringResource(R.string.driving)),
    )

    fun onOptionSelected(selected: Int) = setState {
        copy(selected = selected)
    }

    fun onWorkUsageSelected(selected: Int) = setState {
        copy(workSelected = selected)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<SecondStepViewModel, SecondStepState> {
        override fun create(state: SecondStepState): SecondStepViewModel
    }

    companion object: MavericksViewModelFactory<SecondStepViewModel, SecondStepState>
        by hiltMavericksViewModelFactory()
}