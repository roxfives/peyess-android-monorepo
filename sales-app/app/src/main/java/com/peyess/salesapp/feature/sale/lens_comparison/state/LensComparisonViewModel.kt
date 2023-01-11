package com.peyess.salesapp.feature.sale.lens_comparison.state

import arrow.core.Either
import arrow.core.continuations.either
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.product_picked.ProductPickedEntity
import com.peyess.salesapp.data.model.local_sale.lens_comparison.LensComparisonDocument
import com.peyess.salesapp.data.model.local_sale.prescription.LocalPrescriptionDocument
import com.peyess.salesapp.data.repository.lenses.room.ColoringsResponse
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.MaterialsResponse
import com.peyess.salesapp.data.repository.lenses.room.TechsResponse
import com.peyess.salesapp.data.repository.lenses.room.TreatmentsResponse
import com.peyess.salesapp.data.repository.local_sale.lens_comparison.LensComparisonRepository
import com.peyess.salesapp.data.repository.local_sale.measuring.LocalMeasuringRepository
import com.peyess.salesapp.data.repository.local_sale.measuring.LocalMeasuringResponse
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionResponse
import com.peyess.salesapp.data.utils.query.PeyessQuery
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toColoring
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toLens
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toLensMaterial
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toLensTech
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toPrescription
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toTreatment
import com.peyess.salesapp.feature.sale.lens_comparison.model.ColoringComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.TreatmentComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.toLensComparison
import com.peyess.salesapp.feature.sale.lens_comparison.state.query.buildGroupByForMaterial
import com.peyess.salesapp.feature.sale.lens_comparison.state.query.buildGroupByForTech
import com.peyess.salesapp.feature.sale.lens_comparison.state.query.buildOrderByForMaterial
import com.peyess.salesapp.feature.sale.lens_comparison.state.query.buildOrderByForTech
import com.peyess.salesapp.feature.sale.lens_comparison.state.query.buildQueryFieldsForMaterials
import com.peyess.salesapp.feature.sale.lens_comparison.state.query.buildQueryFieldsForTechs
import com.peyess.salesapp.repository.products.ProductRepository
import com.peyess.salesapp.repository.sale.SaleRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import timber.log.Timber

typealias IndividualComparisonResponse =
        Either<LocalLensRepositoryException, IndividualComparison>

