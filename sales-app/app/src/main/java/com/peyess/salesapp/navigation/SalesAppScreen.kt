package com.peyess.salesapp.navigation

import java.lang.IllegalArgumentException

enum class SalesAppScreens {
    Landing,
    SaleScreen,
    Home,
    Clients,
    AddMeasure,

    StoreAuthentication,
    UserListAuthentication,
    UserAuth,
    LocalPasscode,

    SaleWelcome,
    SalePrescriptionLensType,
    SalePrescriptionPicture,
    SalePrescriptionData,
    SalePrescriptionDataSymptoms,
    FramesLanding,
    SetFramesData,
    FramesMeasureAnimation,
    FramesMeasureTakePicture,
    FramesMeasure,
    AnamnesisFirstStep,
    AnamnesisSecondStep,
    AnamnesisThirdStep,
    AnamnesisFourthStep,
    AnamnesisFifthStep,
    AnamnesisSixthStep,
    AnamnesisSeventhStep,
    LensSuggestion,
    LensSuggestionWithoutSuggestions,
    LensComparison,
    ClientList,
    ServiceOrder,
    EditServiceOrder,
    EditPaymentFee,
    EditPaymentDiscount,
    SalePayment,
    Discount,
    PaymentFee,
    Demonstration,

    CreateNewClientBasicInfo,
    CreateNewClientAddress,
    CreateNewClientContact,

    VisualAcuity,
    SettingsAndActions,

    People,
    AddPerson,
    Frames,
    AddFrames,
    RealMeasures,
    AddRealMeasures,
    MeasurePicture,
    AddAngle,
    PictureAngle;

