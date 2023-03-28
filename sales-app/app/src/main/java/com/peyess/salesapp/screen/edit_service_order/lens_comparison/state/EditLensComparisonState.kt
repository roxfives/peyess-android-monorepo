package com.peyess.salesapp.screen.edit_service_order.lens_comparison.state

import arrow.core.Either
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MavericksState
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.model.local_sale.measure.LocalMeasuringDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.EditLensComparisonFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.error.ReadLensComparisonError
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.lenses.room.ColoringsResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.data.repository.lenses.room.MaterialsResponse
import com.peyess.salesapp.data.repository.lenses.room.TechsResponse
import com.peyess.salesapp.data.repository.lenses.room.TreatmentsResponse
import com.peyess.salesapp.feature.lens_comparison.model.Coloring
import com.peyess.salesapp.feature.lens_comparison.model.ColoringPickResponse
import com.peyess.salesapp.feature.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.lens_comparison.model.LensMaterial
import com.peyess.salesapp.feature.lens_comparison.model.LensPickResponse
import com.peyess.salesapp.feature.lens_comparison.model.LensTech
import com.peyess.salesapp.feature.lens_comparison.model.Treatment
import com.peyess.salesapp.feature.lens_comparison.model.TreatmentPickResponse
import com.peyess.salesapp.feature.lens_suggestion.model.Measuring

typealias LensComparisonResponse = Either<ReadLensComparisonError, List<LensComparisonDocument>>

typealias LensPickingResponse = Either<LocalLensRepositoryException, LensPickResponse>
typealias ColoringPickingResponse = Either<LocalLensRepositoryException, ColoringPickResponse>
typealias TreatmentPickingResponse = Either<LocalLensRepositoryException, TreatmentPickResponse>

data class EditLensComparisonState(
    val serviceOrderId: String = "",
    val saleId: String = "",

    val comparisonListAsync: Async<EditLensComparisonFetchResponse> = Uninitialized,
    val comparisonList: List<LensComparisonDocument> = emptyList(),

    val comparisonsAsync: Async<List<IndividualComparison>> = Uninitialized,
    val comparisons: List<IndividualComparison> = emptyList(),

    val prescriptionDocumentAsync: Async<EditPrescriptionFetchResponse> = Uninitialized,
    val prescriptionDocument: LocalPrescriptionDocument = LocalPrescriptionDocument(),

    val positioningLeftAsync: Async<EditPositioningFetchResponse> = Uninitialized,
    val measuringLeft: Measuring = Measuring(),

    val positioningRightAsync: Async<EditPositioningFetchResponse> = Uninitialized,
    val measuringRight: Measuring = Measuring(),

    val availableTechAsync: Async<TechsResponse> = Uninitialized,
    val availableTechs: List<LensTech> = emptyList(),

    val availableMaterialAsync: Async<MaterialsResponse> = Uninitialized,
    val availableMaterials: List<LensMaterial> = emptyList(),

    val availableColoringsAsync: Async<ColoringsResponse> = Uninitialized,
    val availableColorings: List<Coloring> = emptyList(),

    val availableTreatmentsAsync: Async<TreatmentsResponse> = Uninitialized,
    val availableTreatments: List<Treatment> = emptyList(),

    val lensPickResponseAsync: Async<LensPickingResponse> = Uninitialized,
    val lensPickResponse: LensPickResponse = LensPickResponse(),

    val coloringPickResponseAsync: Async<ColoringPickingResponse> = Uninitialized,
    val coloringPickResponse: ColoringPickResponse = ColoringPickResponse(),

    val treatmentPickResponseAsync: Async<TreatmentPickingResponse> = Uninitialized,
    val treatmentPickResponse: TreatmentPickResponse = TreatmentPickResponse(),
): MavericksState {
    private val hasPrescriptionLoaded = prescriptionDocumentAsync is Success
            && prescriptionDocumentAsync.invoke().isRight()
    private val isPrescriptionLoading = prescriptionDocumentAsync is Loading
    private val hasPrescriptionFailed = prescriptionDocumentAsync is Fail

    private val hasMeasuringLeftLoaded = positioningLeftAsync is Success
            && positioningLeftAsync.invoke().isRight()
    private val isMeasuringLeftLoading = positioningLeftAsync is Loading
    private val hasMeasuringLeftFailed = positioningLeftAsync is Fail

    private val hasMeasuringRightLoaded = positioningRightAsync is Success
            && positioningRightAsync.invoke().isRight()
    private val isMeasuringRightLoading = positioningRightAsync is Loading
    private val hasMeasuringRightFailed = positioningRightAsync is Fail

    val isTechLoading = availableTechAsync is Loading
    val hasTechFailed = availableTechAsync is Fail

    val isMaterialLoading = availableMaterialAsync is Loading
    val hasMaterialFailed = availableMaterialAsync is Fail

    val isColoringLoading = availableColoringsAsync is Loading
    val hasColoringFailed = availableColoringsAsync is Fail

    val isTreatmentLoading = availableTreatmentsAsync is Loading
    val hasTreatmentFailed = availableTreatmentsAsync is Fail

    val hasLoaded = hasPrescriptionLoaded
            && hasMeasuringLeftLoaded
            && hasMeasuringRightLoaded
            && comparisonListAsync is Success
            && comparisonListAsync.invoke().isRight()
    val isLoading = isPrescriptionLoading
            || isMeasuringLeftLoading
            || isMeasuringRightLoading
            || comparisonListAsync is Loading
    val hasFailed = hasPrescriptionFailed
            || hasMeasuringLeftFailed
            || hasMeasuringRightFailed
            || comparisonListAsync is Fail
}
