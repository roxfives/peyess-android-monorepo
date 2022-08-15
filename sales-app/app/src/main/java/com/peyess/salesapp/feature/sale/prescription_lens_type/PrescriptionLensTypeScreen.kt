package com.peyess.salesapp.feature.sale.prescription_lens_type

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.sale.prescription_lens_type.state.PrescriptionLensTypeState
import com.peyess.salesapp.feature.sale.prescription_lens_type.state.PrescriptionLensTypeViewModel
import com.peyess.salesapp.feature.sale.welcome.state.WelcomeState
import com.peyess.salesapp.model.products.LensTypeCategory
import com.peyess.salesapp.ui.component.chip.PeyessChipGroup
import com.peyess.salesapp.ui.component.footer.PeyessNextStep
import com.peyess.salesapp.ui.component.mike.MikeBubbleRight

private val mikeHeight = 300.dp

@Composable
fun PrescriptionLensTypeScreen(
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
) {
    val viewModel: PrescriptionLensTypeViewModel = mavericksViewModel()

    val categories by viewModel.collectAsState(PrescriptionLensTypeState::lensCategories)
    val mikeText by viewModel.collectAsState(PrescriptionLensTypeState::mikeText)

    val categoryPicked by viewModel.collectAsState(PrescriptionLensTypeState::typeCategoryPicked)

    val canGoNext by viewModel.collectAsState(PrescriptionLensTypeState::canGoNext)

//    val hasUpdatedSale by viewModel.collectAsState(PrescriptionLensTypeState::hasUpdatedSale)
//    val hasGoneNext = remember {
//        mutableStateOf(false)
//    }
//    if (hasUpdatedSale) {
//        LaunchedEffect(Unit) {
//            if (!hasGoneNext.value) {
//                viewModel.onNext()
//                onNext()
//
//                hasGoneNext.value = true
//            }
//        }
//    }

    PrescriptionTypeScreenImpl(
        modifier = modifier,
        mikeText = mikeText,

        categoryPicked = categoryPicked,
        lensCategories = categories,
        onSelectChanged = viewModel::onPick,
        onDone = {
            if (canGoNext) {
                onNext()
            }
        },
    )
}


@Composable
private fun PrescriptionTypeScreenImpl(
    modifier: Modifier = Modifier,

    categoryPicked: LensTypeCategory? = null,
    lensCategories: Async<List<LensTypeCategory>> = Uninitialized,
    onSelectChanged: (categoryName: String) -> Unit =  {},

    mikeText: Async<String> = Uninitialized,
    onDone: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (lensCategories) {
            is Success ->
                LensTypeCategories(
                    selected = categoryPicked,
                    categories = lensCategories.invoke(),
                    onSelectChanged = onSelectChanged,
                )
            else ->
                CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.height(26.dp))

        Divider(
            modifier = Modifier.padding(horizontal = 64.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.height(26.dp))

        if (mikeText is Success) {
            MikeBubbleRight(
                modifier = Modifier.height(mikeHeight),
                text = mikeText.invoke(),
            )
        } else {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.weight(1f))

        PeyessNextStep(onNext = onDone)
    }
}

@Composable
private fun LensTypeCategories(
    modifier: Modifier = Modifier,
    categories: List<LensTypeCategory> = emptyList(),
    selected: LensTypeCategory? = null,
    onSelectChanged: (categoryName: String) -> Unit =  {},
) {
    PeyessChipGroup(
        modifier = modifier
            .padding(26.dp),
        keepSameWidth = true,
        items = categories,
        itemName = { it.name },
        selected = selected,
        onSelectedChanged = onSelectChanged,
    )
}