package com.peyess.salesapp.di

import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.MavericksViewModelComponent
import com.airbnb.mvrx.hilt.ViewModelKey
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.feature.authentication_store.state.AuthenticationViewModel
import com.peyess.salesapp.feature.authentication_user.screen.authentication.state.UserAuthViewModel
import com.peyess.salesapp.feature.authentication_user.screen.local_password.state.LocalPasswordViewModel
import com.peyess.salesapp.feature.authentication_user.screen.user_list.state.UserListViewModel
import com.peyess.salesapp.feature.create_client.address.state.ClientAddressViewModel
import com.peyess.salesapp.feature.create_client.basic_info.state.BasicInfoViewModel
import com.peyess.salesapp.feature.create_client.communication.state.CommunicationViewModel
import com.peyess.salesapp.feature.demonstration.state.DemonstrationViewModel
import com.peyess.salesapp.feature.sale.anamnesis.fifth_step_sports.state.FifthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state.FirstTimeViewModel
import com.peyess.salesapp.feature.sale.anamnesis.fourth_step_pain.state.FourthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.state.SecondStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.state.SixthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.third_step_sun_light.state.ThirdStepViewModel
import com.peyess.salesapp.feature.sale.discount.state.DiscountViewModel
import com.peyess.salesapp.feature.sale.fee.state.PaymentFeeViewModel
import com.peyess.salesapp.feature.sale.frames.landing.state.FramesViewModel
import com.peyess.salesapp.feature.sale.frames_measure.state.FramesMeasureViewModel
import com.peyess.salesapp.feature.sale.lens_comparison.state.LensComparisonViewModel
import com.peyess.salesapp.feature.sale.lens_pick.state.LensPickViewModel
import com.peyess.salesapp.feature.sale.payment.state.PaymentViewModel
import com.peyess.salesapp.feature.sale.pick_client.state.PickClientViewModel
import com.peyess.salesapp.feature.sale.prescription_data.state.PrescriptionDataViewModel
import com.peyess.salesapp.feature.sale.prescription_lens_type.state.PrescriptionLensTypeViewModel
import com.peyess.salesapp.feature.sale.prescription_picture.state.PrescriptionPictureViewModel
import com.peyess.salesapp.feature.sale.service_order.state.ServiceOrderViewModel
import com.peyess.salesapp.feature.sale.welcome.state.WelcomeViewModel
import com.peyess.salesapp.feature.settings_actions.state.SettingsAndActionViewModel
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoMap

@Module
@InstallIn(MavericksViewModelComponent::class)
interface ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    fun bindWelcomeViewModelFactory(
        factory: WelcomeViewModel.Factory
    ): AssistedViewModelFactory<*, *>
    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModelFactory(
        factory: MainViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    fun bindStoreAuthenticationViewModelFactory(
        factory: AuthenticationViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    fun bindUserListViewModelFactory(
        factory: UserListViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(UserAuthViewModel::class)
    fun bindUserAuthViewModelFactory(
        factory: UserAuthViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(LocalPasswordViewModel::class)
    fun bindLocalPasswordViewModelFactory(
        factory: LocalPasswordViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(PrescriptionLensTypeViewModel::class)
    fun bindPrescriptionLensTypeViewModelFactory(
        factory: PrescriptionLensTypeViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(PrescriptionPictureViewModel::class)
    fun bindPrescriptionPictureViewModelFactory(
        factory: PrescriptionPictureViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(PrescriptionDataViewModel::class)
    fun bindPrescriptionDataViewModelFactory(
        factory: PrescriptionDataViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(FramesViewModel::class)
    fun bindFramesViewModelFactory(
        factory: FramesViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(FramesMeasureViewModel::class)
    fun bindFramesMeasureViewModelFactory(
        factory: FramesMeasureViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(LensPickViewModel::class)
    fun bindLensPickViewModelFactory(
        factory: LensPickViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(LensComparisonViewModel::class)
    fun bindLensComparisonViewModelFactory(
        factory: LensComparisonViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(PickClientViewModel::class)
    fun bindPickClientViewModelFactory(
        factory: PickClientViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ServiceOrderViewModel::class)
    fun bindServiceOrderViewModelFactory(
        factory: ServiceOrderViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(PaymentViewModel::class)
    fun bindPaymentViewModelFactory(
        factory: PaymentViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(FirstTimeViewModel::class)
    fun bindFirstTimeViewModelFactory(
        factory: FirstTimeViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SecondStepViewModel::class)
    fun bindSecondStepViewModelFactory(
        factory: SecondStepViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ThirdStepViewModel::class)
    fun bindThirdStepViewModelFactory(
        factory: ThirdStepViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(FourthStepViewModel::class)
    fun bindFourthStepViewModelFactory(
        factory: FourthStepViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(FifthStepViewModel::class)
    fun bindFifthStepViewModelFactory(
        factory: FifthStepViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SixthStepViewModel::class)
    fun bindSixthStepViewModelFactory(
        factory: SixthStepViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(DemonstrationViewModel::class)
    fun bindDemonstrationViewModelFactory(
        factory: DemonstrationViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(BasicInfoViewModel::class)
    fun bindBasicInfoViewModelFactory(
        factory: BasicInfoViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(ClientAddressViewModel::class)
    fun bindClientAddressViewModelFactory(
        factory: ClientAddressViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(CommunicationViewModel::class)
    fun bindCommunicationViewModelFactory(
        factory: CommunicationViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(SettingsAndActionViewModel::class)
    fun bindSettingsAndActionViewModelFactory(
        factory: SettingsAndActionViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(DiscountViewModel::class)
    fun bindDiscountViewModelFactory(
        factory: DiscountViewModel.Factory
    ): AssistedViewModelFactory<*, *>

    @Binds
    @IntoMap
    @ViewModelKey(PaymentFeeViewModel::class)
    fun bindPaymentFeeViewModelFactory(
        factory: PaymentFeeViewModel.Factory
    ): AssistedViewModelFactory<*, *>
}
