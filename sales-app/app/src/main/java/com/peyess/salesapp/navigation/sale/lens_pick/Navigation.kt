package com.peyess.salesapp.navigation.sale.lens_pick

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.screen.sale.lens_comparison.LensComparisonScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.sale.lens_pick.ui.LensSuggestionScreen
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.peyess.salesapp.navigation.client_list.buildClientListRoute
import com.peyess.salesapp.navigation.sale.lens_pick.comparison.lensComparisonEnterTransition
import com.peyess.salesapp.navigation.sale.lens_pick.comparison.lensComparisonExitTransition
import com.peyess.salesapp.navigation.sale.lens_pick.suggestion.lensSuggestionEnterTransition
import com.peyess.salesapp.navigation.sale.lens_pick.suggestion.lensSuggestionExitTransition
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderRoute
import com.peyess.salesapp.navigation.sale.service_order.serviceOrderRoute
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val isEditingParam = "isEditing"
const val saleIdArgumentName = "saleId"
const val soIdArgumentName = "soId"

private val lensSuggestionNavRoute = SalesAppScreens.LensSuggestion.name +
        "/{$isEditingParam}" +
        "?$saleIdArgumentName={$saleIdArgumentName}" +
        "?$soIdArgumentName={$soIdArgumentName}"

private val lensComparisonNavRoute = SalesAppScreens.LensComparison.name +
        "/{$isEditingParam}" +
        "/{$saleIdArgumentName}" +
        "/{$soIdArgumentName}"

// TODO: remove this crap after refactoring the suggestions features into three different ones
//  (lenses table, lenses suggestion, lenses comparison)
private val lensSuggestionWithoutSuggestionsNavRoute =
    SalesAppScreens.LensSuggestionWithoutSuggestions.name +
        "/{$isEditingParam}" +
        "?$saleIdArgumentName={$saleIdArgumentName}" +
        "?$soIdArgumentName={$soIdArgumentName}"

fun buildLensSuggestionNavRoute(
    isEditing: Boolean,
    showSuggestions: Boolean,
    saleId: String = "",
    serviceOrderId: String = "",
): String {
    return if (!showSuggestions) {
        "${SalesAppScreens.LensSuggestionWithoutSuggestions.name}/$isEditing"
    } else {
        SalesAppScreens.LensSuggestion.name +
                "/$isEditing" +
                "?$saleIdArgumentName=$saleId" +
                "?$soIdArgumentName=$serviceOrderId"
    }
}

fun buildLensComparisonNavRoute(
    isEditing: Boolean,
    saleId: String,
    serviceOrderId: String,
): String {
    return SalesAppScreens.LensComparison.name +
            "/$isEditing" +
            "/$saleId" +
            "/$serviceOrderId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildLensSuggestionNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = lensSuggestionNavRoute,
        arguments = listOf(
            navArgument(isEditingParam) { type = NavType.BoolType },
            navArgument(saleIdArgumentName) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(soIdArgumentName) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
        enterTransition = lensSuggestionEnterTransition(),
        exitTransition = lensSuggestionExitTransition()
    ) {
        LensSuggestionScreen(
            modifier = modifier,
            navHostController = navHostController,
            showSuggestions = true,
            onLensPicked = { isEditingParam, saleId, serviceOrderId ->
                val route = buildLensComparisonNavRoute(
                    isEditing = isEditingParam,
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                navHostController.navigate(route)
            }
        )
    }

    builder.composable(
        route = lensSuggestionWithoutSuggestionsNavRoute,
        arguments = listOf(
            navArgument(isEditingParam) { type = NavType.BoolType },
            navArgument(saleIdArgumentName) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(soIdArgumentName) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
        enterTransition = lensSuggestionEnterTransition(),
        exitTransition = lensSuggestionExitTransition()
    ) {
        LensSuggestionScreen(
            modifier = modifier,
            showSuggestions = false,
            navHostController = navHostController,
        )
    }

    builder.composable(
        route = lensComparisonNavRoute,
        arguments = listOf(
            navArgument(isEditingParam) { type = NavType.BoolType },
            navArgument(saleIdArgumentName) { type = NavType.StringType },
            navArgument(soIdArgumentName) { type = NavType.StringType },
        ),
        enterTransition = lensComparisonEnterTransition(),
        exitTransition = lensComparisonExitTransition()
    ) {
        LensComparisonScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
            onAddComparison = { navHostController.popBackStack() },
            onLensPicked = { isEditing, saleId, serviceOrderId ->
                if (isEditing) {
                    val route = buildServiceOrderRoute(
                        isCreating = true,
                        saleId = saleId,
                        serviceOrderId = serviceOrderId,
                    )

                    navHostController.navigate(route) {
                        popUpTo(serviceOrderRoute) { inclusive = true }
                    }
                } else {
                    val isPicking = true
                    val pickScenario = PickScenario.ServiceOrder.toName()
                    val route = buildClientListRoute(
                        saleId = saleId,
                        serviceOrderId = serviceOrderId,
                        isPicking = isPicking,
                        pickScenario = pickScenario,
                    )

                    navHostController.navigate(route)
                }
            }
        )
    }
}
