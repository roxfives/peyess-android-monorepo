package com.peyess.salesapp.feature.sale.lens_pick

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.dao.products.firestore.lens_description.LensDescription
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.feature.sale.lens_pick.model.name
import com.peyess.salesapp.feature.sale.lens_pick.state.LensPickState
import com.peyess.salesapp.feature.sale.lens_pick.state.LensPickViewModel
import com.peyess.salesapp.model.products.LensGroup
import com.peyess.salesapp.ui.component.card.ExpandableCard
import com.peyess.salesapp.ui.component.modifier.MinimumHeightState
import com.peyess.salesapp.ui.component.modifier.minimumHeightModifier
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.peyess.salesapp.ui.theme.Shapes
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.listItems
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import com.vanpra.composematerialdialogs.title
import timber.log.Timber

@Composable
fun LensSuggestionScreen(
    modifier: Modifier = Modifier,
    onLensPicked: () -> Unit = {},
) {
    val viewModel: LensPickViewModel = mavericksViewModel()

    val lazyLenses = viewModel.filteredLenses().collectAsLazyPagingItems()

    val isFamilyLensFilterEnabled by
        viewModel.collectAsState(LensPickState::isFamilyLensFilterEnabled)
    val isDescriptionLensFilterEnabled by
        viewModel.collectAsState(LensPickState::isDescriptionLensFilterEnabled)
    val isMaterialLensFilterEnabled by
        viewModel.collectAsState(LensPickState::isMaterialLensFilterEnabled)

    val lensGroups by viewModel.collectAsState(LensPickState::groupsFilter)
    val lensGroupsFilter by viewModel.collectAsState(LensPickState::groupLensFilter)

    val lensTypes by viewModel.collectAsState(LensPickState::typesFilter)
    val lensTypesFilter by viewModel.collectAsState(LensPickState::typeLensFilter)

    val lensSupplier by viewModel.collectAsState(LensPickState::supplierFilter)
    val lensSupplierFilter by viewModel.collectAsState(LensPickState::supplierLensFilter)

    val lensMaterial by viewModel.collectAsState(LensPickState::materialFilter)
    val lensMaterialFilter by viewModel.collectAsState(LensPickState::materialLensFilter)

    val lensFamily by viewModel.collectAsState(LensPickState::familyFilter)
    val lensFamilyFilter by viewModel.collectAsState(LensPickState::familyLensFilter)

    val lensDescription by viewModel.collectAsState(LensPickState::descriptionFilter)
    val lensDescriptionFilter by viewModel.collectAsState(LensPickState::descriptionLensFilter)

    val lensSuggestions by viewModel.suggestions().collectAsState(listOf())

    val isAddingSuggestion by viewModel.collectAsState(LensPickState::isAddingToSuggestion)
    val hasAddedSuggestion by viewModel.collectAsState(LensPickState::hasAddedToSuggestion)

    val hasNavigated = remember { mutableStateOf(false) }
    val canNavigate = remember { mutableStateOf(false) }
    if (canNavigate.value && hasAddedSuggestion) {
        LaunchedEffect(Unit) {
            Timber.i("Trying to navigate: hasNavigated: ${hasNavigated.value} " +
                    "canNavigate: ${canNavigate.value} " +
                    "hasAddedSuggestion: $hasAddedSuggestion ")
            if (!hasNavigated.value) {
                Timber.i("Navigating")

                hasNavigated.value = true
                canNavigate.value = false

                viewModel.lensPicked()
                onLensPicked()
            }
        }
    }

    LensSuggestionScreenImpl(
        modifier = modifier,

        lensSuggestion = lensSuggestions,
        lensesSearch = lazyLenses,

        isFamilyLensFilterEnabled = isFamilyLensFilterEnabled,
        isDescriptionLensFilterEnabled = isDescriptionLensFilterEnabled,
        isMaterialLensFilterEnabled = isMaterialLensFilterEnabled,

        selectedLensGroup = lensGroupsFilter,
        lensGroups = lensGroups,
        onPickGroup = viewModel::onPickGroup,

        selectedLensType = lensTypesFilter,
        lensTypes = lensTypes,
        onPickType = viewModel::onPickType,

        selectedLensSupplier = lensSupplierFilter,
        lensSuppliers = lensSupplier,
        onPickSupplier = viewModel::onPickSupplier,

        selectedLensMaterial = lensMaterialFilter,
        lensMaterials = lensMaterial,
        onPickMaterial = viewModel::onPickMaterial,

        selectedLensFamily = lensFamilyFilter,
        lensFamilies = lensFamily,
        onPickFamily = viewModel::onPickFamily,

        selectedLensDescription = lensDescriptionFilter,
        lensDescriptions = lensDescription,
        onPickDescription = viewModel::onPickDescription,

        onPickLens = {
            viewModel.onPickLens(it)
            canNavigate.value = true
        },
        isAddingSuggestion = isAddingSuggestion,
    )
}

