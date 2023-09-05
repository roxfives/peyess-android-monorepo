package com.peyess.salesapp.navigation.edit_service_order.payment_discount

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.edit_service_order.payment_discount.EditPaymentDiscountScreen
import com.peyess.salesapp.ui.theme.SalesAppTheme
import java.math.BigDecimal

const val saleIdParam = "saleId"
const val fullPriceParam = "fullPriceArgument"

private val editPaymentDiscountRoute = SalesAppScreens.EditPaymentDiscount.name +
        "/{$saleIdParam}" +
        "/{$fullPriceParam}"

fun buildEditPaymentDiscountRoute(
    saleId: String,
    fullPrice: BigDecimal,
): String {
    return "${SalesAppScreens.EditPaymentDiscount.name}/$saleId/$fullPrice"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildEditPaymentDiscountNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = editPaymentDiscountRoute,
        arguments = listOf(
            navArgument(saleIdParam) { type = NavType.StringType },
            navArgument(fullPriceParam) { type = NavType.StringType },
        ),
        enterTransition = editPaymentDiscountEnterTransition(),
        exitTransition = editPaymentDiscountExitTransition(),
    ) {
        EditPaymentDiscountScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { navHostController.popBackStack() }
        )
    }
}
