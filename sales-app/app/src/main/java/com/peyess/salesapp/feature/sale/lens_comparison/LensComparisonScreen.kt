package com.peyess.salesapp.feature.sale.lens_comparison

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.feature.sale.lens_comparison.model.ColoringComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.TreatmentComparison
import com.peyess.salesapp.feature.sale.lens_comparison.state.LensComparisonState
import com.peyess.salesapp.feature.sale.lens_comparison.state.LensComparisonViewModel
import com.peyess.salesapp.ui.component.modifier.MinimumHeightState
import com.peyess.salesapp.ui.component.modifier.MinimumWidthState
import com.peyess.salesapp.ui.component.modifier.minimumHeightModifier
import com.peyess.salesapp.ui.component.modifier.minimumWidthModifier
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import kotlin.math.abs

val buttonHeight = 120.dp

@Composable
fun LensComparisonScreen(
    modifier: Modifier = Modifier,
    onAddComparison: () -> Unit = {},
    onLensPicked: () -> Unit = {},
) {
    val viewModel: LensComparisonViewModel = mavericksViewModel()

    val comparisons by viewModel.comparisons().collectAsState(emptyList())

    val hasPickedProduct by viewModel.collectAsState(LensComparisonState::hasPickedProduct)

    val hasNavigated = remember { mutableStateOf(false) }
    val canNavigate = remember { mutableStateOf(false) }
    if (canNavigate.value && hasPickedProduct) {
        LaunchedEffect(Unit) {
            if (!hasNavigated.value) {
                hasNavigated.value = true
                canNavigate.value = false

                viewModel.lensPicked()
                onLensPicked()
            }
        }
    }

    LensComparisonScreenImpl(
        modifier = modifier,

        comparisons = comparisons,

        onAddComparison = onAddComparison,
        onRemoveComparison = viewModel::removeComparison,
        onSelectComparison = {
            canNavigate.value = true
            viewModel.onPickProduct(it)
        },

        techsFor = viewModel::techsForComparison,
        materialsFor = viewModel::materialForComparison,
        treatmentsFor = viewModel::treatmentsFor,
        coloringsFor = viewModel::coloringsFor,

        onPickTech = viewModel::onPickTech,
        onPickMaterial = viewModel::onPickMaterial,
        onPickTreatment = viewModel::onPickTreatment,
        onPickColoring = viewModel::onPickColoring,
    )
}

@Composable
private fun LensComparisonScreenImpl(
    modifier: Modifier = Modifier,

    comparisons: List<IndividualComparison> = emptyList(),

    techsFor: (comparison: IndividualComparison) -> Flow<List<FilterLensTechEntity>> = { emptyFlow() },
    materialsFor: (comparison: IndividualComparison) -> Flow<List<FilterLensMaterialEntity>> = { emptyFlow() },
    treatmentsFor: (comparison: IndividualComparison) -> Flow<List<LocalTreatmentEntity>> = { emptyFlow() },
    coloringsFor: (comparison: IndividualComparison) -> Flow<List<LocalColoringEntity>> = { emptyFlow() },

    onPickTech: (techId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },
    onPickMaterial: (materialId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },
    onPickTreatment: (treatmentId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },
    onPickColoring: (coloringId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },

    onRemoveComparison: (id: Int) -> Unit = {},
    onAddComparison: () -> Unit = {},
    onSelectComparison: (comparison: IndividualComparison) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(comparisons.size) {
            LensComparisonCard(
                individualComparison = comparisons[it],
                onRemoveComparison = onRemoveComparison,
                onSelectComparison = { onSelectComparison(comparisons[it]) },

                techsFor = techsFor,
                materialsFor = materialsFor,
                treatmentsFor = treatmentsFor,
                coloringsFor = coloringsFor,

                onPickTech = onPickTech,
                onPickMaterial = onPickMaterial,
                onPickColoring = onPickColoring,
                onPickTreatment = onPickTreatment,
            )
        }

        item {
            AddNewComparisonButton(
                onClick = onAddComparison,
            )
        }
    }
}

