package com.peyess.salesapp.feature.sale.lens_pick.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.BackdropScaffold
import androidx.compose.material.BackdropValue
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.rememberBackdropScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.R
import com.peyess.salesapp.feature.sale.lens_pick.state.LensPickState
import com.peyess.salesapp.feature.sale.lens_pick.state.LensPickViewModel
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterDescriptionImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterFamilyImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterGroupImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterMaterialImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterSpecialtyImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterSupplierImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensFilterTypeImpl
import com.peyess.salesapp.feature.sale.lens_pick.model.LensPickModel
import com.peyess.salesapp.feature.sale.lens_pick.utils.parseParameters
import com.peyess.salesapp.ui.component.chip.PeyessContentChip
import com.peyess.salesapp.ui.component.modifier.MinimumHeightState
import com.peyess.salesapp.ui.component.modifier.minimumHeightModifier
import com.peyess.salesapp.ui.component.progress.PeyessProgressIndicatorInfinite
import com.peyess.salesapp.ui.theme.SalesAppTheme
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import timber.log.Timber
import java.math.BigDecimal
import kotlin.math.min

private val frontLayerHeight = 360.dp

private val noFilterColor = Color.hsv(353f, 0.99f, 0.48f)
private val withFilterColor = Color.hsv(79f, 1f, 0.77f)

private const val maxExplanationsForSuggestionCard = 3

