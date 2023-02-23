package com.peyess.salesapp.screen.sale.lens_suggestion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.screen.sale.lens_suggestion.utils.ParseParameters
import com.peyess.salesapp.feature.lens_suggestion.LensSuggestionUI
import com.peyess.salesapp.screen.sale.lens_suggestion.state.LensSuggestionState
import com.peyess.salesapp.screen.sale.lens_suggestion.state.LensSuggestionViewModel
import timber.log.Timber

@Composable
fun LensSuggestionScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    showSuggestions: Boolean = true,
    onLensPicked: (
        isEditingParam: Boolean,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _ -> },
) {
    val viewModel: LensSuggestionViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateIsEditing = viewModel::updateIsEditing,
        onUpdateSaleId = viewModel::updateSaleId,
        onUpdateServiceOrderId = viewModel::updateServiceOrderId,
    )

    val isEditingParameter by viewModel.collectAsState(LensSuggestionState::isEditingParameter)
    val saleId by viewModel.collectAsState(LensSuggestionState::saleId)
    val serviceOrderId by viewModel.collectAsState(LensSuggestionState::serviceOrderId)

    val lensesTableStream by viewModel.collectAsState(LensSuggestionState::lensesTableStream)

    val isFamilyLensFilterEnabled by
        viewModel.collectAsState(LensSuggestionState::isFamilyLensFilterEnabled)
    val isDescriptionLensFilterEnabled by
        viewModel.collectAsState(LensSuggestionState::isDescriptionLensFilterEnabled)
    val isMaterialLensFilterEnabled by
        viewModel.collectAsState(LensSuggestionState::isMaterialLensFilterEnabled)

    val lensTypes by viewModel.collectAsState(LensSuggestionState::lensesTypesResponse)
    val isFilterTypesLoading by viewModel.collectAsState(LensSuggestionState::areTypesLoading)
    val hasFilterTypesFailed by viewModel.collectAsState(LensSuggestionState::hasTypesLoadingFailed)
    val lensTypesFilter by viewModel.collectAsState(LensSuggestionState::typeLensFilter)

    val lensSuppliers by viewModel.collectAsState(LensSuggestionState::lensesSuppliersResponse)
    val isFilterSuppliersLoading by viewModel.collectAsState(LensSuggestionState::areSuppliersLoading)
    val hasFilterSuppliersFailed by viewModel.collectAsState(LensSuggestionState::hasSuppliersLoadingFailed)
    val lensSuppliersFilter by viewModel.collectAsState(LensSuggestionState::supplierLensFilter)

    val lensFamilies by viewModel.collectAsState(LensSuggestionState::lensesFamiliesResponse)
    val isFilterFamiliesLoading by viewModel.collectAsState(LensSuggestionState::areFamiliesLoading)
    val hasFilterFamiliesFailed by viewModel.collectAsState(LensSuggestionState::hasFamiliesLoadingFailed)
    val lensFamiliesFilter by viewModel.collectAsState(LensSuggestionState::familyLensFilter)

    val lensDescriptions by viewModel.collectAsState(LensSuggestionState::lensesDescriptionsResponse)
    val isFilterDescriptionsLoading by viewModel.collectAsState(LensSuggestionState::areDescriptionsLoading)
    val hasFilterDescriptionsFailed by viewModel.collectAsState(LensSuggestionState::hasDescriptionsLoadingFailed)
    val lensDescriptionsFilter by viewModel.collectAsState(LensSuggestionState::descriptionLensFilter)

    val lensMaterials by viewModel.collectAsState(LensSuggestionState::lensesMaterialsResponse)
    val isFilterMaterialsLoading by viewModel.collectAsState(LensSuggestionState::areMaterialsLoading)
    val hasFilterMaterialsFailed by viewModel.collectAsState(LensSuggestionState::hasMaterialsLoadingFailed)
    val lensMaterialsFilter by viewModel.collectAsState(LensSuggestionState::materialLensFilter)

    val lensSpecialties by viewModel.collectAsState(LensSuggestionState::lensesSpecialtiesResponse)
    val isFilterSpecialtiesLoading by viewModel.collectAsState(LensSuggestionState::areSpecialtiesLoading)
    val hasFilterSpecialtiesFailed by viewModel.collectAsState(LensSuggestionState::hasSpecialtiesLoadingFailed)
    val lensSpecialtiesFilter by viewModel.collectAsState(LensSuggestionState::specialtyLensFilter)

    val lensGroups by viewModel.collectAsState(LensSuggestionState::lensesGroupsResponse)
    val isFilterGroupsLoading by viewModel.collectAsState(LensSuggestionState::areGroupsLoading)
    val hasFilterGroupsFailed by viewModel.collectAsState(LensSuggestionState::hasGroupsLoadingFailed)
    val lensGroupsFilter by viewModel.collectAsState(LensSuggestionState::groupLensFilter)

    val hasFilterUv by viewModel.collectAsState(LensSuggestionState::hasFilterUv)
    val hasFilterBlue by viewModel.collectAsState(LensSuggestionState::hasFilterBlue)

    val lensSuggestions by viewModel.collectAsState(LensSuggestionState::lensSuggestionsResponse)

    val isAddingSuggestion by viewModel.collectAsState(LensSuggestionState::isAddingToSuggestion)
    val hasAddedSuggestion by viewModel.collectAsState(LensSuggestionState::hasAddedToSuggestion)

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

                onLensPicked(
                    isEditingParameter,
                    saleId,
                    serviceOrderId,
                )
            }
        }
    }

    LensSuggestionUI(
        modifier = modifier,

        showSuggestions = showSuggestions,

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
