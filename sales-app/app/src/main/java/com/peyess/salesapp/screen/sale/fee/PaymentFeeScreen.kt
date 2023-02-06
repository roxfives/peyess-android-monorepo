package com.peyess.salesapp.screen.sale.fee

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Percent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.screen.sale.fee.state.PaymentFeeState
import com.peyess.salesapp.screen.sale.fee.state.PaymentFeeViewModel
import com.peyess.salesapp.screen.sale.fee.utils.parseParameterFullPrice
import com.peyess.salesapp.screen.sale.fee.utils.parseParameterSaleId
import com.peyess.salesapp.typing.products.PaymentFeeCalcMethod
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.text.PeyessMoneyInput
import com.peyess.salesapp.ui.component.text.PeyessPercentInput
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.utils.string.formatAsPrice
import timber.log.Timber
import java.math.BigDecimal

private val headerSpacerSize = 120.dp
private val titleSpacerSize = 40.dp
private val valueSpacerSize = 40.dp
private val titlePadding = 16.dp

private val dividerWidth = 320.dp

private val feeMethodButtonSize = 120.dp
private val feeMethodButtonUnderlineSize = 80.dp
private val feeMethodButtonSpacerWidth = 40.dp
private val feeMethodButtonSpacer = 4.dp
private val feeMethodButtonPadding = 8.dp
private val feeMethodButtonCornerSize = 8.dp
private val feeMethodButtonHeight = 8.dp

private val pricePreviewSpacer = 32.dp

@Composable
fun PaymentFeeScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),

    onDone: () -> Unit = {},
) {
    val backStackEntry = navHostController.currentBackStackEntryAsState()

    val viewModel: PaymentFeeViewModel = mavericksViewModel()

    val fee by viewModel.collectAsState(PaymentFeeState::fee)
    val pricePreview by viewModel.collectAsState(PaymentFeeState::pricePreview)
    val originalPrice by viewModel.collectAsState(PaymentFeeState::originalPrice)

    parseParameterSaleId(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setSaleId,
    )
    parseParameterFullPrice(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setFullPrice,
    )

    Timber.d("Current value: ${fee.value}")
    PaymentFeeScreenImpl(
        modifier = modifier.fillMaxSize(),

        selectedMethod = fee.method,
        feeValue = BigDecimal(fee.value),
        originalPrice = originalPrice,
        pricePreview = pricePreview,

        onChangeFeeMethod = viewModel::onChangeFeeMethod,
        onChangeFeeValue = { viewModel.onChangeFeeValue(it.toDouble()) },

        onDone = onDone,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun PaymentFeeScreenImpl(
    modifier: Modifier = Modifier,

    selectedMethod: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    feeValue: BigDecimal = BigDecimal(0.0),
    originalPrice: BigDecimal = BigDecimal(0),
    pricePreview: BigDecimal = BigDecimal(0),

    onChangeFeeValue: (value: BigDecimal) -> Unit = {},
    onChangeFeeMethod: (method: PaymentFeeCalcMethod) -> Unit = {},
    onDone: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(headerSpacerSize))
        Text(
            modifier = Modifier.padding(horizontal = titlePadding),
            text = stringResource(id = R.string.fee_title),
            style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
        )
        Spacer(modifier = Modifier.height(titleSpacerSize))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PercentFeeButton(
                isSelected = selectedMethod == PaymentFeeCalcMethod.Percentage,
                onClick = onChangeFeeMethod,
            )

            Spacer(modifier = Modifier.width(feeMethodButtonSpacerWidth))

            WholeFeeButton(
                isSelected = selectedMethod == PaymentFeeCalcMethod.Whole,
                onClick = onChangeFeeMethod,
            )
        }

        Spacer(modifier = Modifier.height(valueSpacerSize))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedVisibility(
                    visible = selectedMethod == PaymentFeeCalcMethod.Whole,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    WholeFeeInput(
                        value = feeValue,
                        onValueChanged = onChangeFeeValue,
                    )
                }

                AnimatedVisibility(
                    visible = selectedMethod == PaymentFeeCalcMethod.Percentage,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    PercentFeeInput(
                        value = feeValue,
                        onValueChanged = onChangeFeeValue,
                    )
                }

                Text(
                    text = stringResource(id = R.string.fee),
                    style = MaterialTheme.typography.body1,
                )
            }
        }

        AnimatedVisibility(
            visible = originalPrice.toDouble() != pricePreview.toDouble(),
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Spacer(modifier = Modifier.height(valueSpacerSize))
                Divider(
                    modifier = Modifier.width(dividerWidth),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.4f),
                )
                Spacer(modifier = Modifier.height(valueSpacerSize))

                PricePreview(
                    originalPrice = originalPrice,
                    price = pricePreview,
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        PeyessStepperFooter(
            nextTitle = stringResource(id = R.string.go_next_default),
            onNext = onDone,
        )
    }
}

