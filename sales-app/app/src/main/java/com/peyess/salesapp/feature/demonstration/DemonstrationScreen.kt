package com.peyess.salesapp.feature.demonstration

import androidx.annotation.RawRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.demonstration.state.DemonstrationShown
import com.peyess.salesapp.feature.demonstration.state.DemonstrationState
import com.peyess.salesapp.feature.demonstration.state.DemonstrationViewModel
import com.peyess.salesapp.ui.component.chip.PeyessChipGroup
import com.peyess.salesapp.ui.component.footer.PeyessNextStep
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title

private val animationWidth = 420.dp
private val animationHeight = 236.dp

private val dialogSpacerHeight = 32.dp
private val infoTextPadding = 32.dp

private val animationDuration = 10000
private val animationProgressStart = 0f
private val animationProgressEnd = 1f


@Composable
fun DemonstrationScreen(
    modifier: Modifier = Modifier,
    onDone: () -> Unit = {},
) {
    val viewModel: DemonstrationViewModel = mavericksViewModel()

    val shown by viewModel.collectAsState(DemonstrationState::demonstrationShown)

    DemonstrationScreenImpl(
        modifier = modifier,

        shown = shown,
        onSelectShown = viewModel::onSelectShown,

        onDone = onDone,
    )
}

@Composable
fun DemonstrationScreenImpl(
    modifier: Modifier = Modifier,
    shown: DemonstrationShown = DemonstrationShown.Multi,
    onSelectShown: (shownName: String) -> Unit = {},
    onDone: () -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        PeyessChipGroup(
            keepSameWidth = true,
            items = DemonstrationShown.listOfDemonstrations(),
            itemName = { DemonstrationShown.nameOf(it ?: DemonstrationShown.Multi) },
            selected = shown,
            onSelectedChanged = onSelectShown,
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (shown) {
            DemonstrationShown.Multi -> MultiList()
            DemonstrationShown.Treatments -> TreatmentsList()
            DemonstrationShown.Photo -> PhotoList()
            DemonstrationShown.Misc -> MiscList()
        }

        Spacer(modifier = Modifier.height(16.dp))
        PeyessNextStep(
            // TODO: use string resource
            nextTitle = "Legal!",
            onNext = onDone,
        )
    }
}

@Composable
private fun MultiList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_multi_category_0),
            animationId = R.raw.lottie_demonstration_multi_category_0,

            infoContent = stringResource(id = R.string.demonstration_multi_category_0_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_multi_category_1),
            animationId = R.raw.lottie_demonstration_multi_category_1,

            infoContent = stringResource(id = R.string.demonstration_multi_category_1_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_multi_category_2),
            animationId = R.raw.lottie_demonstration_multi_category_2,

            infoContent = stringResource(id = R.string.demonstration_multi_category_2_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_multi_category_3),
            animationId = R.raw.lottie_demonstration_multi_category_3,

            infoContent = stringResource(id = R.string.demonstration_multi_category_3_info_content),
        )
    }
}

@Composable
private fun TreatmentsList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_treatment_fat),
            animationId = R.raw.lottie_demonstration_treatment_fat,

            infoContent = stringResource(id = R.string.demonstration_treatment_fat_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_treatment_water),
            animationId = R.raw.lottie_demonstration_treatment_water,

            infoContent = stringResource(id = R.string.demonstration_treatment_water_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_treatment_damaged),
            animationId = R.raw.lottie_demonstration_treatment_damaged,

            infoContent = stringResource(id = R.string.demonstration_treatment_damaged_info_content),
            infoObs = stringResource(id = R.string.demonstration_treatment_damaged_info_obs),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_treatment_clear),
            animationId = R.raw.lottie_demonstration_treatment_clear,

            infoContent = stringResource(id = R.string.demonstration_treatment_clear_info_content),
        )
    }
}

@Composable
private fun PhotoList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_transition_gray),
            animationId = R.raw.lottie_demonstration_transition_gray,

            infoContent = stringResource(id = R.string.demonstration_transition_gray_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_transition_brown),
            animationId = R.raw.lottie_demonstration_transition_brown,

            infoContent = stringResource(id = R.string.demonstration_transition_brown_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_transition_green),
            animationId = R.raw.lottie_demonstration_transition_green,

            infoContent = stringResource(id = R.string.demonstration_transition_green_info_content),
        )
    }
}

@Composable
private fun MiscList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_mono_simple),
            animationId = R.raw.lottie_demonstration_mono_simple,

            infoContent = stringResource(id = R.string.demonstration_mono_simple_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_mono_blue),
            animationId = R.raw.lottie_demonstration_mono_blue,

            infoContent = stringResource(id = R.string.demonstration_mono_blue_info_content),
        )

        LensAnimation(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.demonstration_mono_driving),
            animationId = R.raw.lottie_demonstration_mono_driving,

            infoContent = stringResource(id = R.string.demonstration_mono_driving_info_content),
        )
    }
}

@Composable
private fun LensAnimation(
    modifier: Modifier = Modifier,
    title: String = "",
    @RawRes animationId: Int = R.raw.lottie_demonstration_multi_category_0,
    infoContent: String = "",
    infoObs: String = "",
    onClickInfo: () -> Unit = {}
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationId))

    val isPlaying = remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (isPlaying.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = if (isPlaying.value) {
            tween(
                durationMillis = animationDuration,
                easing = LinearEasing,
            )
        } else {
            snap()
        },
    ) {
        isPlaying.value = false
    }

    val infoDialogState = rememberMaterialDialogState()
    InfoDialog(
        title = title,
        dialogState = infoDialogState,
        infoContent = infoContent,
        infoObs = infoObs,
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = title,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { isPlaying.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }

            Divider(
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
            )

            IconButton(
                onClick = {
                    infoDialogState.show()
                    onClickInfo()
                },
            ) {
                Icon(imageVector = Icons.Filled.Info, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = Modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = composition,
            contentScale = ContentScale.FillWidth,
            progress = { progress }
        )
    }
}

@Composable
private fun InfoDialog(
    modifier: Modifier = Modifier,

    dialogState: MaterialDialogState = rememberMaterialDialogState(),

    title: String = "",
    infoContent: String = "",
    infoObs: String = "",
) {
    // TODO: use string resource
    MaterialDialog(
        dialogState = dialogState,
        buttons = { positiveButton("Legal!") },
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {
            title(title)

            Text(
                modifier = Modifier.padding(horizontal = infoTextPadding),
                text = infoContent,
                style = MaterialTheme.typography.body1
                    .copy(textAlign = TextAlign.Center),
            )

            if (infoObs.isNotBlank()) {
                Spacer(modifier = Modifier.height(dialogSpacerHeight))

                Text(
                    modifier = Modifier.padding(horizontal = infoTextPadding),
                    text = infoObs,
                    style = MaterialTheme.typography.caption,
                )
            }
        }
    }
}

@Preview
@Composable
private fun MultiListPreview() {
    SalesAppTheme {
        MultiList()
    }
}

@Preview
@Composable
private fun TreatmentsListPreview() {
    SalesAppTheme {
        TreatmentsList()
    }
}

@Preview
@Composable
private fun PhotoListPreview() {
    SalesAppTheme {
        PhotoList()
    }
}