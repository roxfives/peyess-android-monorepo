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
import com.peyess.salesapp.navigation.pick_client.isPickingParam
import com.peyess.salesapp.navigation.pick_client.paymentIdParam
import com.peyess.salesapp.navigation.pick_client.pickScenarioParam
import com.peyess.salesapp.navigation.sale.service_order.buildServiceOrderRoute
import com.peyess.salesapp.ui.theme.SalesAppTheme
import timber.log.Timber

const val createScenarioParam = "createScenario"

val basicInfoRoute = "${SalesAppScreens.CreateNewClientBasicInfo.name}/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}"
val addressRoute = "${SalesAppScreens.CreateNewClientAddress.name}/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}"
val communicationRoute = "${SalesAppScreens.CreateNewClientContact.name}/{$createScenarioParam}" +
        "?$paymentIdParam={$paymentIdParam}"

fun formatBasicInfoRoute(
    createScenario: CreateScenario,
    paymentId: Long = 0L,
): String {
    return when(createScenario) {
        CreateScenario.Home ->
            "${SalesAppScreens.CreateNewClientBasicInfo.name}/${createScenario.toName()}"
        else ->
            "${SalesAppScreens.CreateNewClientBasicInfo.name}/${createScenario.toName()}" +
                    "?$paymentIdParam=$paymentId"
    }
}

fun formatAddressRoute(
    createScenario: CreateScenario,
    paymentId: Long = 0L,
): String {
    return when(createScenario) {
        CreateScenario.Home ->
            "${SalesAppScreens.CreateNewClientAddress.name}/${createScenario.toName()}"
        else ->
            "${SalesAppScreens.CreateNewClientAddress.name}/${createScenario.toName()}" +
                    "?$paymentIdParam=$paymentId"
    }
}

fun formatCommunicationRoute(
    createScenario: CreateScenario,
    paymentId: Long = 0L,
): String {
    return when(createScenario) {
        CreateScenario.Home ->
            "${SalesAppScreens.CreateNewClientContact.name}/${createScenario.toName()}"
        else ->
            "${SalesAppScreens.CreateNewClientContact.name}/${createScenario.toName()}" +
                    "?$paymentIdParam=$paymentId"
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun buildCreateClientNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
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
            onDone = { createScenario, paymentId ->
                when(createScenario) {
                    is CreateScenario.Home ->
                        navHostController.navigate(formatAddressRoute(createScenario))
                    else ->
                        navHostController.navigate(
                            formatAddressRoute(createScenario, paymentId)
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
        val viewModelScope = try {
            navHostController.getBackStackEntry(basicInfoRoute)
        } catch (error: Throwable) {
            Timber.e("Could not find viewModelScope", error)
            null
        }

        CreateClientAddressScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            viewModelScope = viewModelScope,
            onDone = { createScenario, paymentId ->
                when(createScenario) {
                    is CreateScenario.Home ->
                        navHostController.navigate(formatCommunicationRoute(createScenario))
                    else ->
                        navHostController.navigate(
                            formatCommunicationRoute(createScenario, paymentId)
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
        val viewModelScope = try {
            navHostController.getBackStackEntry(basicInfoRoute)
        } catch (error: Throwable) {
            Timber.e("Could not find viewModelScope", error)
            null
        }
        
        CreateClientCommunicationScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            viewModelScope = viewModelScope,
            onDone = { createScenario, clientId, paymentId, saleId, serviceOrderId ->
                 when (createScenario) {
                     is CreateScenario.Home ->
                         navHostController.navigate(SalesAppScreens.Clients.name) {
                             popUpTo(SalesAppScreens.Clients.name) {
                                 inclusive = true
                             }
                         }

                     is CreateScenario.Payment ->
                         navHostController
                             .navigate("${SalesAppScreens.SalePayment.name}/$paymentId/$clientId") {
                                 popUpTo("${SalesAppScreens.PickClient.name}/{$isPickingParam}/{$pickScenarioParam}?$paymentIdParam={$paymentIdParam}") {
                                     inclusive = true
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