@Composable
fun LensSuggestionScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onLensPicked: (isEditingParam: Boolean) -> Unit = {},
) {
    val viewModel: LensPickViewModel = mavericksViewModel()

    parseParameters(
        navController = navHostController,
        onUpdateIsEditing = viewModel::updateIsEditing,
        onUpdateSaleId = viewModel::updateSaleId,
        onUpdateServiceOrderId = viewModel::updateServiceOrderId,
    )

    val isEditingParameter by viewModel.collectAsState(LensPickState::isEditingParameter)
//    val serviceOrderId by viewModel.collectAsState(LensPickState::serviceOrderId)
//    val saleId by viewModel.collectAsState(LensPickState::saleId)

    val isSale by viewModel.collectAsState(LensPickState::isSale)

    val lensesTableStream by viewModel.collectAsState(LensPickState::lensesTableStream)

    val isFamilyLensFilterEnabled by
        viewModel.collectAsState(LensPickState::isFamilyLensFilterEnabled)
    val isDescriptionLensFilterEnabled by
        viewModel.collectAsState(LensPickState::isDescriptionLensFilterEnabled)
    val isMaterialLensFilterEnabled by
        viewModel.collectAsState(LensPickState::isMaterialLensFilterEnabled)

    val lensTypes by viewModel.collectAsState(LensPickState::lensesTypesResponse)
    val isFilterTypesLoading by viewModel.collectAsState(LensPickState::areTypesLoading)
    val hasFilterTypesFailed by viewModel.collectAsState(LensPickState::hasTypesLoadingFailed)
    val lensTypesFilter by viewModel.collectAsState(LensPickState::typeLensFilter)

    val lensSuppliers by viewModel.collectAsState(LensPickState::lensesSuppliersResponse)
    val isFilterSuppliersLoading by viewModel.collectAsState(LensPickState::areSuppliersLoading)
    val hasFilterSuppliersFailed by viewModel.collectAsState(LensPickState::hasSuppliersLoadingFailed)
    val lensSuppliersFilter by viewModel.collectAsState(LensPickState::supplierLensFilter)

    val lensFamilies by viewModel.collectAsState(LensPickState::lensesFamiliesResponse)
    val isFilterFamiliesLoading by viewModel.collectAsState(LensPickState::areFamiliesLoading)
    val hasFilterFamiliesFailed by viewModel.collectAsState(LensPickState::hasFamiliesLoadingFailed)
    val lensFamiliesFilter by viewModel.collectAsState(LensPickState::familyLensFilter)

    val lensDescriptions by viewModel.collectAsState(LensPickState::lensesDescriptionsResponse)
    val isFilterDescriptionsLoading by viewModel.collectAsState(LensPickState::areDescriptionsLoading)
    val hasFilterDescriptionsFailed by viewModel.collectAsState(LensPickState::hasDescriptionsLoadingFailed)
    val lensDescriptionsFilter by viewModel.collectAsState(LensPickState::descriptionLensFilter)

    val lensMaterials by viewModel.collectAsState(LensPickState::lensesMaterialsResponse)
    val isFilterMaterialsLoading by viewModel.collectAsState(LensPickState::areMaterialsLoading)
    val hasFilterMaterialsFailed by viewModel.collectAsState(LensPickState::hasMaterialsLoadingFailed)
    val lensMaterialsFilter by viewModel.collectAsState(LensPickState::materialLensFilter)

    val lensSpecialties by viewModel.collectAsState(LensPickState::lensesSpecialtiesResponse)
    val isFilterSpecialtiesLoading by viewModel.collectAsState(LensPickState::areSpecialtiesLoading)
    val hasFilterSpecialtiesFailed by viewModel.collectAsState(LensPickState::hasSpecialtiesLoadingFailed)
    val lensSpecialtiesFilter by viewModel.collectAsState(LensPickState::specialtyLensFilter)

    val lensGroups by viewModel.collectAsState(LensPickState::lensesGroupsResponse)
    val isFilterGroupsLoading by viewModel.collectAsState(LensPickState::areGroupsLoading)
    val hasFilterGroupsFailed by viewModel.collectAsState(LensPickState::hasGroupsLoadingFailed)
    val lensGroupsFilter by viewModel.collectAsState(LensPickState::groupLensFilter)

    val hasFilterUv by viewModel.collectAsState(LensPickState::hasFilterUv)
    val hasFilterBlue by viewModel.collectAsState(LensPickState::hasFilterBlue)

    val lensSuggestions by viewModel.collectAsState(LensPickState::lensSuggestionsResponse)

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
                onLensPicked(isEditingParameter)
            }
        }
    }

    LensSuggestionScreenImpl(
        modifier = modifier,

        isSale = isSale,

        lensSuggestion = lensSuggestions,
        lensesTableStream = lensesTableStream,

        isFamilyLensFilterEnabled = isFamilyLensFilterEnabled,
        isDescriptionLensFilterEnabled = isDescriptionLensFilterEnabled,
        isMaterialLensFilterEnabled = isMaterialLensFilterEnabled,

        hasFilterUv = hasFilterUv,
        onFilterUvChanged = viewModel::onFilterUvChanged,
        hasFilterBlue = hasFilterBlue,
        onFilterBlueChanged = viewModel::onFilterBlueChanged,

        selectedLensType = lensTypesFilter,
        lensFilterTypes = lensTypes,
        isFilterTypesLoading = isFilterTypesLoading,
        hasFilterTypesFailed = hasFilterTypesFailed,
        onLoadFilterTypes = viewModel::loadLensTypes,
        onFilterType = viewModel::onPickType,

        selectedLensSupplier = lensSuppliersFilter,
        lensFilterSuppliers = lensSuppliers,
        isFilterSuppliersLoading = isFilterSuppliersLoading,
        hasFilterSuppliersFailed = hasFilterSuppliersFailed,
        onLoadFilterSuppliers = viewModel::loadLensSuppliers,
        onFilterSupplier = viewModel::onPickSupplier,

        selectedLensFamily = lensFamiliesFilter,
        lensFilterFamilies = lensFamilies,
        isFilterFamiliesLoading = isFilterFamiliesLoading,
        hasFilterFamiliesFailed = hasFilterFamiliesFailed,
        onLoadFilterFamilies = viewModel::loadLensFamilies,
        onFilterFamily = viewModel::onPickFamily,

        selectedLensDescription = lensDescriptionsFilter,
        lensFilterDescriptions = lensDescriptions,
        isFilterDescriptionsLoading = isFilterDescriptionsLoading,
        hasFilterDescriptionsFailed = hasFilterDescriptionsFailed,
        onLoadFilterDescriptions = viewModel::loadLensDescriptions,
        onFilterDescription = viewModel::onPickDescription,

        selectedLensMaterial = lensMaterialsFilter,
        lensFilterMaterials = lensMaterials,
        isFilterMaterialsLoading = isFilterMaterialsLoading,
        hasFilterMaterialsFailed = hasFilterMaterialsFailed,
        onLoadFilterMaterials = viewModel::loadLensMaterials,
        onFilterMaterial = viewModel::onPickMaterial,

        selectedLensSpecialty = lensSpecialtiesFilter,
        lensFilterSpecialties = lensSpecialties,
        isFilterSpecialtiesLoading = isFilterSpecialtiesLoading,
        hasFilterSpecialtiesFailed = hasFilterSpecialtiesFailed,
        onLoadFilterSpecialties = viewModel::loadLensSpecialties,
        onFilterSpecialty = viewModel::onPickSpecialty,

        selectedLensGroup = lensGroupsFilter,
        lensFilterGroups = lensGroups,
        isFilterGroupsLoading = isFilterGroupsLoading,
        hasFilterGroupsFailed = hasFilterGroupsFailed,
        onLoadFilterGroups = viewModel::loadLensGroups,
        onFilterGroup = viewModel::onPickGroup,

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

    isSale: Boolean = false,

    lensSuggestion: List<LensPickModel?> = listOf(),
    lensesTableStream: Flow<PagingData<LensPickModel>>,

    isFamilyLensFilterEnabled: Boolean = false,
    isDescriptionLensFilterEnabled: Boolean = false,
    isMaterialLensFilterEnabled: Boolean = false,

    hasFilterUv: Boolean = false,
    onFilterUvChanged: (Boolean) -> Unit = {},
    hasFilterBlue: Boolean = false,
    onFilterBlueChanged: (Boolean) -> Unit = {},

    selectedLensType: String = "",
    lensFilterTypes: List<LensFilterTypeImpl> = emptyList(),
    isFilterTypesEnabled: Boolean = true,
    isFilterTypesLoading: Boolean = false,
    hasFilterTypesFailed: Boolean = false,
    onLoadFilterTypes: () -> Unit = {},
    onRetryFilterTypes: () -> Unit = {},
    onFilterType: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensSupplier: String = "",
    lensFilterSuppliers: List<LensFilterSupplierImpl> = emptyList(),
    isFilterSuppliersEnabled: Boolean = true,
    isFilterSuppliersLoading: Boolean = false,
    hasFilterSuppliersFailed: Boolean = false,
    onLoadFilterSuppliers: () -> Unit = {},
    onRetryFilterSuppliers: () -> Unit = {},
    onFilterSupplier: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensFamily: String = "",
    lensFilterFamilies: List<LensFilterFamilyImpl> = emptyList(),
    isFilterFamiliesEnabled: Boolean = true,
    isFilterFamiliesLoading: Boolean = false,
    hasFilterFamiliesFailed: Boolean = false,
    onLoadFilterFamilies: () -> Unit = {},
    onRetryFilterFamilies: () -> Unit = {},
    onFilterFamily: (familyId: String, familyName: String) -> Unit = { _, _ -> },

    selectedLensDescription: String = "",
    lensFilterDescriptions: List<LensFilterDescriptionImpl> = emptyList(),
    isFilterDescriptionsEnabled: Boolean = true,
    isFilterDescriptionsLoading: Boolean = false,
    hasFilterDescriptionsFailed: Boolean = false,
    onLoadFilterDescriptions: () -> Unit = {},
    onRetryFilterDescriptions: () -> Unit = {},
    onFilterDescription: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensMaterial: String = "",
    lensFilterMaterials: List<LensFilterMaterialImpl> = emptyList(),
    isFilterMaterialsEnabled: Boolean = true,
    isFilterMaterialsLoading: Boolean = false,
    hasFilterMaterialsFailed: Boolean = false,
    onLoadFilterMaterials: () -> Unit = {},
    onRetryFilterMaterials: () -> Unit = {},
    onFilterMaterial: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensSpecialty: String = "",
    lensFilterSpecialties: List<LensFilterSpecialtyImpl> = emptyList(),
    isFilterSpecialtiesEnabled: Boolean = true,
    isFilterSpecialtiesLoading: Boolean = true,
    hasFilterSpecialtiesFailed: Boolean = false,
    onLoadFilterSpecialties: () -> Unit = {},
    onRetryFilterSpecialties: () -> Unit = {},
    onFilterSpecialty: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensGroup: String = "",
    lensFilterGroups: List<LensFilterGroupImpl> = emptyList(),
    isFilterGroupsEnabled: Boolean = true,
    isFilterGroupsLoading: Boolean = false,
    hasFilterGroupsFailed: Boolean = false,
    onLoadFilterGroups: () -> Unit = {},
    onRetryFilterGroups: () -> Unit = {},
    onFilterGroup: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    onPickLens: (lensId: String) -> Unit = {},
    isAddingSuggestion: Boolean = false,

    onShowSearchScreen: () -> Unit = {},
) {
    val showSearchScreen = remember { mutableStateOf(!isSale)}

    if (isAddingSuggestion) {
        PeyessProgressIndicatorInfinite()
    } else {
        if (isSale) {
            TierSuggestion(
                modifier = modifier,
                lenses = lensSuggestion,
                onShowSearchScreen = {
                    showSearchScreen.value = true
                    onShowSearchScreen()
                },
                onPickLens = onPickLens,
            )
        }

        AnimatedVisibility(
            visible = showSearchScreen.value,
            enter = slideInVertically { it },
            exit = slideOutVertically { 0 },
        ) {
            LensList(
                lensesTableStream = lensesTableStream,

                onPickLens = onPickLens,

                isFamilyLensFilterEnabled = isFamilyLensFilterEnabled,
                isDescriptionLensFilterEnabled = isDescriptionLensFilterEnabled,
                isMaterialLensFilterEnabled = isMaterialLensFilterEnabled,

                hasFilterUv = hasFilterUv,
                onFilterUvChanged = onFilterUvChanged,
                hasFilterBlue = hasFilterBlue,
                onFilterBlueChanged = onFilterBlueChanged,

                selectedLensType = selectedLensType,
                lensFilterTypes = lensFilterTypes,
                isFilterTypesEnabled = isFilterTypesEnabled,
                isFilterTypesLoading = isFilterTypesLoading,
                hasFilterTypesFailed = hasFilterTypesFailed,
                onLoadFilterTypes = onLoadFilterTypes,
                onRetryFilterTypes = onRetryFilterTypes,
                onFilterType = onFilterType,

                selectedLensSupplier = selectedLensSupplier,
                lensFilterSuppliers = lensFilterSuppliers,
                isFilterSuppliersEnabled = isFilterSuppliersEnabled,
                isFilterSuppliersLoading = isFilterSuppliersLoading,
                hasFilterSuppliersFailed = hasFilterSuppliersFailed,
                onLoadFilterSuppliers = onLoadFilterSuppliers,
                onRetryFilterSuppliers = onRetryFilterSuppliers,
                onFilterSupplier = onFilterSupplier,

                selectedLensFamily = selectedLensFamily,
                lensFilterFamilies = lensFilterFamilies,
                isFilterFamiliesEnabled = isFilterFamiliesEnabled,
                isFilterFamiliesLoading = isFilterFamiliesLoading,
                hasFilterFamiliesFailed = hasFilterFamiliesFailed,
                onLoadFilterFamilies = onLoadFilterFamilies,
                onRetryFilterFamilies = onRetryFilterFamilies,
                onFilterFamily = onFilterFamily,

                selectedLensDescription = selectedLensDescription,
                lensFilterDescriptions = lensFilterDescriptions,
                isFilterDescriptionsEnabled = isFilterDescriptionsEnabled,
                isFilterDescriptionsLoading = isFilterDescriptionsLoading,
                hasFilterDescriptionsFailed = hasFilterDescriptionsFailed,
                onLoadFilterDescriptions = onLoadFilterDescriptions,
                onRetryFilterDescriptions = onRetryFilterDescriptions,
                onFilterDescription = onFilterDescription,

                selectedLensMaterial = selectedLensMaterial,
                lensFilterMaterials = lensFilterMaterials,
                isFilterMaterialsEnabled = isFilterMaterialsEnabled,
                isFilterMaterialsLoading = isFilterMaterialsLoading,
                hasFilterMaterialsFailed = hasFilterMaterialsFailed,
                onLoadFilterMaterials = onLoadFilterMaterials,
                onRetryFilterMaterials = onRetryFilterMaterials,
                onFilterMaterial = onFilterMaterial,

                selectedLensSpecialty = selectedLensSpecialty,
                lensFilterSpecialties = lensFilterSpecialties,
                isFilterSpecialtiesEnabled = isFilterSpecialtiesEnabled,
                isFilterSpecialtiesLoading = isFilterSpecialtiesLoading,
                hasFilterSpecialtiesFailed = hasFilterSpecialtiesFailed,
                onLoadFilterSpecialties = onLoadFilterSpecialties,
                onRetryFilterSpecialties = onRetryFilterSpecialties,
                onFilterSpecialty = onFilterSpecialty,

                selectedLensGroup = selectedLensGroup,
                lensFilterGroups = lensFilterGroups,
                isFilterGroupsEnabled = isFilterGroupsEnabled,
                isFilterGroupsLoading = isFilterGroupsLoading,
                hasFilterGroupsFailed = hasFilterGroupsFailed,
                onLoadFilterGroups = onLoadFilterGroups,
                onRetryFilterGroups = onRetryFilterGroups,
                onFilterGroup = onFilterGroup,

                onHideSearchScreen = { showSearchScreen.value = false },
            )
        }
    }
}


