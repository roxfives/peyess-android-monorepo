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
import com.peyess.salesapp.feature.sale.anamnesis.fifth_step_sports.FifthStepScreen
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.FirstTimeScreen
import com.peyess.salesapp.feature.sale.anamnesis.fourth_step_pain.FourthStepScreen
import com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.SecondStepScreen
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.GraphsScreen
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.SixthStepScreen
import com.peyess.salesapp.feature.sale.anamnesis.third_step_sun_light.ThirdStepScreen
import com.peyess.salesapp.navigation.sale.anamnesis.fifth_step_sports.fifthStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.fifth_step_sports.fifthStepExitTransition
import com.peyess.salesapp.navigation.sale.anamnesis.first_step_first_time.firstStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.first_step_first_time.firstStepExitTransition
import com.peyess.salesapp.navigation.sale.anamnesis.fourth_step_pain.fourthStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.fourth_step_pain.fourthStepExitTransition
import com.peyess.salesapp.navigation.sale.anamnesis.second_step_target_usage.secondStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.second_step_target_usage.secondStepExitTransition
import com.peyess.salesapp.navigation.sale.anamnesis.seventh_step_graphs.seventhStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.seventh_step_graphs.seventhStepExitTransition
import com.peyess.salesapp.navigation.sale.anamnesis.sixth_step_time.sixthStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.sixth_step_time.sixthStepExitTransition
import com.peyess.salesapp.navigation.sale.anamnesis.third_step_sun_light.thirdStepEnterTransition
import com.peyess.salesapp.navigation.sale.anamnesis.third_step_sun_light.thirdStepExitTransition
import com.peyess.salesapp.navigation.sale.lens_pick.buildLensSuggestionNavRoute
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
        enterTransition = secondStepEnterTransition(),
        exitTransition = secondStepExitTransition()
    ) {
        val scrollState = rememberScrollState()

        SecondStepScreen(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisThirdStep.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.AnamnesisThirdStep.name,
        enterTransition = thirdStepEnterTransition(),
        exitTransition = thirdStepExitTransition()
    ) {
        val scrollState = rememberScrollState()

        ThirdStepScreen(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisFourthStep.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.AnamnesisFourthStep.name,
        enterTransition = fourthStepEnterTransition(),
        exitTransition = fourthStepExitTransition()
    ) {
        val scrollState = rememberScrollState()

        FourthStepScreen(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisFifthStep.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.AnamnesisFifthStep.name,
        enterTransition = fifthStepEnterTransition(),
        exitTransition = fifthStepExitTransition()
    ) {
        val scrollState = rememberScrollState()

        FifthStepScreen(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisSixthStep.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.AnamnesisSixthStep.name,
        enterTransition = sixthStepEnterTransition(),
        exitTransition = sixthStepExitTransition()
    ) {
        val scrollState = rememberScrollState()

        SixthStepScreen(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.AnamnesisSeventhStep.name)
        }
    }

    builder.composable(
        route = SalesAppScreens.AnamnesisSeventhStep.name,
        enterTransition = seventhStepEnterTransition(),
        exitTransition = seventhStepExitTransition()
    ) {
        val scrollState = rememberScrollState()

        GraphsScreen(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(SalesAppTheme.dimensions.screen_offset),

            onNext = { saleId, serviceOrderId ->
                val route = buildLensSuggestionNavRoute(
                    isEditing = false,
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                navHostController.navigate(route)
            }
        )
    }
}