@Composable
private fun AddNewComparisonButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight),
        shape = MaterialTheme.shapes.large,
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .height(buttonHeight)
                    .width(buttonHeight)
                    .padding(SalesAppTheme.dimensions.grid_2)
                    .background(color = MaterialTheme.colors.primary.copy(alpha = 0.5f)),
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .height(buttonHeight)
                        .width(buttonHeight)
                        .padding(8.dp),
                    imageVector = Icons.Filled.Add,
                    tint = MaterialTheme.colors.onPrimary,
                    contentDescription = "",
                )
            }

            Text(
                text = stringResource(id = R.string.lens_comparison_add_another),
                style = MaterialTheme.typography.body1
                    .copy(textAlign = TextAlign.Center),
            )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LensComparisonCard(
    modifier: Modifier = Modifier,
    individualComparison: IndividualComparison, // TODO: Create an empty instance

    techsFor: (comparison: IndividualComparison) -> Flow<List<FilterLensTechEntity>> = { emptyFlow() },
    materialsFor: (comparison: IndividualComparison) -> Flow<List<FilterLensMaterialEntity>> = { emptyFlow() },
    treatmentsFor: (comparison: IndividualComparison) -> Flow<List<LocalTreatmentEntity>> = { emptyFlow() },
    coloringsFor: (comparison: IndividualComparison) -> Flow<List<LocalColoringEntity>> = { emptyFlow() },

    onPickTech: (techId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },
    onPickMaterial: (materialId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },
    onPickTreatment: (treatmentId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },
    onPickColoring: (coloringId: String, comparison: IndividualComparison) -> Unit = { _, _, -> },

    onRemoveComparison: (id: Int) -> Unit = {},
    onSelectComparison: () -> Unit = {},
) {
    val lensComparison = individualComparison.lensComparison
    val coloringComparison = individualComparison.coloringComparison
    val treatmentComparison = individualComparison.treatmentComparison

    val techs = techsFor(individualComparison).collectAsState(emptyList())
    val materials = materialsFor(individualComparison).collectAsState(emptyList())
    val treatments = treatmentsFor(individualComparison).collectAsState(emptyList())
    val colorings = coloringsFor(individualComparison).collectAsState(emptyList())

    val techDialogState = rememberMaterialDialogState()
    PickTechDialog(
        dialogState = techDialogState,
        techs = techs.value,
        comparison = individualComparison,
        onPickTech = onPickTech,
    )

    val materialDialogState = rememberMaterialDialogState()
    PickMaterialDialog(
        dialogState = materialDialogState,
        materials = materials.value,
        comparison = individualComparison,
        onPickMaterial = onPickMaterial,
    )

    val treatmentDialogState = rememberMaterialDialogState()
    PickTreatmentDialog(
        dialogState = treatmentDialogState,
        treatments = treatments.value,
        comparison = individualComparison,
        onPickTreatment = onPickTreatment,
    )

    val coloringDialogState = rememberMaterialDialogState()
    PickColoringDialog(
        dialogState = coloringDialogState,
        colorings = colorings.value,
        comparison = individualComparison,
        onPickColoring = onPickColoring,
    )

    Column(
        modifier = modifier.shadow(elevation = 1.dp),
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

            IconButton(onClick = { onRemoveComparison(individualComparison.id) }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = ""
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.weight(2f),
                text = "${lensComparison.originalLens.supplier} " +
                        "${lensComparison.originalLens.brand} " +
                        "${lensComparison.originalLens.design}",
                style = MaterialTheme.typography.caption
                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            )

//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                modifier = Modifier.weight(2f),
//                text = lensComparison.originalLens.supplier,
//                style = MaterialTheme.typography.caption
//                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                modifier = Modifier.weight(2f),
//                text = lensComparison.originalLens.brand,
//                style = MaterialTheme.typography.caption
//                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//
//            Text(
//                modifier = Modifier.weight(2f),
//                text = lensComparison.originalLens.design,
//                style = MaterialTheme.typography.caption
//                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
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
            val minimumHeightState = remember { MinimumHeightState() }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                FeatureSelection(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .minimumHeightModifier(
                            minimumHeightState,
                            density,
                        )
                        .minimumWidthModifier(minimumWidthState, density),
                    name = lensComparison.pickedLens.tech,
                    dialogState = techDialogState,
                )

                FeatureSelection(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .minimumHeightModifier(
                            minimumHeightState,
                            density,
                        )
                        .minimumWidthModifier(
                            minimumWidthState,
                            density
                        ),
                    name =  lensComparison.pickedLens.material,
                    dialogState = materialDialogState,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                // TODO: remove predicate when migration is run
                val treatmentName = "${treatmentComparison.pickedTreatment.brand} " +
                        treatmentComparison.pickedTreatment.design

                val coloringName = "${coloringComparison.pickedColoring.brand} " +
                        coloringComparison.pickedColoring.design

                Timber.i("Got name $treatmentName from ${treatmentComparison.pickedTreatment}")

                FeatureSelection(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .minimumHeightModifier(
                            minimumHeightState,
                            density,
                        )
                        .minimumWidthModifier(
                            minimumWidthState,
                            density
                        ),
                    name = treatmentName,
                    dialogState = treatmentDialogState,
                )

                Timber.i("The picked coloring is ${coloringComparison.pickedColoring}")
                FeatureSelection(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .minimumHeightModifier(
                            minimumHeightState,
                            density,
                        )
                        .minimumWidthModifier(
                            minimumWidthState,
                            density
                        ),
                    name = coloringName,
                    dialogState = coloringDialogState,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(color = MaterialTheme.colors.primary.copy(alpha = 0.3f))
                    .clickable { onSelectComparison() },
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
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    onClick: () -> Unit = {},
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
        onClick = { dialogState.show() },
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.button
                .copy(textAlign = TextAlign.Center),
        )
    }
}

@Preview
@Composable
private fun AddNewComparisonButtonPreview() {
    SalesAppTheme {
        AddNewComparisonButton()
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
    priceTag += "R$ %.2f".format(abs(priceDiff / installments))

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

@Composable
private fun PickTechDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    techs: List<FilterLensTechEntity> = emptyList(),
    comparison: IndividualComparison,
    onPickTech: (techId: String, comparison: IndividualComparison) -> Unit = { _, _ -> },
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
        },
    ) {
        val options = techs.map { it.name }

        title(res = R.string.dialog_select_tech)

        listItems(
            list = options
        ) { index, _ ->
            onPickTech(techs[index].id, comparison)

            dialogState.hide()
        }
    }
}