    companion object {
        fun fromRoute(route: String?): SalesAppScreens =
            when (route?.substringBefore("/")?.substringBefore("?")) {
                Landing.name -> Landing

                StoreAuthentication.name -> StoreAuthentication
                UserListAuthentication.name -> UserListAuthentication
                UserAuth.name -> UserAuth
                LocalPasscode.name -> LocalPasscode

                SaleWelcome.name -> SaleWelcome
                SalePrescriptionLensType.name -> SalePrescriptionLensType
                SalePrescriptionPicture.name -> SalePrescriptionPicture
                SalePrescriptionData.name -> SalePrescriptionData
                SalePrescriptionDataSymptoms.name -> SalePrescriptionDataSymptoms
                FramesLanding.name -> FramesLanding
                SetFramesData.name -> SetFramesData
                FramesMeasureAnimation.name -> FramesMeasureAnimation
                FramesMeasureTakePicture.name -> FramesMeasureTakePicture
                FramesMeasure.name -> FramesMeasure
                AnamnesisFirstStep.name -> AnamnesisFirstStep
                AnamnesisSecondStep.name -> AnamnesisSecondStep
                AnamnesisThirdStep.name -> AnamnesisThirdStep
                AnamnesisFourthStep.name -> AnamnesisFourthStep
                AnamnesisFifthStep.name -> AnamnesisFifthStep
                AnamnesisSixthStep.name -> AnamnesisSixthStep
                AnamnesisSeventhStep.name -> AnamnesisSeventhStep
                LensSuggestion.name -> LensSuggestion
                LensSuggestionWithoutSuggestions.name -> LensSuggestionWithoutSuggestions
                LensComparison.name -> LensComparison
                ClientList.name -> ClientList
                ServiceOrder.name -> ServiceOrder
                EditServiceOrder.name -> EditServiceOrder
                EditPaymentFee.name -> EditPaymentFee
                EditPaymentDiscount.name -> EditPaymentDiscount
                SalePayment.name -> SalePayment
                Discount.name -> Discount
                PaymentFee.name -> PaymentFee
                Demonstration.name -> Demonstration

                CreateNewClientBasicInfo.name -> CreateNewClientBasicInfo
                CreateNewClientAddress.name -> CreateNewClientAddress
                CreateNewClientContact.name -> CreateNewClientContact

                VisualAcuity.name -> VisualAcuity
                SettingsAndActions.name -> SettingsAndActions

                SaleScreen.name -> SaleScreen
                Home.name -> Home
                Clients.name -> Clients
                AddMeasure.name -> AddMeasure
                People.name -> People
                AddPerson.name -> AddPerson
                Frames.name -> Frames
                AddFrames.name -> AddFrames
                RealMeasures.name -> RealMeasures
                AddRealMeasures.name -> AddRealMeasures
                MeasurePicture.name -> MeasurePicture
                AddAngle.name -> AddAngle
                PictureAngle.name -> PictureAngle

                null -> Home
                else -> throw IllegalArgumentException("Route $route does not exist")
            }

        fun title(route: String?): String =
            when (route?.substringBefore("/")?.substringBefore("?")) {
                SaleScreen.name -> "Suas vendas"
                Home.name -> "Boas vindas!"
                Clients.name -> "Seus clientes"
                AddMeasure.name -> "Adicionar medida"
                People.name -> "Pessoas"
                UserAuth.name -> "Entrar na loja"
                LocalPasscode.name -> "Criar senha para acesso local"
                SaleWelcome.name -> "Vamos começar!"
                SalePrescriptionLensType.name -> "Qual será o uso"
                SalePrescriptionPicture.name -> "Vamos aos dados da sua receita"
                SalePrescriptionData.name -> "Vamos aos dados da sua receita"
                SalePrescriptionDataSymptoms.name -> "Sintomas e Curiosidades"
                FramesLanding.name -> "Vamos para escolha da sua armação"
                SetFramesData.name -> "Dados da sua armação"
                FramesMeasureAnimation.name -> "Vamos a sua medição"
                FramesMeasureTakePicture.name -> "Vamos a sua medição"
                FramesMeasure.name -> "Vamos a sua medição"
                AnamnesisFirstStep.name -> "Anamnese"
                AnamnesisSecondStep.name -> "Anamnese"
                AnamnesisThirdStep.name -> "Anamnese"
                AnamnesisFourthStep.name -> "Anamnese"
                AnamnesisFifthStep.name -> "Anamnese"
                AnamnesisSixthStep.name -> "Anamnese"
                AnamnesisSeventhStep.name -> "Anamnese"
                LensSuggestion.name -> "Sugestões de lentes"
                LensSuggestionWithoutSuggestions.name -> "Tabela de produtos"
                LensComparison.name -> "Comparação da lente"
                ClientList.name -> "Selecione o cadastro do cliente"
                ServiceOrder.name -> "Ordem de Serviço (O.S.)"
                EditServiceOrder.name -> "Editando a Ordem de Serviço (O.S.)"
                EditPaymentFee.name -> "Editando o desconto"
                EditPaymentDiscount.name -> "Editando a taxa de pagamento"
                SalePayment.name -> "Pagamento"
                Discount.name -> "Aplicar Desconto"
                PaymentFee.name -> "Aplicar Taxa"
                Demonstration.name -> "Demonstração"

                CreateNewClientBasicInfo.name -> "Criar novo cliente"
                CreateNewClientAddress.name -> "Criar novo cliente"
                CreateNewClientContact.name -> "Criar novo cliente"

                VisualAcuity.name -> "Acuidade Visual"
                SettingsAndActions.name -> "Configurações e ações"

                AddPerson.name -> "Adicionar pessoa"
                Frames.name -> "Armações"
                AddFrames.name -> "Adicionar armação"
                RealMeasures.name -> "Medidas reais"
                AddRealMeasures.name -> "Adicionar medida real"
                MeasurePicture.name -> "Tire a foto para medida"
                AddAngle.name -> "Adicione cliente e armação"
                PictureAngle.name -> "Tire a foto para o ângulo"

                else -> "Peyess: Vendas"
            }
    }
}