@Composable
private fun LensSuggestionScreenImpl(
    modifier: Modifier = Modifier,

    lensSuggestion: List<LensSuggestionModel?> = listOf(),
    lensesSearch: LazyPagingItems<LensSuggestionModel>,

    isFamilyLensFilterEnabled: Boolean = false,
    isDescriptionLensFilterEnabled: Boolean = false,
    isMaterialLensFilterEnabled: Boolean = false,

    selectedLensGroup: String = "",
    lensGroups: Async<List<LensGroup>> = Uninitialized,
    onPickGroup: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensType: String = "",
    lensTypes: Async<List<FilterLensTypeEntity>> = Uninitialized,
    onPickType: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensSupplier: String = "",
    lensSuppliers: Async<List<FilterLensSupplierEntity>> = Uninitialized,
    onPickSupplier: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensMaterial: String = "",
    lensMaterials: Async<List<FilterLensMaterialEntity>> = Uninitialized,
    onPickMaterial: (materialId: String, materialName: String) -> Unit = { _, _ -> },

    selectedLensFamily: String = "",
    lensFamilies: Async<List<FilterLensFamilyEntity>> = Uninitialized,
    onPickFamily: (descriptionId: String, descriptionName: String) -> Unit = { _, _ -> },

    selectedLensDescription: String = "",
    lensDescriptions: Async<List<LensDescription>> = Uninitialized,
    onPickDescription: (descriptionId: String, descriptionName: String) -> Unit = { _, _ -> },

    onPickLens: (lensId: String) -> Unit = {},
    isAddingSuggestion: Boolean = false,
) {
    val showSearchScreen = remember { mutableStateOf<Boolean>(false)}

    val groupDialogState = rememberMaterialDialogState()
    PickTechDialog(
        dialogState = groupDialogState,
        groups = lensGroups,
        onPickGroup = onPickGroup,
    )

    val typeDialogState = rememberMaterialDialogState()
    PickTypeDialog(
        dialogState = typeDialogState,
        types = lensTypes,
        onPickType = onPickType,
    )

    val supplierDialogState = rememberMaterialDialogState()
    PickSupplierDialog(
        dialogState = supplierDialogState,
        suppliers = lensSuppliers,
        onPickSupplier = onPickSupplier,
    )

    val materialDialogState = rememberMaterialDialogState()
    PickMaterialDialog(
        dialogState = materialDialogState,
        materials = lensMaterials,
        onPickMaterial = onPickMaterial,
    )

    val familyDialogState = rememberMaterialDialogState()
    PickFamilyDialog(
        dialogState = familyDialogState,
        families = lensFamilies,
        onPickFamily = onPickFamily,
    )

    val descriptionDialogState = rememberMaterialDialogState()
    PickDescriptionDialog(
        dialogState = descriptionDialogState,
        descriptions = lensDescriptions,
        onPickDescription = onPickDescription,
    )

    if (isAddingSuggestion) {
        PeyessProgressIndicatorInfinite()
    } else {
        TierSuggestion(
            modifier = modifier,
            lenses = lensSuggestion,
            onShowSearchScreen = { showSearchScreen.value = true },
            onPickLens = onPickLens,
        )

        AnimatedVisibility(
            visible = showSearchScreen.value,
            enter = slideInVertically { it },
            exit = slideOutVertically { 0 },
        ) {
            LensSuggestionList(
                lenses = lensesSearch,

                onPickLens = onPickLens,

                isFamilyLensFilterEnabled = isFamilyLensFilterEnabled,
                isDescriptionLensFilterEnabled = isDescriptionLensFilterEnabled,
                isMaterialLensFilterEnabled = isMaterialLensFilterEnabled,

                selectedLensGroup = selectedLensGroup,
                groupsDialogState = groupDialogState,

                selectedLensType = selectedLensType,
                typesDialogState = typeDialogState,

                selectedLensSupplier = selectedLensSupplier,
                suppliersDialogState = supplierDialogState,

                selectedLensMaterial = selectedLensMaterial,
                materialsDialogState = materialDialogState,

                selectedLensFamily = selectedLensFamily,
                familiesDialogState = familyDialogState,

                selectedLensDescription = selectedLensDescription,
                descriptionsDialogState = descriptionDialogState,

                onHideSearchScreen = { showSearchScreen.value = false },
            )
        }
    }
}


