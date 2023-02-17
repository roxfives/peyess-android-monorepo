package com.peyess.salesapp.navigation.client_list

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.sale.pick_client.PickClientScreen
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.navigation.create_client.buildBasicInfoRoute
import com.peyess.salesapp.navigation.sale.payment.buildPaymentNavRoute
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderRoute

const val saleIdParam = "saleId"
const val serviceOrderIdParam = "serviceOrderId"
const val isPickingParam = "isPicking"
const val pickScenarioParam = "pickScenario"
const val paymentIdParam = "paymentId"

val clientListRoute = SalesAppScreens.ClientList.name +
        "/{$saleIdParam}" +
        "/{$serviceOrderIdParam}" +
        "/{$isPickingParam}" +
        "/{$pickScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}"

fun buildClientListRoute(
    saleId: String,
    serviceOrderId: String,
    isPicking: Boolean,
    pickScenario: String,
): String {
    return SalesAppScreens.ClientList.name +
            "/$saleId" +
            "/$serviceOrderId" +
            "/$isPicking" +
            "/$pickScenario"
}

fun buildClientListRoute(
    saleId: String,
    serviceOrderId: String,
    isPicking: Boolean,
    pickScenario: String,
    paymentId: Long,
): String {
    return SalesAppScreens.ClientList.name +
            "/$saleId" +
            "/$serviceOrderId" +
            "/$isPicking" +
            "/$pickScenario" +
            "?$paymentIdParam=$paymentId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildPickClientListNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = clientListRoute,
        arguments = listOf(
            navArgument(isPickingParam) { type = NavType.BoolType },
            navArgument(pickScenarioParam) { type = NavType.StringType },
            navArgument(paymentIdParam) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
        enterTransition = clientListEnterTransition(),
        exitTransition = clientListExitTransition()
    ) {
        PickClientScreen(
            modifier = modifier,
            navHostController = navHostController,
            onCreateNewClient = { clientId, paymentId, pickScenario ->
                val createScenario = when (pickScenario) {
                    PickScenario.ServiceOrder -> CreateScenario.ServiceOrder
                    PickScenario.Payment -> CreateScenario.Payment
                    PickScenario.Responsible -> CreateScenario.Responsible
                    PickScenario.User -> CreateScenario.User
                    PickScenario.Witness -> CreateScenario.Witness
                }

                val basicInfoRoute = buildBasicInfoRoute(
                    clientId = clientId,
                    createScenario = createScenario,
                    paymentId = paymentId,
                )

                navHostController.navigate(basicInfoRoute)
            },

            onClientPicked = { paymentId, clientId, scenario, saleId, serviceOrderId ->
                when (scenario) {
                    PickScenario.Payment -> {
                        val paymentRoute = buildPaymentNavRoute(
                            paymentId = paymentId,
                            clientId = clientId,
                            saleId = saleId,
                            serviceOrderId = serviceOrderId,
                        )

                        navHostController.navigate(paymentRoute) {
                            popUpTo(clientListRoute) {
                                inclusive = true
                            }
                        }
                    }

                    PickScenario.ServiceOrder,
                    PickScenario.User,
                    PickScenario.Responsible,
                    PickScenario.Witness -> {
                        val route = buildServiceOrderRoute(
                            isCreating = true,
                            saleId = saleId,
                            serviceOrderId = serviceOrderId,
                        )

                        navHostController.navigate(route)
                    }
                }
            }
        )
    }
}

sealed class PickScenario {
    object ServiceOrder: PickScenario()
    object User: PickScenario()
    object Responsible: PickScenario()
    object Witness: PickScenario()
    object Payment: PickScenario()
//    object EditUser: PickScenario()
//    object EditResponsible: PickScenario()
//    object EditWitness: PickScenario()
//    object EditPayment: PickScenario()

    fun toName() = toName(this)

    companion object {
        fun toName(scenario: PickScenario): String {
            return when (scenario) {
                Responsible -> "Client"
                Payment -> "Payment"
                ServiceOrder -> "ServiceOrder"
                User -> "User"
                Witness -> "Witness"
            }
        }

        fun fromName(name: String): PickScenario? {
            return when (name) {
                "Client" -> Responsible
                "Payment" -> Payment
                "ServiceOrder" -> ServiceOrder
                "User" -> User
                "Witness" -> Witness
                else -> null
            }
        }
    }
}
