package com.peyess.salesapp.navigation.create_client

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.screen.create_client.address.CreateClientAddressScreen
import com.peyess.salesapp.screen.create_client.basic_info.BasicInfoScreen
import com.peyess.salesapp.screen.create_client.communication.CreateClientCommunicationScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.client_list.clientListRoute
import com.peyess.salesapp.navigation.create_client.address.clientAddressEnterTransition
import com.peyess.salesapp.navigation.create_client.address.clientAddressExitTransition
import com.peyess.salesapp.navigation.create_client.basic_info.createClientEnterTransition
import com.peyess.salesapp.navigation.create_client.basic_info.createClientExitTransition
import com.peyess.salesapp.navigation.create_client.communication.createClientCommunicationEnterTransition
import com.peyess.salesapp.navigation.create_client.communication.createClientCommunicationExitTransition
import com.peyess.salesapp.navigation.edit_service_order.payment.buildEditPaymentNavRoute
import com.peyess.salesapp.navigation.edit_service_order.service_order.buildEditServiceOrderRoute
import com.peyess.salesapp.navigation.edit_service_order.service_order.editServiceOrderRoute
import com.peyess.salesapp.navigation.sale.payment.buildPaymentNavRoute
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderRoute
import com.peyess.salesapp.navigation.sale.service_order.serviceOrderRoute
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val clientIdParam = "clientId"
const val createScenarioParam = "createScenario"
const val pickScenarioParam = "pickScenario"
const val isPickingParam = "isPicking"

const val saleIdParam = "saleId"
const val serviceOrderIdParam = "serviceOrderId"
const val paymentIdParam = "paymentId"

private val basicInfoRoute = SalesAppScreens.CreateNewClientBasicInfo.name +
        "/{$clientIdParam}" +
        "/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}" +
        "?$saleIdParam={$saleIdParam}" +
        "?$serviceOrderIdParam={$serviceOrderIdParam}"
private val addressRoute = SalesAppScreens.CreateNewClientAddress.name +
        "/{$clientIdParam}" +
        "/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}" +
        "?$saleIdParam={$saleIdParam}" +
        "?$serviceOrderIdParam={$serviceOrderIdParam}"
private val communicationRoute = SalesAppScreens.CreateNewClientContact.name +
        "/{$clientIdParam}" +
        "/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}" +
        "?$saleIdParam={$saleIdParam}" +
        "?$serviceOrderIdParam={$serviceOrderIdParam}"

fun buildBasicInfoRoute(
    clientId: String,
    createScenario: CreateScenario,
    paymentId: Long = 0L,
    saleId: String = "",
    serviceOrderId: String = "",
): String {
    return when(createScenario) {
        CreateScenario.Home ->
            SalesAppScreens.CreateNewClientBasicInfo.name +
                    "/$clientId" +
                    "/${createScenario.toName()}"
        else ->
            SalesAppScreens.CreateNewClientBasicInfo.name +
                    "/$clientId" +
                    "/${createScenario.toName()}" +
                    "?$paymentIdParam=$paymentId" +
                    "?$saleIdParam=$saleId" +
                    "?$serviceOrderIdParam=$serviceOrderId"
    }
}

fun buildAddressRoute(
    clientId: String,
    createScenario: CreateScenario,
    paymentId: Long = 0L,
    saleId: String = "",
    serviceOrderId: String = "",
): String {
    return when(createScenario) {
        CreateScenario.Home ->
            SalesAppScreens.CreateNewClientAddress.name +
                    "/${clientId}" +
                    "/${createScenario.toName()}"
        else ->
            SalesAppScreens.CreateNewClientAddress.name +
                    "/${clientId}" +
                    "/${createScenario.toName()}" +
                    "?$paymentIdParam=$paymentId" +
                    "?$saleIdParam=$saleId" +
                    "?$serviceOrderIdParam=$serviceOrderId"
    }
}