@Composable
private fun PricePreview(
    modifier: Modifier = Modifier,

    originalPrice: BigDecimal = BigDecimal(0),
    price: BigDecimal = BigDecimal(0),
) {
    val formattedPricePreview by rememberUpdatedState(newValue = formatAsPrice(value = price))
    val formattedPrice by rememberUpdatedState(newValue = formatAsPrice(value = originalPrice))

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = formattedPrice,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
            )

            Text(
                text = stringResource(id = R.string.fee_original_price),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
            )
        }

        Spacer(modifier = Modifier.width(pricePreviewSpacer))
        Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "")
        Spacer(modifier = Modifier.width(pricePreviewSpacer))

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = formattedPricePreview,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
            )

            Text(
                text = stringResource(id = R.string.fee_final_price),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun WholeFeeInput(
    modifier: Modifier = Modifier,

    value: BigDecimal = BigDecimal(0.0),
    onValueChanged: (value: BigDecimal) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    PeyessMoneyInput(
        modifier = modifier,

        value = value,
        onValueChanged = onValueChanged,

        textStyle = MaterialTheme.typography.h5
            .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() },
        ),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PercentFeeInput(
    modifier: Modifier = Modifier,

    value: BigDecimal = BigDecimal(0.0),
    onValueChanged: (value: BigDecimal) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Timber.d("Current value: $value")

    PeyessPercentInput(
        modifier = modifier,

        value = value,
        onValueChanged = onValueChanged,

        textStyle = MaterialTheme.typography.h5
            .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() },
        ),
    )
}

@Composable
private fun PercentFeeButton(
    modifier: Modifier = Modifier,

    isSelected: Boolean = false,
    onClick: (method: PaymentFeeCalcMethod) -> Unit = {},
) {
    FeeMethodButton(
        modifier = modifier,
        isSelected = isSelected,
        icon = Icons.Filled.Percent,
        method = PaymentFeeCalcMethod.Percentage,
        onClick = onClick,
    )
}

@Composable
private fun WholeFeeButton(
    modifier: Modifier = Modifier,

    isSelected: Boolean = false,
    onClick: (method: PaymentFeeCalcMethod) -> Unit = {},
) {
    FeeMethodButton(
        modifier = modifier,
        isSelected = isSelected,
        icon = Icons.Filled.AttachMoney,
        method = PaymentFeeCalcMethod.Whole,
        onClick = onClick,
    )
}

@Composable
private fun FeeMethodButton(
    modifier: Modifier = Modifier,

    isSelected: Boolean = false,
    icon: ImageVector = Icons.Filled.Percent,
    method: PaymentFeeCalcMethod = PaymentFeeCalcMethod.Percentage,
    onClick: (method: PaymentFeeCalcMethod) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        IconButton(
            modifier = modifier.size(feeMethodButtonSize),
            onClick = { onClick(method) },
        ) {
            Icon(
                modifier = Modifier.size(feeMethodButtonSize),
                imageVector = icon,
                tint = MaterialTheme.colors.primary,
                contentDescription = "",
            )
        }

        Spacer(modifier = Modifier.height(feeMethodButtonSpacer))

        AnimatedVisibility(
            visible = isSelected,
            enter = expandHorizontally(),
            exit = shrinkHorizontally(),
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = feeMethodButtonPadding)
                    .height(feeMethodButtonHeight)
                    .width(feeMethodButtonUnderlineSize)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(
                            corner = CornerSize(feeMethodButtonCornerSize),
                        ),
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun WholeFeeInputPreview() {
    SalesAppTheme {
        WholeFeeInput()
    }
}

@Preview
@Composable
private fun PercentFeeButtonPreview() {
    SalesAppTheme {
        PercentFeeButton(isSelected = true)
    }
}

@Preview
@Composable
private fun WholeFeeButtonPreview() {
    SalesAppTheme {
        WholeFeeButton(isSelected = true)
    }
}

@Preview
@Composable
private fun PriceForecastPreview() {
    SalesAppTheme {
        PricePreview()
    }
}

@Preview
@Composable
private fun FeeScreenImplPreview() {
    SalesAppTheme {
        PaymentFeeScreenImpl(
            selectedMethod = PaymentFeeCalcMethod.Whole,
            pricePreview = BigDecimal(1500.0),
        )
    }
}