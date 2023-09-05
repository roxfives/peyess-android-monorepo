package com.peyess.salesapp.screen.edit_service_order.lens_comparison.state

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.leftIfNull
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.EditLensComparisonFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.lens_comparison.EditLensComparisonRepository
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.positioning.EditPositioningRepository
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionFetchResponse
import com.peyess.salesapp.data.repository.edit_service_order.prescription.EditPrescriptionRepository
import com.peyess.salesapp.data.repository.edit_service_order.product_picked.EditProductPickedRepository
import com.peyess.salesapp.data.repository.lenses.room.ColoringsResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.MaterialsResponse
import com.peyess.salesapp.data.repository.lenses.room.TechsResponse
import com.peyess.salesapp.data.repository.lenses.room.TreatmentsResponse
import com.peyess.salesapp.data.repository.lenses.room.Unexpected
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.feature.lens_comparison.model.ColoringComparison
import com.peyess.salesapp.feature.lens_comparison.model.ColoringPickResponse
import com.peyess.salesapp.feature.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.lens_comparison.model.LensComparison
import com.peyess.salesapp.feature.lens_comparison.model.LensPickResponse
import com.peyess.salesapp.feature.lens_comparison.model.TreatmentComparison
import com.peyess.salesapp.feature.lens_comparison.model.TreatmentPickResponse
import com.peyess.salesapp.repository.sale.model.ProductPickedDocument
import com.peyess.salesapp.screen.edit_service_order.service_order.adapter.toMeasuring
import com.peyess.salesapp.screen.sale.lens_comparison.adapter.toColoring
import com.peyess.salesapp.screen.sale.lens_comparison.adapter.toLens
import com.peyess.salesapp.screen.sale.lens_comparison.adapter.toLensMaterial
import com.peyess.salesapp.screen.sale.lens_comparison.adapter.toLensTech
import com.peyess.salesapp.screen.sale.lens_comparison.adapter.toPrescription
import com.peyess.salesapp.screen.sale.lens_comparison.adapter.toTreatment
import com.peyess.salesapp.screen.sale.lens_comparison.state.LensComparisonState
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildGroupByForLensPicking
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildGroupByForMaterial
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildGroupByForTech
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildOrderByForLensPicking
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildOrderByForMaterial
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildOrderByForTech
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildQueryFieldsForLensWithMaterial
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildQueryFieldsForLensWithTech
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildQueryFieldsForMaterials
import com.peyess.salesapp.screen.sale.lens_comparison.state.query.buildQueryFieldsForTechs
import com.peyess.salesapp.typing.general.Eye
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

typealias IndividualComparisonResponse =
        Either<LocalLensRepositoryException, IndividualComparison>

private typealias ViewModelFactory =
        AssistedViewModelFactory<EditLensComparisonViewModel, EditLensComparisonState>
private typealias EditLensComparisonViewModelFactory =
        MavericksViewModelFactory<EditLensComparisonViewModel, EditLensComparisonState>

