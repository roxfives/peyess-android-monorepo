package com.peyess.salesapp.navigation.sale.frames

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.sale.frames.landing.FramesLandingScreen
import com.peyess.salesapp.screen.sale.frames.data.FramesDataScreen
import com.peyess.salesapp.typing.general.Eye
import com.peyess.salesapp.navigation.sale.frames.set_frames.setFramesEnterTransition
import com.peyess.salesapp.navigation.sale.frames.set_frames.setFramesExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val isEditingParam = "isEditing"
const val serviceOrderParamName = "serviceOrderId"

private val framesLandingRoute = SalesAppScreens.FramesLanding.name +
        "?$isEditingParam={$isEditingParam}"
private val framesDataRoute = SalesAppScreens.SetFramesData.name +
        "/{$serviceOrderParamName}"

fun buildFramesLandingRoute(isEditing: Boolean = false): String {
    return SalesAppScreens.FramesLanding.name +
            "?$isEditingParam=$isEditing"
}

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
        route = framesLandingRoute,
        enterTransition = framesEnterTransition(),
        exitTransition = framesExitTransition(),
        arguments = listOf(
            navArgument(isEditingParam) {
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) {
        val scrollState = rememberScrollState()

        FramesLandingScreen(
            modifier = modifier
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
            onAddFrames = {
                val route = buildFramesDataRoute(it)

                navHostController.navigate(route)
            },
            onAddMeasure = {
                val eyeParam = if (it is Eye.Left) { "left" } else { "right" }

                navHostController.navigate("${SalesAppScreens.FramesMeasureAnimation.name}/$eyeParam")
            },

            onEditMeasure = {
                val eyeParam = if (it is Eye.Left) { "left" } else { "right" }

                navHostController.navigate("${SalesAppScreens.FramesMeasure.name}/$eyeParam?isEditing=true")
            },

            onNext = { isEditing ->
                 if (isEditing) {
                     navHostController.popBackStack()
                 } else {
                     navHostController.navigate(SalesAppScreens.AnamnesisFirstStep.name)
                 }
            },
        )
    }

    builder.composable(
        route = framesDataRoute,
        enterTransition = setFramesEnterTransition(),
        exitTransition = setFramesExitTransition()
    ) {
        FramesDataScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
            onDone = { navHostController.popBackStack() },
        )
    }
}