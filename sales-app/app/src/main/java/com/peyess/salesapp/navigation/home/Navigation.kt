package com.peyess.salesapp.navigation.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.screen.home.ClientScreen
import com.peyess.salesapp.screen.home.HomeScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.screen.home.SalesScreen
import com.peyess.salesapp.navigation.create_client.CreateScenario
import com.peyess.salesapp.navigation.create_client.buildBasicInfoRoute
import com.peyess.salesapp.navigation.edit_client.buildEditBasicInfoRoute
import com.peyess.salesapp.navigation.edit_service_order.service_order.buildEditServiceOrderRoute
import com.peyess.salesapp.navigation.sale.lenses.buildLensSuggestionNavRoute
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildHomeNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = SalesAppScreens.Home.name,
        enterTransition = homeEnterTransition(),
        exitTransition = homeExitTransition()
    ) {
        HomeScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),

            onSettings = { navHostController.navigate(SalesAppScreens.SettingsAndActions.name) },

            onSignOut = {
                navHostController.backQueue.clear()
                navHostController.navigate(SalesAppScreens.UserListAuthentication.name)
            },

            onStartVisualAcuity = { navHostController.navigate(SalesAppScreens.VisualAcuity.name) },
            onStartSale = { navHostController.navigate(SalesAppScreens.SaleWelcome.name) },
            onAddClient = {
                val scenario = CreateScenario.Home
                val route = buildBasicInfoRoute(
                    clientId = it,
                    createScenario = scenario,
                )

                navHostController.navigate(route)
            },

            onOpenProductsTable = {
                val route = buildLensSuggestionNavRoute(
                    isEditing = false,
                    showSuggestions = false,
                )

                navHostController.navigate(route)
            },

            onViewDemo = {
                navHostController.navigate(SalesAppScreens.Demonstration.name)
            }
        )
    }

    builder.composable(
        route = SalesAppScreens.SaleScreen.name,
        enterTransition = saleEnterTransition(),
        exitTransition = saleExitTransition()
    ) {
        SalesScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
            onStartNewSale = { navHostController.navigate(SalesAppScreens.SaleWelcome.name) },
            onEditServiceOrder = { saleId, serviceOrderId ->
                val route = buildEditServiceOrderRoute(
                    saleId = saleId,
                    serviceOrderId = serviceOrderId,
                )

                navHostController.navigate(route)
            }
        )
    }

    builder.composable(
        route = SalesAppScreens.Clients.name,
        enterTransition = clientsEnterTransition(),
        exitTransition = clientsExitTransition(),
    ) {
       val scenario = CreateScenario.ClientScreen

        ClientScreen(
            modifier = modifier.padding(SalesAppTheme.dimensions.grid_2),
            onCreateNewClient = {
                navHostController.navigate(buildBasicInfoRoute(it, scenario))
            },
            onEditClient = {
                val route = buildEditBasicInfoRoute(clientId = it)

                navHostController.navigate(route)
            },
        )
    }
}