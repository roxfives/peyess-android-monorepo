package com.peyess.salesapp.navigation.edit_service_order.service_order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.edit_service_order.editServiceOrderEnterTransition
import com.peyess.salesapp.navigation.edit_service_order.editServiceOrderExitTransition
import com.peyess.salesapp.navigation.edit_service_order.payment_discount.buildEditPaymentDiscountRoute
import com.peyess.salesapp.navigation.edit_service_order.payment_fee.buildEditPaymentFeeRoute
import com.peyess.salesapp.screen.edit_service_order.service_order.EditServiceOrderScreen

const val serviceOrderIdParam = "serviceOrderId"
const val saleIdParam = "saleId"

private val editServiceOrderRoute = SalesAppScreens.EditServiceOrder.name +
        "/{$saleIdParam}" +
        "/{$serviceOrderIdParam}"

fun buildEditServiceOrderRoute(
    saleId: String,
    serviceOrderId: String,
): String {
    return SalesAppScreens.EditServiceOrder.name +
            "/$saleId" +
            "/$serviceOrderId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildEditServiceOrderNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = editServiceOrderRoute,
        arguments = listOf(
            navArgument(saleIdParam) { type = NavType.StringType },
            navArgument(serviceOrderIdParam) { type = NavType.StringType },
        ),
        enterTransition = editServiceOrderEnterTransition(),
        exitTransition = editServiceOrderExitTransition(),
    ) {
        EditServiceOrderScreen(
            modifier = modifier,
            navHostController = navHostController,

//            onChangeUser = {
//                val isPicking = true
//                val pickScenario = PickScenario.User.toName()
//
//                navHostController
//                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
//            },
//
//            onChangeResponsible = {
//                val isPicking = true
//                val pickScenario = PickScenario.Responsible.toName()
//
//                navHostController
//                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
//            },
//
//            onChangeWitness = {
//                val isPicking = true
//                val pickScenario = PickScenario.Witness.toName()
//
//                navHostController
//                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
//            },
//
//            onEditPrescription = {
//                val isUpdating = true
//
//                navHostController
//                    .navigate("${SalesAppScreens.SalePrescriptionPicture}/$isUpdating")
//            },
//
//            onEditProducts = { saleId, serviceOrderId ->
//                val route = buildLensSuggestionNavRoute(
//                    isEditing = true,
//                    showSuggestions = true,
//                    saleId = saleId,
//                    serviceOrderId = serviceOrderId,
//                )
//
//                navHostController
//                    .navigate(route)
//            },
//
//            onAddPayment = {
//                val isPicking = true
//                val pickScenario = PickScenario.Payment.toName()
//                val paymentId = it
//
//                navHostController
//                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario?$paymentIdParam=$paymentId")
//            },
//
//            onEditPayment = { paymentId, clientId, saleId, serviceOrderId ->
//                val client = clientId.ifBlank { "-" }
//                val route = buildPaymentNavRoute(
//                    paymentId = paymentId,
//                    clientId = client,
//                    saleId = saleId,
//                    serviceOrderId = serviceOrderId,
//                )
//
//                navHostController.navigate(route)
//            },
//
            onAddDiscount = { saleId, fullPrice ->
                navHostController.navigate(buildEditPaymentDiscountRoute(saleId, fullPrice))
            },
//
            onAddPaymentFee = { saleId, fullPrice ->
                navHostController.navigate(buildEditPaymentFeeRoute(saleId, fullPrice))
            },
//
//            onFinishSale = {
//                navHostController.navigate(SalesAppScreens.Home.name) {
//                    popUpTo(SalesAppScreens.Home.name) { inclusive = true }
//                }
//            },
        )
    }
}
