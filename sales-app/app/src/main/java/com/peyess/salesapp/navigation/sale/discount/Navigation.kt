package com.peyess.salesapp.navigation.sale.discount

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.screen.sale.discount.DiscountScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.theme.SalesAppTheme
import java.math.BigDecimal

const val saleIdArgumentName = "saleId"
const val fullPriceArgumentName = "fullPriceArgument"

private val discountNavRoute = SalesAppScreens.Discount.name +
        "/{$saleIdArgumentName}" +
        "/{$fullPriceArgumentName}"
fun buildDiscountNavRoute(saleId: String, fullPrice: BigDecimal): String {
    return "${SalesAppScreens.Discount.name}/$saleId/$fullPrice"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildDiscountNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = discountNavRoute,
        arguments = listOf(
            navArgument(saleIdArgumentName) { type = NavType.StringType },
            navArgument(fullPriceArgumentName) { type = NavType.StringType },
        ),
        enterTransition = discountEnterTransition(),
        exitTransition = discountExitTransition()
    ) {
        DiscountScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { navHostController.popBackStack() },
        )
    }
}