package com.peyess.salesapp.screen.edit_service_order.service_order

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.service_order.ServiceOrderUI
import com.peyess.salesapp.screen.edit_service_order.service_order.state.EditServiceOrderState
import com.peyess.salesapp.screen.edit_service_order.service_order.state.EditServiceOrderViewModel
import com.peyess.salesapp.screen.edit_service_order.service_order.utils.ParseParameters

@Composable
fun EditServiceOrderScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onChangeUser: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onChangeResponsible: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onChangeWitness: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onAddPayment: (saleId: String, serviceOrderId: String, paymentId: Long) -> Unit = { _, _, _ -> },
    onEditPayment: (
        paymentId: Long,
        clientId: String,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _, _ -> },
    onAddPaymentFee: (saleId: String, fullPrice: Double) -> Unit = { _, _ -> },
    onAddDiscount: (saleId: String, fullPrice: Double) -> Unit = { _, _ -> },
) {
    val viewModel: EditServiceOrderViewModel = mavericksViewModel()
    
    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::onSaleIdChanged,
        onUpdateServiceOrderId = viewModel::onServiceOrderIdChanged,
    )

    val saleId by viewModel.collectAsState(EditServiceOrderState::saleId)
    val serviceOrderId by viewModel.collectAsState(EditServiceOrderState::serviceOrderId)

    val userPicked by viewModel.collectAsState(EditServiceOrderState::userPicked)
    val responsiblePicked by viewModel.collectAsState(EditServiceOrderState::responsiblePicked)
    val witnessPicked by viewModel.collectAsState(EditServiceOrderState::witnessPicked)
    val hasWitness by viewModel.collectAsState(EditServiceOrderState::hasWitness)

    val prescription by viewModel.collectAsState(EditServiceOrderState::prescription)

    val measuringLeft by viewModel.collectAsState(EditServiceOrderState::measuringLeft)
    val measuringRight by viewModel.collectAsState(EditServiceOrderState::measuringRight)

    val lens by viewModel.collectAsState(EditServiceOrderState::lens)
    val coloring by viewModel.collectAsState(EditServiceOrderState::coloring)
    val treatment by viewModel.collectAsState(EditServiceOrderState::treatment)
    val frames by viewModel.collectAsState(EditServiceOrderState::frames)

    val canAddNewPayment by viewModel.collectAsState(EditServiceOrderState::canAddNewPayment)
    val payments by viewModel.collectAsState(EditServiceOrderState::payments)
    val totalPaid by viewModel.collectAsState(EditServiceOrderState::totalPaid)
    val fullPrice by viewModel.collectAsState(EditServiceOrderState::fullPrice)
    val priceWithDiscount by viewModel.collectAsState(EditServiceOrderState::priceWithDiscountOnly)
    val finalPrice by viewModel.collectAsState(EditServiceOrderState::finalPrice)

    ServiceOrderUI(
        modifier = modifier,

        prescription = prescription,

        measureLeft = measuringLeft,
        measureRight = measuringRight,

        hasWitness = hasWitness,
        user = userPicked,
        responsible = responsiblePicked,
        witness = witnessPicked,

        lens = lens,
        coloring = coloring,
        treatment = treatment,
        frames = frames,

        canAddNewPayment = canAddNewPayment,
        payments = payments,
        totalPaid = totalPaid,
        finalPrice = finalPrice,

        onChangeUser = { onChangeUser(saleId, serviceOrderId) },
        onChangeResponsible = { onChangeResponsible(saleId, serviceOrderId) },
        onChangeWitness = { onChangeWitness(saleId, serviceOrderId) },

        onAddPaymentFee = { onAddPaymentFee(saleId, priceWithDiscount) },
        onAddDiscount = { onAddDiscount(saleId, fullPrice) },

        onAddPayment = {
            viewModel.createPayment {
                onAddPayment(saleId, serviceOrderId, it)
            }
        },
        onEditPayment = { payment ->
            onEditPayment(payment.id, payment.clientId, saleId, serviceOrderId)
        },
        onDeletePayment = { viewModel.deletePayment(it.id) },
    )
}