@Composable
private fun TierSuggestion(
    modifier: Modifier = Modifier,
    lenses: List<LensSuggestionModel?> = listOf(null, null, null, null),
    onShowSearchScreen: () -> Unit = {},
    onPickLens: (lensId: String) -> Unit = {},
) {
    val density = LocalDensity.current
    val minimumHeightState = remember { MinimumHeightState() }

    val configuration = LocalConfiguration.current

    val cardSeparationPadding = 8
    val screenWidth = configuration.screenWidthDp

    Column(
        modifier = modifier
            .fillMaxSize(),
    ) {
        if (lenses.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(lenses.size) { index ->
                    lenses[index].let {
                        LensSuggestionCard(
                            modifier = Modifier
                                .width(screenWidth
                                    .div(2)
                                    .minus(cardSeparationPadding).dp)
                                .minimumHeightModifier(
                                    state = minimumHeightState,
                                    density = density,
                                )
                                .padding(vertical = 16.dp),
                            lens = it,
                            onPickLens = onPickLens
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))

            PeyessProgressIndicatorInfinite(
                modifier = Modifier
                    .weight(3f)
                    .padding(120.dp)
            )

            Spacer(modifier = Modifier.weight(2f))
        }

        Row(
            modifier = Modifier
                .height(60.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
                .clickable { onShowSearchScreen() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                tint = MaterialTheme.colors.onPrimary,
                contentDescription = "",
            )

            Spacer(modifier = Modifier.width(24.dp))

            Text(
                text = stringResource(id = R.string.lens_suggestion_search_lenses),
                style = MaterialTheme.typography.h6
                    .copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colors.onPrimary),
            )
        }
    }
}

@Composable
private fun PickTechDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    groups: Async<List<LensGroup>> = Uninitialized,
    onPickGroup: (groupId: String, groupName: String) -> Unit = { _, _ -> },
) {
    val groupsList: List<LensGroup>

    if (groups is Success) {
        groupsList = groups.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_group_none))
            val options = noneOption + groupsList.map { it.name }

            title(res = R.string.dialog_select_prism_axis_title)

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickGroup("", "")
                } else {
                    onPickGroup(groupsList[index - 1].id, groupsList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
private fun PickTypeDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    types: Async<List<FilterLensTypeEntity>> = Uninitialized,
    onPickType: (typeId: String, typeName: String) -> Unit = { _, _ -> },
) {
    val typesList: List<FilterLensTypeEntity>

    if (types is Success) {
        typesList = types.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_type_none))
            val options = noneOption + typesList.map { it.name }

            title(res = R.string.dialog_select_prism_axis_title)

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickType("", "")
                } else {
                    onPickType(typesList[index - 1].id, typesList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
private fun PickSupplierDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    suppliers: Async<List<FilterLensSupplierEntity>> = Uninitialized,
    onPickSupplier: (typeId: String, typeName: String) -> Unit = { _, _ -> },
) {
    val suppliersList: List<FilterLensSupplierEntity>

    if (suppliers is Success) {
        suppliersList = suppliers.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_supplier_none))
            val options = noneOption + suppliersList.map { it.name }

            title(res = R.string.dialog_select_prism_axis_title)

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickSupplier("", "")
                } else {
                    onPickSupplier(suppliersList[index - 1].id, suppliersList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
private fun PickMaterialDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    materials: Async<List<FilterLensMaterialEntity>> = Uninitialized,
    onPickMaterial: (materialId: String, materialName: String) -> Unit = { _, _ -> },
) {
    val materialsList: List<FilterLensMaterialEntity>

    if (materials is Success) {
        materialsList = materials.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_supplier_none))
            val options = noneOption + materialsList.map { it.name }

            title(res = R.string.dialog_select_prism_axis_title)

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickMaterial("", "")
                } else {
                    onPickMaterial(materialsList[index - 1].id, materialsList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
private fun PickFamilyDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    families: Async<List<FilterLensFamilyEntity>> = Uninitialized,
    onPickFamily: (familyId: String, familyName: String) -> Unit = { _, _ -> },
) {
    val familiesList: List<FilterLensFamilyEntity>

    if (families is Success) {
        familiesList = families.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_supplier_none))
            val options = noneOption + familiesList.map { it.name }

            title(res = R.string.dialog_select_prism_axis_title)

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickFamily("", "")
                } else {
                    onPickFamily(familiesList[index - 1].id, familiesList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@Composable
private fun PickDescriptionDialog(
    dialogState: MaterialDialogState = rememberMaterialDialogState(),
    descriptions: Async<List<LensDescription>> = Uninitialized,
    onPickDescription: (descriptionId: String, descriptionName: String) -> Unit = { _, _ -> },
) {
    val familiesList: List<LensDescription>

    if (descriptions is Success) {
        familiesList = descriptions.invoke()

        MaterialDialog(
            dialogState = dialogState,
            buttons = {
                negativeButton(stringResource(id = R.string.dialog_select_prism_axis_cancel))
            },
        ) {
            val noneOption = listOf(stringResource(id = R.string.lens_suggestion_pick_supplier_none))
            val options = noneOption + familiesList.map { it.name }

            title(res = R.string.dialog_select_prism_axis_title)

            listItems(
                list = options
            ) { index, item ->
                if (item == noneOption[0]) {
                    onPickDescription("", "")
                } else {
                    onPickDescription(familiesList[index - 1].id, familiesList[index - 1].name)
                }

                dialogState.hide()
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LensSuggestionList(
    modifier: Modifier = Modifier,

    onPickLens: (lensId: String) -> Unit,
    onHideSearchScreen: () -> Unit = {},

    lenses: LazyPagingItems<LensSuggestionModel>,

    isFamilyLensFilterEnabled: Boolean = false,
    isDescriptionLensFilterEnabled: Boolean = false,
    isMaterialLensFilterEnabled: Boolean = false,

    selectedLensGroup: String = "",
    groupsDialogState: MaterialDialogState = rememberMaterialDialogState(),

    selectedLensType: String = "",
    typesDialogState: MaterialDialogState = rememberMaterialDialogState(),

    selectedLensSupplier: String = "",
    suppliersDialogState: MaterialDialogState = rememberMaterialDialogState(),

    selectedLensMaterial: String = "",
    materialsDialogState: MaterialDialogState = rememberMaterialDialogState(),

    selectedLensFamily: String = "",
    familiesDialogState: MaterialDialogState = rememberMaterialDialogState(),

    selectedLensDescription: String = "",
    descriptionsDialogState: MaterialDialogState = rememberMaterialDialogState(),
) {
    val scaffoldState = rememberBackdropScaffoldState(initialValue = BackdropValue.Concealed)

    val headerHeight = 60.dp

    BackdropScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        frontLayerScrimColor = Color.Unspecified,

        appBar = {},

        backLayerContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SalesAppTheme.dimensions.minimum_touch_target)
                        .clickable { onHideSearchScreen() }
                ) {
                    Spacer(Modifier.weight(1f))

                    Icon(imageVector = Icons.Filled.FilterList, contentDescription = "")
                    Spacer(Modifier.width(24.dp))
                    Text(text = stringResource(id = R.string.lens_suggestion_filter_title))

                    Spacer(Modifier.weight(1f))
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, contentDescription = "")
                    Spacer(Modifier.width(16.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        title = if (selectedLensGroup.isEmpty()) {
                            stringResource(id = R.string.lens_suggestion_filter_group)
                        } else {
                            selectedLensGroup
                        },
                        onClick = { groupsDialogState.show() }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        title = if (selectedLensType.isEmpty()) {
                            stringResource(id = R.string.lens_suggestion_filter_type)
                        } else {
                            selectedLensType
                        },
                        onClick = { typesDialogState.show() },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        title = if (selectedLensSupplier.isEmpty()) {
                            stringResource(id = R.string.lens_suggestion_filter_supplier)
                        } else {
                            selectedLensSupplier
                        },
                        onClick = { suppliersDialogState.show() },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        enabled = isMaterialLensFilterEnabled,
                        title = if (selectedLensMaterial.isEmpty()) {
                            stringResource(id = R.string.lens_suggestion_filter_material)
                        } else {
                            selectedLensMaterial
                        },
                        onClick = { materialsDialogState.show() },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        enabled = isFamilyLensFilterEnabled,
                        title = if (selectedLensFamily.isEmpty()) {
                            stringResource(id = R.string.lens_suggestion_filter_family)
                        } else {
                            selectedLensFamily
                        },
                        onClick = { familiesDialogState.show() },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        enabled = isDescriptionLensFilterEnabled,
                        title = if (selectedLensDescription.isEmpty()) {
                            stringResource(id = R.string.lens_suggestion_filter_description)
                        } else {
                            selectedLensDescription
                        },
                        onClick = { descriptionsDialogState.show() },
                    )
                }
            }
        },
        frontLayerContent = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                val listState = rememberLazyListState()

                Row(
                    modifier = Modifier
                        .height(headerHeight)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "",
                        tint = MaterialTheme.colors.primary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = stringResource(id = R.string.lens_suggestion_search_lenses),
                        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Spacer(modifier = Modifier.height(36.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = listState,
                ) {
                    items(lenses.itemCount) { index ->
                        Timber.i("Displaying $index for ${lenses.itemCount}")

                        lenses[index].let {
                            LensCard(
                                lens = it!!,
                                onPickLens = onPickLens,
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    lenses.apply {
                        when {
                            loadState.refresh is LoadState.NotLoading -> {
                                if (lenses.itemCount == 0) {
                                    item {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.Top,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            val composition by rememberLottieComposition(
                                                LottieCompositionSpec.RawRes(R.raw.lottie_no_search_results))

                                            LottieAnimation(
                                                modifier = modifier.padding(36.dp),
                                                composition = composition,
                                                iterations = LottieConstants.IterateForever,
                                                clipSpec = LottieClipSpec.Progress(0f, 1f),
                                            )

                                            Text(
                                                text = stringResource(id = R.string.lens_suggestion_no_results),
                                                style = MaterialTheme.typography.h6
                                                    .copy(fontWeight = FontWeight.Bold),
                                            )
                                        }
                                    }
                                }
                            }

                            loadState.refresh is LoadState.Loading -> {
                                item {
                                    PeyessProgressIndicatorInfinite(
                                        modifier = Modifier.fillParentMaxSize()
                                    )
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item { CircularProgressIndicator() }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val e = lenses.loadState.refresh as LoadState.Error

                                item {
                                    ErrorItem(
                                        modifier = Modifier.fillParentMaxSize(),
                                        message = e.error.localizedMessage!!,
                                        onRetry = { retry() }
                                    )
                                }
                            }

                            loadState.append is LoadState.Error -> {
                                val e = lenses.loadState.append as LoadState.Error

                                item {
                                    ErrorItem(
                                        message = e.error.localizedMessage!!,
                                        onRetry = { retry() }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        peekHeight = 40.dp,
        headerHeight = 360.dp,
        gesturesEnabled = true,
    )
}

@Composable
private fun ErrorItem(
   modifier: Modifier = Modifier,
   message: String = "Aconteceu algo inesperado",
   onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = message, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.lens_suggestion_retry))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FilterButton(
    modifier: Modifier = Modifier,
    title: String = "",
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    OutlinedButton(
        modifier = modifier,
        border = BorderStroke(width = 4.dp, color = MaterialTheme.colors.onPrimary),
        colors = ButtonDefaults
            .buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                disabledBackgroundColor = Color.Gray.copy(alpha = 0.5f),
            ),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(text = title, color = MaterialTheme.colors.onPrimary)
    }
}

@Composable
private fun LensCard(
    modifier: Modifier = Modifier,
    lens: LensSuggestionModel = LensSuggestionModel(),
    onPickLens: (lensId: String) -> Unit = {},
) {
    ExpandableCard(
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colors.primary.copy(alpha = 0.1f),
            shape = Shapes.medium,
        ),

        visibleContent = {
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LensCircle(
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        if (lens.needsCheck) {
                            Color.Yellow
                        } else {
                            Color.Green
                        }
                    } else {
                        Color.Gray
                    }
                )

                Spacer(modifier = Modifier.width(32.dp))

                Text(
                    modifier = Modifier.weight(1f),
                    text = lens.name(),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    }
                )
            }
        },

        expandableContent = {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Divider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary.copy(alpha = 0.3f)
                    } else {
                        Color.Gray
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = lens.supplier,
                    style = MaterialTheme.typography.body1
                        .copy(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = lens.observation,
                    style = MaterialTheme.typography.body1
                        .copy(textAlign = TextAlign.Center),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary
                    } else {
                        Color.Gray
                    }
                )


                Spacer(modifier = Modifier.height(24.dp))
                Divider(
                    modifier = Modifier.padding(horizontal = 120.dp),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary.copy(alpha = 0.3f)
                    } else {
                        Color.Gray
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (lens.explanations.isNotEmpty()) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            text = lens.explanations[0],
                            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                            color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                                MaterialTheme.colors.primary
                            } else {
                                Color.Gray
                            },
                        )
                    }

                    if (lens.explanations.size >= 2) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            text = lens.explanations[1],
                            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                            color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                                MaterialTheme.colors.primary
                            } else {
                                Color.Gray
                            },
                        )
                    }
                }

                if (lens.explanations.size >= 3) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = lens.explanations[2],
                        style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                        color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                            MaterialTheme.colors.primary
                        } else {
                            Color.Gray
                        },
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                        MaterialTheme.colors.primary.copy(alpha = 0.3f)
                    } else {
                        Color.Gray.copy(alpha = 0.3f)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // TODO: add installments from store config data
                    LensPrice(
                        price = lens.price,
                        installments = 10.0,
                        color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                            MaterialTheme.colors.primary
                        } else {
                            Color.Gray
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TextButton(
                        enabled = lens.supportedDisponibilitites.isNotEmpty(),
                        onClick = { onPickLens(lens.id) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.lens_suggestion_select).uppercase(),
                            color = if (lens.supportedDisponibilitites.isNotEmpty()) {
                                MaterialTheme.colors.primary
                            } else {
                                Color.Gray
                            }
                        )
                    }
                }

            }
        }
    )
}

