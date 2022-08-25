package com.peyess.salesapp.navigation

import java.lang.IllegalArgumentException

enum class SalesAppScreens {
    Landing,
    Home,
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
    LensSuggestion,
    LensComparison,
    PickClient,
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
                LensSuggestion.name -> LensSuggestion
                LensComparison.name -> LensComparison
                PickClient.name -> PickClient
                Home.name -> Home
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
                Home.name -> "Medidas"
                AddMeasure.name -> "Adicionar medida"
                People.name -> "Pessoas"
                UserAuth.name -> "Entrar na loja"
                LocalPasscode.name -> "Criar senha para acesso local"
                SaleWelcome.name -> "Vamos começar!"
                SalePrescriptionLensType.name -> "Vamos aos dados da sua receita"
                SalePrescriptionPicture.name -> "Vamos aos dados da sua receita"
                SalePrescriptionData.name -> "Vamos aos dados da sua receita"
                SalePrescriptionDataSymptoms.name -> "Simtomas e Curiosidades"
                FramesLanding.name -> "Vamos escolher e medir a sua armação"
                SetFramesData.name -> "Vamos escolher e medir a sua armação"
                FramesMeasureAnimation.name -> "Vamos a sua medição"
                FramesMeasureTakePicture.name -> "Vamos a sua medição"
                FramesMeasure.name -> "Vamos a sua medição"
                LensSuggestion.name -> "Sugestões de lentes"
                LensComparison.name -> "Comparação da lente"
                PickClient.name -> "Comparação da lente"
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