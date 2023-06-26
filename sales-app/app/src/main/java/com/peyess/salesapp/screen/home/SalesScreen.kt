package com.peyess.salesapp.screen.home

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.peyess.salesapp.BuildConfig
import com.peyess.salesapp.R
import com.peyess.salesapp.app.state.MainAppState
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.app.state.PurchaseStream
import com.peyess.salesapp.data.model.sale.purchase.DenormalizedClientDocument
import com.peyess.salesapp.data.model.sale.purchase.PurchaseDocument
import com.peyess.salesapp.screen.home.dialog.ConfirmFinishSaleDialog
import com.peyess.salesapp.screen.home.utils.PurchaseBadge
import com.peyess.salesapp.screen.home.utils.canFinishFromState
import com.peyess.salesapp.screen.home.utils.displayName
import com.peyess.salesapp.screen.sale.anamnesis.fifth_step_sports.state.FifthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.first_step_first_time.state.FirstTimeViewModel
import com.peyess.salesapp.screen.sale.anamnesis.fourth_step_pain.state.FourthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.second_step_glass_usage.state.SecondStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.sixth_step_time.state.SixthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.third_step_sun_light.state.ThirdStepViewModel
import com.peyess.salesapp.typing.sale.PurchaseSyncState
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.NumberFormat
import java.time.format.DateTimeFormatter

private val buttonHeight = 72.dp

private val pictureSize = 60.dp
private val pictureSizePx = 60

private val profilePicPadding = 8.dp

@Composable
fun SalesScreen(
    modifier: Modifier = Modifier,
    onStartNewSale: () -> Unit = {},
    onEditServiceOrder: (saleId: String, serviceOrderId: String) -> Unit = { _, _ ->},
) {
    val context = LocalContext.current

    val firstStepViewModel: FirstTimeViewModel = mavericksActivityViewModel()
    val secondStepViewModel: SecondStepViewModel = mavericksActivityViewModel()
    val thirdStepViewModel: ThirdStepViewModel = mavericksActivityViewModel()
    val fourthStepViewModel: FourthStepViewModel = mavericksActivityViewModel()
    val fifthStepViewModel: FifthStepViewModel = mavericksActivityViewModel()
    val sixthStepViewModel: SixthStepViewModel = mavericksActivityViewModel()

    val viewModel: MainViewModel = mavericksActivityViewModel()

    val isCreatingNewSale by viewModel.collectAsState(MainAppState::isCreatingNewSale)
    val hasCreatedSale by viewModel.collectAsState(MainAppState::hasCreatedSale)

    val isUpdatingProducts by viewModel.collectAsState(MainAppState::isUpdatingProducts)

    val isServiceOrderListLoading by viewModel.collectAsState(MainAppState::isServiceOrderListLoading)
    val serviceOrderList by viewModel.collectAsState(MainAppState::purchaseListStream)

    val isGeneratingPdfFor by viewModel.collectAsState(MainAppState::isGeneratingPdfFor)

    val finishingSales by viewModel.collectAsState(MainAppState::finishingSales)

    if (hasCreatedSale) {
        LaunchedEffect(Unit) {
            firstStepViewModel.resetState()
            secondStepViewModel.resetState()
            thirdStepViewModel.resetState()
            fourthStepViewModel.resetState()
            fifthStepViewModel.resetState()
            sixthStepViewModel.resetState()

            viewModel.newSaleStarted()
            onStartNewSale()
        }
    }

    if (isCreatingNewSale) {
        PeyessProgressIndicatorInfinite(modifier = modifier)
    } else {
        SaleList(
            modifier = modifier,

            isUpdatingProducts = isUpdatingProducts,
            isServiceOrderListLoading = isServiceOrderListLoading,

            serviceOrderList = serviceOrderList,
            onStartNewSale = {
                viewModel.startNewSale()
            },

            pictureForClient = viewModel::pictureForClient,

            isGeneratingPdfFor = isGeneratingPdfFor,
            onGenerateSalePdf = {
                viewModel.generateServiceOrderPdf(
                    context = context,
                    purchase = it,
                    onPdfGenerationFailure = {},
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
                    }
                )
            },

            finishingSales = finishingSales,
            onFinishingSale = viewModel::finishSale,

            onSyncRetry = viewModel::retrySyncSale,

            onEditServiceOrder = { saleId, serviceOrderId ->
                onEditServiceOrder(saleId, serviceOrderId)
            }
        )
    }
}

