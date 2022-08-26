package com.peyess.salesapp.navigation.sale.service_order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.service_order.ServiceOrderScreen
import com.peyess.salesapp.navigation.sale.prescription.lens_type.prescriptionLensTypeScreenExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildServiceOrderNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = SalesAppScreens.ServiceOrder.name,
        enterTransition = serviceOrderEnterTransition(),
        exitTransition = serviceOrderExitTransition(),
    ) {
        ServiceOrderScreen(modifier = modifier
            .padding(SalesAppTheme.dimensions.screen_offset)
        ) {
            navHostController.navigate(SalesAppScreens.ServiceOrder.name)
        }
    }
}