class EditLensComparisonViewModel @AssistedInject constructor(
    @Assisted initialState: EditLensComparisonState,
    private val editPositioningRepository: EditPositioningRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val localPrescriptionRepository: EditPrescriptionRepository,
    private val lensComparisonRepository: EditLensComparisonRepository,
    private val pickedProductRepository: EditProductPickedRepository,
): MavericksViewModel<EditLensComparisonState>(initialState) {

    init {
        onEach(EditLensComparisonState::serviceOrderId) {
            loadPrescription(it)
            loadMeasurings(it)
        }
        onEach(EditLensComparisonState::prescriptionDocument) { _ ->
            withState { streamComparisons(it.serviceOrderId) }
        }
        onEach(EditLensComparisonState::comparisonList) { comparisons ->
            withState { buildComparisonsFromList(comparisons, it.prescriptionDocument) }
        }

        onEach(EditLensComparisonState::lensPickResponse) { updateComparisonForLensResponse(it) }
        onEach(EditLensComparisonState::coloringPickResponse) {
            updateComparisonForColoringResponse(it)
        }
        onEach(EditLensComparisonState::treatmentPickResponse) {
            updateComparisonForTreatmentResponse(it)
        }

        onAsync(EditLensComparisonState::comparisonsAsync) { updateComparisonList(it) }
        onAsync(EditLensComparisonState::comparisonListAsync) { processComparisonListResponse(it) }

        onAsync(EditLensComparisonState::availableTechAsync) { processTechResponse(it) }
        onAsync(EditLensComparisonState::availableMaterialAsync) { processMaterialResponse(it) }

        onAsync(EditLensComparisonState::prescriptionDocumentAsync) { processPrescriptionResponse(it) }
        onAsync(EditLensComparisonState::positioningLeftAsync) { processMeasuringLeftResponse(it) }
        onAsync(EditLensComparisonState::positioningRightAsync) { processMeasuringRightResponse(it) }
        onAsync(EditLensComparisonState::availableColoringsAsync) { processColoringsResponse(it) }
        onAsync(EditLensComparisonState::availableTreatmentsAsync) { processTreatmentsResponse(it) }
//
        onAsync(EditLensComparisonState::lensPickResponseAsync) { processLensPickResponse(it) }
        onAsync(EditLensComparisonState::coloringPickResponseAsync) {
            processColoringPickingResponse(it)
        }
        onAsync(EditLensComparisonState::treatmentPickResponseAsync) {
            processTreatmentPickingResponse(it)
        }
    }

    private fun loadPrescription(serviceOrderId: String) {
        suspend {
            localPrescriptionRepository.prescriptionByServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(prescriptionDocumentAsync = it)
        }
    }

    private fun processPrescriptionResponse(
        response: EditPrescriptionFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    prescriptionDocumentAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(prescriptionDocument = it) }
        )
    }

    private fun loadMeasurings(serviceOrderId: String) {
        loadMeasuringLeft(serviceOrderId)
        loadMeasuringRight(serviceOrderId)
    }

    private fun loadMeasuringLeft(serviceOrderId: String) {
        suspend {
            editPositioningRepository.positioningForServiceOrder(serviceOrderId, Eye.Left)
        }.execute(Dispatchers.IO) {
            copy(positioningLeftAsync = it)
        }
    }

    private fun loadMeasuringRight(serviceOrderId: String) {
        suspend {
            editPositioningRepository.positioningForServiceOrder(serviceOrderId, Eye.Right)
        }.execute(Dispatchers.IO) {
            copy(positioningRightAsync = it)
        }
    }

    private fun processMeasuringLeftResponse(response: EditPositioningFetchResponse) = setState {
        response.fold(
            ifLeft = {
                copy(positioningLeftAsync = Fail(error = it.error))
            },

            ifRight = { copy(measuringLeft = it.toMeasuring()) }
        )
    }

    private fun processMeasuringRightResponse(response: EditPositioningFetchResponse) = setState {
        response.fold(
            ifLeft = {
                copy(positioningRightAsync = Fail(error = it.error))
            },

            ifRight = { copy(measuringRight = it.toMeasuring()) }
        )
    }

    private fun streamComparisons(serviceOrderId: String) {
        lensComparisonRepository
            .streamLensComparisonsForServiceOrder(serviceOrderId)
            .execute(Dispatchers.IO) {
                copy(comparisonListAsync = it)
            }
    }

    private fun processComparisonListResponse(
        response: EditLensComparisonFetchResponse,
    ) = setState {
        response.fold(
            ifLeft = { copy(comparisonListAsync = Fail(it.error)) },
            ifRight = { copy(comparisonList = it) },
        )
    }

    private fun buildComparisonsFromList(
        comparisons: List<LensComparisonDocument>,
        prescriptionDocument: LocalPrescriptionDocument,
    ) {
        suspend {
            comparisons.mapNotNull {
                buildIndividualComparison(it, prescriptionDocument).fold(
                    ifLeft = { error ->
                        Timber.e("Failed to build comparison $it", error)
                        null
                        // TODO: create warning for the user through the state
                    },
                    ifRight = { comparison -> comparison }
                )
            }
        }.execute(Dispatchers.IO) {
            copy(comparisonsAsync = it)
        }
    }

    private fun updateComparisonList(response: List<IndividualComparison>) = setState {
        copy(comparisons = response)
    }

    private suspend fun buildIndividualComparison(
        comparison: LensComparisonDocument,
        prescriptionDocument: LocalPrescriptionDocument,
    ): IndividualComparisonResponse = either {
        val originalLens = localLensesRepository
            .getLensById(comparison.originalLensId)
            .bind()
        val comparisonLens = localLensesRepository
            .getLensById(comparison.comparisonLensId)
            .bind()

        val originalColoring = localLensesRepository
            .getColoringById(comparison.originalLensId, comparison.originalColoringId)
            .bind()
        val comparisonColoring = localLensesRepository
            .getColoringById(comparison.comparisonLensId, comparison.comparisonColoringId)
            .bind()

        val originalTreatment = localLensesRepository
            .getTreatmentById(comparison.originalLensId, comparison.originalTreatmentId)
            .bind()
        val comparisonTreatment = localLensesRepository
            .getTreatmentById(comparison.comparisonLensId, comparison.comparisonTreatmentId)
            .bind()

        val lensComparison = LensComparison(
            originalLens = originalLens.toLens(),
            pickedLens = comparisonLens.toLens(),
        )

        val coloringComparison = ColoringComparison(
            originalColoring = originalColoring.toColoring(),
            pickedColoring = comparisonColoring.toColoring(),
        )

        val treatmentComparison = TreatmentComparison(
            originalTreatment = originalTreatment.toTreatment(),
            pickedTreatment = comparisonTreatment.toTreatment(),
        )

        IndividualComparison(
            id = comparison.id,
            soId = comparison.soId,
            prescription = prescriptionDocument.toPrescription(),
            lensComparison = lensComparison,
            coloringComparison = coloringComparison,
            treatmentComparison = treatmentComparison,
        )
    }

    private fun updateComparisonForLensResponse(lensPickResponse: LensPickResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val individualComparison = lensPickResponse.lensComparison

            val originalLens = individualComparison.lensComparison.originalLens
            val originalColoring = individualComparison.coloringComparison.originalColoring
            val originalTreatment = individualComparison.treatmentComparison.originalTreatment

            val pickedColoring = individualComparison.coloringComparison.pickedColoring
            val pickedTreatment = individualComparison.treatmentComparison.pickedTreatment

            val pickedLens = lensPickResponse.lensPicked

            lensComparisonRepository.updateLensComparison(
                lensComparison = LensComparisonDocument(
                    id = individualComparison.id,
                    soId = individualComparison.soId,

                    originalLensId = originalLens.id,
                    originalColoringId = originalColoring.id,
                    originalTreatmentId = originalTreatment.id,

                    comparisonLensId = pickedLens.id,
                    comparisonColoringId = pickedColoring.id,
                    comparisonTreatmentId = pickedTreatment.id,
                )
            )
        }
    }

    private fun updateComparisonForColoringResponse(coloringPickResponse: ColoringPickResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val individualComparison = coloringPickResponse.lensComparison

            val originalLens = individualComparison.lensComparison.originalLens
            val originalColoring = individualComparison.coloringComparison.originalColoring
            val originalTreatment = individualComparison.treatmentComparison.originalTreatment

            val pickedLens = individualComparison.lensComparison.pickedLens
            val pickedTreatment = individualComparison.treatmentComparison.pickedTreatment

            val pickedColoring = coloringPickResponse.coloringPicked

            lensComparisonRepository.updateLensComparison(
                lensComparison = LensComparisonDocument(
                    id = individualComparison.id,
                    soId = individualComparison.soId,

                    originalLensId = originalLens.id,
                    originalColoringId = originalColoring.id,
                    originalTreatmentId = originalTreatment.id,

                    comparisonLensId = pickedLens.id,
                    comparisonColoringId = pickedColoring.id,
                    comparisonTreatmentId = pickedTreatment.id,
                )
            )
        }
    }

    private fun updateComparisonForTreatmentResponse(treatmentPickResponse: TreatmentPickResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val individualComparison = treatmentPickResponse.lensComparison

            val originalLens = individualComparison.lensComparison.originalLens
            val originalColoring = individualComparison.coloringComparison.originalColoring
            val originalTreatment = individualComparison.treatmentComparison.originalTreatment

            val pickedLens = individualComparison.lensComparison.pickedLens
            val pickedColoring = individualComparison.coloringComparison.pickedColoring

            val pickedTreatment = treatmentPickResponse.treatment


            lensComparisonRepository.updateLensComparison(
                lensComparison = LensComparisonDocument(
                    id = individualComparison.id,
                    soId = individualComparison.soId,

                    originalLensId = originalLens.id,
                    originalColoringId = originalColoring.id,
                    originalTreatmentId = originalTreatment.id,

                    comparisonLensId = pickedLens.id,
                    comparisonColoringId = pickedColoring.id,
                    comparisonTreatmentId = pickedTreatment.id,
                )
            )
        }
    }

    private fun processTechResponse(response: TechsResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    availableTechAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = {
                copy(availableTechs = it.map { tech -> tech.toLensTech() })
            }
        )
    }

    private fun processMaterialResponse(response: MaterialsResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    availableMaterialAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = {
                copy(availableMaterials = it.map { tech -> tech.toLensMaterial() })
            }
        )
    }

    private fun processColoringsResponse(response: ColoringsResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    availableColoringsAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = {
                copy(
                    availableColorings = it.map { c -> c.toColoring() }
                )
            }
        )
    }

    private fun processTreatmentsResponse(response: TreatmentsResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    availableTreatmentsAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = {
                copy(
                    availableTreatments = it.map { t -> t.toTreatment() }
                )
            }
        )
    }

    private fun processLensPickResponse(
        response: LensPickingResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    lensPickResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(lensPickResponse = it) }
        )
    }

    private fun processColoringPickingResponse(
        response: ColoringPickingResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    coloringPickResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(coloringPickResponse = it) }
        )
    }

    private fun processTreatmentPickingResponse(
        response: TreatmentPickingResponse,
    ) = setState {
        response.fold(
            ifLeft = {
                copy(
                    treatmentPickResponseAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(treatmentPickResponse = it) }
        )
    }

    fun setSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun setServiceOrder(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun removeComparison(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            lensComparisonRepository.deleteById(id)
        }
    }

    fun loadAvailableTechForComparison(lensComparison: IndividualComparison) = withState {
        suspend {
            val queryFields = buildQueryFieldsForTechs(
                lens = lensComparison.lensComparison.originalLens,
                prescription = it.prescriptionDocument,
                measuringLeft = it.measuringLeft,
                measuringRight = it.measuringRight,
            )

            val groupBy = buildGroupByForTech()
            val orderBy = buildOrderByForTech()

            localLensesRepository.getTechsFilteredByDisponibilities(
                query = PeyessQuery(
                    queryFields = queryFields,
                    groupBy = groupBy,
                    orderBy = orderBy,
                )
            )
        }.execute(Dispatchers.IO) {
            copy(availableTechAsync = it)
        }
    }

    fun loadAvailableMaterialForComparison(lensComparison: IndividualComparison) = withState {
        suspend {
            val queryFields = buildQueryFieldsForMaterials(
                lens = lensComparison.lensComparison.originalLens,
                prescription = it.prescriptionDocument,
                measuringLeft = it.measuringLeft,
                measuringRight = it.measuringRight,
            )

            val groupBy = buildGroupByForMaterial()
            val orderBy = buildOrderByForMaterial()

            localLensesRepository.getMaterialsFilteredByDisponibilities(
                query = PeyessQuery(
                    queryFields = queryFields,
                    groupBy = groupBy,
                    orderBy = orderBy,
                )
            )
        }.execute(Dispatchers.IO) {
            copy(availableMaterialAsync = it)
        }
    }

    fun loadAvailableColorings(lensComparison: IndividualComparison) {
        suspend {
            localLensesRepository.getColoringsForLens(lensComparison.lensComparison.pickedLens.id)
        }.execute(Dispatchers.IO) {
            copy(availableColoringsAsync = it)
        }
    }

    fun loadAvailableTreatments(lensComparison: IndividualComparison) {
        suspend {
            localLensesRepository.getTreatmentsForLens(lensComparison.lensComparison.pickedLens.id)
        }.execute(Dispatchers.IO) {
            copy(availableTreatmentsAsync = it)
        }
    }

    fun onPickTech(techId: String, lensComparison: IndividualComparison) = withState {
        suspend {
            val queryFields = buildQueryFieldsForLensWithTech(
                lens = lensComparison.lensComparison.originalLens,
                techId = techId,
                prescription = it.prescriptionDocument,
                measuringLeft = it.measuringLeft,
                measuringRight = it.measuringRight,
            )

            val groupBy = buildGroupByForLensPicking()
            val orderBy = buildOrderByForLensPicking()

            val query = PeyessQuery(
                queryFields = queryFields,
                groupBy = groupBy,
                orderBy = orderBy,
                withLimit = 1,
            )

            localLensesRepository
                .getLensFilteredByDisponibility(query)
                .leftIfNull {
                    Unexpected(
                        error = Throwable("No lens found for query: $query")
                    )
                }.map {
                    LensPickResponse(
                        lensComparison = lensComparison,
                        lensPicked = it,
                    )
                }
        }.execute(Dispatchers.IO) {
            copy(lensPickResponseAsync = it)
        }
    }

    fun onPickMaterial(materialId: String, lensComparison: IndividualComparison) = withState {
        suspend {
            val queryFields = buildQueryFieldsForLensWithMaterial(
                lens = lensComparison.lensComparison.originalLens,
                materialId = materialId,
                prescription = it.prescriptionDocument,
                measuringLeft = it.measuringLeft,
                measuringRight = it.measuringRight,
            )

            val groupBy = buildGroupByForLensPicking()
            val orderBy = buildOrderByForLensPicking()

            val query = PeyessQuery(
                queryFields = queryFields,
                groupBy = groupBy,
                orderBy = orderBy,
                withLimit = 1,
            )

            localLensesRepository
                .getLensFilteredByDisponibility(query)
                .leftIfNull {
                    Unexpected(
                        error = Throwable("No lens found for query: $query")
                    )
                }.map {
                    LensPickResponse(
                        lensComparison = lensComparison,
                        lensPicked = it,
                    )
                }
        }.execute(Dispatchers.IO) {
            copy(lensPickResponseAsync = it)
        }
    }

    fun onPickTreatment(treatmentId: String, lensComparison: IndividualComparison) {
        suspend {
            localLensesRepository
                .getTreatmentById(lensComparison.lensComparison.pickedLens.id, treatmentId)
                .map {
                    TreatmentPickResponse(
                        lensComparison = lensComparison,
                        treatment = it.toTreatment(),
                    )
                }
        }.execute(Dispatchers.IO) {
            copy(treatmentPickResponseAsync = it)
        }
    }

    fun onPickColoring(coloringId: String, lensComparison: IndividualComparison) {
        suspend {
            localLensesRepository
                .getColoringById(lensComparison.lensComparison.pickedLens.id, coloringId)
                .map {
                    ColoringPickResponse(
                        lensComparison = lensComparison,
                        coloringPicked = it.toColoring(),
                    )
                }
        }.execute(Dispatchers.IO) {
            copy(coloringPickResponseAsync = it)
        }
    }

    fun onPickProduct(comparison: IndividualComparison) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val picked = ProductPickedDocument(
                soId = it.serviceOrderId,
                lensId = comparison.lensComparison.pickedLens.id,
                treatmentId = comparison.treatmentComparison.pickedTreatment.id,
                coloringId = comparison.coloringComparison.pickedColoring.id
            )

            pickedProductRepository.addProductPicked(picked)
        }
    }

    @AssistedFactory
    interface Factory: ViewModelFactory {
        override fun create(state: EditLensComparisonState): EditLensComparisonViewModel
    }

    companion object: EditLensComparisonViewModelFactory by hiltMavericksViewModelFactory()
}
