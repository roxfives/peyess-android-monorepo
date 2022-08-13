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