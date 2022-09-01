package com.peyess.salesapp.navigation.sale.service_order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.peyess.salesapp.navigation.SalesAppScreens
import com.peyess.salesapp.feature.sale.service_order.ServiceOrderScreen
import com.peyess.salesapp.navigation.pick_client.PickScenario
import com.peyess.salesapp.navigation.pick_client.paymentIdParam
import com.peyess.salesapp.navigation.sale.prescription.lens_type.prescriptionLensTypeScreenExitTransition
import com.peyess.salesapp.ui.theme.SalesAppTheme

@OptIn(ExperimentalAnimationApi::class)
fun buildServiceOrderNavGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    builder: NavGraphBuilder,
) {
    builder.composable(
        route = SalesAppScreens.ServiceOrder.name,
        enterTransition = serviceOrderEnterTransition(),
        exitTransition = serviceOrderExitTransition(),
    ) {
        ServiceOrderScreen(
            modifier = modifier,

            onChangeUser = {
                val isPicking = true
                val pickScenario = PickScenario.User.toName()

                navHostController
                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
            },
            onChangeResponsible = {
                val isPicking = true
                val pickScenario = PickScenario.Responsible.toName()

                navHostController
                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
            },
            onChangeWitness = {
                val isPicking = true
                val pickScenario = PickScenario.Witness.toName()

                navHostController
                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario")
            },

            onEditPrescription = {
                val isUpdating = true

                navHostController
                    .navigate("${SalesAppScreens.SalePrescriptionPicture}/$isUpdating")
            },

            onAddPayment = {
                val isPicking = true
                val pickScenario = PickScenario.Payment.toName()
                val paymentId = it

                navHostController
                    .navigate("${SalesAppScreens.PickClient.name}/$isPicking/$pickScenario?$paymentIdParam=$paymentId")
            },
            onEditPayment = { paymentId, clientId ->
                val client = clientId.ifBlank { "-" }

                navHostController
                    .navigate("${SalesAppScreens.SalePayment.name}/$paymentId/$client")
            }
        ) {
            navHostController.navigate(SalesAppScreens.ServiceOrder.name)
        }
    }
}