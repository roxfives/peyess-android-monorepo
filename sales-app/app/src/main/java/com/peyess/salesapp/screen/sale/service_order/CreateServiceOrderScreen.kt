package com.peyess.salesapp.screen.sale.service_order

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.sale.service_order.state.ServiceOrderState
import com.peyess.salesapp.screen.sale.service_order.state.ServiceOrderViewModel
import com.peyess.salesapp.screen.sale.service_order.utils.parseParameters
import com.peyess.salesapp.feature.service_order.ServiceOrderUI
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

@Composable
fun CreateServiceOrderScreen(
    modifier: Modifier = Modifier,

    navHostController: NavHostController = rememberNavController(),

    onChangeUser: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onChangeResponsible: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onChangeWitness: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },

    onEditPrescription: () -> Unit = {},

    onEditProducts: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },

    onEditMeasure: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onConfirmMeasure: () -> Unit = {},

    onAddPayment: (
        saleId: String,
        serviceOrderId: String,
        paymentId: Long,
    ) -> Unit = { _, _, _ -> },
    onEditPayment: (
        paymentId: Long,
        clientId: String,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _, _ -> },
    onAddDiscount: (saleId: String, fullPrice: BigDecimal) -> Unit = { _, _, -> },
    onAddPaymentFee: (saleId: String, fullPrice: BigDecimal) -> Unit = { _, _, -> },

    onGenerateBudget: () -> Unit = {},
    onFinishSale: () -> Unit = {},
) {
    val context = LocalContext.current

    val viewModel: ServiceOrderViewModel = mavericksViewModel()

    parseParameters(
        navController = navHostController,
        onUpdateIsCreating = viewModel::onUpdateIsCreating,
        onUpdateSaleId = viewModel::onUpdateSaleId,
        onUpdateServiceOrderId = viewModel::onUpdateServiceOrderId,
    )

    val saleId by viewModel.collectAsState(ServiceOrderState::saleId)
    val serviceOrderId by viewModel.collectAsState(ServiceOrderState::serviceOrderId)

    val user by viewModel.collectAsState(ServiceOrderState::userClient)
    val responsible by viewModel.collectAsState(ServiceOrderState::responsibleClient)
    val witness by viewModel.collectAsState(ServiceOrderState::witnessClient)
    val hasWitness by viewModel.collectAsState(ServiceOrderState::hasWitness)

    val userIsLoading by viewModel.collectAsState(ServiceOrderState::isUserLoading)
    val responsibleIsLoading by viewModel.collectAsState(ServiceOrderState::isResponsibleLoading)
    val witnessIsLoading by viewModel.collectAsState(ServiceOrderState::isWitnessLoading)

    val isPrescriptionLoading by viewModel.collectAsState(ServiceOrderState::isPrescriptionLoading)
    val prescription by viewModel.collectAsState(ServiceOrderState::prescription)

    val lens by viewModel.collectAsState(ServiceOrderState::lens)
    val coloring by viewModel.collectAsState(ServiceOrderState::coloring)
    val treatment by viewModel.collectAsState(ServiceOrderState::treatment)
    val frames by viewModel.collectAsState(ServiceOrderState::frames)

    val isLensLoading by viewModel.collectAsState(ServiceOrderState::isLensLoading)
    val isColoringLoading by viewModel.collectAsState(ServiceOrderState::isColoringLoading)
    val isTreatmentLoading by viewModel.collectAsState(ServiceOrderState::isTreatmentLoading)
    val isFramesLoading by viewModel.collectAsState(ServiceOrderState::isFramesLoading)

    val isPositioningLeftLoading by viewModel.collectAsState(ServiceOrderState::isPositioningLeftLoading)
    val measureLeft by viewModel.collectAsState(ServiceOrderState::measureLeft)
    val isPositioningRightLoading by viewModel.collectAsState(ServiceOrderState::isPositioningRightLoading)
    val measureRight by viewModel.collectAsState(ServiceOrderState::measureRight)

    val isPaymentsLoading by viewModel.collectAsState(ServiceOrderState::isPaymentLoading)
    val payments by viewModel.collectAsState(ServiceOrderState::payments)

    val canAddNewPayment by viewModel.collectAsState(ServiceOrderState::canAddNewPayment)
    val totalPaid by viewModel.collectAsState(ServiceOrderState::totalPaid)
    val totalToPay by viewModel.collectAsState(ServiceOrderState::totalToPay)
    val totalToPayWithDiscount by viewModel.collectAsState(ServiceOrderState::totalToPayWithDiscount)
    val totalToPayWithFee by viewModel.collectAsState(ServiceOrderState::totalToPayWithFee)

    val isSaleDone by viewModel.collectAsState(ServiceOrderState::isSaleDone)
    val isSaleLoading by viewModel.collectAsState(ServiceOrderState::isSaleLoading)
    val hasSaleFailed by viewModel.collectAsState(ServiceOrderState::hasSaleFailed)

    val isGeneratingPdfDocument by viewModel.collectAsState(ServiceOrderState::isSOPdfBeingGenerated)

    val confirmationMessage by viewModel.collectAsState(ServiceOrderState::confirmationMessage)

    if (isSaleLoading) {
        SaleLoading(modifier = Modifier.fillMaxSize())
    } else if (hasSaleFailed) {
        SaleFail(modifier = Modifier.fillMaxSize(), onTimeout = viewModel::failedAnimationFinished)
    } else if (isSaleDone) {
        SaleDone(modifier = Modifier.fillMaxSize(), onTimeout = onFinishSale)
    } else {
        ServiceOrderUI(
            modifier = modifier,

            canUpdate = true,
            canUpdateMeasuring = true,
            isUpdating = false,

            onFinishSale = { viewModel.generateSale(context) },

            pictureForClient = viewModel::pictureForClient,

            onChangeResponsible = { onChangeResponsible(saleId, serviceOrderId) },
            onChangeUser = { onChangeUser(saleId, serviceOrderId) },
            onChangeWitness = { onChangeWitness(saleId, serviceOrderId) },

            areUsersLoading = userIsLoading || responsibleIsLoading || witnessIsLoading,
            user = user,
            responsible = responsible,
            witness = witness,
            hasWitness = hasWitness,

            isPrescriptionLoading = isPrescriptionLoading,
            prescription = prescription,
            onEditPrescription = onEditPrescription,

            isProductLoading = isLensLoading
                    || isColoringLoading
                    || isTreatmentLoading
                    || isFramesLoading,
            lens = lens,
            coloring = coloring,
            treatment = treatment,
            frames = frames,
            onEditProducts = {
                viewModel.onEditProducts()
                onEditProducts(saleId, serviceOrderId)
            },

            isMeasureLoading = isPositioningLeftLoading || isPositioningRightLoading,
            measureLeft = measureLeft,
            measureRight = measureRight,
            onEditMeasure = { onEditMeasure(saleId, serviceOrderId) },

            canAddNewPayment = canAddNewPayment,
            totalPaid = totalPaid,
            finalPrice = totalToPayWithFee,
            isPaymentLoading = isPaymentsLoading,
            payments = payments,
            onAddPayment = {
                viewModel.createPayment {
                    onAddPayment(saleId, serviceOrderId, it)
                }
            },
            onAddPaymentFee = {
                onAddPaymentFee(
                    saleId,
                    totalToPayWithDiscount.setScale(2, RoundingMode.HALF_EVEN),
                )
            },
            onDeletePayment = viewModel::deletePayment,
            onEditPayment = { onEditPayment(it.id, it.clientId, saleId, serviceOrderId) },
            onAddDiscount = {
                onAddDiscount(
                    saleId,
                    totalToPay.setScale(2, RoundingMode.HALF_EVEN),
                )
            },

            isGeneratingPdfDocument = isGeneratingPdfDocument,
            onGenerateServiceOrderPdf = {
                viewModel.generateServiceOrderPdf(
                    context = context,
                    onPdfGenerated = {
                        val intent = Intent(Intent.ACTION_VIEW)
                        val uri = FileProvider
                            .getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", it)

                        Timber.i("New uri: $uri")
                        intent.setDataAndType(uri, "application/pdf")
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        try {
                            startActivity(context, intent, null)
                        } catch (err: Throwable) {
                            Timber.e("Failed to open pdf handler: ${err.message}", err)

                        }
                    },
                    onPdfGenerationFailed = {},
                )
            },

            confirmationMessage = confirmationMessage,
        )
    }
}

@Composable
fun SaleDone(modifier: Modifier = Modifier, onTimeout: () -> Unit = {}) {
    val animationStart = remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (!animationStart.value) 0f else 1f,
        animationSpec = tween(4000)
    ) {
        onTimeout()
    }

    LaunchedEffect(true) {
        animationStart.value = true
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_so_successful)
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        LottieAnimation(
            modifier = modifier
                .padding(164.dp)
                .fillMaxSize(),
            composition = composition,
            progress = { progress },
        )
    }
}

@Composable
fun SaleFail(modifier: Modifier = Modifier, onTimeout: () -> Unit = {}) {
    val animationStart = remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (!animationStart.value) 0f else 1f,
        animationSpec = tween(4000)
    ) {
        onTimeout()
    }

    LaunchedEffect(true) {
        animationStart.value = true
    }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_so_failure)
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        LottieAnimation(
            modifier = modifier
                .padding(164.dp)
                .fillMaxSize(),
            composition = composition,
            progress = { progress },
        )
    }
}

@Composable
fun SaleLoading(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_so_loading)
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        LottieAnimation(
            modifier = modifier
                .padding(164.dp)
                .fillMaxSize(),
            composition = composition,
            iterations = LottieConstants.IterateForever,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )
    }
}
