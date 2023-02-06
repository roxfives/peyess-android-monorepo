package com.peyess.salesapp.navigation.visual_acuity

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.screen.visual_acuity.VisualAcuityScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildVisualAcuityNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.VisualAcuity.name,
        enterTransition = visualAcuityEnterTransition(),
        exitTransition = visualAcuityExitTransition()
    ) {
        val scrollState = rememberScrollState()

        VisualAcuityScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.grid_2),
        )
    }
}