fun buildCommunicationRoute(
    clientId: String,
    createScenario: CreateScenario,
    paymentId: Long = 0L,
    saleId: String = "",
    serviceOrderId: String = "",
): String {
    return when(createScenario) {
        CreateScenario.Home ->
            SalesAppScreens.CreateNewClientContact.name +
                    "/${clientId}" +
                    "/${createScenario.toName()}"
        else ->
            SalesAppScreens.CreateNewClientContact.name +
                    "/${clientId}" +
                    "/${createScenario.toName()}" +
                    "?$paymentIdParam=$paymentId" +
                    "?$saleIdParam=$saleId" +
                    "?$serviceOrderIdParam=$serviceOrderId"
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun buildCreateClientNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = basicInfoRoute,
        arguments = listOf(
            navArgument(createScenarioParam) { type = NavType.StringType },
            navArgument(paymentIdParam) {
                type = NavType.LongType
                defaultValue = 0L
            },
            navArgument(saleIdParam) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(serviceOrderIdParam) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
        enterTransition = createClientEnterTransition(),
        exitTransition = createClientExitTransition()
    ) {
        BasicInfoScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { clientId, createScenario, paymentId, saleId, serviceOrderId ->
                when(createScenario) {
                    CreateScenario.Home ->
                        navHostController.navigate(
                            buildAddressRoute(
                                clientId = clientId,
                                createScenario = createScenario,
                            )
                        )
                    else ->
                        navHostController.navigate(
                            buildAddressRoute(
                                clientId = clientId,
                                createScenario = createScenario,
                                paymentId = paymentId,
                                saleId = saleId,
                                serviceOrderId = serviceOrderId,
                            )
                        )
                }
            },
        )
    }

    builder.composable(
        route = addressRoute,
        arguments = listOf(
            navArgument(createScenarioParam) { type = NavType.StringType },
            navArgument(paymentIdParam) {
                type = NavType.LongType
                defaultValue = 0L
            },
            navArgument(saleIdParam) {
                type = NavType.StringType
                defaultValue = ""
            },
            navArgument(serviceOrderIdParam) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
        enterTransition = clientAddressEnterTransition(),
        exitTransition = clientAddressExitTransition()
    ) {
        CreateClientAddressScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { clientId, createScenario, paymentId, saleId, serviceOrderId ->
                when(createScenario) {
                    CreateScenario.Home ->
                        navHostController.navigate(
                            buildCommunicationRoute(
                                clientId = clientId,
                                createScenario = createScenario,
                            )
                        )

                    else ->
                        navHostController.navigate(
                            buildCommunicationRoute(
                                clientId = clientId,
                                createScenario = createScenario,
                                paymentId = paymentId,
                                saleId = saleId,
                                serviceOrderId = serviceOrderId,
                            )
                        )
                }
            },
        )
    }

    builder.composable(
        route = communicationRoute,
        arguments = listOf(
            navArgument(createScenarioParam) { type = NavType.StringType },
            navArgument(paymentIdParam) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
        enterTransition = createClientCommunicationEnterTransition(),
        exitTransition = createClientCommunicationExitTransition(),
    ) {
        CreateClientCommunicationScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { createScenario, clientId, paymentId, saleId, serviceOrderId ->
                 when (createScenario) {
                     CreateScenario.Home -> {
                         navHostController.navigate(SalesAppScreens.Home.name) {
                             popUpTo(SalesAppScreens.Home.name) {
                                 inclusive = true
                             }
                         }
                     }

                     CreateScenario.ServiceOrder -> {
                         val route = buildServiceOrderRoute(
                             isCreating = true,
                             saleId = saleId,
                             serviceOrderId = serviceOrderId,
                         )

                         navHostController.navigate(route) {
                             popUpTo(basicInfoRoute) {
                                 inclusive = true
                             }
                         }
                     }

                     CreateScenario.ClientScreen -> {
                         navHostController.navigate(SalesAppScreens.Clients.name) {
                             popUpTo(SalesAppScreens.Clients.name) {
                                 inclusive = true
                             }
                         }
                     }

                     CreateScenario.Payment -> {
                         val paymentRoute = buildPaymentNavRoute(
                             paymentId = paymentId,
                             clientId = clientId,
                             saleId = saleId,
                             serviceOrderId = serviceOrderId
                         )

                         navHostController.navigate(paymentRoute) {
                             popUpTo(clientListRoute) {
                                 inclusive = true
                             }
                         }
                     }


                     CreateScenario.Responsible,
                     CreateScenario.User,
                     CreateScenario.Witness -> {
                         val route = buildServiceOrderRoute(
                             isCreating = true,
                             saleId = saleId,
                             serviceOrderId = serviceOrderId,
                         )

                         navHostController.navigate(route) {
                             popUpTo(clientListRoute) { inclusive = true }
                         }
                     }

                     CreateScenario.EditPayment -> {
                         val route = buildEditPaymentNavRoute(
                             paymentId = paymentId,
                             clientId = clientId,
                             serviceOrderId = serviceOrderId,
                             saleId = saleId,
                         )

                         navHostController.navigate(route) {
                             popUpTo(clientListRoute) { inclusive = true }
                         }
                     }

                     CreateScenario.EditResponsible,
                     CreateScenario.EditUser,
                     CreateScenario.EditWitness -> {
                         val route = buildEditServiceOrderRoute(
                             saleId = saleId,
                             serviceOrderId = serviceOrderId,
                         )

                         navHostController.navigate(route) {
                             popUpTo(clientListRoute) { inclusive = true }
                         }
                     }
                 }
            },
        )
    }
}

sealed class CreateScenario {
    object Home: CreateScenario()
    object ClientScreen: CreateScenario()
    object ServiceOrder: CreateScenario()

    object Payment: CreateScenario()
    object User: CreateScenario()
    object Responsible: CreateScenario()
    object Witness: CreateScenario()

    object EditPayment: CreateScenario()
    object EditUser: CreateScenario()
    object EditResponsible: CreateScenario()
    object EditWitness: CreateScenario()

    fun toName() = toName(this)

    companion object {
        fun toName(scenario: CreateScenario): String {
            return when (scenario) {
                Home -> "Home"
                ClientScreen -> "ClientScreen"
                ServiceOrder -> "ServiceOrder"
                Payment -> "Payment"
                User -> "User"
                Responsible -> "Responsible"
                Witness -> "Witness"
                EditPayment -> "EditPayment"
                EditUser -> "EditUser"
                EditResponsible -> "EditResponsible"
                EditWitness -> "EditWitness"
            }
        }

        fun fromName(name: String): CreateScenario? {
            return when (name) {
                "Home" -> Home
                "ClientScreen" -> ClientScreen
                "ServiceOrder" -> ServiceOrder
                "Payment" -> Payment
                "Responsible" -> Responsible
                "User" -> User
                "Witness" -> Witness
                "EditPayment" -> EditPayment
                "EditUser" -> EditUser
                "EditResponsible" -> EditResponsible
                "EditWitness" -> EditWitness
                else -> null
            }
        }
    }
}