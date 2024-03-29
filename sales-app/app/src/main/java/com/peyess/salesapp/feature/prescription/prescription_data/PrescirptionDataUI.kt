package com.peyess.salesapp.feature.prescription.prescription_data

import androidx.annotation.RawRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assistant
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.peyess.salesapp.R
import com.peyess.salesapp.constants.minAddition
import com.peyess.salesapp.constants.minPrismDegree
import com.peyess.salesapp.typing.prescription.PrismPosition
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight
import com.peyess.salesapp.ui.component.modifier.MinimumHeightState
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumHeightModifier
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.holdable
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.utils.screen.isHighResolution
import com.peyess.salesapp.utils.screen.isScreenSizeLarge
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.listItemsSingleChoice
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import timber.log.Timber

private val sectionTitleSpacer = 16.dp
private val betweenSectionSpacer = 32.dp

private val symptomsButtonSize = 48.dp
private val symptomsButtonPaddingSize = 12.dp
private val symptomsButtonSpacer = 2.dp

@Composable
fun PrescriptionDataUI(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    onShowSymptoms: () -> Unit = {},

    isMessageLoading: Boolean = false,
    generalMessage: String = stringResource(id = R.string.empty_string),

    isAnimationLoading: Boolean = false,
    @RawRes animationId: Int = R.raw.lottie_lens_far,

    hasAxisRight: Boolean = false,
    hasAxisLeft: Boolean = false,

    hasAddition: Boolean = false,
    hasPrism: Boolean = false,
    toggleHasPrism: () -> Unit = {},

    isPrismAxisRightEnabled: Boolean = false,
    isPrismAxisLeftEnabled: Boolean = false,

    sphericalRight: Double = 0.0,
    onSphericalRightIncrease: (curValue: Double) -> Unit = {},
    onSphericalRightDecrease: (curValue: Double) -> Unit = {},

    cylindricalRight: Double = 0.0,
    onCylindricalRightIncrease: (curValue: Double) -> Unit = {},
    onCylindricalRightDecrease: (curValue: Double) -> Unit = {},

    axisRight: Double = 0.0,
    onAxisRightIncrease: (curValue: Double) -> Unit = {},
    onAxisRightDecrease: (curValue: Double) -> Unit = {},

    additionRight: Double = 0.0,
    onAdditionRightIncrease: (curValue: Double) -> Unit = {},
    onAdditionRightDecrease: (curValue: Double) -> Unit = {},

    prismDegreeRight: Double = 0.0,
    onPrismDegreeRightIncrease: (curValue: Double) -> Unit = {},
    onPrismDegreeRightDecrease: (curValue: Double) -> Unit = {},

    prismAxisRight: Double = 0.0,
    onPrismAxisRightIncrease: (curValue: Double) -> Unit = {},
    onPrismAxisRightDecrease: (curValue: Double) -> Unit = {},

    sphericalLeft: Double = 0.0,
    onSphericalLeftIncrease: (curValue: Double) -> Unit = {},
    onSphericalLeftDecrease: (curValue: Double) -> Unit = {},

    cylindricalLeft: Double = 0.0,
    onCylindricalLeftIncrease: (curValue: Double) -> Unit = {},
    onCylindricalLeftDecrease: (curValue: Double) -> Unit = {},

    axisLeft: Double = 0.0,
    onAxisLeftIncrease: (curValue: Double) -> Unit = {},
    onAxisLeftDecrease: (curValue: Double) -> Unit = {},

    additionLeft: Double = 0.0,
    onAdditionLeftIncrease: (curValue: Double) -> Unit = {},
    onAdditionLeftDecrease: (curValue: Double) -> Unit = {},

    prismDegreeLeft: Double = 0.0,
    onPrismDegreeLeftIncrease: (curValue: Double) -> Unit = {},
    onPrismDegreeLeftDecrease: (curValue: Double) -> Unit = {},

    prismAxisLeft: Double = 0.0,
    onPrismAxisLeftIncrease: (curValue: Double) -> Unit = {},
    onPrismAxisLeftDecrease: (curValue: Double) -> Unit = {},

    prismAxisPositionLeft: PrismPosition = PrismPosition.None,
    onPrismAxisLeftPicked: (position: PrismPosition) -> Unit = {},

    prismAxisPositionRight: PrismPosition = PrismPosition.None,
    onPrismAxisRightPicked: (position: PrismPosition) -> Unit = {},
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(animationId)
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(sectionTitleSpacer))
        if (isAnimationLoading || isMessageLoading) {
            CircularProgressIndicator()
        } else {
            LottieAnimation(
                modifier = Modifier
                    .height(240.dp)
                    .width(240.dp),
                composition = composition,
                iterations = 1,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
            )

            Text(
                text = generalMessage,
                style = MaterialTheme.typography.body1
                    .copy(fontWeight = FontWeight.Bold)
            )
        }
        Spacer(modifier = Modifier.height(betweenSectionSpacer))

        PrescriptionDegrees(
            hasAddition = hasAddition,
            hasPrism = hasPrism,
            toggleHasPrism = toggleHasPrism,

            isPrismAxisLeftEnabled = isPrismAxisLeftEnabled,
            isPrismAxisRightEnabled = isPrismAxisRightEnabled,

            hasAxisRight = hasAxisRight,
            hasAxisLeft = hasAxisLeft,

            sphericalRight = sphericalRight,
            onSphericalRightIncrease = onSphericalRightIncrease,
            onSphericalRightDecrease = onSphericalRightDecrease,

            cylindricalRight = cylindricalRight,
            onCylindricalRightIncrease = onCylindricalRightIncrease,
            onCylindricalRightDecrease = onCylindricalRightDecrease,

            axisRight = axisRight,
            onAxisRightIncrease = onAxisRightIncrease,
            onAxisRightDecrease = onAxisRightDecrease,

            additionRight = additionRight,
            onAdditionRightIncrease = onAdditionRightIncrease,
            onAdditionRightDecrease = onAdditionRightDecrease,

            prismDegreeRight = prismDegreeRight,
            onPrismDegreeRightIncrease = onPrismDegreeRightIncrease,
            onPrismDegreeRightDecrease = onPrismDegreeRightDecrease,

            prismAxisRight = prismAxisRight,
            onPrismAxisRightIncrease = onPrismAxisRightIncrease,
            onPrismAxisRightDecrease = onPrismAxisRightDecrease,

            sphericalLeft = sphericalLeft,
            onSphericalLeftIncrease = onSphericalLeftIncrease,
            onSphericalLeftDecrease = onSphericalLeftDecrease,

            cylindricalLeft = cylindricalLeft,
            onCylindricalLeftIncrease = onCylindricalLeftIncrease,
            onCylindricalLeftDecrease = onCylindricalLeftDecrease,

            axisLeft = axisLeft,
            onAxisLeftIncrease = onAxisLeftIncrease,
            onAxisLeftDecrease = onAxisLeftDecrease,

            additionLeft = additionLeft,
            onAdditionLeftIncrease = onAdditionLeftIncrease,
            onAdditionLeftDecrease = onAdditionLeftDecrease,

            prismDegreeLeft = prismDegreeLeft,
            onPrismDegreeLeftIncrease = onPrismDegreeLeftIncrease,
            onPrismDegreeLeftDecrease = onPrismDegreeLeftDecrease,

            prismAxisLeft = prismAxisLeft,
            onPrismAxisLeftIncrease = onPrismAxisLeftIncrease,
            onPrismAxisLeftDecrease = onPrismAxisLeftDecrease,

            prismAxisPositionLeft = prismAxisPositionLeft,
            onPrismAxisLeftPicked = onPrismAxisLeftPicked,

            prismAxisPositionRight = prismAxisPositionRight,
            onPrismAxisRightPicked = onPrismAxisRightPicked,
        )

        Spacer(modifier = Modifier.height(betweenSectionSpacer))
        Spacer(modifier = Modifier.weight(1f))

        PeyessStepperFooter(
            middle = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.primary,
                                shape = CircleShape,
                            ),
                        onClick = onShowSymptoms,
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(symptomsButtonSize)
                                .padding(symptomsButtonPaddingSize),
                            imageVector = Icons.Filled.Assistant,
                            contentDescription = "Info",
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }

                    Spacer(modifier = Modifier.height(symptomsButtonSpacer))

                    Text(
                        text = stringResource(id = R.string.curiosities).lowercase(),
                        style = MaterialTheme.typography.caption
                            .copy(fontWeight = FontWeight.Bold),
                    )
                }
            },

            onNext = onNext,
        )
    }
}