@Composable
private fun PickMaterialDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    materials: List<FilterLensMaterialEntity> = emptyList(),
    comparison: IndividualComparison,
    onPickMaterial: (materialId: String, comparison: IndividualComparison) -> Unit = { _, _ -> },
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
        },
    ) {
        val options = materials.map { it.name }

        title(res = R.string.dialog_select_material)

        listItems(
            list = options
        ) { index, _ ->
            onPickMaterial(materials[index].id, comparison)

            dialogState.hide()
        }
    }
}

@Composable
private fun PickTreatmentDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    treatments: List<LocalTreatmentEntity> = emptyList(),
    comparison: IndividualComparison,
    onPickTreatment: (treatmentId: String, comparison: IndividualComparison) -> Unit = { _, _ -> },
) {

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
        },
    ) {
        val options = treatments.map {
            Timber.i("Displaying treatment option: $it")

            "${it.brand} ${it.design}"
        }

        title(res = R.string.dialog_select_treatment)

        listItems(
            list = options
        ) { index, _ ->
            onPickTreatment(treatments[index].id, comparison)

            dialogState.hide()
        }
    }
}

@Composable
private fun PickColoringDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    colorings: List<LocalColoringEntity> = emptyList(),
    comparison: IndividualComparison,
    onPickColoring: (coloringId: String, comparison: IndividualComparison) -> Unit = { _, _ -> },
) {
    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
        },
    ) {
        val options = colorings.map {
            "${it.brand} ${it.design}"
        }

        title(res = R.string.dialog_select_coloring)

        listItems(
            list = options
        ) { index, _ ->
            onPickColoring(colorings[index].id, comparison)

            dialogState.hide()
        }
    }
}
