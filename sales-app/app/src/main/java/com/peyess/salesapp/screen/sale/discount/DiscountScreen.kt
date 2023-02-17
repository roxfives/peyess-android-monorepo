package com.peyess.salesapp.screen.sale.discount

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
import com.peyess.salesapp.feature.payment_discount.DiscountScreenUI
import com.peyess.salesapp.screen.sale.discount.state.DiscountState
import com.peyess.salesapp.screen.sale.discount.state.DiscountViewModel
import com.peyess.salesapp.screen.sale.discount.utils.parseParameterFullPrice
import com.peyess.salesapp.screen.sale.discount.utils.parseParameterSaleId
import timber.log.Timber
import java.math.BigDecimal

@Composable
fun DiscountScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),

    onDone: () -> Unit = {},
) {
    val backStackEntry = navHostController.currentBackStackEntryAsState()

    val viewModel: DiscountViewModel = mavericksViewModel()

    val discount by viewModel.collectAsState(DiscountState::currentDiscount)
    val pricePreview by viewModel.collectAsState(DiscountState::pricePreview)
    val originalPrice by viewModel.collectAsState(DiscountState::originalPrice)

    val hasFinished by viewModel.collectAsState(DiscountState::hasFinished)

    parseParameterSaleId(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setSaleId,
    )
    parseParameterFullPrice(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setFullPrice,
    )

    if (hasFinished) {
        LaunchedEffect(Unit) {
            viewModel.onNavigate()
            onDone()
        }
    }

    Timber.d("Current value: ${discount.value}")
    DiscountScreenUI(
        modifier = modifier.fillMaxSize(),

        selectedMethod = discount.method,
        discountValue = BigDecimal(discount.value),
        originalPrice = originalPrice,
        pricePreview = pricePreview,

        onChangeDiscountMethod = viewModel::onChangeDiscountMethod,
        onChangeDiscountValue = { viewModel.onChangeDiscountValue(it.toDouble()) },

        onDone = viewModel::onFinished,
    )
}
