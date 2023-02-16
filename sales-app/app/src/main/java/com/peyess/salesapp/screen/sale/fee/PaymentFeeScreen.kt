package com.peyess.salesapp.screen.sale.fee

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.payment_fee.PaymentFeeUI
import com.peyess.salesapp.screen.sale.fee.state.PaymentFeeState
import com.peyess.salesapp.screen.sale.fee.state.PaymentFeeViewModel
import com.peyess.salesapp.screen.sale.fee.utils.parseParameterFullPrice
import com.peyess.salesapp.screen.sale.fee.utils.parseParameterSaleId
import timber.log.Timber
import java.math.BigDecimal

@Composable
fun PaymentFeeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),

    onDone: () -> Unit = {},
) {
    val backStackEntry = navHostController.currentBackStackEntryAsState()

    val viewModel: PaymentFeeViewModel = mavericksViewModel()

    val fee by viewModel.collectAsState(PaymentFeeState::currentPaymentFee)
    val pricePreview by viewModel.collectAsState(PaymentFeeState::pricePreview)
    val originalPrice by viewModel.collectAsState(PaymentFeeState::originalPrice)

    val hasFinished by viewModel.collectAsState(PaymentFeeState::hasFinished)

    if (hasFinished) {
        LaunchedEffect(Unit) {
            viewModel.onNavigate()
            onDone()
        }
    }

    parseParameterSaleId(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setSaleId,
    )
    parseParameterFullPrice(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setFullPrice,
    )

    Timber.d("Current value: ${fee.value}")
    PaymentFeeUI(
        modifier = modifier.fillMaxSize(),

        selectedMethod = fee.method,
        feeValue = BigDecimal(fee.value),
        originalPrice = originalPrice,
        pricePreview = pricePreview,

        onChangeFeeMethod = viewModel::onChangeFeeMethod,
        onChangeFeeValue = { viewModel.onChangeFeeValue(it.toDouble()) },

        onDone = viewModel::onFinished,
    )
}
