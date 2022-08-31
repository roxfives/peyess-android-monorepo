package com.peyess.salesapp.navigation.sale.lens_pick

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.sale.lens_comparison.LensComparisonScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.lens_pick.LensSuggestionScreen
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.navigation.sale.lens_pick.comparison.lensComparisonEnterTransition
import com.peyess.salesapp.navigation.sale.lens_pick.comparison.lensComparisonExitTransition
import com.peyess.salesapp.navigation.sale.lens_pick.suggestion.lensSuggestionEnterTransition
import com.peyess.salesapp.navigation.sale.lens_pick.suggestion.lensSuggestionExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildLensSuggestionNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.LensSuggestion.name,
        enterTransition = lensSuggestionEnterTransition(),
        exitTransition = lensSuggestionExitTransition()
    ) {
        LensSuggestionScreen(
            modifier = modifier,
        ) {
            navHostController.navigate(SalesAppScreens.LensComparison.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.LensComparison.name,
        enterTransition = lensComparisonEnterTransition(),
        exitTransition = lensComparisonExitTransition()
    ) {
        LensComparisonScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            onAddComparison = { navHostController.popBackStack() }
        ) {
            val isPicking = true
            val pickScenario = PickScenario.ServiceOrder.toName()

            navHostController
                .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
        }
    }
}