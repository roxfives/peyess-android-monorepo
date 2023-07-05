package com.peyess.salesapp.screen.sale.payment

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.payment.PaymentUI
import com.peyess.salesapp.screen.sale.payment.state.PaymentState
import com.peyess.salesapp.screen.sale.payment.state.PaymentViewModel
import com.peyess.salesapp.screen.sale.payment.utils.parseParameters

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: () -> Unit = {},
) {
    val viewModel: PaymentViewModel = mavericksViewModel()

    parseParameters(
        navController = navHostController,
        onUpdatePaymentId = viewModel::onUpdatePaymentId,
        onUpdateClientId = viewModel::onUpdateClientId,
        onUpdateSaleId = viewModel::onUpdateSaleId,
        onUpdateServiceOrderId = viewModel::onUpdateServiceOrderId,
    )

    val isClientLoading by viewModel.collectAsState(PaymentState::isClientLoading)
    val client by viewModel.collectAsState(PaymentState::client)

    val areCardFlagsLoading by viewModel.collectAsState(PaymentState::areCardFlagsLoading)
    val cardFlags by viewModel.collectAsState(PaymentState::cardFlags)

    val payment by viewModel.collectAsState(PaymentState::paymentInput)

    val totalLeftToPay by viewModel.collectAsState(PaymentState::totalLeftToPay)

    val paymentMethods by viewModel.collectAsState(PaymentState::paymentMethods)
    val arePaymentMethodsLoading by viewModel.collectAsState(PaymentState::arePaymentsLoading)
    val activePaymentMethod by viewModel.collectAsState(PaymentState::activePaymentMethod)

    val finishedPayment by viewModel.collectAsState(PaymentState::finishedPayment)

    val cardFlagIcon = payment.cardFlagIcon
    val cardFlagName = payment.cardFlagName

    if (finishedPayment) {
        LaunchedEffect(Unit) {
            onDone()
        }
    }

    PaymentUI(
        modifier = modifier,

        pictureForClient = viewModel::pictureForClient,

        isClientLoading = isClientLoading,
        client = client,
        toBePaid = totalLeftToPay,

        areCardFlagsLoading = areCardFlagsLoading,
        cardFlags = cardFlags,

        cardFlagIcon = cardFlagIcon,
        cardFlagName = cardFlagName,
        onCardFlagChanged = viewModel::onCardFlagChanged,

        arePaymentMethodsLoading = arePaymentMethodsLoading,
        paymentMethods = paymentMethods,

        methodDocument = payment.document,
        onMethodDocumentUpdate = viewModel::onMethodPaymentChanged,

        installments = payment.installments,
        onIncreaseInstallments = viewModel::onIncreaseInstallments,
        onDecreaseInstallments = viewModel::onDecreaseInstallments,

        periodToDueDate = payment.dueDatePeriod,
        onIncreasePeriodDueDate = viewModel::onIncreasePeriodDueDate,
        onDecreasePeriodDueDate = viewModel::onDecreasePeriodDueDate,

        activePaymentMethod = activePaymentMethod,
        payment = payment,
        onTotalPaidChanged = viewModel::onTotalPaidChange,
        onPaymentMethodChanged = viewModel::onPaymentMethodChanged,

        onCancel = {
            viewModel.cancelPayment()
            onDone()
        },
        onDone = viewModel::onFinishPayment,
    )
}
