package com.peyess.salesapp.screen.edit_service_order.service_order

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import arrow.core.Either
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.feature.service_order.ServiceOrderUI
import com.peyess.salesapp.screen.edit_service_order.service_order.state.EditServiceOrderState
import com.peyess.salesapp.screen.edit_service_order.service_order.state.EditServiceOrderViewModel
import com.peyess.salesapp.screen.edit_service_order.service_order.utils.ParseParameters
import timber.log.Timber

@Composable
fun EditServiceOrderScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onChangeUser: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onChangeResponsible: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onChangeWitness: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onEditProducts: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onEditPrescription: (saleId: String, serviceOrderId: String, prescriptionId: String) -> Unit = { _, _, _ -> },
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
    val context = LocalContext.current

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

    val isGeneratingPdf by viewModel.collectAsState(EditServiceOrderState::isGeneratingPdf)

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

        onEditPrescription = { onEditPrescription(saleId, serviceOrderId, prescription.id) },

        onEditProducts = { onEditProducts(saleId, serviceOrderId) },

        onAddPayment = {
            viewModel.createPayment {
                onAddPayment(saleId, serviceOrderId, it)
            }
        },
        onEditPayment = { payment ->
            onEditPayment(payment.id, payment.clientId, saleId, serviceOrderId)
        },
        onDeletePayment = { viewModel.deletePayment(it.id) },

        isGeneratingPdfDocument = isGeneratingPdf,
        onGenerateServiceOrderPdf = {
            viewModel.generateServiceOrderPdf(
                context = context,
                onPdfGenerated = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    val uri = FileProvider.getUriForFile(
                        /* context = */ context,
                        /* authority = */ BuildConfig.APPLICATION_ID + ".provider",
                        /* file = */ it,
                    )

                    Timber.i("New uri: $uri")
                    intent.setDataAndType(uri, "application/pdf")
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    Either.catch {
                        ContextCompat.startActivity(context, intent, null)
                    }.tapLeft { err ->
                        Timber.e("Failed to open pdf handler: ${err.message}", err)
                    }
                },
            )
        },
    )
}
