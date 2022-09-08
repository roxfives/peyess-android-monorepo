package com.peyess.salesapp.navigation.sale.frames

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.landing.Landing
import com.peyess.salesapp.feature.sale.frames.FramesLandingScreen
import com.peyess.salesapp.feature.sale.frames.SetFramesScreen
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.welcome.WelcomeScreen
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildFramesNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.FramesLanding.name,
        enterTransition = framesEnterTransition(),
        exitTransition = framesExitTransition()
    ) {
        val scrollState = rememberScrollState()

        FramesLandingScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset),
            onAddFrames = { navHostController.navigate(SalesAppScreens.SetFramesData.name) },
            onAddMeasure = {
                val eyeParam = if (it is Eye.Left) {
                    "left"
                } else {
                    "right"
                }

                navHostController.navigate("${SalesAppScreens.FramesMeasureAnimation.name}/$eyeParam")
            }
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisFirstStep.name)

//            val isEditing= false
//
//            navHostController.navigate("${SalesAppScreens.LensSuggestion.name}/$isEditing")
        }
    }

    builder.composable(
        route = SalesAppScreens.SetFramesData.name,
        enterTransition = framesEnterTransition(),
        exitTransition = framesExitTransition()
    ) {
        SetFramesScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
        ) {
            navHostController.popBackStack()
        }
    }
}