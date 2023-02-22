package com.peyess.salesapp.navigation.edit_service_order.prescription

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
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.edit_service_order.service_order.editServiceOrderRoute
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenEnterTransition
import com.peyess.salesapp.navigation.sale.prescription.data.prescriptionDataScreenExitTransition
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_data.EditPrescriptionDataScreen
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_picture.EditPrescriptionPictureScreen
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_symptoms.EditPrescriptionSymptomsScreen
import com.peyess.salesapp.screen.edit_service_order.prescription.prescription_symptoms.state.EditPrescriptionSymptomsState
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val saleIdParam = "saleId"
const val serviceOrderIdParam = "serviceOrderId"
const val prescriptionIdParam = "prescriptionId"

val editPrescriptionPictureRoute = SalesAppScreens.EditPrescriptionPicture.name +
        "/{$saleIdParam}" +
        "/{$serviceOrderIdParam}" +
        "/{$prescriptionIdParam}"
val editPrescriptionDataRoute = SalesAppScreens.EditPrescriptionData.name +
        "/{$saleIdParam}" +
        "/{$serviceOrderIdParam}" +
        "/{$prescriptionIdParam}"
val editPrescriptionSymptomsRoute = SalesAppScreens.EditPrescriptionSymptoms.name +
        "/{$saleIdParam}" +
        "/{$serviceOrderIdParam}" +
        "/{$prescriptionIdParam}"

fun buildEditPrescriptionPictureRoute(
    saleId: String,
    serviceOrderId: String,
    prescriptionId: String,
): String {
    return SalesAppScreens.EditPrescriptionPicture.name +
            "/$saleId" +
            "/$serviceOrderId" +
            "/$prescriptionId"
}

fun buildEditPrescriptionDataRoute(
    saleId: String,
    serviceOrderId: String,
    prescriptionId: String,
): String {
    return SalesAppScreens.EditPrescriptionData.name +
            "/$saleId" +
            "/$serviceOrderId" +
            "/$prescriptionId"
}

fun buildEditPrescriptionSymptomsRoute(
    saleId: String,
    serviceOrderId: String,
    prescriptionId: String,
): String {
    return SalesAppScreens.EditPrescriptionSymptoms.name +
            "/$saleId" +
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
        route = editPrescriptionPictureRoute,
        arguments = listOf(
            navArgument(saleIdParam) { type = NavType.StringType },
            navArgument(serviceOrderIdParam) { type = NavType.StringType },
            navArgument(prescriptionIdParam) { type = NavType.StringType },
        ),
        enterTransition = editPrescriptionPictureEnterTransition(),
        exitTransition = editPrescriptionPictureExitTransition(),
    ) {
        EditPrescriptionPictureScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,

            onNext = { saleId, serviceOrderId, prescriptionId ->
                val route = buildEditPrescriptionDataRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                    prescriptionId = prescriptionId,
                )

                navHostController.navigate(route)
            }
        )
    }

    builder.composable(
        route = editPrescriptionDataRoute,
        arguments = listOf(
            navArgument(saleIdParam) { type = NavType.StringType },
            navArgument(serviceOrderIdParam) { type = NavType.StringType },
            navArgument(prescriptionIdParam) { type = NavType.StringType },
        ),
        enterTransition = editPrescriptionDataEnterTransition(),
        exitTransition = editPrescriptionDataExitTransition(),
    ) {
        val scrollState = rememberScrollState()

        EditPrescriptionDataScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
            onShowSymptoms = { saleId, serviceOrderId, prescriptionId ->
                val route = buildEditPrescriptionSymptomsRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                    prescriptionId = prescriptionId,
                )

                navHostController.navigate(route)
            },

            onNext = {
                navHostController.popBackStack(editServiceOrderRoute, false)
            }
        )
    }

    builder.composable(
        route = editPrescriptionSymptomsRoute,
        arguments = listOf(
            navArgument(saleIdParam) { type = NavType.StringType },
            navArgument(serviceOrderIdParam) { type = NavType.StringType },
            navArgument(prescriptionIdParam) { type = NavType.StringType },
        ),
        enterTransition = editPrescriptionSymptomsEnterTransition(),
        exitTransition = editPrescriptionSymptomsExitTransition(),
    ) {
        val scrollState = rememberScrollState()

        EditPrescriptionSymptomsScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,

            onNext = {
                navHostController.popBackStack(editPrescriptionDataRoute, false)
            }
        )
    }
}
