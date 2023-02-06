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
import com.peyess.salesapp.screen.sale.frames.landing.FramesLandingScreen
import com.peyess.salesapp.screen.sale.frames.data.FramesDataScreen
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.navigation.sale.frames.set_frames.setFramesEnterTransition
import com.peyess.salesapp.navigation.sale.frames.set_frames.setFramesExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val serviceOrderParamName = "serviceOrderId"

private val framesDataRoute = SalesAppScreens.SetFramesData.name +
        "/{$serviceOrderParamName}"

fun buildFramesDataRoute(serviceOrderId: String): String {
    return "${SalesAppScreens.SetFramesData.name}/$serviceOrderId"
}

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
            onAddFrames = {
                val route = buildFramesDataRoute(it)

                navHostController.navigate(route)
            },
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
        }
    }

    builder.composable(
        route = framesDataRoute,
        enterTransition = setFramesEnterTransition(),
        exitTransition = setFramesExitTransition()
    ) {
        FramesDataScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
        ) {
            navHostController.popBackStack()
        }
    }
}