@Composable
private fun PrescriptionDegrees(
    modifier: Modifier = Modifier,

    hasAddition: Boolean = false,
    hasPrism: Boolean = false,
    toggleHasPrism: () -> Unit = {},

    isPrismAxisLeftEnabled: Boolean = false,
    isPrismAxisRightEnabled: Boolean = false,

    hasAxisRight: Boolean = false,
    hasAxisLeft: Boolean = false,

    sphericalRight: Double = 0.0,
    onSphericalRightIncrease: (curValue: Double) -> Unit = {},
    onSphericalRightDecrease: (curValue: Double) -> Unit = {},

    cylindricalRight: Double = 0.0,
    onCylindricalRightIncrease: (curValue: Double) -> Unit = {},
    onCylindricalRightDecrease: (curValue: Double) -> Unit = {},

    axisRight: Double = 0.0,
    onAxisRightIncrease: (curValue: Double) -> Unit = {},
    onAxisRightDecrease: (curValue: Double) -> Unit = {},

    additionRight: Double = 0.0,
    onAdditionRightIncrease: (curValue: Double) -> Unit = {},
    onAdditionRightDecrease: (curValue: Double) -> Unit = {},

    prismDegreeRight: Double = 0.0,
    onPrismDegreeRightIncrease: (curValue: Double) -> Unit = {},
    onPrismDegreeRightDecrease: (curValue: Double) -> Unit = {},

    prismAxisRight: Double = 0.0,
    onPrismAxisRightIncrease: (curValue: Double) -> Unit = {},
    onPrismAxisRightDecrease: (curValue: Double) -> Unit = {},

    sphericalLeft: Double = 0.0,
    onSphericalLeftIncrease: (curValue: Double) -> Unit = {},
    onSphericalLeftDecrease: (curValue: Double) -> Unit = {},

    cylindricalLeft: Double = 0.0,
    onCylindricalLeftIncrease: (curValue: Double) -> Unit = {},
    onCylindricalLeftDecrease: (curValue: Double) -> Unit = {},

    axisLeft: Double = 0.0,
    onAxisLeftIncrease: (curValue: Double) -> Unit = {},
    onAxisLeftDecrease: (curValue: Double) -> Unit = {},

    additionLeft: Double = 0.0,
    onAdditionLeftIncrease: (curValue: Double) -> Unit = {},
    onAdditionLeftDecrease: (curValue: Double) -> Unit = {},

    prismDegreeLeft: Double = 0.0,
    onPrismDegreeLeftIncrease: (curValue: Double) -> Unit = {},
    onPrismDegreeLeftDecrease: (curValue: Double) -> Unit = {},

    prismAxisLeft: Double = 0.0,
    onPrismAxisLeftIncrease: (curValue: Double) -> Unit = {},
    onPrismAxisLeftDecrease: (curValue: Double) -> Unit = {},

    prismAxisPositionLeft: PrismPosition = PrismPosition.None,
    onPrismAxisLeftPicked: (position: PrismPosition) -> Unit = {},

    prismAxisPositionRight: PrismPosition = PrismPosition.None,
    onPrismAxisRightPicked: (position: PrismPosition) -> Unit = {},
) {
    val density = LocalDensity.current
    val minimumWidthState = remember { MinimumWidthState() }
    val minimumWidthModifier = Modifier.minimumWidthModifier(
        minimumWidthState,
        density
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        SectionTitle(title = stringResource(id = R.string.title_degree))
        Spacer(modifier = Modifier.height(sectionTitleSpacer))
        PrescriptionDegreesTitle(
            minimumWidthModifier = minimumWidthModifier,
            isAxisEnabled = hasAxisRight || hasAxisLeft,
        )
        PrescriptionDegreesInput(
            minimumWidthModifier = minimumWidthModifier,

            subtitle = stringResource(id = R.string.right_eye),

            spherical = sphericalRight,
            onSphericalIncrease = onSphericalRightIncrease,
            onSphericalDecrease = onSphericalRightDecrease,

            cylindrical = cylindricalRight,
            onCylindricalIncrease = onCylindricalRightIncrease,
            onCylindricalDecrease = onCylindricalRightDecrease,

            isAxisEnabled = hasAxisRight,
            axis = axisRight,
            onAxisIncrease = onAxisRightIncrease,
            onAxisDecrease = onAxisRightDecrease,
        )
        PrescriptionDegreesInput(
            minimumWidthModifier = minimumWidthModifier,

            subtitle = stringResource(id = R.string.left_eye),

            spherical = sphericalLeft,
            onSphericalIncrease = onSphericalLeftIncrease,
            onSphericalDecrease = onSphericalLeftDecrease,

            cylindrical = cylindricalLeft,
            onCylindricalIncrease = onCylindricalLeftIncrease,
            onCylindricalDecrease = onCylindricalLeftDecrease,

            isAxisEnabled = hasAxisLeft,
            axis = axisLeft,
            onAxisIncrease = onAxisLeftIncrease,
            onAxisDecrease = onAxisLeftDecrease,
        )
        Spacer(modifier = Modifier.height(betweenSectionSpacer))

        if (hasAddition) {
            SectionTitle(title = stringResource(id = R.string.title_addition))
            Spacer(modifier = Modifier.height(sectionTitleSpacer))
            PrescriptionAdditionInput(
                minimumWidthModifier = minimumWidthModifier,

                subtitle = stringResource(id = R.string.right_eye),

                addition = additionRight,
                onAdditionIncrease = onAdditionRightIncrease,
                onAdditionDecrease = onAdditionRightDecrease,
            )
            PrescriptionAdditionInput(
                minimumWidthModifier = minimumWidthModifier,

                subtitle = stringResource(id = R.string.left_eye),

                addition = additionLeft,
                onAdditionIncrease = onAdditionLeftIncrease,
                onAdditionDecrease = onAdditionLeftDecrease,
            )
            Spacer(modifier = Modifier.height(betweenSectionSpacer))
        }

        SectionTitle(title = stringResource(id = R.string.title_prism)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    checked = hasPrism,
                    onCheckedChange = { toggleHasPrism() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary,
                        checkedTrackColor = MaterialTheme.colors.primary,
                        checkedTrackAlpha = 0.5f,
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color.Gray,
                        uncheckedTrackAlpha = 0.5f,
                    )
                )

                Text(
                    text = stringResource(id = R.string.prism_necessary),
                    style = MaterialTheme.typography.caption,
                )
            }
        }
        Spacer(modifier = Modifier.height(sectionTitleSpacer))
        PrescriptionPrismTitle(
            minimumWidthModifier = minimumWidthModifier,
            enabled = hasPrism,
            axisEnabled = isPrismAxisRightEnabled || isPrismAxisLeftEnabled,
        )
        PrescriptionPrismInput(
            minimumWidthModifier = minimumWidthModifier,

            subtitle = stringResource(id = R.string.right_eye),

            prismDegree = prismDegreeRight,
            onPrismDegreeIncrease = onPrismDegreeRightIncrease,
            onPrismDegreeDecrease = onPrismDegreeRightDecrease,

            prismAxis = prismAxisRight,
            onPrismAxisIncrease = onPrismAxisRightIncrease,
            onPrismAxisDecrease = onPrismAxisRightDecrease,

            prismAxisPosition = prismAxisPositionRight,
            onPrismAxisPicked = onPrismAxisRightPicked,

            enabled = hasPrism,
            axisEnabled = isPrismAxisRightEnabled,
        )
        PrescriptionPrismInput(
            minimumWidthModifier = minimumWidthModifier,

            subtitle = stringResource(id = R.string.left_eye),

            prismDegree = prismDegreeLeft,
            onPrismDegreeIncrease = onPrismDegreeLeftIncrease,
            onPrismDegreeDecrease = onPrismDegreeLeftDecrease,

            prismAxis = prismAxisLeft,
            onPrismAxisIncrease = onPrismAxisLeftIncrease,
            onPrismAxisDecrease = onPrismAxisLeftDecrease,

            prismAxisPosition = prismAxisPositionLeft,
            onPrismAxisPicked = onPrismAxisLeftPicked,

            enabled = hasPrism,
            axisEnabled = isPrismAxisLeftEnabled,
        )
    }
}

