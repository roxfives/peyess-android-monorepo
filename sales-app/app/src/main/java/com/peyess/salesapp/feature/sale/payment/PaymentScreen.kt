package com.peyess.salesapp.feature.sale.payment

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.data.model.sale.card_flags.CardFlagDocument
import com.peyess.salesapp.feature.sale.payment.model.Client
import com.peyess.salesapp.feature.sale.payment.model.Payment
import com.peyess.salesapp.feature.sale.payment.model.PaymentMethod
import com.peyess.salesapp.feature.sale.payment.state.PaymentState
import com.peyess.salesapp.feature.sale.payment.state.PaymentViewModel
import com.peyess.salesapp.feature.sale.payment.utils.methodDocumentPlaceholder
import com.peyess.salesapp.feature.sale.payment.utils.methodDocumentTitle
import com.peyess.salesapp.feature.sale.payment.utils.parseParameters
import com.peyess.salesapp.ui.annotated_string.annotatedStringResource
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.modifier.MinimumHeightState
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumHeightModifier
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.component.text.PeyessOutlinedTextField
import com.peyess.salesapp.ui.component.text.utils.currencyDigitsOnlyOrEmpty
import com.peyess.salesapp.ui.holdable
import com.peyess.salesapp.ui.text_transformation.CurrencyVisualTransformation
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

private val pictureSize = 220.dp
private val pictureSizePx = 220

private val paymentDataSpacerHeight = 32.dp

private val cardFlagHeight = 40.dp
private val cardFlagWidth = 60.dp
private val cardFlagHeightPx = 40
private val cardFlagWidthPx = 60

private val cardFlagEditSpacerWidth = 8.dp

private val cardSpacerWidth = 2.dp
private val profilePicPadding = 8.dp

private val installmentsButtonSize = 64.dp

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

    val payment by viewModel.collectAsState(PaymentState::payment)

    val totalLeftToPay by viewModel.collectAsState(PaymentState::totalLeftToPay)

    val paymentMethods by viewModel.collectAsState(PaymentState::paymentMethods)
    val arePaymentMethodsLoading by viewModel.collectAsState(PaymentState::arePaymentsLoading)
    val activePaymentMethod by viewModel.collectAsState(PaymentState::activePaymentMethod)

    val cardFlagIcon = payment.cardFlagIcon
    val cardFlagName = payment.cardFlagName

    PaymentScreenImpl(
        modifier = modifier,

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

        activePaymentMethod = activePaymentMethod,
        payment = payment,
        onTotalPaidChanged = viewModel::onTotalPaidChange,
        onPaymentMethodChanged = viewModel::onPaymentMethodChanged,

        onCancel = {
            viewModel.cancelPayment()
            onDone()
        },
        onDone = onDone,
    )
}

@Composable
private fun PaymentScreenImpl(
    modifier: Modifier = Modifier,

    isClientLoading: Boolean = false,
    client: Client = Client(),
    toBePaid: Double = 0.0,

    areCardFlagsLoading: Boolean = false,
    cardFlags: List<CardFlagDocument> = emptyList(),

    cardFlagIcon: Uri = Uri.EMPTY,
    cardFlagName: String = "",
    onCardFlagChanged: (cardFlagIcon: Uri, cardFlagName: String) -> Unit = { _, _ -> },

    arePaymentMethodsLoading: Boolean = false,
    paymentMethods: List<PaymentMethod> = emptyList(),

    methodDocument: String = "",
    onMethodDocumentUpdate: (value: String) -> Unit = {},

    installments: Int = 1,
    onIncreaseInstallments: (value: Int) -> Unit = {},
    onDecreaseInstallments: (value: Int) -> Unit = {},

    activePaymentMethod: PaymentMethod? = null,
    payment: Payment = Payment(),
    onTotalPaidChanged: (value: Double) -> Unit = {},
    onPaymentMethodChanged: (method: PaymentMethod) -> Unit = {},

    onDone: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Column(modifier = modifier) {
        ClientView(
            modifier = Modifier.fillMaxWidth(),

            isClientLoading = isClientLoading,
            client = client,

            toBePaid = toBePaid,
        )

        Divider(
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        PaymentView(
            modifier = Modifier.fillMaxWidth(),

            areCardFlagsLoading = areCardFlagsLoading,
            cardFlags = cardFlags,

            cardFlagIcon = cardFlagIcon,
            cardFlagName = cardFlagName,
            onCardFlagChanged = onCardFlagChanged,

            activePaymentMethod = activePaymentMethod,
            arePaymentMethodsLoading = arePaymentMethodsLoading,
            paymentMethods = paymentMethods,

            methodDocument = methodDocument,
            onMethodDocumentUpdate = onMethodDocumentUpdate,

            installments = installments,
            onIncreaseInstallments = onIncreaseInstallments,
            onDecreaseInstallments = onDecreaseInstallments,

            payment = payment,
            onTotalPaidChanged = onTotalPaidChanged,
            onPaymentMethodChanged = onPaymentMethodChanged,
        )

        Spacer(modifier = Modifier.weight(1f))

        PeyessStepperFooter(
            startButton = {
                OutlinedButton(
                    modifier = Modifier.height(SalesAppTheme.dimensions.minimum_touch_target),
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.error,
                            disabledBackgroundColor = Color.Gray.copy(alpha = 0.5f),
                        ),
                ) {
                    Text(text = stringResource(id = R.string.payment_delete))
                }
            },

            canGoNext = payment.value > 0.0,
            onNext = onDone,
        )
    }
}

