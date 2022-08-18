package com.peyess.salesapp.navigation.sale.frames_measure

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.frames_measure.LandingAnimationHelperScreen
import com.peyess.salesapp.feature.sale.frames_measure.TakePictureScreen
import com.peyess.salesapp.navigation.sale.frames_measure.landing.landingAnimationEnterTransition
import com.peyess.salesapp.navigation.sale.frames_measure.landing.landingAnimationExitTransition
import com.peyess.salesapp.navigation.sale.frames_measure.measure.measureFramesEnterTransition
import com.peyess.salesapp.navigation.sale.frames_measure.measure.measureFramesExitTransition
import com.peyess.salesapp.navigation.sale.frames_measure.picture.measureTakePictureEnterTransition
import com.peyess.salesapp.navigation.sale.frames_measure.picture.measureTakePictureExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildFramesMeasureNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = "${SalesAppScreens.FramesMeasureAnimation.name}/{eye}",
        arguments = listOf(
            navArgument("eye") { type = NavType.StringType }
        ),
        enterTransition = landingAnimationEnterTransition(),
        exitTransition = landingAnimationExitTransition()
    ) {
        LandingAnimationHelperScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
        ) {
            val eyeParam = if (it is Eye.Left) {
                "left"
            } else {
                "right"
            }

            navHostController.navigate("${SalesAppScreens.FramesMeasureTakePicture.name}/$eyeParam")
        }
    }

    builder.composable(
        route = "${SalesAppScreens.FramesMeasureTakePicture.name}/{eye}",
        arguments = listOf(
            navArgument("eye") { type = NavType.StringType }
        ),
        enterTransition = measureTakePictureEnterTransition(),
        exitTransition = measureTakePictureExitTransition()
    ) {
        TakePictureScreen(
            modifier = modifier
                .fillMaxSize(),
            navHostController = navHostController,
        ) {
            val eyeParam = if (it is Eye.Left) {
                "left"
            } else {
                "right"
            }

            navHostController.navigate("${SalesAppScreens.FramesMeasure.name}/$eyeParam")
        }
    }


    builder.composable(
        route = SalesAppScreens.FramesMeasure.name,
        enterTransition = measureFramesEnterTransition(),
        exitTransition = measureFramesExitTransition()
    ) {
//        LandingAnimationHelperScreen(
//            modifier = modifier
//                .padding(SalesAppTheme.dimensions.screen_offset),
//        ) {
//            navHostController.navigate(SalesAppScreens.FramesMeasureTakePicture.name)
//        }
    }
}