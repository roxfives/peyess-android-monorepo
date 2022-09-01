package com.peyess.salesapp.navigation.sale.anamnesis

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.landing.Landing
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.FirstTimeScreen
import com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.SecondStepScreen
import com.peyess.salesapp.feature.sale.welcome.WelcomeScreen
import com.peyess.salesapp.navigation.sale.anamnesis.first_step_first_time.firstStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.first_step_first_time.firstStepExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildAnamnesisNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.AnamnesisFirstStep.name,
        enterTransition = firstStepEnterTransition(),
        exitTransition = firstStepExitTransition()
    ) {
        FirstTimeScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisSecondStep.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.AnamnesisSecondStep.name,
        enterTransition = firstStepEnterTransition(),
        exitTransition = firstStepExitTransition()
    ) {
        val scrollState = rememberScrollState()

        SecondStepScreen(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisSecondStep.name)
        }
    }
}