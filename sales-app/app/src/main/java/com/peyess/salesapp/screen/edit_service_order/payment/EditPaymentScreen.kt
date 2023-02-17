package com.peyess.salesapp.screen.edit_service_order.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.payment.PaymentUI
import com.peyess.salesapp.screen.edit_service_order.payment.state.EditPaymentState
import com.peyess.salesapp.screen.edit_service_order.payment.state.EditPaymentViewModel
import com.peyess.salesapp.screen.edit_service_order.payment.utils.ParseParameters

@Composable
fun EditPaymentScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {
    val viewModel: EditPaymentViewModel = mavericksViewModel()

    val isClientLoading by viewModel.collectAsState(EditPaymentState::isClientLoading)
    val client by viewModel.collectAsState(EditPaymentState::client)

    ParseParameters(
        navController = navHostController,
        onUpdatePaymentId = viewModel::onUpdatePaymentId,
        onUpdateClientId = viewModel::onUpdateClientId,
        onUpdateSaleId = viewModel::onUpdateSaleId,
        onUpdateServiceOrderId = viewModel::onUpdateServiceOrderId,
    )

    PaymentUI(
        modifier = modifier,

        client = client,
        isClientLoading = isClientLoading,
        pictureForClient = viewModel::pictureForClient,
    )
}
