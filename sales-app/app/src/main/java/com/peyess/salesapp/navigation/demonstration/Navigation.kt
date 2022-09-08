package com.peyess.salesapp.navigation.demonstration

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.demonstration.DemonstrationScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildDemonstrationNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.Demonstration.name,
        enterTransition = demonstrationEnterTransition(),
        exitTransition = demonstrationExitTransition()
    ) {
        val scrollState = rememberScrollState()

        DemonstrationScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.grid_2)
                .verticalScroll(scrollState),
            onDone = {
                navHostController.popBackStack()
            }
        )
    }
}