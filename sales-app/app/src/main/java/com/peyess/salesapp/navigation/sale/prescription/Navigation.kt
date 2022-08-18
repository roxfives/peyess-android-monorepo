package com.peyess.salesapp.navigation.sale.prescription

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.sale.prescription_data.PrescriptionDataScreen
import com.peyess.salesapp.feature.sale.prescription_data.PrescriptionDataSymptomsScreen
import com.peyess.salesapp.feature.sale.prescription_lens_type.PrescriptionLensTypeScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.prescription_picture.PrescriptionPictureScreen
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenExitTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataSymptomsScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataSymptomsScreenExitTransition
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
            navHostController.navigate(SalesAppScreens.SalePrescriptionData.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.SalePrescriptionData.name,
        enterTransition = prescriptionDataScreenEnterTransition(),
        exitTransition = prescriptionDataScreenExitTransition(),
    ) {
        val scrollState = rememberScrollState()

        PrescriptionDataScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset),
            onShowSymptoms = {
                navHostController.navigate(SalesAppScreens.SalePrescriptionDataSymptoms.name)
            }
        ) {
            navHostController.navigate(SalesAppScreens.FramesLanding.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.SalePrescriptionDataSymptoms.name,
        enterTransition = prescriptionDataSymptomsScreenEnterTransition(),
        exitTransition = prescriptionDataSymptomsScreenExitTransition(),
    ) {
        val scrollState = rememberScrollState()

        PrescriptionDataSymptomsScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.popBackStack()
        }
    }
}