@Composable
private fun SectionTitle(
    modifier: Modifier = Modifier,
    title: String = "",
    Trailing: @Composable () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = title, style = MaterialTheme.typography.h5)

        Divider(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .weight(1f),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Trailing()
        }
    }
}

@Composable
private fun PrescriptionDegreesTitle(
    modifier: Modifier = Modifier,
    minimumWidthModifier: Modifier = Modifier,
    isAxisEnabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(id = R.string.left_eye),
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary.copy(alpha = 0f),
                ),
        )

        Text(
            modifier = minimumWidthModifier,
            text = stringResource(id = R.string.degree_spherical),
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
        )

        Text(
            modifier = minimumWidthModifier,
            text = stringResource(id = R.string.degree_cylindrical),
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
        )

        Text(
            modifier = minimumWidthModifier,
            text = stringResource(id = R.string.degree_axis),
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            color = if (isAxisEnabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            }
        )
    }
}

@Composable
private fun PrescriptionDegreesInput(
    modifier: Modifier = Modifier,
    minimumWidthModifier: Modifier = Modifier,
    subtitle: String = "",

    enabled: Boolean = true,

    spherical: Double = 0.0,
    onSphericalIncrease: (value: Double) -> Unit = {},
    onSphericalDecrease: (value: Double) -> Unit = {},

    cylindrical: Double = 0.0,
    onCylindricalIncrease: (value: Double) -> Unit = {},
    onCylindricalDecrease: (value: Double) -> Unit = {},

    isAxisEnabled: Boolean = true,
    axis: Double = 0.0,
    onAxisIncrease: (value: Double) -> Unit = {},
    onAxisDecrease: (value: Double) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = subtitle,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.End),
            color = if (enabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            }
        )

        NumericMeasuring(
            modifier = minimumWidthModifier,

            value = spherical,
            onIncrease = onSphericalIncrease,
            onDecrease = onSphericalDecrease,
        )
        NumericMeasuring(
            modifier = minimumWidthModifier,

            value = cylindrical,
            onIncrease = onCylindricalIncrease,
            onDecrease = onCylindricalDecrease,
        )
        NumericMeasuring(
            modifier = minimumWidthModifier,

            enabled = isAxisEnabled,
            value = axis,
            onIncrease = onAxisIncrease,
            onDecrease = onAxisDecrease,
        )
    }
}

