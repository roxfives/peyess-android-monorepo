package com.peyess.salesapp.navigation.sale.prescription

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.prescription_picture.PrescriptionPictureScreen
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildPrescriptionScreenNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.SaleWelcome.name,
        enterTransition = prescriptionScreenEnterTransition(),
        exitTransition = prescriptionScreenExitTransition(),
    ) {
        PrescriptionPictureScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.Home.name)
        }
    }
}