package com.peyess.salesapp.navigation.edit_service_order.prescription

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenExitTransition
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_picture.EditPrescriptionPictureScreen
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val serviceOrderIdParam = "serviceOrderId"
const val prescriptionIdParam = "prescriptionId"

val editPrescriptionDataRoute = SalesAppScreens.EditPrescriptionData.name +
        "/{$serviceOrderIdParam}" +
        "/{$prescriptionIdParam}"
val editPrescriptionPictureRoute = SalesAppScreens.EditPrescriptionPicture.name +
        "/{$serviceOrderIdParam}" +
        "/{$prescriptionIdParam}"

fun buildEditPrescriptionDataRoute(
    serviceOrderId: String,
    prescriptionId: String,
): String {
    return SalesAppScreens.EditPrescriptionData.name +
            "/$serviceOrderId" +
            "/$prescriptionId"
}

fun buildEditPrescriptionPictureRoute(
    serviceOrderId: String,
    prescriptionId: String,
): String {
    return SalesAppScreens.EditPrescriptionPicture.name +
            "/$serviceOrderId" +
            "/$prescriptionId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildEditPrescriptionNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = editPrescriptionDataRoute,
        arguments = listOf(
            navArgument(prescriptionIdParam) { type = NavType.StringType },
        ),
        enterTransition = editPrescriptionEnterTransition(),
        exitTransition = editPrescriptionExitTransition(),
    ) {
        EditPrescriptionPictureScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,

            onNext = {}
        )
    }

    builder.composable(
        route = editPrescriptionPictureRoute,
        arguments = listOf(
            navArgument(prescriptionIdParam) { type = NavType.StringType },
        ),
        enterTransition = prescriptionDataScreenEnterTransition(),
        exitTransition = prescriptionDataScreenExitTransition(),
    ) {
//        val scrollState = rememberScrollState()
//
//        PrescriptionDataScreen(
//            modifier = modifier
//                .verticalScroll(scrollState)
//                .padding(SalesAppTheme.dimensions.screen_offset),
//            navHostController = navHostController,
//            onShowSymptoms = {
//                navHostController.navigate(SalesAppScreens.SalePrescriptionDataSymptoms.name)
//            },
//
//            onNext = { isUpdating, saleId, serviceOrderId ->
//                val route = buildServiceOrderRoute(
//                    isCreating = true,
//                    saleId = saleId,
//                    serviceOrderId = serviceOrderId,
//                )
//
//                if (isUpdating) {
//                    navHostController.navigate(route) {
//                        popUpTo(route) { inclusive = true }
//                    }
//                } else {
//                    navHostController.navigate(SalesAppScreens.FramesLanding.name)
//                }
//            }
//        )
    }
}
