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
import com.peyess.salesapp.navigation.edit_client.buildEditBasicInfoRoute
import com.peyess.salesapp.navigation.edit_service_order.payment.buildEditPaymentNavRoute
import com.peyess.salesapp.navigation.edit_service_order.service_order.buildEditServiceOrderRoute
import com.peyess.salesapp.navigation.edit_service_order.service_order.editServiceOrderRoute
import com.peyess.salesapp.navigation.sale.payment.buildPaymentNavRoute
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderRoute
import com.peyess.salesapp.navigation.sale.service_order.serviceOrderRoute

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
            onEditClient = {
                val basicInfoRoute = buildEditBasicInfoRoute(clientId = it)

                navHostController.navigate(basicInfoRoute)
            },
            onCreateNewClient = { clientId, paymentId, pickScenario, saleId, serviceOrderId ->
                val createScenario = when (pickScenario) {
                    PickScenario.ServiceOrder -> CreateScenario.ServiceOrder
                    PickScenario.Payment -> CreateScenario.Payment
                    PickScenario.Responsible -> CreateScenario.Responsible
                    PickScenario.User -> CreateScenario.User
                    PickScenario.Witness -> CreateScenario.Witness
                    PickScenario.EditPayment -> CreateScenario.EditPayment
                    PickScenario.EditResponsible -> CreateScenario.EditResponsible
                    PickScenario.EditUser -> CreateScenario.EditUser
                    PickScenario.EditWitness -> CreateScenario.EditWitness
                }

                val basicInfoRoute = buildBasicInfoRoute(
                    clientId = clientId,
                    createScenario = createScenario,
                    paymentId = paymentId,
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
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

                    PickScenario.ServiceOrder -> {
                        val route = buildServiceOrderRoute(
                            isCreating = true,
                            saleId = saleId,
                            serviceOrderId = serviceOrderId,
                        )

                        navHostController.navigate(route)
                    }

                    PickScenario.User,
                    PickScenario.Responsible,
                    PickScenario.Witness -> {
                        val route = buildServiceOrderRoute(
                            isCreating = true,
                            saleId = saleId,
                            serviceOrderId = serviceOrderId,
                        )

                        navHostController.navigate(route) {
                            popUpTo(serviceOrderRoute) {
                                inclusive = true
                            }
                        }
                    }

                    PickScenario.EditPayment -> {
                        val paymentRoute = buildEditPaymentNavRoute(
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

                    PickScenario.EditResponsible,
                    PickScenario.EditUser,
                    PickScenario.EditWitness -> {
                        val route = buildEditServiceOrderRoute(
                            saleId = saleId,
                            serviceOrderId = serviceOrderId,
                            reloadFromServer = false,
                        )

                        navHostController.navigate(route) {
                            popUpTo(editServiceOrderRoute) {
                                inclusive = true
                            }
                        }
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
    object EditUser: PickScenario()
    object EditResponsible: PickScenario()
    object EditWitness: PickScenario()
    object EditPayment: PickScenario()

    fun toName() = toName(this)

    companion object {
        fun toName(scenario: PickScenario): String {
            return when (scenario) {
                ServiceOrder -> "ServiceOrder"
                Payment -> "Payment"
                User -> "User"
                Responsible -> "Client"
                Witness -> "Witness"
                EditPayment -> "EditPayment"
                EditResponsible -> "EditUser"
                EditUser -> "EditClient"
                EditWitness -> "EditWitness"
            }
        }

        fun fromName(name: String): PickScenario? {
            return when (name) {
                "Client" -> Responsible

                "Payment" -> Payment
                "ServiceOrder" -> ServiceOrder
                "User" -> User
                "Witness" -> Witness

                "EditPayment" -> EditPayment
                "EditUser" -> EditResponsible
                "EditClient" -> EditUser
                "EditWitness" -> EditWitness

                else -> null
            }
        }
    }
}