@Composable
private fun ClientView(
    modifier: Modifier = Modifier,

    isClientLoading: Boolean = false,
    client: Client,
    toBePaid: Double = 0.0,
    pictureForClient: suspend (clientId: String) -> Uri = { Uri.EMPTY },
) {
    val density = LocalDensity.current
    val minHeightState = remember { MinimumHeightState() }

    val coroutineScope = rememberCoroutineScope()
    val pictureUri = remember { mutableStateOf(Uri.EMPTY) }
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val picture = pictureForClient(client.id)

            pictureUri.value = picture
        }
    }

    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .weight(1f)
                .minimumHeightModifier(state = minHeightState, density = density),
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(profilePicPadding)
                    .size(pictureSize)
                    // Clip image to be shaped as a circle
                    .border(width = 2.dp, color = MaterialTheme.colors.primary, shape = CircleShape)
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
        }

        Spacer(modifier = Modifier.width(cardSpacerWidth))

        Column(
            modifier = Modifier
                .weight(1f)
                .minimumHeightModifier(state = minHeightState, density = density),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (isClientLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = client.name,
                    style = MaterialTheme.typography.h6,
//                        .copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(id = R.string.payment_still_missing),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = NumberFormat.getCurrencyInstance().format(toBePaid),
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PaymentView(
    modifier: Modifier = Modifier,

    areCardFlagsLoading: Boolean = false,
    cardFlags: List<CardFlagDocument> = emptyList(),

    cardFlagIcon: Uri = Uri.EMPTY,
    cardFlagName: String = "",
    onCardFlagChanged: (cardFlagIcon: Uri, cardFlagName: String) -> Unit = { _, _ -> },

    arePaymentMethodsLoading: Boolean = false,
    activePaymentMethod: PaymentMethod? = null,
    paymentMethods: List<PaymentMethod> = emptyList(),

    installments: Int = 1,
    onIncreaseInstallments: (value: Int) -> Unit = {},
    onDecreaseInstallments: (value: Int) -> Unit = {},

    methodDocument: String = "",
    onMethodDocumentUpdate: (value: String) -> Unit = {},

    payment: Payment = Payment(),
    onTotalPaidChanged: (value: Double) -> Unit = {},
    onPaymentMethodChanged: (method: PaymentMethod) -> Unit = {},
) {
    AnimatedVisibility(
        visible = arePaymentMethodsLoading,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        CircularProgressIndicator()
    }

    AnimatedVisibility(
        visible = !arePaymentMethodsLoading,
        enter = scaleIn(),
        exit = scaleOut(),
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top,
        ) {
            val localDensity = LocalDensity.current
            val minimumWidthState = remember {
                MinimumWidthState()
            }

            LazyColumn(
                modifier = Modifier
                    .minimumWidthModifier(
                        state = minimumWidthState,
                        density = localDensity,
                    ),
            ) {
                items(paymentMethods.size) {
                    val isActive = activePaymentMethod?.id == paymentMethods[it].id

                    Box(
                        modifier = Modifier
                            .minimumWidthModifier(
                                state = minimumWidthState,
                                density = localDensity,
                            )
                            .padding(16.dp)
                            .drawBehind {
                                val strokeWidth = 2.dp.value * density
                                val y = size.height - strokeWidth / 2

                                drawLine(
                                    if (isActive) {
                                        Color.LightGray
                                    } else {
                                        Color.Transparent
                                    },
                                    Offset(0f, y),
                                    Offset(size.width, y),
                                    strokeWidth,
                                )
                            }
                            .clickable {
                                onPaymentMethodChanged(paymentMethods[it])
                            },
                    ) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.Center),
                            text = paymentMethods[it].name,
                            style = MaterialTheme.typography.body1
                                .copy(
                                    fontWeight = if (isActive) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    },
                                )
                        )
                    }
                }
            }

            PaymentCard(
                modifier = Modifier.weight(1f),
                paymentMethod = activePaymentMethod,

                total = payment.value,
                onTotalChanged = onTotalPaidChanged,

                cardFlagIcon = cardFlagIcon,
                cardFlagName = cardFlagName,
                onCardFlagChanged = onCardFlagChanged,

                areCardFlagsLoading = areCardFlagsLoading,
                cardFlags = cardFlags,

                methodDocument = methodDocument,
                onMethodDocumentUpdate = onMethodDocumentUpdate,

                installments = installments,
                onIncreaseInstallments = onIncreaseInstallments,
                onDecreaseInstallments = onDecreaseInstallments,
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun PaymentCard(
    modifier: Modifier = Modifier,
    paymentMethod: PaymentMethod? = null,

    areCardFlagsLoading: Boolean = false,
    cardFlags: List<CardFlagDocument> = emptyList(),

    cardFlagIcon: Uri = Uri.EMPTY,
    cardFlagName: String = "",
    onCardFlagChanged: (cardFlagIcon: Uri, cardFlagName: String) -> Unit = { _, _ -> },

    total: Double = 0.0,
    onTotalChanged: (value: Double) -> Unit = {},

    installments: Int = 1,
    onIncreaseInstallments: (value: Int) -> Unit = {},
    onDecreaseInstallments: (value: Int) -> Unit = {},

    methodDocument: String = "",
    onMethodDocumentUpdate: (value: String) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val curPriceInput = BigDecimal(total)
        .setScale(2, RoundingMode.HALF_EVEN)
//        .multiply(BigDecimal(100))

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = annotatedStringResource(id = R.string.payment_how_much),
            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = currencyDigitsOnlyOrEmpty(curPriceInput),
            onValueChange = {
                val value = try {
                    BigDecimal(it)
                        .setScale(2, RoundingMode.DOWN)
                        .divide(BigDecimal(100))
                        .toDouble()
                } catch (t: Throwable) {
                    Timber.e(t, "Failed to parse $it")
                    0.0
                }

                onTotalChanged(value)
            },
//            label = { Text(stringResource(id = R.string.frames_value)) },
//            placeholder = { Text(stringResource(id = R.string.frames_value)) },
            visualTransformation = CurrencyVisualTransformation(fixedCursorAtTheEnd = false),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            textStyle = MaterialTheme.typography.h5
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() },
            ),
        )

        AnimatedVisibility(
            visible = paymentMethod?.hasInstallments ?: false,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Column {
                Spacer(modifier = Modifier.height(paymentDataSpacerHeight))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(id = R.string.payment_installments))

                    Spacer(modifier = Modifier.width(16.dp))

                    InstallmentsInput(
                        value = installments,
                        onIncrease = onIncreaseInstallments,
                        onDecrease = onDecreaseInstallments,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = paymentMethod?.hasDocument ?: false,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            val title = stringResource(id = methodDocumentTitle(paymentMethod?.type))
            val placeholder = stringResource(id = methodDocumentPlaceholder(paymentMethod?.type))

            Column {
                Spacer(modifier = Modifier.height(paymentDataSpacerHeight))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(title)

                    Spacer(modifier = Modifier.width(16.dp))

                    MethodDocumentInput(
                        placeholder = placeholder,

                        methodDocument = methodDocument,
                        onMethodDocumentUpdate = onMethodDocumentUpdate,
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = paymentMethod?.cardFlags?.isNotEmpty() ?: false,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Column {
                Spacer(modifier = Modifier.height(paymentDataSpacerHeight))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(id = R.string.payment_card))

                    Spacer(modifier = Modifier.width(16.dp))

                    CardFlagInput(
                        areCardFlagsLoading = areCardFlagsLoading,
                        cardFlags = cardFlags,

                        cardFlagIcon = cardFlagIcon,
                        cardFlagName = cardFlagName,
                        onCardFlagChanged = onCardFlagChanged,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MethodDocumentInput(
    modifier: Modifier = Modifier,

    placeholder: String = "",
    methodDocument: String = "",
    onMethodDocumentUpdate: (value: String) -> Unit = {},
) {
    val softKeyboardController = LocalSoftwareKeyboardController.current

    PeyessOutlinedTextField(
        modifier = modifier,

        value = methodDocument,
        onValueChange = onMethodDocumentUpdate,

        placeholder = { Text(text = placeholder) },
        label = { /*TODO*/ },

        isError = false,
        errorMessage = "",

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { softKeyboardController?.hide() }
        )
    )
}

@Composable
private fun CardFlagInput(
    modifier: Modifier = Modifier,

    areCardFlagsLoading: Boolean = false,
    cardFlags: List<CardFlagDocument> = emptyList(),


    cardFlagIcon: Uri = Uri.EMPTY,
    cardFlagName: String = "",
    onCardFlagChanged: (cardFlagIcon: Uri, cardFlagName: String) -> Unit = { _, _ -> },
) {
    val dialogState = rememberMaterialDialogState()
    CardFlagInputDialog(
        dialogState = dialogState,

        areCardFlagsLoading = areCardFlagsLoading,
        cardFlags = cardFlags,
        onCardFlagPicked = onCardFlagChanged,
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LazyImage(
                modifier = Modifier.clickable { dialogState.show() },
                image = cardFlagIcon,
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (cardFlagName.isNotBlank()) {
                Text(
                    text = cardFlagName,
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.width(cardFlagEditSpacerWidth))

        IconButton(onClick = { dialogState.show() }) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "",
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CardFlagInputDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    areCardFlagsLoading: Boolean = false,
    cardFlags: List<CardFlagDocument> = emptyList(),
    
    onCardFlagPicked: (cardFlagIcon: Uri, cardFlagName: String) -> Unit = { _, _ -> },
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
        },
    ) {
        AnimatedVisibility(
            visible = areCardFlagsLoading,
            enter = scaleIn(),
            exit = scaleOut()
        ) {
            CircularProgressIndicator()
        }

        listItems(
            list = cardFlags,
            onClick = { _, item -> onCardFlagPicked(item.icon, item.name) },
        ) { _, cardFlag ->
            Row(
                modifier = Modifier.padding(32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .height(cardFlagHeight)
                        .width(cardFlagWidth),
                ) {
                    LazyImage(image = cardFlag.icon)
                }
                
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = cardFlag.name,
                )
            }
        }
    }
}

@Composable
private fun LazyImage(
    modifier: Modifier = Modifier,
    image: Uri = Uri.EMPTY,
) {
    AsyncImage(
        modifier = modifier
            .width(
                if (image == Uri.EMPTY) {
                    cardFlagHeight
                } else {
                    cardFlagWidth
                }
            )
            .height(cardFlagHeight)
            .clip(RoundedCornerShape(8.dp)),

        model = ImageRequest.Builder(LocalContext.current)
            .data(image)
            .decoderFactory(SvgDecoder.Factory())
            .crossfade(true)
            .size(
                width = cardFlagWidthPx,
                height = cardFlagHeightPx,
            )
            .build(),

        contentScale = ContentScale.FillBounds,
        contentDescription = "",

        error = painterResource(id = R.drawable.ic_default_placeholder),
        fallback = painterResource(id = R.drawable.ic_default_placeholder),
        placeholder = painterResource(id = R.drawable.ic_default_placeholder),
    )
}

@Composable
private fun InstallmentsInput(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: Int = 1,
    onIncrease: (value: Int) -> Unit = {},
    onDecrease: (value: Int) -> Unit = {},
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .border(
                BorderStroke(
                    2.dp, if (enabled) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray.copy(alpha = 0.5f)
                    }
                ),
                RoundedCornerShape(36.dp),
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.minimum_touch_target)
                .width(SalesAppTheme.dimensions.minimum_touch_target)
                .holdable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = enabled,
                    onClick = { onDecrease(value) },
                ),
            enabled = enabled,
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "",
                tint = if (enabled) {
                    MaterialTheme.colors.primary
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                },
            )
        }

        Text(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(installmentsButtonSize),
            text = "%02d".format(value),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
            color = if (enabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            },
        )

        IconButton(
            modifier = Modifier
                .holdable(
                    interactionSource = remember { MutableInteractionSource() },
                    enabled = enabled,
                    onClick = { onIncrease(value) }
                ),
            enabled = enabled,
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
                tint = if (enabled) {
                    MaterialTheme.colors.primary
                } else {
                    Color.Gray.copy(alpha = 0.5f)
                },
            )
        }
    }
}

@Preview
@Composable
private fun PaymentCardPreview() {
    SalesAppTheme {
        PaymentCard()
    }
}

@Preview
@Composable
private fun ClientViewPreview() {
    SalesAppTheme {
        ClientView(
            modifier = Modifier.fillMaxWidth(),
            client = Client(name = "Nome Um Pouco Longo"),
            toBePaid = 1200.0,
        )
    }
}

