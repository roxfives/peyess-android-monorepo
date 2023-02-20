package com.peyess.salesapp.navigation.edit_service_order.service_order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.peyess.salesapp.navigation.client_list.buildClientListRoute
import com.peyess.salesapp.navigation.edit_service_order.editServiceOrderEnterTransition
import com.peyess.salesapp.navigation.edit_service_order.editServiceOrderExitTransition
import com.peyess.salesapp.navigation.edit_service_order.payment.buildEditPaymentNavRoute
import com.peyess.salesapp.navigation.edit_service_order.payment_discount.buildEditPaymentDiscountRoute
import com.peyess.salesapp.navigation.edit_service_order.payment_fee.buildEditPaymentFeeRoute
import com.peyess.salesapp.screen.edit_service_order.service_order.EditServiceOrderScreen

const val serviceOrderIdParam = "serviceOrderId"
const val saleIdParam = "saleId"

val editServiceOrderRoute = SalesAppScreens.EditServiceOrder.name +
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

            onChangeUser = { saleId, serviceOrderId ->
                val isPicking = true
                val pickScenario = PickScenario.EditUser.toName()
                val route = buildClientListRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                    isPicking = isPicking,
                    pickScenario = pickScenario,
                )

                navHostController.navigate(route)
            },

            onChangeResponsible = { saleId, serviceOrderId ->
                val isPicking = true
                val pickScenario = PickScenario.Responsible.toName()
                val route = buildClientListRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                    isPicking = isPicking,
                    pickScenario = pickScenario,
                )

                navHostController.navigate(route)
            },

            onChangeWitness = { saleId, serviceOrderId ->
                val isPicking = true
                val pickScenario = PickScenario.Witness.toName()
                val route = buildClientListRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                    isPicking = isPicking,
                    pickScenario = pickScenario,
                )

                navHostController.navigate(route)
            },

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
            onAddPayment = { saleId, serviceOrderId, paymentId ->
                val isPicking = true
                val pickScenario = PickScenario.EditPayment.toName()
                val route = buildClientListRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                    isPicking = isPicking,
                    pickScenario = pickScenario,
                    paymentId = paymentId,
                )

                navHostController.navigate(route)
            },
            onEditPayment = { paymentId, clientId, saleId, serviceOrderId ->
                val client = clientId.ifBlank { "-" }
                val route = buildEditPaymentNavRoute(
                    paymentId = paymentId,
                    clientId = client,
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                navHostController.navigate(route)
            },

            onAddDiscount = { saleId, fullPrice ->
                navHostController.navigate(buildEditPaymentDiscountRoute(saleId, fullPrice))
            },

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