@Composable
private fun PrescriptionAdditionInput(
    modifier: Modifier = Modifier,
    minimumWidthModifier: Modifier = Modifier,

    subtitle: String = "",

    addition: Double = minAddition,
    onAdditionIncrease: (curValue: Double) -> Unit = {},
    onAdditionDecrease: (curValue: Double) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.subtitle1
                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.End),
            )

            NumericMeasuring(
                modifier = minimumWidthModifier,

                value = addition,
                onDecrease = onAdditionDecrease,
                onIncrease = onAdditionIncrease,
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun PrescriptionPrismTitle(
    modifier: Modifier = Modifier,
    minimumWidthModifier: Modifier = Modifier,
    axisEnabled: Boolean = true,
    enabled: Boolean = true,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = stringResource(id = R.string.left_eye),
            style = MaterialTheme.typography.subtitle1
                .copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.primary.copy(alpha = 0f),
                ),
            color = MaterialTheme.colors.primary.copy(alpha = 0f),
        )

        Text(
            modifier = minimumWidthModifier,
            text = stringResource(id = R.string.prism_degree),
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            color = if (enabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            },
        )

        Text(
            modifier = minimumWidthModifier,
            text = stringResource(id = R.string.prism_position),
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            color = if (enabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            },
        )

        Text(
            modifier = minimumWidthModifier,
            text = stringResource(id = R.string.prism_axis),
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
            color = if (axisEnabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            },
        )
    }
}

