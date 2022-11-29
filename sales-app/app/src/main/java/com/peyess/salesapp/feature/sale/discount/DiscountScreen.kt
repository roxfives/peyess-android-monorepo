package com.peyess.salesapp.feature.sale.discount

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
import com.peyess.salesapp.feature.sale.discount.state.DiscountState
import com.peyess.salesapp.feature.sale.discount.state.DiscountViewModel
import com.peyess.salesapp.feature.sale.discount.utils.parseParameterFullPrice
import com.peyess.salesapp.feature.sale.discount.utils.parseParameterSaleId
import com.peyess.salesapp.typing.products.DiscountCalcMethod
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

private val discountMethodButtonSize = 120.dp
private val discountMethodButtonUnderlineSize = 80.dp
private val discountMethodButtonSpacerWidth = 40.dp
private val discountMethodButtonSpacer = 4.dp
private val discountMethodButtonPadding = 8.dp
private val discountMethodButtonCornerSize = 8.dp
private val discountMethodButtonHeight = 8.dp

private val pricePreviewSpacer = 32.dp

@Composable
fun DiscountScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),

    onDone: () -> Unit = {},
) {
    val backStackEntry = navHostController.currentBackStackEntryAsState()

    val viewModel: DiscountViewModel = mavericksViewModel()

    val discount by viewModel.collectAsState(DiscountState::discount)
    val pricePreview by viewModel.collectAsState(DiscountState::pricePreview)
    val originalPrice by viewModel.collectAsState(DiscountState::originalPrice)

    parseParameterSaleId(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setSaleId,
    )
    parseParameterFullPrice(
        backStackEntry = backStackEntry.value,
        onUpdate = viewModel::setFullPrice,
    )

    Timber.d("Current value: ${discount.value}")
    DiscountScreenImpl(
        modifier = modifier.fillMaxSize(),

        selectedMethod = discount.method,
        discountValue = BigDecimal(discount.value),
        originalPrice = originalPrice,
        pricePreview = pricePreview,

        onChangeDiscountMethod = viewModel::onChangeDiscountMethod,
        onChangeDiscountValue = { viewModel.onChangeDiscountValue(it.toDouble()) },

        onDone = onDone,
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun DiscountScreenImpl(
    modifier: Modifier = Modifier,

    selectedMethod: DiscountCalcMethod = DiscountCalcMethod.Percentage,
    discountValue: BigDecimal = BigDecimal(0.0),
    originalPrice: BigDecimal = BigDecimal(0),
    pricePreview: BigDecimal = BigDecimal(0),

    onChangeDiscountValue: (value: BigDecimal) -> Unit = {},
    onChangeDiscountMethod: (method: DiscountCalcMethod) -> Unit = {},
    onDone: () -> Unit = {},
) {
    Timber.d("Current value: $discountValue")

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(modifier = Modifier.height(headerSpacerSize))
        Text(
            modifier = Modifier.padding(horizontal = titlePadding),
            text = stringResource(id = R.string.discount_title),
            style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
        )
        Spacer(modifier = Modifier.height(titleSpacerSize))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PercentDiscountButton(
                isSelected = selectedMethod == DiscountCalcMethod.Percentage,
                onClick = onChangeDiscountMethod,
            )

            Spacer(modifier = Modifier.width(discountMethodButtonSpacerWidth))

            WholeDiscountButton(
                isSelected = selectedMethod == DiscountCalcMethod.Whole,
                onClick = onChangeDiscountMethod,
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
                    visible = selectedMethod == DiscountCalcMethod.Whole,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    WholeDiscountInput(
                        value = discountValue,
                        onValueChanged = onChangeDiscountValue,
                    )
                }

                AnimatedVisibility(
                    visible = selectedMethod == DiscountCalcMethod.Percentage,
                    enter = scaleIn(),
                    exit = scaleOut(),
                ) {
                    PercentDiscountInput(
                        value = discountValue,
                        onValueChanged = onChangeDiscountValue,
                    )
                }

                Text(
                    text = stringResource(id = R.string.discount),
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
                text = stringResource(id = R.string.discount_original_price),
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
                text = stringResource(id = R.string.discount_final_price),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun WholeDiscountInput(
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
private fun PercentDiscountInput(
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
private fun PercentDiscountButton(
    modifier: Modifier = Modifier,

    isSelected: Boolean = false,
    onClick: (method: DiscountCalcMethod) -> Unit = {},
) {
    DiscountMethodButton(
        modifier = modifier,
        isSelected = isSelected,
        icon = Icons.Filled.Percent,
        method = DiscountCalcMethod.Percentage,
        onClick = onClick,
    )
}

@Composable
private fun WholeDiscountButton(
    modifier: Modifier = Modifier,

    isSelected: Boolean = false,
    onClick: (method: DiscountCalcMethod) -> Unit = {},
) {
    DiscountMethodButton(
        modifier = modifier,
        isSelected = isSelected,
        icon = Icons.Filled.AttachMoney,
        method = DiscountCalcMethod.Whole,
        onClick = onClick,
    )
}

@Composable
private fun DiscountMethodButton(
    modifier: Modifier = Modifier,

    isSelected: Boolean = false,
    icon: ImageVector = Icons.Filled.Percent,
    method: DiscountCalcMethod = DiscountCalcMethod.None,
    onClick: (method: DiscountCalcMethod) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        IconButton(
            modifier = modifier.size(discountMethodButtonSize),
            onClick = { onClick(method) },
        ) {
            Icon(
                modifier = Modifier.size(discountMethodButtonSize),
                imageVector = icon,
                tint = MaterialTheme.colors.primary,
                contentDescription = "",
            )
        }

        Spacer(modifier = Modifier.height(discountMethodButtonSpacer))

        AnimatedVisibility(
            visible = isSelected,
            enter = expandHorizontally(),
            exit = shrinkHorizontally(),
        ) {
            Box(
                modifier = Modifier
                    .padding(horizontal = discountMethodButtonPadding)
                    .height(discountMethodButtonHeight)
                    .width(discountMethodButtonUnderlineSize)
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(
                            corner = CornerSize(discountMethodButtonCornerSize),
                        ),
                    ),
            )
        }
    }
}

@Preview
@Composable
private fun WholeDiscountInputPreview() {
    SalesAppTheme {
        WholeDiscountInput()
    }
}

@Preview
@Composable
private fun PercentDiscountButtonPreview() {
    SalesAppTheme {
        PercentDiscountButton(isSelected = true)
    }
}

@Preview
@Composable
private fun WholeDiscountButtonPreview() {
    SalesAppTheme {
        WholeDiscountButton(isSelected = true)
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
private fun DiscountScreenImplPreview() {
    SalesAppTheme {
        DiscountScreenImpl(
            selectedMethod = DiscountCalcMethod.Whole,
            pricePreview = BigDecimal(1500.0),
        )
    }
}