package com.peyess.salesapp.screen.edit_service_order.lens_suggestion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.lens_suggestion.LensSuggestionUI
import com.peyess.salesapp.screen.edit_service_order.lens_suggestion.state.EditLensSuggestionState
import com.peyess.salesapp.screen.edit_service_order.lens_suggestion.state.EditLensSuggestionViewModel
import com.peyess.salesapp.screen.edit_service_order.lens_comparison.utils.ParseParameters

@Composable
fun EditLensSuggestionScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onLensPicked: (saleId: String, serviceOrderId: String) -> Unit = { _, _ -> },
) {
    val viewModel: EditLensSuggestionViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::setSaleId,
        onUpdateServiceOrderId = viewModel::setServiceOrderId
    )

    val saleId by viewModel.collectAsState(EditLensSuggestionState::saleId)
    val serviceOrderId by viewModel.collectAsState(EditLensSuggestionState::serviceOrderId)

    val lensesTableStream by viewModel.collectAsState(EditLensSuggestionState::lensesTableStream)

    val isFamilyLensFilterEnabled by viewModel
        .collectAsState(EditLensSuggestionState::isFamilyLensFilterEnabled)
    val isDescriptionLensFilterEnabled by viewModel
        .collectAsState(EditLensSuggestionState::isDescriptionLensFilterEnabled)
    val isMaterialLensFilterEnabled by viewModel
        .collectAsState(EditLensSuggestionState::isMaterialLensFilterEnabled)

    val lensTypes by viewModel.collectAsState(EditLensSuggestionState::lensesTypesResponse)
    val isFilterTypesLoading by viewModel.collectAsState(EditLensSuggestionState::areTypesLoading)
    val hasFilterTypesFailed by viewModel.collectAsState(EditLensSuggestionState::hasTypesLoadingFailed)
    val lensTypesFilter by viewModel.collectAsState(EditLensSuggestionState::typeLensFilter)

    val lensSuppliers by viewModel.collectAsState(EditLensSuggestionState::lensesSuppliersResponse)
    val isFilterSuppliersLoading by viewModel.collectAsState(EditLensSuggestionState::areSuppliersLoading)
    val hasFilterSuppliersFailed by viewModel.collectAsState(EditLensSuggestionState::hasSuppliersLoadingFailed)
    val lensSuppliersFilter by viewModel.collectAsState(EditLensSuggestionState::supplierLensFilter)

    val lensFamilies by viewModel.collectAsState(EditLensSuggestionState::lensesFamiliesResponse)
    val isFilterFamiliesLoading by viewModel.collectAsState(EditLensSuggestionState::areFamiliesLoading)
    val hasFilterFamiliesFailed by viewModel.collectAsState(EditLensSuggestionState::hasFamiliesLoadingFailed)
    val lensFamiliesFilter by viewModel.collectAsState(EditLensSuggestionState::familyLensFilter)

    val lensDescriptions by viewModel.collectAsState(EditLensSuggestionState::lensesDescriptionsResponse)
    val isFilterDescriptionsLoading by viewModel.collectAsState(EditLensSuggestionState::areDescriptionsLoading)
    val hasFilterDescriptionsFailed by viewModel.collectAsState(EditLensSuggestionState::hasDescriptionsLoadingFailed)
    val lensDescriptionsFilter by viewModel.collectAsState(EditLensSuggestionState::descriptionLensFilter)

    val lensMaterials by viewModel.collectAsState(EditLensSuggestionState::lensesMaterialsResponse)
    val isFilterMaterialsLoading by viewModel.collectAsState(EditLensSuggestionState::areMaterialsLoading)
    val hasFilterMaterialsFailed by viewModel.collectAsState(EditLensSuggestionState::hasMaterialsLoadingFailed)
    val lensMaterialsFilter by viewModel.collectAsState(EditLensSuggestionState::materialLensFilter)

    val lensSpecialties by viewModel.collectAsState(EditLensSuggestionState::lensesSpecialtiesResponse)
    val isFilterSpecialtiesLoading by viewModel.collectAsState(EditLensSuggestionState::areSpecialtiesLoading)
    val hasFilterSpecialtiesFailed by viewModel.collectAsState(EditLensSuggestionState::hasSpecialtiesLoadingFailed)
    val lensSpecialtiesFilter by viewModel.collectAsState(EditLensSuggestionState::specialtyLensFilter)

    val lensGroups by viewModel.collectAsState(EditLensSuggestionState::lensesGroupsResponse)
    val isFilterGroupsLoading by viewModel.collectAsState(EditLensSuggestionState::areGroupsLoading)
    val hasFilterGroupsFailed by viewModel.collectAsState(EditLensSuggestionState::hasGroupsLoadingFailed)
    val lensGroupsFilter by viewModel.collectAsState(EditLensSuggestionState::groupLensFilter)

    val hasFilterUv by viewModel.collectAsState(EditLensSuggestionState::hasFilterUv)
    val hasFilterBlue by viewModel.collectAsState(EditLensSuggestionState::hasFilterBlue)

    val lensSuggestions by viewModel.collectAsState(EditLensSuggestionState::lensSuggestionsResponse)

//    val isAddingSuggestion by viewModel.collectAsState(EditLensSuggestionState::isAddingToSuggestion)
//    val hasAddedSuggestion by viewModel.collectAsState(EditLensSuggestionState::hasAddedToSuggestion)

//    val hasNavigated = remember { mutableStateOf(false) }
//    val canNavigate = remember { mutableStateOf(false) }
//    if (canNavigate.value && hasAddedSuggestion) {
//        LaunchedEffect(Unit) {
//            Timber.i("Trying to navigate: hasNavigated: ${hasNavigated.value} " +
//                    "canNavigate: ${canNavigate.value} " +
//                    "hasAddedSuggestion: $hasAddedSuggestion ")
//
//            if (!hasNavigated.value) {
//                Timber.i("Navigating")
//
//                hasNavigated.value = true
//                canNavigate.value = false

//                onLensPicked(
//                    isEditingParameter,
//                    saleId,
//                    serviceOrderId,
//                )
//            }
//        }
//    }

    LensSuggestionUI(
        modifier = modifier,

        showSuggestions = true,

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
            viewModel.onPickLens(serviceOrderId, it)
            onLensPicked(saleId, serviceOrderId)
        },
//        isAddingSuggestion = isAddingSuggestion,
    )
}
