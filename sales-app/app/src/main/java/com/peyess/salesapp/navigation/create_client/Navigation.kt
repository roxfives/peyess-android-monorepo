package com.peyess.salesapp.navigation.create_client

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
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
import com.peyess.salesapp.ui.theme.SalesAppTheme
import timber.log.Timber

const val createScenarioParam = "createScenario"

val basicInfoRoute = "${SalesAppScreens.CreateNewClientBasicInfo.name}/{$createScenarioParam}"
val addressRoute = "${SalesAppScreens.CreateNewClientAddress.name}/{$createScenarioParam}"
val communicationRoute = "${SalesAppScreens.CreateNewClientContact.name}/{$createScenarioParam}"

fun formatBasicInfoRoute(param: String): String {
    return "${SalesAppScreens.CreateNewClientBasicInfo.name}/{$param}"
}

fun formatAddressRoute(param: String): String {
    return "${SalesAppScreens.CreateNewClientAddress.name}/{$param}"
}

fun formatCommunicationRoute(param: String): String {
    return "${SalesAppScreens.CreateNewClientContact.name}/{$param}"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildCreateClientNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = basicInfoRoute,
//        arguments = listOf(
//            navArgument(pickScenarioParam) { type = NavType.StringType },
//        ),
        enterTransition = createClientEnterTransition(),
        exitTransition = createClientExitTransition()
    ) {
        BasicInfoScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            onDone = {
                 navHostController.navigate(formatAddressRoute(it.toName()))
            },
        )
    }

    builder.composable(
        route = addressRoute,
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
            viewModelScope = viewModelScope,
            onDone = {
                navHostController.navigate(formatCommunicationRoute(it.toName()))
            },
        )
    }

    builder.composable(
        route = communicationRoute,
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
            viewModelScope = viewModelScope,
            onDone = {
                 when (it) {
                     CreateScenario.Home ->
                         navHostController.navigate(SalesAppScreens.Clients.name) {
                             popUpTo(SalesAppScreens.Clients.name) {
                                 inclusive = true
                             }
                         }

                     CreateScenario.Sale -> {}
//                         navHostController.navigate(SalesAppScreens.Clients.name) {
//                             popUpTo(basicInfoRoute) {
//                                 inclusive = true
//                             }
//                         }
                 }
            },
        )
    }
}

sealed class CreateScenario {
    object Home: CreateScenario()
    object Sale: CreateScenario()

    fun toName() = toName(this)

    companion object {
        fun toName(scenario: CreateScenario): String {
            return when (scenario) {
                Home -> "Home"
                Sale -> "Sale"
            }
        }

        fun fromName(name: String): CreateScenario? {
            return when (name) {
                "Home" -> Home
                "Sale" -> Sale
                else -> null
            }
        }
    }
}