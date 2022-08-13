package com.peyess.salesapp.navigation.sale.prescription

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.sale.prescription_lens_type.PrescriptionLensTypeScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.prescription_picture.PrescriptionPictureScreen
import com.peyess.salesapp.navigation.sale.prescription.lens_type.prescriptionLensTypeScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.lens_type.prescriptionLensTypeScreenExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildPrescriptionScreenNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.SalePrescriptionLensType.name,
        enterTransition = prescriptionLensTypeScreenEnterTransition(),
        exitTransition = prescriptionLensTypeScreenExitTransition(),
    ) {
        PrescriptionLensTypeScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.SalePrescriptionPicture.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.SalePrescriptionPicture.name,
        enterTransition = prescriptionScreenEnterTransition(),
        exitTransition = prescriptionScreenExitTransition(),
    ) {
        PrescriptionPictureScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.Home.name)
        }
    }

//    builder.composable(
//        route = SalesAppScreens.SalePrescriptionData.name,
//        enterTransition = prescriptionDataScreenEnterTransition(),
//        exitTransition = prescriptionDataScreenExitTransition(),
//    ) {
//        PrescriptionPictureScreen(
//            modifier = modifier
//                .padding(SalesAppTheme.dimensions.screen_offset)
//        ) {
////            navHostController.navigate(SalesAppScreens.Home.name)
//        }
//    }
}