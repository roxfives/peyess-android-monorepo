package com.peyess.salesapp.navigation.sale.payment

import androidx.activity.compose.BackHandler
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
import timber.log.Timber

const val paymentIdParam = "paymentId"
const val clientIdParam = "clientId"
const val serviceOrderIdParam = "serviceOrderId"
const val saleIdParam = "saleId"

private val paymentNavRoute = SalesAppScreens.SalePayment.name +
        "/{$paymentIdParam}" +
        "/{$clientIdParam}" +
        "/{$serviceOrderIdParam}" +
        "/{$saleIdParam}"

fun buildPaymentNavRoute(
    paymentId: Long,
    clientId: String,
    serviceOrderId: String,
    saleId: String,
): String {
    return SalesAppScreens.SalePayment.name +
            "/$paymentId" +
            "/$clientId" +
            "/$serviceOrderId" +
            "/$saleId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildPaymentNavGraph(
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
        enterTransition = paymentEnterTransition(),
        exitTransition = paymentExitTransition()
    ) {
        BackHandler(true) {
            // Disable physical back button
        }

        PaymentScreen(
            modifier = modifier
                .padding(SalesAppTheme.dimensions.screen_offset),
            navHostController = navHostController,
        ) {
            navHostController.popBackStack()
        }
    }
}