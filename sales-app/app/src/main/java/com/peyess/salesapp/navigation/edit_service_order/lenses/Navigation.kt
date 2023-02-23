package com.peyess.salesapp.navigation.edit_service_order.lenses

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.edit_service_order.lenses.lens_suggestion.editLensSuggestionEnterTransition
import com.peyess.salesapp.navigation.edit_service_order.lenses.lens_suggestion.editLensSuggestionExitTransition
import com.peyess.salesapp.navigation.sale.lenses.comparison.lensComparisonEnterTransition
import com.peyess.salesapp.navigation.sale.lenses.comparison.lensComparisonExitTransition
import com.peyess.salesapp.screen.edit_service_order.lens_suggestion.EditLensSuggestionScreen

const val saleIdArgumentName = "saleId"
const val soIdArgumentName = "soId"

val editLensSuggestionNavRoute = SalesAppScreens.EditLensSuggestion.name +
        "/{$saleIdArgumentName}" +
        "/{$soIdArgumentName}"

val editLensComparisonNavRoute = SalesAppScreens.EditLensComparison.name +
        "/{$saleIdArgumentName}" +
        "/{$soIdArgumentName}"

fun buildEditLensSuggestionNavRoute(
    saleId: String = "",
    serviceOrderId: String = "",
): String {
    return SalesAppScreens.EditLensSuggestion.name +
            "/$saleId" +
            "/$serviceOrderId"
}

fun buildEditLensComparisonNavRoute(
    saleId: String,
    serviceOrderId: String,
): String {
    return SalesAppScreens.EditLensComparison.name +
            "/$saleId" +
            "/$serviceOrderId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildEditLensesNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = editLensSuggestionNavRoute,
        arguments = listOf(
            navArgument(saleIdArgumentName) { type = NavType.StringType },
            navArgument(soIdArgumentName) { type = NavType.StringType },
        ),
        enterTransition = editLensSuggestionEnterTransition(),
        exitTransition = editLensSuggestionExitTransition()
    ) {
        EditLensSuggestionScreen(
            modifier = modifier,
            navHostController = navHostController,
            onLensPicked = { saleId, serviceOrderId ->
                val route = buildEditLensComparisonNavRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                navHostController.navigate(route)
            }
        )
    }

    builder.composable(
        route = editLensComparisonNavRoute,
        arguments = listOf(
            navArgument(saleIdArgumentName) { type = NavType.StringType },
            navArgument(soIdArgumentName) { type = NavType.StringType },
        ),
        enterTransition = lensComparisonEnterTransition(),
        exitTransition = lensComparisonExitTransition()
    ) {
//        LensComparisonScreen(
//            modifier = modifier
//                .padding(SalesAppTheme.dimensions.screen_offset),
//            navHostController = navHostController,
//            onAddComparison = { navHostController.popBackStack() },
//            onLensPicked = { isEditing, saleId, serviceOrderId ->
//                if (isEditing) {
//                    val route = buildServiceOrderRoute(
//                        isCreating = true,
//                        saleId = saleId,
//                        serviceOrderId = serviceOrderId,
//                    )
//
//                    navHostController.navigate(route) {
//                        popUpTo(serviceOrderRoute) { inclusive = true }
//                    }
//                } else {
//                    val isPicking = true
//                    val pickScenario = PickScenario.ServiceOrder.toName()
//                    val route = buildClientListRoute(
//                        saleId = saleId,
//                        serviceOrderId = serviceOrderId,
//                        isPicking = isPicking,
//                        pickScenario = pickScenario,
//                    )
//
//                    navHostController.navigate(route)
//                }
//            }
//        )
    }
}