class LensComparisonViewModel @AssistedInject constructor(
    @Assisted initialState: LensComparisonState,
    private val productRepository: ProductRepository,
    private val saleRepository: SaleRepository,
    private val localLensesRepository: LocalLensesRepository,
    private val lensComparisonRepository: LensComparisonRepository,
    private val localPrescriptionRepository: LocalPrescriptionRepository,
    private val localMeasuringRepository: LocalMeasuringRepository,
): MavericksViewModel<LensComparisonState>(initialState) {

    init {
        onAsync(LensComparisonState::comparisonsAsync) { updateComparisonList(it) }
        onAsync(LensComparisonState::comparisonListAsync) { processComparisonListResponse(it) }

        onAsync(LensComparisonState::availableTechAsync) { processTechResponse(it) }
        onAsync(LensComparisonState::availableMaterialAsync) { processMaterialResponse(it) }

        onAsync(LensComparisonState::prescriptionDocumentAsync) { processPrescriptionResponse(it) }
        onAsync(LensComparisonState::measuringLeftAsync) { processMeasuringLeftResponse(it) }
        onAsync(LensComparisonState::measuringRightAsync) { processMeasuringRightResponse(it) }
        onAsync(LensComparisonState::availableColoringsAsync) { processColoringsResponse(it) }
        onAsync(LensComparisonState::availableTreatmentsAsync) { processTreatmentsResponse(it) }

        onEach(LensComparisonState::serviceOrderId) {
            loadPrescription(it)
            loadMeasurings(it)
        }
        onEach(LensComparisonState::prescriptionDocument) { _ ->
            withState { loadComparisons(it.serviceOrderId) }
        }
        onEach(LensComparisonState::comparisonList) { comparisons ->
            withState { buildComparisonsFromList(comparisons, it.prescriptionDocument) }
        }
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
            .getColoringById(comparison.originalColoringId)
            .bind()
        val comparisonColoring = localLensesRepository
            .getColoringById(comparison.comparisonColoringId)
            .bind()

        val originalTreatment = localLensesRepository
            .getTreatmentById(comparison.originalTreatmentId)
            .bind()
        val comparisonTreatment = localLensesRepository
            .getTreatmentById(comparison.comparisonTreatmentId)
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

    private fun processPrescriptionResponse(
        response: LocalPrescriptionResponse,
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

    private fun loadPrescription(serviceOrderId: String) {
        suspend {
            localPrescriptionRepository
                .getPrescriptionForServiceOrder(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(prescriptionDocumentAsync = it)
        }
    }

    private fun loadMeasurings(serviceOrderId: String) {
        loadMeasuringLeft(serviceOrderId)
        loadMeasuringRight(serviceOrderId)
    }

    private fun processMeasuringLeftResponse(response: LocalMeasuringResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    measuringLeftAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(measuringLeft = it) }
        )
    }

    private fun loadMeasuringLeft(serviceOrderId: String) {
        suspend {
            localMeasuringRepository
                .measuringForServiceOrder(serviceOrderId, Eye.Left)
        }.execute(Dispatchers.IO) {
            copy(measuringLeftAsync = it)
        }
    }

    private fun processMeasuringRightResponse(response: LocalMeasuringResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    measuringRightAsync = Fail(
                        error = it.error ?: Throwable(it.description)
                    ),
                )
            },

            ifRight = { copy(measuringRight = it) }
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

    private fun loadMeasuringRight(serviceOrderId: String) {
        suspend {
            localMeasuringRepository
                .measuringForServiceOrder(serviceOrderId, Eye.Right)
        }.execute(Dispatchers.IO) {
            copy(measuringRightAsync = it)
        }
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

    private fun processComparisonListResponse(response: LensComparisonResponse) = setState {
        response.fold(
            ifLeft = {
                copy(
                    comparisonListAsync =
                        Fail(it.error ?: Throwable(it.description))
                )
            },

            ifRight = {
                copy(comparisonList = it)
            }
        )
    }

    private fun updateComparisonList(response: List<IndividualComparison>) = setState {
        copy(comparisons = response)
    }

    private fun loadComparisons(serviceOrderId: String) {
        suspend {
            lensComparisonRepository.getBySo(serviceOrderId)
        }.execute(Dispatchers.IO) {
            copy(comparisonListAsync = it)
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

    fun onUpdateIsEditing(isEditing: Boolean) = setState {
        copy(isEditing = isEditing)
    }

    fun onUpdateSaleId(saleId: String) = setState {
        copy(saleId = saleId)
    }

    fun onUpdateServiceOrderId(serviceOrderId: String) = setState {
        copy(serviceOrderId = serviceOrderId)
    }

    fun removeComparison(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            lensComparisonRepository.deleteById(id)
            withState { loadComparisons(it.serviceOrderId) }
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
            localLensesRepository
                .getColoringsForLens(lensComparison.lensComparison.pickedLens.id)
        }.execute(Dispatchers.IO) {
            copy(availableColoringsAsync = it)
        }
    }

    fun loadAvailableTreatments(lensComparison: IndividualComparison) {
        suspend {
            localLensesRepository
                .getTreatmentsForLens(lensComparison.lensComparison.pickedLens.id)
        }.execute(Dispatchers.IO) {
            copy(availableTreatmentsAsync = it)
        }
    }

    fun techsForComparison(lensComparison: IndividualComparison): Flow<List<FilterLensTechEntity>> {
        return productRepository
            .techsForLens(
                lensComparison.lensComparison.pickedLens.supplierId,
                lensComparison.lensComparison.pickedLens.brandId,
                lensComparison.lensComparison.pickedLens.designId,
            )
            .flowOn(Dispatchers.IO)
    }

    fun materialForComparison(lensComparison: IndividualComparison): Flow<List<FilterLensMaterialEntity>> {
        return productRepository
            .materialsForLens(
                lensComparison.lensComparison.pickedLens.supplierId,
                lensComparison.lensComparison.pickedLens.brandId,
                lensComparison.lensComparison.pickedLens.designId,
            )
            .flowOn(Dispatchers.IO)
    }

    fun treatmentsFor(lensComparison: IndividualComparison): Flow<List<LocalTreatmentEntity>> {
        return productRepository
            .treatmentsForLens(lensComparison.lensComparison.pickedLens.id)
            .flowOn(Dispatchers.IO)
    }

    fun coloringsFor(lensComparison: IndividualComparison): Flow<List<LocalColoringEntity>> {
        return productRepository
            .coloringsForLens(lensComparison.lensComparison.pickedLens.id)
            .flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onPickTech(techId: String, lensComparison: IndividualComparison) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val supplierId = lensComparison.lensComparison.pickedLens.supplierId
            val brandId = lensComparison.lensComparison.pickedLens.brandId
            val designId = lensComparison.lensComparison.pickedLens.designId
            val materialId = lensComparison.lensComparison.pickedLens.materialId

            productRepository.lensWith(
                supplierId = supplierId,
                brandId = brandId,
                designId = designId,
                materialId = materialId,
                techId = techId
            ).flatMapLatest {
                combine(
                    productRepository.treatmentsForLens(it?.id ?: ""),
                    productRepository.coloringsForLens(it?.id ?: ""),
                ) { treatments, colorings ->
                    lensComparison
                        .toLensComparison()
                        .copy(
                            comparisonLensId = it?.id ?: "",
                            comparisonTreatmentId = treatments[0].id,
                            comparisonColoringId = colorings[0].id,
                        )
                }
            }.take(1)
                .onEach {
                    saleRepository.updateSaleComparison(it)
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun onPickMaterial(materialId: String, lensComparison: IndividualComparison) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val supplierId = lensComparison.lensComparison.pickedLens.supplierId
            val brandId = lensComparison.lensComparison.pickedLens.brandId
            val designId = lensComparison.lensComparison.pickedLens.designId
            val techId = lensComparison.lensComparison.pickedLens.techId

            productRepository.lensWith(
                supplierId = supplierId,
                brandId = brandId,
                designId = designId,
                materialId = materialId,
                techId = techId
            ).flatMapLatest {
                combine(
                    productRepository.treatmentsForLens(it?.id ?: ""),
                    productRepository.coloringsForLens(it?.id ?: ""),
                ) { treatments, colorings ->
                    lensComparison
                        .toLensComparison()
                        .copy(
                            comparisonLensId = it?.id ?: "",
                            comparisonTreatmentId = treatments[0].id,
                            comparisonColoringId = colorings[0].id,
                        )
                }
            }.take(1)
                .onEach {
                    saleRepository.updateSaleComparison(it)
                }
                .flowOn(Dispatchers.IO)
                .collect()
        }
    }

    fun onPickTreatment(treatmentId: String, lensComparison: IndividualComparison) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val comparison = lensComparison
                .toLensComparison()
                .copy(
                    comparisonTreatmentId = treatmentId,
                )

            saleRepository.updateSaleComparison(comparison)
        }
    }

    fun onPickColoring(coloringId: String, lensComparison: IndividualComparison) = withState {
        viewModelScope.launch(Dispatchers.IO) {
            val comparison = lensComparison
                .toLensComparison()
                .copy(
                    comparisonColoringId = coloringId,
                )

            saleRepository.updateSaleComparison(comparison)
        }
    }

    fun onPickProduct(comparison: IndividualComparison) = setState {
        viewModelScope.launch(Dispatchers.IO) {
            saleRepository.activeSO()
                .filterNotNull()
                .take(1)
                .onEach {
                    saleRepository.pickProduct(
                        ProductPickedEntity(
                            soId = it.id,
                            lensId = comparison.lensComparison.pickedLens.id,
                            treatmentId = comparison.treatmentComparison.pickedTreatment.id,
                            coloringId = comparison.coloringComparison.pickedColoring.id
                        )
                    )
            }.collect()
        }

        copy(hasPickedProduct = true)
    }

    fun lensPicked() = setState {
        copy(hasPickedProduct = false)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LensComparisonViewModel, LensComparisonState> {
        override fun create(state: LensComparisonState): LensComparisonViewModel
    }

    companion object:
        MavericksViewModelFactory<LensComparisonViewModel, LensComparisonState> by hiltMavericksViewModelFactory()
}