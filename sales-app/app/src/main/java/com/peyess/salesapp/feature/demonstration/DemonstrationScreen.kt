package com.peyess.salesapp.feature.demonstration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
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
    val compositionCategory0 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_multi_category_0)
    )
    val compositionCategory1 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_multi_category_1)
    )
    val compositionCategory2 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_multi_category_2)
    )
    val compositionCategory3 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_multi_category_3)
    )

    // TODO: use string resource
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Alto Desempenho",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategory0,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Melhor Escolha",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategory1,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Melhor Custo x Benefício",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategory2,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Tradicinal",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategory3,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )
    }
}

@Composable
private fun TreatmentsList(modifier: Modifier = Modifier) {
    val compositionCategoryFat by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_treatment_fat)
    )
    val compositionCategoryWater by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_treatment_water)
    )
    val compositionCategoryDamaged by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_treatment_damaged)
    )
    val compositionCategoryClear by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_treatment_clear)
    )

    // TODO: use string resource
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Gordura",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategoryFat,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Água",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategoryWater,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Riscos",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategoryDamaged,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Nítido",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategoryClear,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )
    }
}

@Composable
private fun PhotoList(modifier: Modifier = Modifier) {
    val compositionCategoryGray by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_transition_gray)
    )
    val compositionCategoryBrown by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_transition_brown)
    )
    val compositionCategoryGreen by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_transition_green)
    )

    // TODO: use string resource
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Transition Cinza",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategoryGray,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Transition Marrom",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategoryBrown,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )

        Text(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            text = "Transition Verde",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1
                .copy(fontWeight = FontWeight.Bold),
        )

        LottieAnimation(
            modifier = modifier.size(240.dp),
            composition = compositionCategoryGreen,
            iterations = 1,
            clipSpec = LottieClipSpec.Progress(0f, 1f),
        )
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