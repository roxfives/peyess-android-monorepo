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

    val payment by viewModel.collectAsState(EditPaymentState::paymentInput)
    val totalLeftToPay by viewModel.collectAsState(EditPaymentState::totalLeftToPay)

    val arePaymentMethodsLoading by
        viewModel.collectAsState(EditPaymentState::arePaymentMethodsLoading)
    val paymentMethods by viewModel.collectAsState(EditPaymentState::paymentMethods)
    val activePaymentMethod by viewModel.collectAsState(EditPaymentState::activePaymentMethod)

    val areCardFlagsLoading by viewModel.collectAsState(EditPaymentState::areCardFlagsLoading)
    val cardFlags by viewModel.collectAsState(EditPaymentState::cardFlags)

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

        payment = payment,
        toBePaid = totalLeftToPay,

        installments = payment.installments,
        dueDate = payment.dueDate,

        arePaymentMethodsLoading = arePaymentMethodsLoading,
        paymentMethods = paymentMethods,
        activePaymentMethod = activePaymentMethod,

        areCardFlagsLoading = areCardFlagsLoading,
        cardFlags = cardFlags,
        cardFlagIcon = payment.cardFlagIcon,
        cardFlagName = payment.cardFlagName,

        methodDocument = payment.document,

        onDueDateChanged = viewModel::onDueDateChanged,
        onTotalPaidChanged = viewModel::onTotalPaidChange,
        onMethodDocumentUpdate = viewModel::onDocumentChanged,
        onCardFlagChanged = viewModel::onCardFlagChanged,
        onIncreaseInstallments = viewModel::onIncreaseInstallments,
        onDecreaseInstallments = viewModel::onDecreaseInstallments,
        onPaymentMethodChanged = viewModel::onPaymentMethodChanged,
        onDone = {
            viewModel.checkPaymentForMissingClient()
            navHostController.popBackStack()
        },
        onCancel = {
            viewModel.cancelPayment()
            navHostController.popBackStack()
        },
    )
}