@Preview
@Composable
private fun LensPrice(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
    style: TextStyle = MaterialTheme.typography.h6,
    price: Double = 2500.0,
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
            text = "R$ ${price / installments}",
            style = MaterialTheme.typography.h6,
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
private fun ErrorItemPreview() {
    SalesAppTheme {
        ErrorItem()
    }
}

@Composable
private fun LensCircle(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
) {
    Box(
        modifier = modifier
            .background(color = color, shape = CircleShape)
            .border(
                border = BorderStroke(width = 2.dp, color = color),
                shape = CircleShape,
            )
            .width(10.dp)
            .height(10.dp),
    )
}

@Composable
private fun NoCardFound(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(120.dp))


        Icon(
            modifier = Modifier
                .height(60.dp)
                .width(60.dp),
            imageVector = Icons.Filled.SentimentDissatisfied,
            contentDescription = "",
        )
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            text = stringResource(id = R.string.lens_suggestion_filter_none_found),
            style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
        )
    }
}

@Composable
private fun LensSuggestionCard(
    modifier: Modifier = Modifier,
    lens: LensSuggestionModel? = null,
    onPickLens: (lensId: String) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp),
        elevation = 4.dp,
    ) {
        if (lens != null) {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = lens.group,
                    style = MaterialTheme.typography.h5
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .fillMaxWidth(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                )

                Text(
                    text = lens.brand,
                    style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .fillMaxWidth(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                )

                Text(
                    text = lens.design,
                    style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .fillMaxWidth(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                )

                Text(
                    text = lens.tech,
                    style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .fillMaxWidth(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                )

                Text(
                    text = lens.material,
                    style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .fillMaxWidth(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                )

                // TODO: get installments from store settings
                LensPrice(
                    price = lens.price,
                    installments = 10.0,
                    style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = lens.supplier,
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center),
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = lens.observation,
                    style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center),
                )

                if (lens.explanations.size > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lens.explanations[0],
                        style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center),
                    )
                }

                if (lens.explanations.size > 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lens.explanations[1],
                        style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center),
                    )
                }

                if (lens.explanations.size > 2) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lens.explanations[2],
                        style = MaterialTheme.typography.caption.copy(textAlign = TextAlign.Center),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .height(SalesAppTheme.dimensions.minimum_touch_target)
                        .width(120.dp),
                    onClick = { onPickLens(lens.id) },
                ) {
                    Text(text = stringResource(id = R.string.lens_suggestion_select).uppercase())
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            NoCardFound(modifier = modifier.fillMaxWidth())
        }
    }
}