@Composable
fun SaleList(
    modifier: Modifier = Modifier,

    isUpdatingProducts: Boolean = true,
    isServiceOrderListLoading: Boolean = false,

    serviceOrderList: PurchaseStream = emptyFlow(),

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },

    isGeneratingPdfFor: Pair<Boolean, String> = Pair(false, ""),
    onGenerateSalePdf: (purchase: PurchaseDocument) -> Unit = {},

    onEditServiceOrder: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },

    finishingSales: List<String> = emptyList(),
    onFinishingSale: (saleId: String) -> Unit = {},

    onSyncRetry: (saleId: String) -> Unit = {},

    onStartNewSale: () -> Unit = {},
) {
    val serviceOrders = serviceOrderList.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {

        item {
            if (isServiceOrderListLoading) {
                PeyessProgressIndicatorInfinite(
                    modifier = Modifier.height(240.dp)
                )
            }
        }

        items(serviceOrders.itemCount) { index ->
            serviceOrders[index]?.let {
                PurchaseCard(
                    modifier = Modifier
                        .padding(SalesAppTheme.dimensions.grid_1_5)
                        .fillMaxWidth(),
                    purchase = it,
                    pictureForClient = pictureForClient,

                    isGeneratingPdf = isGeneratingPdfFor.first
                            && isGeneratingPdfFor.second == it.id,
                    onGenerateSalePdf = onGenerateSalePdf,
                    
                    isFinishingSale = finishingSales.contains(it.id),
                    onFinishingSale = onFinishingSale,
                    onEditSale = onEditServiceOrder,
                    onSyncRetry = onSyncRetry,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun PurchaseCard(
    modifier: Modifier = Modifier,
    purchase: PurchaseDocument = PurchaseDocument(),

    onGenerateSalePdf: (purchase: PurchaseDocument) -> Unit = {},
    isGeneratingPdf: Boolean = false,

    isFinishingSale: Boolean = false,
    onEditSale: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
    onFinishingSale: (saleId: String) -> Unit = {},
    onSyncRetry: (saleId: String) -> Unit = {},

    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
) {
    val client = remember {
        purchase.clients.firstOrNull() ?: DenormalizedClientDocument()
    }

    val coroutineScope = rememberCoroutineScope()
    val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val picture = pictureForClient(client.uid)

            pictureUri.value = picture
        }
    }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val message = stringResource(id = R.string.purchase_card_message)
        .format(
            NumberFormat.getCurrencyInstance().format(purchase.finalPrice),
            purchase.created.format(formatter),
            purchase.responsibleName,
        )

    Column(
        modifier = modifier
            .border(
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(
                    width = 2.dp,
                    MaterialTheme.colors.primary.copy(alpha = 0.5f),
                ),
            ),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(profilePicPadding)
                    .size(pictureSize)
                    // Clip image to be shaped as a circle
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape,
                    )
                    .clip(CircleShape),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pictureUri.value)
                    .crossfade(true)
                    .size(width = pictureSizePx, height = pictureSizePx)
                    .build(),
                contentScale = ContentScale.FillBounds,
                contentDescription = "",
                error = painterResource(id = R.drawable.ic_profile_placeholder),
                fallback = painterResource(id = R.drawable.ic_profile_placeholder),
                placeholder = painterResource(id = R.drawable.ic_profile_placeholder),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.height(IntrinsicSize.Max),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = client.name,
                    style = MaterialTheme.typography.subtitle1
                        .copy(fontWeight = FontWeight.Bold),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    purchase.state.PurchaseBadge()

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = purchase.state.displayName(),
                        style = MaterialTheme.typography.body1,
                    )
                }

                Spacer(modifier = Modifier.width(2.dp))
            }
        }

        Text(
            modifier = Modifier.padding(16.dp),
            text = message,
        )

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(
                visible = isFinishingSale,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CircularProgressIndicator()
            }

            AnimatedVisibility(
                visible = purchase.syncState == PurchaseSyncState.Syncing,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp))

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = R.string.purchase_sync_status_syncing),
                        style = MaterialTheme.typography.caption
                            .copy(fontWeight = FontWeight.Bold),
                    )
                }
            }

            AnimatedVisibility(
                visible = purchase.syncState == PurchaseSyncState.SyncSuccessful,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = Icons.Filled.CheckCircle,
                        tint = Color.hsl(116f, 0.46f, 0.486f),
                        contentDescription = "",
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = stringResource(id = R.string.purchase_sync_status_sync_successful),
                        style = MaterialTheme.typography.caption
                            .copy(fontWeight = FontWeight.Bold),
                    )
                }
            }

            AnimatedVisibility(
                visible = purchase.syncState == PurchaseSyncState.SyncFailed,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.Filled.Cancel,
                            tint = MaterialTheme.colors.error,
                            contentDescription = "",
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(id = R.string.purchase_sync_status_sync_failed),
                            style = MaterialTheme.typography.caption
                                .copy(fontWeight = FontWeight.Bold),
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = CircleShape,
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape,
                                ),
                            onClick = { onSyncRetry(purchase.id) },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Sync,
                                contentDescription = "",
                                tint = MaterialTheme.colors.primary,
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(id = R.string.sales_sync_retry),
                            style = MaterialTheme.typography.caption
                                .copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = purchase.state.canFinishFromState(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
//                        val serviceOrderId = purchase.soIds.firstOrNull() ?: "not-found"
                        val dialogState = rememberMaterialDialogState()
                        ConfirmFinishSaleDialog(
                            dialogState = dialogState,
                            onConfirmFinish = { onFinishingSale(purchase.id) },
                            onCancelFinish = { dialogState.hide() }
                        )

                        IconButton(
                            modifier = Modifier
                                .background(
                                    color = Color.Transparent,
                                    shape = CircleShape,
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape,
                                ),
                            onClick = { dialogState.show() },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "",
                                tint = MaterialTheme.colors.primary,
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = stringResource(id = R.string.sales_finish_sale),
                            style = MaterialTheme.typography
                                .caption.copy(fontWeight = FontWeight.Bold),
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        IconButton(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape,
                                )
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colors.primary,
                                    shape = CircleShape,
                                ),
                            onClick = {
                                val serviceOrderId = purchase.soIds.firstOrNull() ?: "not-found"
                                onEditSale(purchase.id, serviceOrderId)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "",
                                tint = MaterialTheme.colors.onPrimary,
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = stringResource(id = R.string.sales_edit_sale),
                            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = isGeneratingPdf,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                CircularProgressIndicator()
            }

            AnimatedVisibility(visible = !isGeneratingPdf) {
                Button(
                    onClick = {
                        if (!isGeneratingPdf) {
                            onGenerateSalePdf(purchase)
                        }
                    },
                ) {
                    Text(text = stringResource(id = R.string.btn_open_pdf))
                }
            }
        }
    }
}

@Preview
@Composable
private fun SalesPreview() {
    SalesAppTheme {
        SalesScreen()
    }
}

@Preview
@Composable
private fun ServiceOrderCardPreview() {
    SalesAppTheme {
        PurchaseCard(
            modifier = Modifier.fillMaxWidth(),
            purchase = PurchaseDocument()
        )
    }
}