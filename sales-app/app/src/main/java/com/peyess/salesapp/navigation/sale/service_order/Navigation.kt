package com.peyess.salesapp.navigation.sale.service_order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.sale.service_order.CreateServiceOrderScreen
import com.peyess.salesapp.navigation.client_list.PickScenario
import com.peyess.salesapp.navigation.client_list.buildClientListRoute
import com.peyess.salesapp.navigation.sale.discount.buildDiscountNavRoute
import com.peyess.salesapp.navigation.sale.fee.buildFeeNavRoute
import com.peyess.salesapp.navigation.sale.frames.buildFramesLandingRoute
import com.peyess.salesapp.navigation.sale.lenses.buildLensSuggestionNavRoute
import com.peyess.salesapp.navigation.sale.payment.buildPaymentNavRoute

const val serviceOrderIdParam = "serviceOrderId"
const val saleIdParam = "saleId"
const val isCreatingParam = "isCreating"

val serviceOrderRoute = SalesAppScreens.ServiceOrder.name +
        "/{$isCreatingParam}" +
        "/{$saleIdParam}" +
        "/{$serviceOrderIdParam}"

fun buildServiceOrderRoute(
    isCreating: Boolean,
    saleId: String,
    serviceOrderId: String,
): String {
    return SalesAppScreens.ServiceOrder.name +
            "/$isCreating" +
            "/$saleId" +
            "/$serviceOrderId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildServiceOrderNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = serviceOrderRoute,
        arguments = listOf(
            navArgument(isCreatingParam) { type = NavType.BoolType },
            navArgument(saleIdParam) { type = NavType.StringType },
            navArgument(serviceOrderIdParam) { type = NavType.StringType },
        ),
        enterTransition = serviceOrderEnterTransition(),
        exitTransition = serviceOrderExitTransition(),
    ) {
        CreateServiceOrderScreen(
            modifier = modifier,
            navHostController = navHostController,

            onChangeUser = { saleId, serviceOrderId ->
                val isPicking = true
                val pickScenario = PickScenario.User.toName()
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

            onEditPrescription = {
                val isUpdating = true

                navHostController
                    .navigate("${SalesAppScreens.SalePrescriptionPicture}/$isUpdating")
            },

            onEditProducts = { saleId, serviceOrderId ->
                val route = buildLensSuggestionNavRoute(
                    isEditing = true,
                    showSuggestions = true,
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                navHostController
                    .navigate(route)
            },

            onEditMeasure = { _, _ ->
                val route = buildFramesLandingRoute(isEditing = true)

                navHostController.navigate(route)
            },

            onAddPayment = { saleId, serviceOrderId, paymentId ->
                val isPicking = true
                val pickScenario = PickScenario.Payment.toName()
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
                val route = buildPaymentNavRoute(
                    paymentId = paymentId,
                    clientId = client,
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                navHostController.navigate(route)
            },
            onAddDiscount = { saleId, fullPrice ->
                navHostController
                    .navigate(buildDiscountNavRoute(saleId, fullPrice))
            },
            onAddPaymentFee = { saleId, fullPrice ->
                navHostController
                    .navigate(buildFeeNavRoute(saleId, fullPrice))
            },

            onFinishSale = {
                navHostController.navigate(SalesAppScreens.Home.name) {
                    popUpTo(SalesAppScreens.Home.name) { inclusive = true}
                }
            }
        )
    }
}