package com.peyess.salesapp.navigation.edit_service_order.payment_fee

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.edit_service_order.payment_fee.EditPaymentFeeScreen

const val saleIdParam = "saleId"
const val fullPriceParam = "fullPriceArgument"

private val editPaymentFeeRoute = SalesAppScreens.EditPaymentFee.name +
        "/{$saleIdParam}" +
        "/{$fullPriceParam}"

fun buildEditPaymentFeeRoute(
    saleId: String,
    fullPrice: Double,
): String {
    return "${SalesAppScreens.EditPaymentFee.name}/$saleId/$fullPrice"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildEditPaymentFeeNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = editPaymentFeeRoute,
        arguments = listOf(
            navArgument(saleIdParam) { type = NavType.StringType },
            navArgument(fullPriceParam) { type = NavType.StringType },
        ),
        enterTransition = editPaymentFeeEnterTransition(),
        exitTransition = editPaymentFeeExitTransition(),
    ) {
        EditPaymentFeeScreen(
            modifier = modifier,
            navHostController = navHostController,
        )
    }
}
