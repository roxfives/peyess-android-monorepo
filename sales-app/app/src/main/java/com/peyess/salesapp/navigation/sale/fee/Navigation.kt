package com.peyess.salesapp.navigation.sale.fee

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.sale.fee.PaymentFeeScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.theme.SalesAppTheme
import java.math.BigDecimal

const val saleIdArgumentName = "saleId"
const val fullPriceArgumentName = "fullPriceArgument"

private val feeNavRoute = SalesAppScreens.PaymentFee.name +
        "/{$saleIdArgumentName}" +
        "/{$fullPriceArgumentName}"
fun buildFeeNavRoute(saleId: String, fullPrice: BigDecimal): String {
    return "${SalesAppScreens.PaymentFee.name}/$saleId/$fullPrice"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildFeeNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = feeNavRoute,
        arguments = listOf(
            navArgument(saleIdArgumentName) { type = NavType.StringType },
            navArgument(fullPriceArgumentName) { type = NavType.StringType },
        ),
        enterTransition = paymentFeeEnterTransition(),
        exitTransition = paymentFeeExitTransition()
    ) {
        PaymentFeeScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { navHostController.popBackStack() },
        )
    }
}