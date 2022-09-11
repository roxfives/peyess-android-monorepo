package com.peyess.salesapp.feature.demonstration

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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

private val animationWidth = 420.dp
private val animationHeight = 236.dp

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
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_multi_category_3)
    )
    val compositionCategory3 by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_demonstration_multi_category_2)
    )

    val animationStartCategory0 = remember { mutableStateOf(false) }
    val progressCategory0 by animateFloatAsState(
        targetValue = if (animationStartCategory0.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategory0.value = false
    }

    val animationStartCategory1 = remember { mutableStateOf(false) }
    val progressCategory1 by animateFloatAsState(
        targetValue = if (animationStartCategory1.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategory1.value = false
    }

    val animationStartCategory2 = remember { mutableStateOf(false) }
    val progressCategory2 by animateFloatAsState(
        targetValue = if (animationStartCategory2.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategory2.value = false
    }

    val animationStartCategory3 = remember { mutableStateOf(false) }
    val progressCategory3 by animateFloatAsState(
        targetValue = if (animationStartCategory3.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategory3.value = false
    }

    // TODO: use string resource
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
                text = "Alto Desempenho",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategory0.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategory0,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategory0 }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Melhor Escolha",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategory1.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }


        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategory1,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategory1 }
        )


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Melhor Custo x Benefício",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategory2.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategory2,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategory2 }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Tradicional",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategory3.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }


        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategory3,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategory3 }
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

    val animationStartCategoryFat = remember { mutableStateOf(false) }
    val progressCategoryFat by animateFloatAsState(
        targetValue = if (animationStartCategoryFat.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategoryFat.value = false
    }

    val animationStartCategoryWater = remember { mutableStateOf(false) }
    val progressCategoryWater by animateFloatAsState(
        targetValue = if (animationStartCategoryWater.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategoryWater.value = false
    }

    val animationStartCategoryDamage = remember { mutableStateOf(false) }
    val progressCategoryDamaged by animateFloatAsState(
        targetValue = if (animationStartCategoryDamage.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategoryDamage.value = false
    }

    val animationStartCategoryClear = remember { mutableStateOf(false) }
    val progressCategoryClear by animateFloatAsState(
        targetValue = if (animationStartCategoryClear.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategoryClear.value = false
    }

    // TODO: use string resource
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
                text = "Gordura",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategoryFat.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategoryFat,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategoryFat }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Água",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategoryWater.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategoryWater,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategoryWater }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Riscos",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategoryDamage.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategoryDamaged,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategoryDamaged }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Nítido",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategoryClear.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategoryClear,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategoryClear }
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

    val animationStartCategoryGray = remember { mutableStateOf(false) }
    val progressCategoryGray by animateFloatAsState(
        targetValue = if (animationStartCategoryGray.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategoryGray.value = false
    }

    val animationStartCategoryBrown = remember { mutableStateOf(false) }
    val progressCategoryBrown by animateFloatAsState(
        targetValue = if (animationStartCategoryBrown.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategoryBrown.value = false
    }

    val animationStartCategoryGreen = remember { mutableStateOf(false) }
    val progressCategoryGreen by animateFloatAsState(
        targetValue = if (animationStartCategoryGreen.value) {
            animationProgressEnd
        } else {
            animationProgressStart
        },
        animationSpec = tween(animationDuration)
    ) {
        animationStartCategoryGreen.value = false
    }

    // TODO: use string resource
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
                text = "Transition Cinza",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategoryGray.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategoryGray,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategoryGray }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Transition Marrom",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategoryBrown.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategoryBrown,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategoryBrown }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "Transition Verde",
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold),
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { animationStartCategoryGreen.value = true }) {
                Icon(imageVector = Icons.Filled.PlayCircle, contentDescription = "")
            }
        }

        LottieAnimation(
            modifier = modifier
                .height(animationHeight)
                .width(animationWidth),
            composition = compositionCategoryGreen,
            contentScale = ContentScale.FillWidth,
            progress = { progressCategoryGreen }
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