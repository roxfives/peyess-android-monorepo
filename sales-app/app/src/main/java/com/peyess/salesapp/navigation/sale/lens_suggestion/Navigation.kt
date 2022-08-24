package com.peyess.salesapp.navigation.sale.lens_suggestion

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.lens_pick.LensSuggestionScreen
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
            modifier = modifier
//                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.LensComparison.name)
        }
    }
}