@Composable
private fun PrescriptionPrismInput(
    modifier: Modifier = Modifier,
    minimumWidthModifier: Modifier = Modifier,
    subtitle: String = "",

    axisEnabled: Boolean = true,
    enabled: Boolean = true,

    prismDegree: Double = minPrismDegree,
    onPrismDegreeIncrease: (curValue: Double) -> Unit = {},
    onPrismDegreeDecrease: (curValue: Double) -> Unit = {},

    prismAxis: Double = minPrismDegree,
    onPrismAxisIncrease: (curValue: Double) -> Unit = {},
    onPrismAxisDecrease: (curValue: Double) -> Unit = {},

    prismAxisPosition: PrismPosition = PrismPosition.None,
    onPrismAxisPicked: (position: PrismPosition) -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp),
            text = subtitle,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.End),
            color = if (enabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            },
        )

        NumericMeasuring(
            modifier = minimumWidthModifier,
            enabled = enabled,

            value = prismDegree,
            onIncrease = onPrismDegreeIncrease,
            onDecrease = onPrismDegreeDecrease,
        )

        TextSelectInput(
            modifier = minimumWidthModifier,
            enabled = enabled,
            position = prismAxisPosition,
            onPick = onPrismAxisPicked,
        )

        NumericMeasuring(
            modifier = minimumWidthModifier,
            enabled = axisEnabled,

            value = prismAxis,
            onIncrease = onPrismAxisIncrease,
            onDecrease = onPrismAxisDecrease,
        )
    }
}

