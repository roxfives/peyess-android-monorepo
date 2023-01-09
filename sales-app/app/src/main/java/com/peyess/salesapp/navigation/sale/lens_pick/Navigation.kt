package com.peyess.salesapp.navigation.sale.lens_pick

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.sale.lens_comparison.LensComparisonScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.lens_pick.ui.LensSuggestionScreen
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.navigation.sale.lens_pick.comparison.lensComparisonEnterTransition
import com.peyess.salesapp.navigation.sale.lens_pick.comparison.lensComparisonExitTransition
import com.peyess.salesapp.navigation.sale.lens_pick.suggestion.lensSuggestionEnterTransition
import com.peyess.salesapp.navigation.sale.lens_pick.suggestion.lensSuggestionExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val isEditingParam = "isEditing"
const val saleIdArgumentName = "saleId"
const val soIdArgumentName = "soId"

val lensSuggestionNavRoute = SalesAppScreens.LensSuggestion.name +
        "/{$isEditingParam}" +
        "?$saleIdArgumentName={$saleIdArgumentName}" +
        "?$soIdArgumentName={$soIdArgumentName}"

// TODO: remove this crap after refactoring the suggestions features into three different ones
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
        ) {
            navHostController.navigate("${SalesAppScreens.LensComparison.name}/$it")
        }
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
        ) {
            navHostController.navigate("${SalesAppScreens.LensComparison.name}/$it")
        }
    }

    builder.composable(
        route = "${SalesAppScreens.LensComparison.name}/{$isEditingParam}",
        arguments = listOf(
            navArgument(isEditingParam) { type = NavType.BoolType }
        ),enterTransition = lensComparisonEnterTransition(),
        exitTransition = lensComparisonExitTransition()
    ) {
        LensComparisonScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
            onAddComparison = { navHostController.popBackStack() }
        ) { isEditing ->

            val isPicking = true
            val pickScenario = PickScenario.ServiceOrder.toName()

            if (isEditing) {
                navHostController.navigate(SalesAppScreens.ServiceOrder.name) {
                    popUpTo(SalesAppScreens.ServiceOrder.name) { inclusive = true }
                }
            } else {
                navHostController
                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
            }
        }
    }
}