@Composable
private fun TierSuggestion(
    modifier: Modifier = Modifier,
    lenses: List<LensPickModel?> = listOf(null, null, null, null),
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
                                .width(
                                    screenWidth
                                        .div(2)
                                        .minus(cardSeparationPadding).dp
                                )
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LensList(
    modifier: Modifier = Modifier,

    onPickLens: (lensId: String) -> Unit,
    onHideSearchScreen: () -> Unit = {},

    lensesTableStream: Flow<PagingData<LensPickModel>> = emptyFlow(),

    isFamilyLensFilterEnabled: Boolean = false,
    isDescriptionLensFilterEnabled: Boolean = false,
    isMaterialLensFilterEnabled: Boolean = false,

    hasFilterUv: Boolean = false,
    onFilterUvChanged: (Boolean) -> Unit = {},
    hasFilterBlue: Boolean = false,
    onFilterBlueChanged: (Boolean) -> Unit = {},

    selectedLensType: String = "",
    lensFilterTypes: List<LensFilterTypeImpl> = emptyList(),
    isFilterTypesEnabled: Boolean = true,
    isFilterTypesLoading: Boolean = false,
    hasFilterTypesFailed: Boolean = false,
    onLoadFilterTypes: () -> Unit = {},
    onRetryFilterTypes: () -> Unit = {},
    onFilterType: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensSupplier: String = "",
    lensFilterSuppliers: List<LensFilterSupplierImpl> = emptyList(),
    isFilterSuppliersEnabled: Boolean = true,
    isFilterSuppliersLoading: Boolean = false,
    hasFilterSuppliersFailed: Boolean = false,
    onLoadFilterSuppliers: () -> Unit = {},
    onRetryFilterSuppliers: () -> Unit = {},
    onFilterSupplier: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensFamily: String = "",
    lensFilterFamilies: List<LensFilterFamilyImpl> = emptyList(),
    isFilterFamiliesEnabled: Boolean = true,
    isFilterFamiliesLoading: Boolean = false,
    hasFilterFamiliesFailed: Boolean = false,
    onLoadFilterFamilies: () -> Unit = {},
    onRetryFilterFamilies: () -> Unit = {},
    onFilterFamily: (familyId: String, familyName: String) -> Unit = { _, _ -> },

    selectedLensDescription: String = "",
    lensFilterDescriptions: List<LensFilterDescriptionImpl> = emptyList(),
    isFilterDescriptionsEnabled: Boolean = true,
    isFilterDescriptionsLoading: Boolean = false,
    hasFilterDescriptionsFailed: Boolean = false,
    onLoadFilterDescriptions: () -> Unit = {},
    onRetryFilterDescriptions: () -> Unit = {},
    onFilterDescription: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensMaterial: String = "",
    lensFilterMaterials: List<LensFilterMaterialImpl> = emptyList(),
    isFilterMaterialsEnabled: Boolean = true,
    isFilterMaterialsLoading: Boolean = false,
    hasFilterMaterialsFailed: Boolean = false,
    onLoadFilterMaterials: () -> Unit = {},
    onRetryFilterMaterials: () -> Unit = {},
    onFilterMaterial: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensSpecialty: String = "",
    lensFilterSpecialties: List<LensFilterSpecialtyImpl> = emptyList(),
    isFilterSpecialtiesEnabled: Boolean = true,
    isFilterSpecialtiesLoading: Boolean = true,
    hasFilterSpecialtiesFailed: Boolean = false,
    onLoadFilterSpecialties: () -> Unit = {},
    onRetryFilterSpecialties: () -> Unit = {},
    onFilterSpecialty: (groupId: String, groupName: String) -> Unit = { _, _ -> },

    selectedLensGroup: String = "",
    lensFilterGroups: List<LensFilterGroupImpl> = emptyList(),
    isFilterGroupsEnabled: Boolean = true,
    isFilterGroupsLoading: Boolean = false,
    hasFilterGroupsFailed: Boolean = false,
    onLoadFilterGroups: () -> Unit = {},
    onRetryFilterGroups: () -> Unit = {},
    onFilterGroup: (groupId: String, groupName: String) -> Unit = { _, _ -> },
) {
    val scaffoldState = rememberBackdropScaffoldState(
        initialValue = BackdropValue.Revealed,
    )

    val headerHeight = 60.dp

    val typeDialogState = rememberMaterialDialogState()
    FilterLensDialog(
        dialogState = typeDialogState,
        filterTitleRes = R.string.lens_suggestion_filter_type,
        filterList = lensFilterTypes,
        isEnabled = isFilterTypesEnabled,
        isLoading = isFilterTypesLoading,
        hasFailed = hasFilterTypesFailed,
        onPick = onFilterType,
        onRetry = onRetryFilterTypes,
    )

    val supplierDialogState = rememberMaterialDialogState()
    FilterLensDialog(
        dialogState = supplierDialogState,
        filterTitleRes = R.string.lens_suggestion_filter_supplier,
        filterList = lensFilterSuppliers,
        isEnabled = isFilterSuppliersEnabled,
        isLoading = isFilterSuppliersLoading,
        hasFailed = hasFilterSuppliersFailed,
        onPick = onFilterSupplier,
        onRetry = onRetryFilterSuppliers,
    )

    val familyDialogState = rememberMaterialDialogState()
    FilterLensDialog(
        dialogState = familyDialogState,
        filterTitleRes = R.string.lens_suggestion_filter_family,
        filterList = lensFilterFamilies,
        isEnabled = isFilterFamiliesEnabled,
        isLoading = isFilterFamiliesLoading,
        hasFailed = hasFilterFamiliesFailed,
        onPick = onFilterFamily,
        onRetry = onRetryFilterFamilies,
    )

    val descriptionDialogState = rememberMaterialDialogState()
    FilterLensDialog(
        dialogState = descriptionDialogState,
        filterTitleRes = R.string.lens_suggestion_filter_description,
        filterList = lensFilterDescriptions,
        isEnabled = isFilterDescriptionsEnabled,
        isLoading = isFilterDescriptionsLoading,
        hasFailed = hasFilterDescriptionsFailed,
        onPick = onFilterDescription,
        onRetry = onRetryFilterDescriptions,
    )

    val materialDialogState = rememberMaterialDialogState()
    FilterLensDialog(
        dialogState = materialDialogState,
        filterTitleRes = R.string.lens_suggestion_filter_material,
        filterList = lensFilterMaterials,
        isEnabled = isFilterMaterialsEnabled,
        isLoading = isFilterMaterialsLoading,
        hasFailed = hasFilterMaterialsFailed,
        onPick = onFilterMaterial,
        onRetry = onRetryFilterMaterials,
    )

    val specialtyDialogState = rememberMaterialDialogState()
    FilterLensDialog(
        dialogState = specialtyDialogState,
        filterTitleRes = R.string.lens_suggestion_filter_specialty,
        filterList = lensFilterSpecialties,
        isEnabled = isFilterSpecialtiesEnabled,
        isLoading = isFilterSpecialtiesLoading,
        hasFailed = hasFilterSpecialtiesFailed,
        onPick = onFilterSpecialty,
        onRetry = onRetryFilterSpecialties,
    )

    val groupDialogState = rememberMaterialDialogState()
    FilterLensDialog(
        dialogState = groupDialogState,
        filterTitleRes = R.string.lens_suggestion_filter_group,
        filterList = lensFilterGroups,
        isEnabled = isFilterGroupsEnabled,
        isLoading = isFilterGroupsLoading,
        hasFailed = hasFilterGroupsFailed,
        onPick = onFilterGroup,
        onRetry = onRetryFilterGroups,
    )

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
                        title = selectedLensType.ifEmpty {
                            stringResource(id = R.string.lens_suggestion_filter_type)
                        },
                        onClick = {
                            onLoadFilterTypes()
                            typeDialogState.show()
                        },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        title = selectedLensSupplier.ifEmpty {
                            stringResource(id = R.string.lens_suggestion_filter_supplier)
                        },
                        onClick = {
                            onLoadFilterSuppliers()
                            supplierDialogState.show()
                        },
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
                        title = selectedLensFamily.ifEmpty {
                            stringResource(id = R.string.lens_suggestion_filter_family)
                        },
                        onClick = {
                            onLoadFilterFamilies()
                            familyDialogState.show()
                        },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        enabled = isDescriptionLensFilterEnabled,
                        title = selectedLensDescription.ifEmpty {
                            stringResource(id = R.string.lens_suggestion_filter_description)
                        },
                        onClick = {
                            onLoadFilterDescriptions()
                            descriptionDialogState.show()
                        },
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
                        enabled = isMaterialLensFilterEnabled,
                        title = selectedLensMaterial.ifEmpty {
                            stringResource(id = R.string.lens_suggestion_filter_material)
                        },
                        onClick = {
                            onLoadFilterMaterials()
                            materialDialogState.show()
                        },
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FilterButton(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                        title = selectedLensSpecialty.ifEmpty {
                            stringResource(id = R.string.lens_suggestion_filter_specialty)
                        },
                        onClick = {
                            onLoadFilterSpecialties()
                            specialtyDialogState.show()
                        },
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
                        title = selectedLensGroup.ifEmpty {
                            stringResource(id = R.string.lens_suggestion_filter_group)
                        },
                        onClick = {
                            onLoadFilterGroups()
                            groupDialogState.show()
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Spacer(
                        modifier = Modifier
                            .height(SalesAppTheme.dimensions.minimum_touch_target)
                            .width(240.dp)
                            .padding(horizontal = 16.dp),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PeyessContentChip(
                        isSelected = hasFilterUv,
                        onSelectionChanged = onFilterUvChanged,
                        content = {
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.WbSunny,
                                    contentDescription = "",
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(text = "Filtro para ultravioleta")
                            }
                        },

                        toggleOnBackgroundColor = MaterialTheme.colors.background,
                        toggleOnBorderColor = MaterialTheme.colors.background,
                        toggleOnTextColor = MaterialTheme.colors.onBackground,
                        toggleOffBackgroundColor = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        toggleOffBorderColor = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        toggleOffTextColor = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    PeyessContentChip(
                        isSelected = hasFilterBlue,
                        onSelectionChanged = onFilterBlueChanged,
                        content = {
                            Row {
                                Icon(
                                    imageVector = Icons.Filled.FlashlightOn,
                                    contentDescription = "",
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(text = "Filtro para luz azul")
                            }
                        },

                        toggleOnBackgroundColor = MaterialTheme.colors.background,
                        toggleOnBorderColor = MaterialTheme.colors.background,
                        toggleOnTextColor = MaterialTheme.colors.onBackground,
                        toggleOffBackgroundColor = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        toggleOffBorderColor = MaterialTheme.colors.background.copy(alpha = 0.8f),
                        toggleOffTextColor = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                    )
                }
            }
        },

        frontLayerContent = {
            val lensesTable = lensesTableStream.collectAsLazyPagingItems()

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
                    items(lensesTable.itemCount) { index ->
                        Timber.i("Displaying $index for ${lensesTable.itemCount}")

                        lensesTable[index].let {
                            LensCard(
                                lens = it!!,
                                onPickLens = onPickLens,
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    lensesTable.apply {
                        when {
                            loadState.refresh is LoadState.NotLoading -> {
                                if (lensesTable.itemCount == 0) {
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
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Top
                                    ) {
                                        PeyessProgressIndicatorInfinite(
                                            modifier = Modifier
                                                .padding(vertical = 16.dp)
                                                .height(frontLayerHeight)
                                        )
                                    }
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                item { CircularProgressIndicator() }
                            }

                            loadState.refresh is LoadState.Error -> {
                                val e = lensesTable.loadState.refresh as LoadState.Error

                                item {
                                    ErrorItem(
                                        modifier = Modifier.fillParentMaxSize(),
                                        message = e.error.localizedMessage!!,
                                        onRetry = { retry() }
                                    )
                                }
                            }

                            loadState.append is LoadState.Error -> {
                                val e = lensesTable.loadState.append as LoadState.Error

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
        headerHeight = frontLayerHeight,
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
    lens: LensPickModel? = null,
    onPickLens: (lensId: String) -> Unit = {},
) {
    Card(
        modifier = modifier
            .padding(horizontal = 16.dp),
        elevation = 4.dp,
    ) {
        if (lens != null) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
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
                    text = lens.family,
                    style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center),
                )

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .fillMaxWidth(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                )

                Text(
                    text = lens.description,
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

                if (lens.hasFilterUv) {
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .fillMaxWidth(1f),
                        color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = if (lens.hasFilterUv) {
                                Icons.Filled.WbTwilight
                            } else {
                                Icons.Filled.WbSunny
                            },
                            tint = if (lens.hasFilterUv) {
                                withFilterColor
                            } else {
                                noFilterColor
                            },
                            contentDescription = "",
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = if (lens.hasFilterUv) {
                                "Com filtro ultravioleta"
                            } else {
                                "Sem filtro ultravioleta"
                            },
                            color = if (lens.hasFilterUv) {
                                withFilterColor
                            } else {
                                noFilterColor
                            },
                            style = MaterialTheme.typography.body1
                                .copy(textAlign = TextAlign.Center),
                        )
                    }
                }

                if (lens.hasFilterBlue) {
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 12.dp)
                            .fillMaxWidth(1f),
                        color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = if (lens.hasFilterBlue) {
                                Icons.Filled.FlashlightOff
                            } else {
                                Icons.Filled.FlashlightOn
                            },
                            tint = if (lens.hasFilterBlue) {
                                withFilterColor
                            } else {
                                noFilterColor
                            },
                            contentDescription = "",
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = if (lens.hasFilterBlue) {
                                "Com filtro para luz azul"
                            } else {
                                "Sem filtro para luz azul"
                            },
                            color = if (lens.hasFilterBlue) {
                                withFilterColor
                            } else {
                                noFilterColor
                            },
                            style = MaterialTheme.typography.body1
                                .copy(textAlign = TextAlign.Center),
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                        .fillMaxWidth(1f),
                    color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = lens.supplier,
                    style = MaterialTheme.typography.body1
                        .copy(fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = lens.observation,
                    style = MaterialTheme.typography.body2.copy(textAlign = TextAlign.Center),
                )

                if (lens.explanations.isNotEmpty()) {
                    val lastIndex =
                        min(lens.explanations.size, maxExplanationsForSuggestionCard)

                    lens.explanations
                        .subList(0, lastIndex)
                        .forEach {
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                modifier = Modifier.padding(horizontal = 4.dp),
                                text = it,
                                style = MaterialTheme
                                    .typography
                                    .body2
                                    .copy(textAlign = TextAlign.Center),
                            )
                        }
                }

                Spacer(modifier = Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .height(SalesAppTheme.dimensions.minimum_touch_target),
                    onClick = { onPickLens(lens.id) },
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        text = stringResource(id = R.string.lens_suggestion_select).uppercase(),
                    )
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
            lens = LensPickModel(
                supplier = "Zeiss",
                group = "Grupo",
                family = "Familia",
                description = "Descrição um pouco longa porque tem umas monstra",
                tech = "Tecnologia",
                material = "Material",
                price = BigDecimal(1526.0),

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
private fun LensCardPreview() {
    SalesAppTheme {
        LensCard(
            modifier = Modifier.fillMaxWidth(),
            lens = LensPickModel(
                supplier = "Zeiss",
                family = "Acabadas",
                description = "DuraVIsion BlueProtect",
                material = "1.60",
                price = BigDecimal(10500.0),
                observation = "As lentes ZEISS EnergizeMe foram feitas para usuários de lentes de contato",
                explanations = listOf(
                    "Evitam o aumento da tensão ocular",
                    "Proteção premium contra os raios UV acima de 400nm",
                    "Alta tecnologia aliada a uma Visão mais nítida",
                ),

                hasFilterBlue = true,
                hasFilterUv = true,
            ),
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