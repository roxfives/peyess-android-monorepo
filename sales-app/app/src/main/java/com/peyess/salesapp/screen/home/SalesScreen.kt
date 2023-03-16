package com.peyess.salesapp.screen.home

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.peyess.salesapp.data.model.sale.service_order.ServiceOrderDocument
import com.peyess.salesapp.screen.home.utils.PurchaseBadge
import com.peyess.salesapp.screen.home.utils.actionButtonTitle
import com.peyess.salesapp.screen.home.utils.displayName
import com.peyess.salesapp.screen.sale.anamnesis.fifth_step_sports.state.FifthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.first_step_first_time.state.FirstTimeViewModel
import com.peyess.salesapp.screen.sale.anamnesis.fourth_step_pain.state.FourthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.second_step_glass_usage.state.SecondStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.sixth_step_time.state.SixthStepViewModel
import com.peyess.salesapp.screen.sale.anamnesis.third_step_sun_light.state.ThirdStepViewModel
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme
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

    onStartNewSale: () -> Unit = {},
) {
    val serviceOrders = serviceOrderList.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        item {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = SalesAppTheme.dimensions.grid_1_5)
                    .fillMaxWidth()
                    .height(buttonHeight),
                shape = MaterialTheme.shapes.large,
                enabled = !isUpdatingProducts,
                onClick = onStartNewSale,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = SalesAppTheme.dimensions.grid_3)
                            .background(color = MaterialTheme.colors.primary.copy(alpha = 0.5f)),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "",
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.btn_make_new_sale),
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }

        item {
            if (isServiceOrderListLoading) {
                PeyessProgressIndicatorInfinite(
                    modifier = Modifier.height(240.dp)
                )
            }
        }

        items(serviceOrders.itemCount) { index ->
            serviceOrders[index]?.let {
                ServiceOrderCard(
                    modifier = Modifier
                        .padding(SalesAppTheme.dimensions.grid_1_5)
                        .fillMaxWidth(),
                    purchase = it,
                    pictureForClient = pictureForClient,

                    isGeneratingPdf = isGeneratingPdfFor.first
                            && isGeneratingPdfFor.second == it.id,
                    onGenerateSalePdf = onGenerateSalePdf,

                    canEditServiceOrder = !isUpdatingProducts,
                    onEditServiceOrder = onEditServiceOrder,
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
private fun ServiceOrderCard(
    modifier: Modifier = Modifier,
    purchase: PurchaseDocument = PurchaseDocument(),

    onGenerateSalePdf: (purchase: PurchaseDocument) -> Unit = {},
    isGeneratingPdf: Boolean = false,

    canEditServiceOrder: Boolean = true,
    onEditServiceOrder: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },

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
                visible = canEditServiceOrder,
                enter = fadeIn(),
                exit = fadeOut(),
            ){
                Button(
                    onClick = {
                        val serviceOrderId = purchase.soIds.firstOrNull() ?: "not-found"

                        onEditServiceOrder(purchase.id, serviceOrderId)
                    },
                ) {
                    Text(text = purchase.state.actionButtonTitle())
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

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
        ServiceOrderCard(
            modifier = Modifier.fillMaxWidth(),
            purchase = PurchaseDocument()
        )
    }
}