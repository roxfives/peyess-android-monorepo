package com.peyess.salesapp.navigation.edit_service_order.payment

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.screen.sale.payment.PaymentScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val paymentIdParam = "paymentId"
const val clientIdParam = "clientId"
const val serviceOrderIdParam = "serviceOrderId"
const val saleIdParam = "saleId"

private val paymentNavRoute = SalesAppScreens.EditSalePayment.name +
        "/{$paymentIdParam}" +
        "/{$clientIdParam}" +
        "/{$serviceOrderIdParam}" +
        "/{$saleIdParam}"

fun buildEditPaymentNavRoute(
    paymentId: Long,
    clientId: String,
    serviceOrderId: String,
    saleId: String,
): String {
    return SalesAppScreens.EditSalePayment.name +
            "/$paymentId" +
            "/$clientId" +
            "/$serviceOrderId" +
            "/$saleId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildEditPaymentNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = paymentNavRoute,
        arguments = listOf(
            navArgument(paymentIdParam) { type = NavType.LongType },
            navArgument(clientIdParam) { type = NavType.StringType },
            navArgument(serviceOrderIdParam) { type = NavType.StringType },
            navArgument(saleIdParam) { type = NavType.StringType },
        ),
        enterTransition = editPaymentEnterTransition(),
        exitTransition = editPaymentExitTransition()
    ) {
        PaymentScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
        ) {
            navHostController.popBackStack()
        }
    }
}
