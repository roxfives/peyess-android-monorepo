package com.peyess.salesapp.screen.sale.prescription_lens_type

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.screen.sale.prescription_lens_type.state.PrescriptionLensTypeState
import com.peyess.salesapp.screen.sale.prescription_lens_type.state.PrescriptionLensTypeViewModel
import com.peyess.salesapp.data.model.lens.categories.LensTypeCategoryDocument
import com.peyess.salesapp.screen.sale.prescription_lens_type.model.LensTypeCategory
import com.peyess.salesapp.ui.component.chip.PeyessChipGroup
import com.peyess.salesapp.ui.component.footer.PeyessStepperFooter
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

    val areLensTypeCategoriesLoading by
        viewModel.collectAsState(PrescriptionLensTypeState::areCategoriesLoading)
    val isMikeLoading by viewModel.collectAsState(PrescriptionLensTypeState::isMikeLoading)

    val canGoNext by viewModel.collectAsState(PrescriptionLensTypeState::canGoNext)

    PrescriptionTypeScreenImpl(
        modifier = modifier,

        isMikeLoading = isMikeLoading,
        mikeText = mikeText,

        areCategoriesLoading = areLensTypeCategoriesLoading,
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

    areCategoriesLoading: Boolean = false,
    categoryPicked: LensTypeCategory = LensTypeCategory(),
    lensCategories: List<LensTypeCategory> = emptyList(),
    onSelectChanged: (categoryName: String) -> Unit =  {},

    isMikeLoading: Boolean = false,
    mikeText: String = "",
    onDone: () -> Unit = {},
) {

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (areCategoriesLoading) {
            CircularProgressIndicator()
        } else {
            LensTypeCategories(
                selected = categoryPicked,
                categories = lensCategories,
                onSelectChanged = onSelectChanged,
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        Divider(
            modifier = Modifier.padding(horizontal = 64.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )

        Spacer(modifier = Modifier.height(26.dp))

        if (isMikeLoading) {
            CircularProgressIndicator()
        } else {
            MikeBubbleRight(
                modifier = Modifier.height(mikeHeight),
                text = mikeText,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        PeyessStepperFooter(onNext = onDone)
    }
}

@Composable
private fun LensTypeCategories(
    modifier: Modifier = Modifier,
    categories: List<LensTypeCategory> = emptyList(),
    selected: LensTypeCategory = LensTypeCategory(),
    onSelectChanged: (categoryName: String) -> Unit =  {},
) {
    PeyessChipGroup(
        modifier = modifier.padding(26.dp),
        keepSameWidth = true,
        items = categories,
        itemName = { it?.name ?: "" },
        selected = selected,
        onSelectedChanged = onSelectChanged,
    )
}