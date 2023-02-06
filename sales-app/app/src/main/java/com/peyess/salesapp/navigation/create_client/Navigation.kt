package com.peyess.salesapp.navigation.create_client

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import arrow.core.Either
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.feature.create_client.address.CreateClientAddressScreen
import com.peyess.salesapp.feature.create_client.basic_info.BasicInfoScreen
import com.peyess.salesapp.feature.create_client.communication.CreateClientCommunicationScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.navigation.create_client.address.clientAddressEnterTransition
import com.peyess.salesapp.navigation.create_client.address.clientAddressExitTransition
import com.peyess.salesapp.navigation.create_client.basic_info.createClientEnterTransition
import com.peyess.salesapp.navigation.create_client.basic_info.createClientExitTransition
import com.peyess.salesapp.navigation.create_client.communication.createClientCommunicationEnterTransition
import com.peyess.salesapp.navigation.create_client.communication.createClientCommunicationExitTransition
import com.peyess.salesapp.navigation.sale.payment.buildPaymentNavRoute
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderRoute
import com.peyess.salesapp.ui.theme.SalesAppTheme
import timber.log.Timber

const val clientIdParam = "clientId"
const val paymentIdParam = "paymentId"
const val createScenarioParam = "createScenario"
const val pickScenarioParam = "pickScenario"
const val isPickingParam = "isPicking"

private val basicInfoRoute = SalesAppScreens.CreateNewClientBasicInfo.name +
        "/{$clientIdParam}" +
        "/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}"
private val addressRoute = SalesAppScreens.CreateNewClientAddress.name +
        "/{$clientIdParam}" +
        "/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}"
private val communicationRoute = SalesAppScreens.CreateNewClientContact.name +
        "/{$clientIdParam}" +
        "/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}"

fun buildBasicInfoRoute(
    clientId: String,
    createScenario: CreateScenario,
    paymentId: Long = 0L,
): String {
    return when(createScenario) {
        CreateScenario.Home ->
            SalesAppScreens.CreateNewClientBasicInfo.name +
                    "/${clientId}" +
                    "/${createScenario.toName()}"
        else ->
            SalesAppScreens.CreateNewClientBasicInfo.name +
                    "/${clientId}" +
                    "/${createScenario.toName()}" +
                    "?$paymentIdParam=$paymentId"
    }
}

fun buildAddressRoute(
    clientId: String,
    createScenario: CreateScenario,
    paymentId: Long = 0L,
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
                    "?$paymentIdParam=$paymentId"
    }
}

fun buildCommunicationRoute(
    clientId: String,
    createScenario: CreateScenario,
    paymentId: Long = 0L,
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
                    "?$paymentIdParam=$paymentId"
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
        ),
        enterTransition = createClientEnterTransition(),
        exitTransition = createClientExitTransition()
    ) {
        BasicInfoScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { clientId, createScenario, paymentId ->
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
        ),
        enterTransition = clientAddressEnterTransition(),
        exitTransition = clientAddressExitTransition()
    ) {
        CreateClientAddressScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { clientId, createScenario, paymentId ->
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
                     CreateScenario.Home ->
                         navHostController.navigate(SalesAppScreens.Home.name) {
                             popUpTo(SalesAppScreens.Home.name) {
                                 inclusive = true
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

                     CreateScenario.ClientScreen ->
                         navHostController.navigate(SalesAppScreens.Clients.name) {
                             popUpTo(SalesAppScreens.Clients.name) {
                                 inclusive = true
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
                                 popUpTo("${SalesAppScreens.PickClient.name}/{$isPickingParam}/{$pickScenarioParam}?$paymentIdParam={$paymentIdParam}") {
                                     inclusive = true
                                 }
                             }
                     }

                     else -> {
                         val route = buildServiceOrderRoute(
                             isCreating = true,
                             saleId = saleId,
                             serviceOrderId = serviceOrderId,
                         )

                         navHostController.navigate(route) {
                             popUpTo(basicInfoRoute) { inclusive = true }
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

    object Responsible: CreateScenario()
    object User: CreateScenario()
    object Witness: CreateScenario()
    object Payment: CreateScenario()

    fun toName() = toName(this)

    companion object {
        fun toName(scenario: CreateScenario): String {
            return when (scenario) {
                Home -> "Home"
                ClientScreen -> "ClientScreen"
                ServiceOrder -> "ServiceOrder"
                Payment -> "Payment"
                Responsible -> "Responsible"
                User -> "User"
                Witness -> "Witness"
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
                else -> null
            }
        }
    }
}