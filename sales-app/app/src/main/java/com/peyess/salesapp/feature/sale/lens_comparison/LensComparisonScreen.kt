package com.peyess.salesapp.feature.sale.lens_comparison

import android.icu.text.NumberFormat
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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
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
import com.peyess.salesapp.navigation.sale.lens_pick.isEditingParam
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

private val noFilterColor = Color.hsv(353f, 0.99f, 0.48f)
private val withFilterColor = Color.hsv(79f, 1f, 0.77f)

private val dialogSpacerHeight = 32.dp
private val infoTextPadding = 32.dp

val buttonHeight = 120.dp

private val thicknessAnimationHeight = 128.dp

@Composable
fun LensComparisonScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onAddComparison: () -> Unit = {},
    onLensPicked: (isEditing: Boolean) -> Unit = {},
) {
    val isEditingParameter = navHostController
        .currentBackStackEntry
        ?.arguments
        ?.getBoolean(isEditingParam)
        ?: false

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
                onLensPicked(isEditingParameter)
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

    val animationDialogState = rememberMaterialDialogState()
    AnimationDialog(
        dialogState = animationDialogState,
        individualComparison = individualComparison,
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

            Spacer(modifier = Modifier.weight(1f))

            if (lensComparison.pickedLens.hasFilterBlue) {
                Icon(
                    imageVector = if (lensComparison.pickedLens.hasFilterUv) {
                        Icons.Filled.WbTwilight
                    } else {
                        Icons.Filled.WbSunny
                    },
                    tint = if (lensComparison.pickedLens.hasFilterUv) {
                        withFilterColor
                    } else {
                        noFilterColor
                    },
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.width(12.dp))
            }

            if (lensComparison.pickedLens.hasFilterUv) {
                Icon(
                    imageVector = if (lensComparison.pickedLens.hasFilterBlue) {
                        Icons.Filled.FlashlightOn
                    } else {
                        Icons.Filled.FlashlightOff
                    },
                    tint = if (lensComparison.pickedLens.hasFilterBlue) {
                        withFilterColor
                    } else {
                        noFilterColor
                    },
                    contentDescription = "",
                )

                Spacer(modifier = Modifier.width(12.dp))
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
                style = MaterialTheme.typography.body1
                    .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(
            modifier = Modifier.padding(horizontal = 12.dp),
            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(individualComparison.animationId)
            )

            LottieAnimation(
                modifier = Modifier
                    .height(thicknessAnimationHeight)
                    .weight(1f)
//                    .width(SalesAppTheme.dimensions.minimum_touch_target)
                    .clickable { animationDialogState.show() },
                composition = composition,
                iterations = 1,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            AnimatedVisibility(
                modifier = Modifier.weight(1f),
                visible = individualComparison.finalPriceDifference != 0.0,
                enter = scaleIn(),
                exit = scaleOut(),
            ) {
                Column {
                    PriceDifference(
                        isPriceBad = individualComparison.isPriceBad,
                        priceDiff = individualComparison.finalPriceDifference,
                    )
                }
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
                    content = lensComparison.pickedLens.tech,
                    // TODO: use string resource
                    subtitle = "Tecnologia",
                    dialogState = techDialogState,
                )

                FeatureSelection(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .minimumHeightModifier(
                            minimumHeightState,
                            density,
                        )
                        .minimumWidthModifier(minimumWidthState, density),
                    content =  lensComparison.pickedLens.material,
                    // TODO: use string resource
                    subtitle = "Material",
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
                        .minimumWidthModifier(minimumWidthState, density),
                    content = treatmentName,
                    // TODO: use string resource
                    subtitle = "Tratamento",
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
                        .minimumWidthModifier(minimumWidthState, density),
                    content = coloringName,
                    // TODO: use string resource
                    subtitle = "Coloração",
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
                PriceTag(
                    modifier = Modifier.align(Alignment.Center),
                    pricePerMonth = NumberFormat.getCurrencyInstance()
                        .format(individualComparison.finalPrice / 10f)
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
    content: String = "",
    subtitle: String = "",
    enabled: Boolean = true,
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
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
                text = content,
                style = MaterialTheme.typography.button
                    .copy(textAlign = TextAlign.Center),
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = subtitle, style = MaterialTheme.typography.caption)
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
    priceTag += NumberFormat.getCurrencyInstance()
        .format(abs(priceDiff / installments))

    PriceTag(
        modifier = modifier,
        style = style,
        color = color,
        pricePerMonth = priceTag,
        installments = installments,
    )
}

@Preview
@Composable
private fun PriceTag(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.h6,
    color: Color = MaterialTheme.colors.primary,
    pricePerMonth: String = "",
    installments: Double = 10.0,
) {
    Box(modifier = modifier) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 28.dp,
                    vertical = 12.dp,
                )
                .align(Alignment.Center),
            // TODO: localize price symbol
            text = pricePerMonth,
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
        FeatureSelection(content = "X-ray vision", subtitle = "Tratamento")
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

@Composable
private fun AnimationDialog(
    modifier: Modifier = Modifier,
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    individualComparison: IndividualComparison,
) {
    val infoFrontDialogState = rememberMaterialDialogState()
    InfoDialog(
        dialogState = infoFrontDialogState,

        title = stringResource(id = R.string.dialog_front_info_title),
        infoContent = stringResource(id = R.string.dialog_front_info_content),
    )

    val infoSidewaysDialogState = rememberMaterialDialogState()
    InfoDialog(
        dialogState = infoSidewaysDialogState,

        title = stringResource(id = R.string.dialog_sideways_info_title),
        infoContent = stringResource(id = R.string.dialog_sideways_info_content),
    )

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            // TODO: Use string resource
            positiveButton("Ok")
        },
    ) {
        val compositionSideways by rememberLottieComposition(
            LottieCompositionSpec.RawRes(individualComparison.bigAnimationId)
        )

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lottie_comparison_thickness)
        )

        Column(
            modifier = modifier
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    // TODO: use string resource
                    text = "Pontos de espessura da lente",
                    style = MaterialTheme.typography.h6
                        .copy(fontWeight = FontWeight.Bold),
                )

                Divider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                )

                IconButton(onClick = { infoFrontDialogState.show() }) {
                    Icon(imageVector = Icons.Filled.Info, contentDescription = "")
                }
            }

            LottieAnimation(
                modifier = Modifier.weight(1f),
                composition = composition,
                iterations = 1,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                )

                IconButton(onClick = { infoSidewaysDialogState.show() }) {
                    Icon(imageVector = Icons.Filled.Info, contentDescription = "")
                }
            }

            LottieAnimation(
                modifier = Modifier.weight(1f),
                composition = compositionSideways,
                iterations = 1,
                clipSpec = LottieClipSpec.Progress(0f, 1f),
            )
        }
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
