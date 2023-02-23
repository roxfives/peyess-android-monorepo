package com.peyess.salesapp.screen.sale.lens_comparison

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
import com.peyess.salesapp.feature.lens_comparison.LensComparisonScreenUI
import com.peyess.salesapp.screen.sale.lens_comparison.state.LensComparisonState
import com.peyess.salesapp.screen.sale.lens_comparison.state.LensComparisonViewModel
import com.peyess.salesapp.feature.lens_comparison.utils.ParseParameters

@Composable
fun LensComparisonScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    onAddComparison: () -> Unit = {},
    onLensPicked: (
        isEditing: Boolean,
        saleId: String,
        serviceOrderId: String,
    ) -> Unit = { _, _, _ -> },
) {
    val viewModel: LensComparisonViewModel = mavericksViewModel()

    ParseParameters(
        navController = navHostController,
        onUpdateIsEditing = viewModel::onUpdateIsEditing,
        onUpdateSaleId = viewModel::onUpdateSaleId,
        onUpdateServiceOrderId = viewModel::onUpdateServiceOrderId,
    )

    val isEditingParameter by viewModel.collectAsState(LensComparisonState::isEditing)
    val serviceOrderId by viewModel.collectAsState(LensComparisonState::serviceOrderId)
    val saleId by viewModel.collectAsState(LensComparisonState::saleId)

    val comparisons by viewModel.collectAsState(LensComparisonState::comparisons)

    val isTechLoading by viewModel.collectAsState(LensComparisonState::isTechLoading)
    val hasTechFailed by viewModel.collectAsState(LensComparisonState::hasTechFailed)
    val availableTechs by viewModel.collectAsState(LensComparisonState::availableTechs)

    val isMaterialLoading by viewModel.collectAsState(LensComparisonState::isMaterialLoading)
    val hasMaterialFailed by viewModel.collectAsState(LensComparisonState::hasMaterialFailed)
    val availableMaterials by viewModel.collectAsState(LensComparisonState::availableMaterials)

    val isColoringLoading by viewModel.collectAsState(LensComparisonState::isColoringLoading)
    val hasColoringFailed by viewModel.collectAsState(LensComparisonState::hasColoringFailed)
    val availableColorings by viewModel.collectAsState(LensComparisonState::availableColorings)

    val isTreatmentLoading by viewModel.collectAsState(LensComparisonState::isTreatmentLoading)
    val hasTreatmentFailed by viewModel.collectAsState(LensComparisonState::hasTreatmentFailed)
    val availableTreatments by viewModel.collectAsState(LensComparisonState::availableTreatments)

    val hasPickedProduct by viewModel.collectAsState(LensComparisonState::hasPickedProduct)
    val hasNavigated = remember { mutableStateOf(false) }
    val canNavigate = remember { mutableStateOf(false) }
    if (canNavigate.value && hasPickedProduct) {
        LaunchedEffect(Unit) {
            if (!hasNavigated.value) {
                hasNavigated.value = true
                canNavigate.value = false

                viewModel.lensPicked()
                onLensPicked(
                    isEditingParameter,
                    saleId,
                    serviceOrderId,
                )
            }
        }
    }

    LensComparisonScreenUI(
        modifier = modifier,

        comparisons = comparisons,

        onAddComparison = onAddComparison,
        onRemoveComparison = viewModel::removeComparison,
        onSelectComparison = {
            canNavigate.value = true
            viewModel.onPickProduct(it)
        },

        onLoadTechsFor = viewModel::loadAvailableTechForComparison,
        isTechListLoading = isTechLoading,
        hasTechListFailed = hasTechFailed,
        availableTechList = availableTechs,

        onLoadMaterialsFor = viewModel::loadAvailableMaterialForComparison,
        isMaterialListLoading = isMaterialLoading,
        hasMaterialListFailed = hasMaterialFailed,
        availableMaterialList = availableMaterials,

        onLoadColoringsFor = viewModel::loadAvailableColorings,
        isColoringListLoading = isColoringLoading,
        hasColoringListFailed = hasColoringFailed,
        availableColoringList = availableColorings,

        onLoadTreatmentsFor = viewModel::loadAvailableTreatments,
        isTreatmentListLoading = isTreatmentLoading,
        hasTreatmentListFailed = hasTreatmentFailed,
        availableTreatmentList = availableTreatments,

        onPickTech = viewModel::onPickTech,
        onPickMaterial = viewModel::onPickMaterial,
        onPickTreatment = viewModel::onPickTreatment,
        onPickColoring = viewModel::onPickColoring,
    )
}
