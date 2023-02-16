package com.peyess.salesapp.screen.edit_service_order.payment_fee

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.peyess.salesapp.feature.payment_fee.PaymentFeeUI

@Composable
fun EditPaymentFeeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {

    PaymentFeeUI(
        modifier = modifier,
//        selectedMethod =,
//        feeValue =,
//        originalPrice =,
//        pricePreview =,
//        onChangeFeeValue = {},
//        onChangeFeeMethod = {},
//        onDone = {}
    )
}