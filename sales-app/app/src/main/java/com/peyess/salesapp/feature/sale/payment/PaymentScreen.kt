package com.peyess.salesapp.feature.sale.payment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import coil.request.ImageRequest
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.client.firestore.ClientDocument
import com.peyess.salesapp.dao.payment_methods.PaymentMethod
import com.peyess.salesapp.dao.sale.payment.SalePaymentEntity
import com.peyess.salesapp.feature.sale.payment.state.PaymentState
import com.peyess.salesapp.feature.sale.payment.state.PaymentViewModel
import com.peyess.salesapp.ui.annotated_string.annotatedStringResource
import com.peyess.salesapp.ui.component.modifier.MinimumHeightState
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumHeightModifier
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.text_transformation.CurrencyVisualTransformation
import com.peyess.salesapp.ui.theme.SalesAppTheme
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

private val pictureSize = 220.dp
private val pictureSizePx = 220

private val cardPadding = 16.dp
private val cardSpacerWidth = 2.dp
private val profilePicPadding = 8.dp

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onDone: () -> Unit = {},
) {
    val viewModel: PaymentViewModel = mavericksViewModel()

    val clientId = navHostController.currentBackStackEntry?.arguments?.getString("clientId")
    val paymentId = navHostController.currentBackStackEntry?.arguments?.getLong("paymentId")

    LaunchedEffect(clientId) {
        viewModel.loadClient(clientId ?: "")
    }
    LaunchedEffect(paymentId) {
        viewModel.loadPayment(paymentId ?: 0L)
    }

    val isClientLoading by viewModel.collectAsState(PaymentState::isClientLoading)
    val client by viewModel.collectAsState(PaymentState::client)

    val payment by viewModel.collectAsState(PaymentState::payment)

    val totalLeftToPay by viewModel.collectAsState(PaymentState::totalLeftToPay)

    val paymentMethods by viewModel.collectAsState(PaymentState::paymentMethods)
    val arePaymentMethodsLoading by viewModel.collectAsState(PaymentState::arePaymentsLoading)

    PaymentScreenImpl(
        modifier = modifier,

        isClientLoading = isClientLoading,
        client = client,
        toBePaid = totalLeftToPay,

        arePaymentMethodsLoading = arePaymentMethodsLoading,
        paymentMethods = paymentMethods,

        payment = payment,
        onTotalPaidChanged = viewModel::onTotalPaidChange,

        onDone = onDone,
    )
}

@Composable
private fun PaymentScreenImpl(
    modifier: Modifier = Modifier,

    isClientLoading: Boolean = false,
    client: ClientDocument = ClientDocument(),
    toBePaid: Double = 0.0,

    arePaymentMethodsLoading: Boolean = false,
    paymentMethods: List<PaymentMethod> = emptyList(),

    payment: SalePaymentEntity = SalePaymentEntity(),
    onTotalPaidChanged: (value: Double) -> Unit = {},

    onDone: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    Column(
        modifier = modifier,

    ) {
        ClientView(
            modifier = Modifier.fillMaxWidth(),

            isClientLoading = isClientLoading,
            client = client,

            toBePaid = toBePaid,
        )

        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        PaymentView(
            modifier = Modifier.fillMaxWidth(),

            arePaymentMethodsLoading = arePaymentMethodsLoading,
            paymentMethods = paymentMethods,

            payment = payment,
            onTotalPaidChanged = onTotalPaidChanged,
        )
    }
}

@Composable
private fun ClientView(
    modifier: Modifier = Modifier,

    isClientLoading: Boolean = false,
    client: ClientDocument,
    toBePaid: Double = 0.0,
) {
    val density = LocalDensity.current
    val minHeightState = remember { MinimumHeightState() }

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
                    .data(client.picture)
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

    arePaymentMethodsLoading: Boolean = false,
    activePaymentMethod: PaymentMethod? = null,
    paymentMethods: List<PaymentMethod> = emptyList(),

    payment: SalePaymentEntity = SalePaymentEntity(),
    onTotalPaidChanged: (value: Double) -> Unit = {},
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
                    val isActive = remember {
                        activePaymentMethod?.id == paymentMethods[it].id
                    }

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
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
private fun PaymentCard(
    modifier: Modifier = Modifier,
    paymentMethod: PaymentMethod? = null,

    total: Double = 0.0,
    onTotalChanged: (value: Double) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val curPriceInput = BigDecimal(total)
        .setScale(2, RoundingMode.HALF_EVEN)
        .multiply(BigDecimal(100))
        .toBigInteger()

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
            value = "$curPriceInput",
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

        Spacer(modifier = Modifier.height(8.dp))

        AnimatedVisibility(
            visible = paymentMethod?.cardFlags?.isNotEmpty() ?: false,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Text(text = "Card flags")
        }

        AnimatedVisibility(
            visible = paymentMethod?.hasDocument ?: false,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Text(text = "Document input")
        }

        AnimatedVisibility(
            visible = paymentMethod?.hasDocumentPicture ?: false,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Text(text = "Add document picture")
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
            client = ClientDocument(name = "Nome Um Pouco Longo"),
            toBePaid = 1200.0,
        )
    }
}

