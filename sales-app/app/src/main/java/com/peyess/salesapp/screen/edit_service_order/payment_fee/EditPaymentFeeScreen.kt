package com.peyess.salesapp.screen.edit_service_order.payment_fee

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.payment_fee.PaymentFeeUI
import com.peyess.salesapp.screen.edit_service_order.payment_fee.state.EditPaymentFeeState
import com.peyess.salesapp.screen.edit_service_order.payment_fee.state.EditPaymentFeeViewModel
import com.peyess.salesapp.screen.edit_service_order.payment_fee.utils.ParseParameters
import java.math.BigDecimal

@Composable
fun EditPaymentFeeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: () -> Unit = {},
) {

    val viewModel: EditPaymentFeeViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::onUpdateSaleId,
        onUpdateFullPrice = viewModel::onUpdateFullPrice,
    )

    val currentFee by viewModel.collectAsState(EditPaymentFeeState::currentPaymentFee)
    val fullPrice by viewModel.collectAsState(EditPaymentFeeState::fullPrice)
    val pricePreview by viewModel.collectAsState(EditPaymentFeeState::pricePreview)

    PaymentFeeUI(
        modifier = modifier,
        selectedMethod = currentFee.method,
        feeValue = BigDecimal(currentFee.value),
        originalPrice = fullPrice,
        pricePreview = pricePreview,
        onChangeFeeValue = { viewModel.onChangeFeeValue(it.toDouble()) },
        onChangeFeeMethod = viewModel::onChangeFeeMethod,
        onDone = onDone,
    )
}