@Preview
@Composable
private fun NoCardFoundPreview() {
    SalesAppTheme {
        NoCardFound()
    }
}

@Preview
@Composable
private fun TierSuggestionPreview() {
    SalesAppTheme {
        TierSuggestion(
            lenses = listOf(null, null, null, null)
        )
    }
}

@Preview
@Composable
private fun LensSuggestionCardPreview() {
    SalesAppTheme {
        LensSuggestionCard(
            modifier = Modifier.fillMaxSize(),
            lens = LensSuggestionModel(
                supplier = "Zeiss",
                group = "Grupo",
                brand = "Familia",
                design = "Descrição um pouco longa porque tem umas monstra",
                tech = "Tecnologia",
                material = "Material",
                price = 1526.0,

                observation = "Produzidas com o DNA HOYA, o que garante características tecnológicas superiores a diversas outras lentes do mercado",
                explanations = listOf(
                    "100% digital",
                    "Design esférico",
                    "Amplitude de visão",
                )
            )
        )
    }
}

@Preview
@Composable
private fun FilterButtonPreview() {
    SalesAppTheme {
        FilterButton(modifier = Modifier.width(120.dp), title = "Fornecedor")
    }
}

@Preview
@Composable
private fun LensCirclePreview() {
    SalesAppTheme {
        LensCircle()
    }
}

@Preview
@Composable
private fun LensCardPreview() {
    SalesAppTheme {
        LensCard(
            modifier = Modifier.fillMaxWidth(),
            lens = LensSuggestionModel(
                supplier = "Zeiss",
                brand = "Acabadas",
                design = "DuraVIsion BlueProtect",
                material = "1.60",
                price = 10500.0,
                observation = "As lentes ZEISS EnergizeMe foram feitas para usuários de lentes de contato",
                explanations = listOf(
                    "Evitam o aumento da tensão ocular",
                    "Proteção premium contra os raios UV acima de 400nm",
                    "Alta tecnologia aliada a uma Visão mais nítida",
                )
            ),
        )
    }
}