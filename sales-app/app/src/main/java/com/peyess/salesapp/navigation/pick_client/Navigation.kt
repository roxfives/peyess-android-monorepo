package com.peyess.salesapp.navigation.pick_client

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.pick_client.PickClientScreen

const val isPickingParam = "isPicking"
const val pickScenarioParam = "pickScenario"
const val paymentIdParam = "paymentId"

@OptIn(ExperimentalAnimationApi::class)
fun buildPickClientNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = "${SalesAppScreens.PickClient.name}/{$isPickingParam}/{$pickScenarioParam}?$paymentIdParam={$paymentIdParam}",
        arguments = listOf(
            navArgument(isPickingParam) { type = NavType.BoolType },
            navArgument(pickScenarioParam) { type = NavType.StringType },
            navArgument(paymentIdParam) {
                type = NavType.LongType
                defaultValue = 0L
            },
        ),
        enterTransition = pickclientEnterTransition(),
        exitTransition = pickclientExitTransition()
    ) {
        PickClientScreen(
            modifier = modifier,
            navHostController = navHostController,
        ) { paymentId, clientId, scenario ->
            when (scenario) {
                PickScenario.Payment ->
                    navHostController
                        .navigate("${SalesAppScreens.SalePayment.name}/$paymentId/$clientId") {
                            popUpTo("${SalesAppScreens.PickClient.name}/{$isPickingParam}/{$pickScenarioParam}?$paymentIdParam={$paymentIdParam}") {
                                inclusive = true
                            }
                        }
                else ->
                    navHostController.navigate(SalesAppScreens.ServiceOrder.name)
            }
        }
    }
}

sealed class PickScenario {
    object ServiceOrder: PickScenario()
    object Responsible: PickScenario()
    object User: PickScenario()
    object Witness: PickScenario()
    object Payment: PickScenario()

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