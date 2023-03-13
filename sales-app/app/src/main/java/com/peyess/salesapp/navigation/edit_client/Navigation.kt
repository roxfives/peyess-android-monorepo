package com.peyess.salesapp.navigation.edit_client

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
import com.peyess.salesapp.navigation.edit_client.address.editClientAddressEnterTransition
import com.peyess.salesapp.navigation.edit_client.address.editClientAddressExitTransition
import com.peyess.salesapp.navigation.edit_client.basic_info.editClientEnterTransition
import com.peyess.salesapp.navigation.edit_client.basic_info.editClientExitTransition
import com.peyess.salesapp.navigation.edit_client.communication.editClientCommunicationEnterTransition
import com.peyess.salesapp.navigation.edit_client.communication.editClientCommunicationExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val clientIdParam = "clientId"

private val basicInfoRoute = SalesAppScreens.EditClientBasicInfo.name +
        "/{$clientIdParam}"
private val addressRoute = SalesAppScreens.EditClientAddress.name +
        "/{$clientIdParam}"
private val communicationRoute = SalesAppScreens.EditClientContact.name +
        "/{$clientIdParam}"

fun buildEditBasicInfoRoute(clientId: String): String {
    return SalesAppScreens.EditClientBasicInfo.name +
            "/$clientId"
}

fun buildEditAddressRoute(clientId: String): String {
    return SalesAppScreens.EditClientAddress.name +
            "/$clientId"
}

fun buildEditCommunicationRoute(clientId: String): String {
    return SalesAppScreens.EditClientContact.name +
            "/$clientId"
}

@OptIn(ExperimentalAnimationApi::class)
fun buildEditClientNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = basicInfoRoute,
        arguments = listOf(
            navArgument(clientIdParam) { type = NavType.StringType },
        ),
        enterTransition = editClientEnterTransition(),
        exitTransition = editClientExitTransition()
    ) {
        BasicInfoScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { clientId, _, _, _, _ ->
                navHostController.navigate(buildEditAddressRoute(clientId))
            },
        )
    }

    builder.composable(
        route = addressRoute,
        arguments = listOf(
            navArgument(clientIdParam) { type = NavType.StringType },
        ),
        enterTransition = editClientAddressEnterTransition(),
        exitTransition = editClientAddressExitTransition()
    ) {
        CreateClientAddressScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,
            onDone = { clientId, _, _, _, _ ->
                val route = buildEditCommunicationRoute(clientId)

                navHostController.navigate(route)
            },
        )
    }

    builder.composable(
        route = communicationRoute,
        arguments = listOf(
            navArgument(clientIdParam) { type = NavType.StringType },
        ),
        enterTransition = editClientCommunicationEnterTransition(),
        exitTransition = editClientCommunicationExitTransition(),
    ) {
        CreateClientCommunicationScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            navHostController = navHostController,

            isUpdating = true,
            onDone = { _, _, _, _, _ ->
                 navHostController.popBackStack(
                     route = basicInfoRoute,
                     inclusive = true,
                 )
            },
        )
    }
}
