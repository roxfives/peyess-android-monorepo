package com.peyess.salesapp.screen.edit_service_order.lens_comparison

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.peyess.salesapp.feature.lens_comparison.LensComparisonScreenUI
import com.peyess.salesapp.screen.edit_service_order.lens_comparison.state.EditLensComparisonState
import com.peyess.salesapp.screen.edit_service_order.lens_comparison.state.EditLensComparisonViewModel
import com.peyess.salesapp.screen.edit_service_order.lens_comparison.utils.ParseParameters

@Composable
fun EditLensComparisonScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onAddComparison: () -> Unit = {},
    onLensPicked: () -> Unit = {},
) {

    val viewModel: EditLensComparisonViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateSaleId = viewModel::setSaleId,
        onUpdateServiceOrderId = viewModel::setServiceOrder,
    )

    val comparisons by viewModel.collectAsState(EditLensComparisonState::comparisons)

    val availableTechList by viewModel.collectAsState(EditLensComparisonState::availableTechs)
    val isTechListLoading by viewModel.collectAsState(EditLensComparisonState::isTechLoading)
    val hasTechListFailed by viewModel.collectAsState(EditLensComparisonState::hasTechFailed)

    val isMaterialListLoading by viewModel.collectAsState(EditLensComparisonState::isMaterialLoading)
    val hasMaterialListFailed by viewModel.collectAsState(EditLensComparisonState::hasMaterialFailed)
    val availableMaterialList by viewModel.collectAsState(EditLensComparisonState::availableMaterials)

    val isColoringListLoading by viewModel.collectAsState(EditLensComparisonState::isColoringLoading)
    val hasColoringListFailed by viewModel.collectAsState(EditLensComparisonState::hasColoringFailed)
    val availableColoringList by viewModel.collectAsState(EditLensComparisonState::availableColorings)

    val isTreatmentListLoading by viewModel.collectAsState(EditLensComparisonState::isTreatmentLoading)
    val hasTreatmentListFailed by viewModel.collectAsState(EditLensComparisonState::hasTreatmentFailed)
    val availableTreatmentList by viewModel.collectAsState(EditLensComparisonState::availableTreatments)

    LensComparisonScreenUI(
        modifier = modifier,
        comparisons = comparisons,
        isTechListLoading = isTechListLoading,
        hasTechListFailed = hasTechListFailed,
        availableTechList = availableTechList,
        onLoadTechsFor = viewModel::loadAvailableTechForComparison,
        onLoadMaterialsFor = viewModel::loadAvailableMaterialForComparison,
        isMaterialListLoading = isMaterialListLoading,
        hasMaterialListFailed = hasMaterialListFailed,
        availableMaterialList = availableMaterialList,
        onLoadColoringsFor = viewModel::loadAvailableColorings,
        isColoringListLoading = isColoringListLoading,
        hasColoringListFailed = hasColoringListFailed,
        availableColoringList = availableColoringList,
        onLoadTreatmentsFor = viewModel::loadAvailableTreatments,
        isTreatmentListLoading = isTreatmentListLoading,
        hasTreatmentListFailed = hasTreatmentListFailed,
        availableTreatmentList = availableTreatmentList,
        onPickTech = viewModel::onPickTech,
        onPickMaterial = viewModel::onPickMaterial,
        onPickTreatment = viewModel::onPickTreatment,
        onPickColoring = viewModel::onPickColoring,
        onRemoveComparison = viewModel::removeComparison,
        onAddComparison = onAddComparison,
        onSelectComparison = {
            viewModel.onPickProduct(it)
            onLensPicked()
        },
    )
}
