package com.peyess.salesapp.navigation.sale.payment

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.sale.payment.PaymentScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildPaymentNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = "${SalesAppScreens.SalePayment.name}/{paymentId}/{clientId}",
        arguments = listOf(
            navArgument("paymentId") { type = NavType.LongType },
            navArgument("clientId") { type = NavType.StringType },
        ),
        enterTransition = paymentEnterTransition(),
        exitTransition = paymentExitTransition()
    ) {
        PaymentScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
        ) {
            navHostController.popBackStack()
        }
    }
}