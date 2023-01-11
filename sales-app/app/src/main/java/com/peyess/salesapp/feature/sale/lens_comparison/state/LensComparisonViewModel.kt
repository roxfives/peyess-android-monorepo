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
import com.peyess.salesapp.data.repository.lenses.room.LocalLensRepositoryException
import com.peyess.salesapp.data.repository.lenses.room.LocalLensesRepository
import com.peyess.salesapp.data.repository.lenses.room.Unexpected
import com.peyess.salesapp.data.repository.local_sale.lens_comparison.LensComparisonRepository
import com.peyess.salesapp.data.repository.local_sale.prescription.LocalPrescriptionRepository
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toColoring
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toLens
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toPrescription
import com.peyess.salesapp.feature.sale.lens_comparison.adapter.toTreatment
import com.peyess.salesapp.feature.sale.lens_comparison.model.ColoringComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.TreatmentComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.toLensComparison
import com.peyess.salesapp.feature.sale.lens_pick.state.LensPickState
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
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
): MavericksViewModel<LensComparisonState>(initialState) {

    init {
        onAsync(LensComparisonState::comparisonListAsync) { processComparisonListResponse(it) }

        onAsync(LensComparisonState::comparisonsAsync) { updateComparisonList(it) }

        onEach(LensComparisonState::comparisonList) { buildComparisonsFromList(it) }

        onEach(LensComparisonState::serviceOrderId) { loadComparisons(it) }
    }
//
//    data class NTuple4<T1, T2, T3, T4>(val t1: T1, val t2: T2, val t3: T3, val t4: T4)
//    private fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combineAll(
//        flow: Flow<T1>,
//        flow2: Flow<T2>,
//        flow3: Flow<T3>,
//        flow4: Flow<T4>,
//        flow5: Flow<T5>,
//        flow6: Flow<T6>,
//        flow7: Flow<T7>,
//        flow8: Flow<T8>,
//        transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R
//    ): Flow<R> = combine(
//        combine(flow, flow2, flow3, flow4, ::NTuple4),
//        combine(flow5, flow6, flow7,  flow8, ::NTuple4),
//    ) { t1, t2 ->
//        transform(
//            t1.t1,
//            t1.t2,
//            t1.t3,
//            t1.t4,
//            t2.t1,
//            t2.t2,
//            t2.t3,
//            t2.t4,
//        )
//    }

    private suspend fun buildIndividualComparison(
        comparison: LensComparisonDocument,
    ): IndividualComparisonResponse = either {localPrescriptionRepository
        val prescriptionDocument = localPrescriptionRepository
            .getPrescriptionForServiceOrder(comparison.soId)
            .mapLeft {
                Unexpected(
                    description = it.description,
                    error = it.error,
                )
            }.bind()

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

    private fun buildComparisonsFromList(comparisons: List<LensComparisonDocument>) {
        suspend {
            comparisons.mapNotNull {
                buildIndividualComparison(it).fold(
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

//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun comparisons(): Flow<List<IndividualComparison>> {
//        return saleRepository
//            .comparisons()
//            .flatMapLatest { comparisonsIds ->
//                val comparisons: MutableList<Flow<IndividualComparison>> = mutableListOf()
//
//                comparisonsIds.forEach { comparison ->
//                    comparisons.add(
//                        combineAll(
//                            productRepository.lensById(comparison.originalLensId).take(1),
//                            productRepository.coloringById(comparison.originalColoringId).take(1),
//                            productRepository.treatmentById(comparison.originalTreatmentId).take(1),
//
//                            productRepository.lensById(comparison.comparisonLensId).take(1),
//                            productRepository.coloringById(comparison.comparisonColoringId).take(1),
//                            productRepository.treatmentById(comparison.comparisonTreatmentId).take(1),
//
//                            saleRepository.activeSO().filterNotNull().take(1),
//                            saleRepository.currentPrescriptionData().take(1),
//                        ) { originalLens, originalColoring, originalTreatment,
//                            pickedLens, pickedColoring, pickedTreatment, so, prescription ->
//
//                            if (
//                                originalLens == null
//                                || originalColoring == null
//                                || originalTreatment == null
//
//                                || pickedLens == null
//                                || pickedColoring == null
//                                || pickedTreatment == null
//                            ) {
//                                error("One of the comparisons is null " +
//                                        "originalLens: $originalLens \n" +
//                                        "originalColoring: $originalColoring \n" +
//                                        "originalTreatment: $originalTreatment \n" +
//                                        "pickedLens: $pickedLens \n" +
//                                        "pickedColoring: $pickedColoring \n" +
//                                        "pickedTreatment: $pickedTreatment \n")
//                            }
//
//                            IndividualComparison(
//                                id = comparison.id,
//                                soId = so.id,
//
//                                prescription = prescription,
//
//                                lensComparison = LensComparison(
//                                    originalLens = originalLens,
//                                    pickedLens = pickedLens,
//                                ),
//
//                                treatmentComparison = TreatmentComparison(
//                                    originalTreatment = originalTreatment,
//                                    pickedTreatment = pickedTreatment,
//                                ),
//
//                                coloringComparison = ColoringComparison(
//                                    originalColoring = originalColoring,
//                                    pickedColoring = pickedColoring,
//                                )
//                            )
//                        }
//                    )
//                }
//
//                if (comparisons.isEmpty()) {
//                    flowOf(emptyList())
//                } else {
//                    combine(comparisons.map { it }) { it.asList() }
//                }
//            }
//            .flowOn(Dispatchers.IO)
//    }

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