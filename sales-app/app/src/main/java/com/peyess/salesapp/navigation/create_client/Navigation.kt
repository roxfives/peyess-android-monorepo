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
import com.peyess.salesapp.feature.create_client.basic_info.BasicInfoScreen
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.pick_client.PickClientScreen
import com.peyess.salesapp.ui.theme.SalesAppTheme

const val isPickingParam = "isPicking"
const val pickScenarioParam = "pickScenario"
const val paymentIdParam = "paymentId"

@OptIn(ExperimentalAnimationApi::class)
fun buildCreateClientNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder
) {
    builder.composable(
        route = SalesAppScreens.CreateNewClientBasicInfo.name,
//        arguments = listOf(
//            navArgument(isPickingParam) { type = NavType.BoolType },
//            navArgument(pickScenarioParam) { type = NavType.StringType },
//            navArgument(paymentIdParam) {
//                type = NavType.LongType
//                defaultValue = 0L
//            },
//        ),
        enterTransition = createClientEnterTransition(),
        exitTransition = createClientExitTransition()
    ) {
        BasicInfoScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(SalesAppTheme.dimensions.grid_2),
            onDone = {},
        )
    }
}

//sealed class CreateScenario {
//    object ServiceOrder: CreateScenario()
//    object Responsible: CreateScenario()
//    object User: CreateScenario()
//    object Witness: CreateScenario()
//    object Payment: CreateScenario()
//
//    fun toName() = toName(this)
//
//    companion object {
//        fun toName(scenario: CreateScenario): String {
//            return when (scenario) {
//                Responsible -> "Client"
//                Payment -> "Payment"
//                ServiceOrder -> "ServiceOrder"
//                User -> "User"
//                Witness -> "Witness"
//            }
//        }
//
//        fun fromName(name: String): CreateScenario? {
//            return when (name) {
//                "Client" -> Responsible
//                "Payment" -> Payment
//                "ServiceOrder" -> ServiceOrder
//                "User" -> User
//                "Witness" -> Witness
//                else -> null
//            }
//        }
//    }
//}