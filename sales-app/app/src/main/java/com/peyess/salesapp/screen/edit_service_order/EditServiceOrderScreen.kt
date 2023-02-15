package com.peyess.salesapp.screen.edit_service_order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.service_order.ServiceOrderUI
import com.peyess.salesapp.screen.edit_service_order.state.EditServiceOrderState
import com.peyess.salesapp.screen.edit_service_order.state.EditServiceOrderViewModel
import com.peyess.salesapp.screen.edit_service_order.utils.ParseParameters

@Composable
fun EditServiceOrderScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {
    val viewModel: EditServiceOrderViewModel = mavericksViewModel()
    
    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::onSaleIdChanged,
        onUpdateServiceOrderId = viewModel::onServiceOrderIdChanged,
    )

    val saleId by viewModel.collectAsState(EditServiceOrderState::saleId)
    val serviceOrderId by viewModel.collectAsState(EditServiceOrderState::serviceOrderId)

    val prescription by viewModel.collectAsState(EditServiceOrderState::prescription)

    val lens by viewModel.collectAsState(EditServiceOrderState::lens)
    val coloring by viewModel.collectAsState(EditServiceOrderState::coloring)
    val treatment by viewModel.collectAsState(EditServiceOrderState::treatment)
    val frames by viewModel.collectAsState(EditServiceOrderState::frames)
    
    ServiceOrderUI(
        modifier = modifier,

        prescription = prescription,
        lens = lens,
        coloring = coloring,
        treatment = treatment,
        frames = frames,
    )
}
