package com.peyess.salesapp.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksActivityViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.app.state.MainAppState
import com.peyess.salesapp.app.state.MainViewModel
import com.peyess.salesapp.feature.sale.anamnesis.fifth_step_sports.state.FifthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.first_step_first_time.state.FirstTimeViewModel
import com.peyess.salesapp.feature.sale.anamnesis.fourth_step_pain.state.FourthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.second_step_glass_usage.state.SecondStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.sixth_step_time.state.SixthStepViewModel
import com.peyess.salesapp.feature.sale.anamnesis.third_step_sun_light.state.ThirdStepViewModel
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme

private val buttonHeight = 72.dp

@Composable
fun SalesScreen(
    modifier: Modifier = Modifier,
    onStartNewSale: () -> Unit = {},
) {
    val firstStepViewModel: FirstTimeViewModel = mavericksActivityViewModel()
    val secondStepViewModel: SecondStepViewModel = mavericksActivityViewModel()
    val thirdStepViewModel: ThirdStepViewModel = mavericksActivityViewModel()
    val fourthStepViewModel: FourthStepViewModel = mavericksActivityViewModel()
    val fifthStepViewModel: FifthStepViewModel = mavericksActivityViewModel()
    val sixthStepViewModel: SixthStepViewModel = mavericksActivityViewModel()


    val viewModel: MainViewModel = mavericksActivityViewModel()

    val isCreatingNewSale by viewModel.collectAsState(MainAppState::isCreatingNewSale)
    val createNewSale by viewModel.collectAsState(MainAppState::createNewSale)

    val isUpdatingProducts by viewModel.collectAsState(MainAppState::isUpdatingProducts)

    val hasStartedNewSale = remember {
        mutableStateOf(false)
    }

    if (createNewSale is Success && createNewSale.invoke()!!) {
        LaunchedEffect(Unit) {
            if (!hasStartedNewSale.value) {
                onStartNewSale()
            }

            hasStartedNewSale.value = true
            viewModel.newSaleStarted()

            firstStepViewModel.resetState()
            secondStepViewModel.resetState()
            thirdStepViewModel.resetState()
            fourthStepViewModel.resetState()
            fifthStepViewModel.resetState()
            sixthStepViewModel.resetState()
        }
    }

    if (isCreatingNewSale) {
        PeyessProgressIndicatorInfinite(modifier = modifier)
    } else {
        SaleList(
            modifier = modifier,
            isUpdatingProducts = isUpdatingProducts,
            onStartNewSale = {
                viewModel.startNewSale()
            },
        )
    }
}

@Composable
fun SaleList(
    modifier: Modifier = Modifier,
    isUpdatingProducts: Boolean = true,
    onStartNewSale: () -> Unit = {},
) {
    LazyColumn(modifier = modifier.fillMaxSize()) {
        item {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = SalesAppTheme.dimensions.grid_1_5)
                    .fillMaxWidth()
                    .height(buttonHeight),
                shape = MaterialTheme.shapes.large,
                enabled = !isUpdatingProducts,
                onClick = onStartNewSale,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = SalesAppTheme.dimensions.grid_3)
                            .background(color = MaterialTheme.colors.primary.copy(alpha = 0.5f)),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            tint = MaterialTheme.colors.onPrimary,
                            contentDescription = "",
                        )
                    }

                    Text(
                        text = stringResource(id = R.string.btn_make_new_sale),
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    SalesScreen()
}