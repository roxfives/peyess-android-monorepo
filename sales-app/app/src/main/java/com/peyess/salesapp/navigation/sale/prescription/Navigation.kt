package com.peyess.salesapp.navigation.sale.prescription

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.screen.sale.prescription_data.PrescriptionDataScreen
import com.peyess.salesapp.screen.sale.prescription_data.PrescriptionSymptomsScreen
import com.peyess.salesapp.screen.sale.prescription_lens_type.PrescriptionLensTypeScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.sale.frames.buildFramesLandingRoute
import com.peyess.salesapp.screen.sale.prescription_picture.PrescriptionPictureScreen
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenExitTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataSymptomsScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataSymptomsScreenExitTransition
import com.peyess.salesapp.navigation.sale.prescription.lens_type.prescriptionLensTypeScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.lens_type.prescriptionLensTypeScreenExitTransition
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderRoute
import com.peyess.salesapp.ui.theme.SalesAppTheme
import timber.log.Timber

const val isUpdatingParam = "isUpdating"

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
            val isUpdating = false

            navHostController.navigate("${SalesAppScreens.SalePrescriptionPicture.name}/$isUpdating")
        }
    }

    builder.composable(
        route = "${SalesAppScreens.SalePrescriptionPicture.name}/{$isUpdatingParam}",
        arguments = listOf(
            navArgument(isUpdatingParam) { type = NavType.BoolType }
        ),
        enterTransition = prescriptionScreenEnterTransition(),
        exitTransition = prescriptionScreenExitTransition(),
    ) {
        PrescriptionPictureScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
        ) { isUpdating ->
            Timber.i("Is updating picture: $isUpdating")
            navHostController.navigate("${SalesAppScreens.SalePrescriptionData.name}/$isUpdating")
        }
    }

    builder.composable(
        route = "${SalesAppScreens.SalePrescriptionData.name}/{$isUpdatingParam}",
        arguments = listOf(
            navArgument(isUpdatingParam) { type = NavType.BoolType }
        ),
        enterTransition = prescriptionDataScreenEnterTransition(),
        exitTransition = prescriptionDataScreenExitTransition(),
    ) {
        val scrollState = rememberScrollState()

        PrescriptionDataScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
            onShowSymptoms = {
                navHostController.navigate(SalesAppScreens.SalePrescriptionDataSymptoms.name)
            },

            onNext = { isUpdating, saleId, serviceOrderId ->
                val route = buildServiceOrderRoute(
                    isCreating = true,
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                if (isUpdating) {
                    navHostController.navigate(route) {
                        popUpTo(route) { inclusive = true }
                    }
                } else {
                    val framesRoute = buildFramesLandingRoute(isEditing = false)

                    navHostController.navigate(framesRoute)
                }
            }
        )
    }

    builder.composable(
        route = SalesAppScreens.SalePrescriptionDataSymptoms.name,
        enterTransition = prescriptionDataSymptomsScreenEnterTransition(),
        exitTransition = prescriptionDataSymptomsScreenExitTransition(),
    ) {
        val scrollState = rememberScrollState()

        PrescriptionSymptomsScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.popBackStack()
        }
    }
}