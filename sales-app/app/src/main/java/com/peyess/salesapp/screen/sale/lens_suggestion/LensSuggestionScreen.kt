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
import com.peyess.salesapp.feature.lens_suggestion.LensSuggestionUI
import com.peyess.salesapp.screen.sale.lens_suggestion.state.LensPickState
import com.peyess.salesapp.screen.sale.lens_suggestion.state.LensPickViewModel
import com.peyess.salesapp.screen.sale.lens_suggestion.utils.parseParameters
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
    val viewModel: LensPickViewModel = mavericksViewModel()

    parseParameters(
        navController = navHostController,
        onUpdateIsEditing = viewModel::updateIsEditing,
        onUpdateSaleId = viewModel::updateSaleId,
        onUpdateServiceOrderId = viewModel::updateServiceOrderId,
    )

    val isEditingParameter by viewModel.collectAsState(LensPickState::isEditingParameter)
    val saleId by viewModel.collectAsState(LensPickState::saleId)
    val serviceOrderId by viewModel.collectAsState(LensPickState::serviceOrderId)

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
