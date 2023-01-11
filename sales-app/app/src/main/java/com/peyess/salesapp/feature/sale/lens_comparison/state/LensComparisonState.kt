package com.peyess.salesapp.feature.sale.lens_comparison.state

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
import com.peyess.salesapp.data.repository.lenses.room.MaterialsResponse
import com.peyess.salesapp.data.repository.lenses.room.TechsResponse
import com.peyess.salesapp.data.repository.local_sale.lens_comparison.LocalComparisonReadError
import com.peyess.salesapp.data.repository.local_sale.measuring.LocalMeasuringResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensMaterial
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensTech

typealias LensComparisonResponse = Either<LocalComparisonReadError, List<LensComparisonDocument>>

data class LensComparisonState(
    val isEditing: Boolean = false,
    val serviceOrderId: String = "",
    val saleId: String = "",

    val comparisonListAsync: Async<LensComparisonResponse> = Uninitialized,
    val comparisonList: List<LensComparisonDocument> = emptyList(),

    val comparisonsAsync: Async<List<IndividualComparison>> = Uninitialized,
    val comparisons: List<IndividualComparison> = emptyList(),

    val prescriptionDocumentAsync: Async<LocalPrescriptionResponse> = Uninitialized,
    val prescriptionDocument: LocalPrescriptionDocument = LocalPrescriptionDocument(),

    val measuringLeftAsync: Async<LocalMeasuringResponse> = Uninitialized,
    val measuringLeft: LocalMeasuringDocument = LocalMeasuringDocument(),

    val measuringRightAsync: Async<LocalMeasuringResponse> = Uninitialized,
    val measuringRight: LocalMeasuringDocument = LocalMeasuringDocument(),

    val availableTechAsync: Async<TechsResponse> = Uninitialized,
    val availableTech: List<LensTech> = emptyList(),

    val availableMaterialAsync: Async<MaterialsResponse> = Uninitialized,
    val availableMaterial: List<LensMaterial> = emptyList(),

    val hasPickedProduct: Boolean = false,
): MavericksState {
    private val hasPrescriptionLoaded = prescriptionDocumentAsync is Success
            && prescriptionDocumentAsync.invoke().isRight()
    private val isPrescriptionLoading = prescriptionDocumentAsync is Loading
    private val hasPrescriptionFailed = prescriptionDocumentAsync is Fail

    private val hasMeasuringLeftLoaded = measuringLeftAsync is Success
            && measuringLeftAsync.invoke().isRight()
    private val isMeasuringLeftLoading = measuringLeftAsync is Loading
    private val hasMeasuringLeftFailed = measuringLeftAsync is Fail

    private val hasMeasuringRightLoaded = measuringRightAsync is Success
            && measuringRightAsync.invoke().isRight()
    private val isMeasuringRightLoading = measuringRightAsync is Loading
    private val hasMeasuringRightFailed = measuringRightAsync is Fail

    val isTechLoading = availableTechAsync is Loading
    val hasTechFailed = availableTechAsync is Fail

    val isMaterialLoading = availableMaterialAsync is Loading
    val hasMaterialFailed = availableMaterialAsync is Fail

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