@Composable
fun NumericMeasuring(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: Double = 0.0,
    onIncrease: (value: Double) -> Unit = {},
    onDecrease: (value: Double) -> Unit = {},
    onClick: () -> Unit = {},
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
                .width(IntrinsicSize.Max)
                .clickable { onClick() },
            text = "%.2f".format(value),
            textAlign = TextAlign.Center,
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextSelectInput(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    position: PrismPosition = PrismPosition.None,
    onPick: (value: PrismPosition) -> Unit = {},
) {
    val dialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dialogState,
        properties = DialogProperties(
            usePlatformDefaultWidth = !isHighResolution() || isScreenSizeLarge(),
        ),
        buttons = {
            positiveButton(stringResource(id = R.string.dialog_select_prism_axis_ok))
            negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
        },
    ) {
        title(res = R.string.dialog_select_prism_axis_title)

        listItemsSingleChoice(
            list = PrismPosition.listOfPositions.map { PrismPosition.toName(it) },
            initialSelection = PrismPosition.listOfPositions
                .indexOf(position)
                .coerceAtLeast(0)
        ) {
            val index = it.coerceAtLeast(0)
                .coerceAtMost(PrismPosition.listOfPositions.size)

            Timber.i("Picking from (${PrismPosition.listOfPositions.size}) ${PrismPosition.listOfPositions}")
            Timber.i("Picking at index $index: ${PrismPosition.listOfPositions[index]}")

            val pick = PrismPosition.listOfPositions[index]

            onPick(pick)
            dialogState.hide()
        }
    }


    Row(
        modifier = modifier
            .padding(8.dp)
            .border(
                BorderStroke(
                    2.dp,
                    if (enabled) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray.copy(alpha = 0.5f)
                    },
                ),
                RoundedCornerShape(36.dp),
            )
            .holdable(
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    if (enabled) {
                        dialogState.show()
                    }
                },
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.minimum_touch_target)
                .width(SalesAppTheme.dimensions.minimum_touch_target),
        )

        Text(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .width(IntrinsicSize.Max),
            text = PrismPosition.toName(position),
            textAlign = TextAlign.Center,
            color = if (enabled) {
                MaterialTheme.colors.primary
            } else {
                Color.Gray.copy(alpha = 0.5f)
            },
        )

        Spacer(
            modifier = Modifier
                .height(SalesAppTheme.dimensions.minimum_touch_target)
                .width(SalesAppTheme.dimensions.minimum_touch_target),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CuriositiesDialog(
    showDialog: Boolean = false,
    mikeMessageAmetropies: String = "",
) {
    val scrollState = rememberScrollState()
    val showingCuriosities = remember {
        mutableStateOf(false)
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showingCuriosities.value = false }) {
            SalesAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .requiredWidth(LocalConfiguration.current.screenWidthDp.dp * 0.96f)
                        .scrollable(
                            state = scrollState,
                            orientation = Orientation.Vertical,
                            enabled = true,
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.LightGray
                ) {
                    Column {
                        Text(
                            text = stringResource(
                                id = if (showingCuriosities.value) {
                                    R.string.dialog_curiosities_title
                                } else {
                                    R.string.empty_string
                                }
                            )
                        )

                        AnimatedVisibility(
                            visible = !showingCuriosities.value,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                MikeBubbleRight(
                                    text = mikeMessageAmetropies
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = showingCuriosities.value,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            SymptomsAndCuriosities(modifier = Modifier.fillMaxSize())
                        }


                        if (showingCuriosities.value) {
                            TextButton(onClick = { showingCuriosities.value = false }) {
                                Text(text = stringResource(id = R.string.dialog_curiosities_return_button))

                            }
                        } else {
                            TextButton(onClick = { showingCuriosities.value = true }) {
                                Text(text = stringResource(id = R.string.dialog_curiosities_show_me_button))

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomsAndCuriosities(
    modifier: Modifier = Modifier,

    hasHypermetropia: Boolean = true,
    hasMyopia: Boolean = true,
    hasAstigmatism: Boolean = true,
    hasPresbyopia: Boolean = true,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.scrollable(scrollState, orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        SymptomsAndCuriositiesContent(
            title = stringResource(id = R.string.myopia),
            symptoms = stringResource(id = R.string.dialog_symptoms_myopia),
            curiosities = stringResource(id = R.string.dialog_curiosities_myopia),
        )
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        SymptomsAndCuriositiesContent(
            title = stringResource(id = R.string.astigmatism),
            symptoms = stringResource(id = R.string.dialog_symptoms_astigmatism),
            curiosities = stringResource(id = R.string.dialog_curiosities_astigmatism),
        )
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        SymptomsAndCuriositiesContent(
            title = stringResource(id = R.string.presbyopia),
            symptoms = stringResource(id = R.string.dialog_symptoms_presbyopia),
            curiosities = stringResource(id = R.string.dialog_curiosities_presbyopia),
        )
        Divider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        SymptomsAndCuriositiesContent(
            title = stringResource(id = R.string.hypermetropia),
            symptoms = stringResource(id = R.string.dialog_symptoms_hypermetropia),
            curiosities = stringResource(id = R.string.dialog_curiosities_hypermetropia),
        )
    }
}

@Composable
private fun SymptomsAndCuriositiesContent(
    modifier: Modifier = Modifier,

    title: String = "",
    symptoms: String = "",
    curiosities: String = "",
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = stringResource(id = R.string.dialog_symptoms),
            style = MaterialTheme.typography.h6.copy(textIndent = TextIndent(8.sp, 8.sp)),
        )
        Text(
            text = symptoms,
            style = MaterialTheme.typography.body1.copy(textIndent = TextIndent(18.sp, 18.sp)),
        )

        Text(
            text = stringResource(id = R.string.curiosities),
            style = MaterialTheme.typography.h6.copy(textIndent = TextIndent(8.sp, 8.sp)),
        )
        Text(
            text = curiosities,
            style = MaterialTheme.typography.body1.copy(textIndent = TextIndent(18.sp, 18.sp)),
        )
    }
}



@Preview
@Composable
private fun SymptomsAndCuriositiesPreview() {
    SalesAppTheme {
        SymptomsAndCuriosities(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun SymptomsAndCuriositiesContentPreview() {
    SalesAppTheme {
        SymptomsAndCuriositiesContent(
            modifier = Modifier.fillMaxWidth(),

            title = stringResource(id = R.string.myopia),
            symptoms = stringResource(id = R.string.dialog_symptoms_myopia),
            curiosities = stringResource(id = R.string.dialog_curiosities_myopia),
        )
    }
}

@Preview
@Composable
private fun NumericMeasurePreview() {
    SalesAppTheme {
        NumericMeasuring(modifier = Modifier.height(64.dp), enabled = false)
    }
}

@Preview
@Composable
private fun PrescriptionDegreesPreview() {
    SalesAppTheme {
        PrescriptionDegrees(modifier = Modifier.fillMaxSize())
    }
}

