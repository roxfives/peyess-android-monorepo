package com.peyess.salesapp.screen.edit_service_order.payment_discount

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.payment_discount.DiscountScreenUI
import com.peyess.salesapp.screen.edit_service_order.payment_discount.state.EditPaymentDiscountState
import com.peyess.salesapp.screen.edit_service_order.payment_discount.state.EditPaymentDiscountViewModel
import com.peyess.salesapp.screen.edit_service_order.payment_discount.utils.ParseParameters

@Composable
fun EditPaymentDiscountScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: () -> Unit = {},
) {

    val viewModel: EditPaymentDiscountViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::setSaleId,
        onUpdateFullPrice = viewModel::setFullPrice,
    )

    val currentDiscount by viewModel.collectAsState(EditPaymentDiscountState::currentDiscount)
    val originalPrice by viewModel.collectAsState(EditPaymentDiscountState::fullPrice)
    val pricePreview by viewModel.collectAsState(EditPaymentDiscountState::pricePreview)

    DiscountScreenUI(
        modifier = modifier,
        selectedMethod = currentDiscount.method,
        discountValue = currentDiscount.value,
        originalPrice = originalPrice,
        pricePreview = pricePreview,
        onChangeDiscountValue = { viewModel.onChangeDiscountValue(it) },
        onChangeDiscountMethod = viewModel::onChangeDiscountMethod,
        onDone = onDone,
    )
}
