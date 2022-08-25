package com.peyess.salesapp.feature.sale.lens_comparison

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.feature.sale.lens_comparison.model.ColoringComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.TreatmentComparison
import com.peyess.salesapp.feature.sale.lens_comparison.state.LensComparisonViewModel
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme

val buttonHeight = 120.dp

@Composable
fun LensComparisonScreen(
    modifier: Modifier = Modifier,
    onLensPicked: () -> Unit = {},
) {
    val viewModel: LensComparisonViewModel = mavericksViewModel()

    val comparisons by viewModel.comparisons().collectAsState(emptyList())

    LensComparisonScreenImpl(
        modifier = modifier,

        comparisons = comparisons,
        onSelectComparison = {}
    )

}

@Composable
private fun LensComparisonScreenImpl(
    modifier: Modifier = Modifier,

    comparisons: List<IndividualComparison> = emptyList(),
    onSelectComparison: () -> Unit = {},
) {
    LazyColumn(modifier = modifier) {
        items(comparisons.size) {
            LensComparisonCard(
                individualComparison = comparisons[it]
            )
        }

        item {
            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = SalesAppTheme.dimensions.grid_1_5)
                    .fillMaxWidth()
                    .height(buttonHeight),
                shape = MaterialTheme.shapes.large,
                onClick = {},
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LensComparisonCard(
    modifier: Modifier = Modifier,
    individualComparison: IndividualComparison, // TODO: Create an empty instace
) {
    val lensComparison = individualComparison.lensComparison
    val coloringComparison = individualComparison.coloringComparison
    val treatmentComparison = individualComparison.treatmentComparison

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = ""
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.weight(2f),
                text = lensComparison.originalLens.supplier,
                style = MaterialTheme.typography.caption
                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier.weight(2f),
                text = lensComparison.originalLens.brand,
                style = MaterialTheme.typography.caption
                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.weight(1f))


            Text(
                modifier = Modifier.weight(2f),
                text = lensComparison.originalLens.design,
                style = MaterialTheme.typography.caption
                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(
            visible = individualComparison.finalPriceDifference != 0.0,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                PriceDifference(
                    isPriceBad = individualComparison.isPriceBad,
                    priceDiff = individualComparison.finalPriceDifference,
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val density = LocalDensity.current
            val minimumWidthState = remember { MinimumWidthState() }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                FeatureSelection(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density
                    ),
                    name = lensComparison.pickedLens.tech,
                )

                FeatureSelection(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density
                    ),
                    name =  lensComparison.pickedLens.material,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                FeatureSelection(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density
                    ),
                    name = treatmentComparison.pickedTreatment.brand,
                )

                FeatureSelection(
                    modifier = Modifier.minimumWidthModifier(
                        minimumWidthState,
                        density
                    ),
                    name = coloringComparison.pickedColoring.brand,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(color = MaterialTheme.colors.primary.copy(alpha = 0.3f)),
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    // TODO: localize price symbol
                    text = "R$ %.2f".format(individualComparison.finalPrice),
                )

                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
private fun FeatureSelection(
    modifier: Modifier = Modifier,
    name: String = "",
    enabled: Boolean = true,
    onSelect: () -> Unit = {}
) {
    OutlinedButton(
        modifier = modifier
            .height(SalesAppTheme.dimensions.minimum_touch_target),
        border = BorderStroke(width = 4.dp, color = MaterialTheme.colors.primary),
//        colors = ButtonDefaults
//            .buttonColors(
//                backgroundColor = MaterialTheme.colors.primary,
//                disabledBackgroundColor = Color.Gray.copy(alpha = 0.5f),
//            ),
        enabled = enabled,
        onClick = onSelect,
    ) {
        Text(text = name)
    }
}

@Preview
@Composable
private fun PriceDifference(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.h6,
    isPriceBad: Boolean = false,
    priceDiff: Double = 2500.0,
    installments: Double = 10.0,
) {
    val color = if (isPriceBad) {
        // TODO: add color to app theme
        Color(0xFFF44336)
    } else {
        Color(0xFF62AA1A)
    }

    var priceTag = if (isPriceBad) {
        "- "
    } else {
        ""
    }
    priceTag += "R$ %.2f".format(priceDiff / installments)

    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 28.dp,
                    vertical = 12.dp,
                )
                .align(Alignment.Center),
            // TODO: localize price symbol
            text = priceTag,
            style = MaterialTheme.typography.body1,
            color = color,
        )

        Text(
            modifier = Modifier.align(Alignment.BottomEnd),
            text = "$installments X",
            style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
            color = color,
        )
    }
}

@Preview
@Composable
private fun FeatureSelectionPreview() {
    SalesAppTheme {
        FeatureSelection(name = "Tratamentos")
    }
}

@Preview
@Composable
private fun LensComparisonCardPreview() {
    SalesAppTheme {
        LensComparisonCard(
            modifier = Modifier.fillMaxWidth(),
            IndividualComparison(
                lensComparison = LensComparison(
                    originalLens = LocalLensEntity(
                        supplier = "Zeiss",
                        brand = "Acabadas",
                        design = "DuraVIsion BlueProtect",
                        material = "1.60",
                        tech = "SmartLife",
                        price = 1500.0,
                    ),
                    pickedLens = LocalLensEntity(
                        supplier = "Zeiss",
                        brand = "Acabadas",
                        design = "DuraVIsion BlueProtect",
                        material = "1.60",
                        tech = "SmartLife",
                        price = 1700.8,
                    )
                ),

                treatmentComparison = TreatmentComparison(
                    originalTreatment = LocalTreatmentEntity(brand = "Incolor", price = 0.0),
                    pickedTreatment = LocalTreatmentEntity(brand = "Marrom 85%", price = 100.0),
                ),

                coloringComparison = ColoringComparison(
                    originalColoring = LocalColoringEntity(brand = "Incolor", price = 0.0),
                    pickedColoring = LocalColoringEntity(brand = "Antirreflexo", price = 10.0)
                )
            )
        )
    }
}
