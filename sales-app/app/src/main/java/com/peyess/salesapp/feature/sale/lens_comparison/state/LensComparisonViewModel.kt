package com.peyess.salesapp.feature.sale.lens_comparison.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.lens_comparison.LensComparisonEntity
import com.peyess.salesapp.feature.sale.lens_comparison.model.ColoringComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.IndividualComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.LensComparison
import com.peyess.salesapp.feature.sale.lens_comparison.model.TreatmentComparison
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class LensComparisonViewModel @AssistedInject constructor(
    @Assisted initialState: LensComparisonState,
    val productRepository: ProductRepository,
    val saleRepository: SaleRepository,
): MavericksViewModel<LensComparisonState>(initialState) {

    private fun <T1, T2, T3, T4, T5, T6, R> combineAll(
        flow: Flow<T1>,
        flow2: Flow<T2>,
        flow3: Flow<T3>,
        flow4: Flow<T4>,
        flow5: Flow<T5>,
        flow6: Flow<T6>,
        transform: suspend (T1, T2, T3, T4, T5, T6) -> R
    ): Flow<R> = combine(
        combine(flow, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple)
    ) { t1, t2 ->
        transform(
            t1.first,
            t1.second,
            t1.third,
            t2.first,
            t2.second,
            t2.third
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun comparisons(): Flow<List<IndividualComparison>> {
        return saleRepository
            .comparisons()
            .flatMapLatest { comparisonsIds ->
                val comparisons: MutableList<Flow<IndividualComparison>> = mutableListOf()

                comparisonsIds.forEach { comparison ->
                    comparisons.add(
                        combineAll(
                            productRepository.lensById(comparison.originalLensId).take(1),
                            productRepository.coloringById(comparison.originalColoringId).take(1),
                            productRepository.treatmentById(comparison.originalTreatmentId).take(1),

                            productRepository.lensById(comparison.comparisonLensId).take(1),
                            productRepository.coloringById(comparison.comparisonColoringId).take(1),
                            productRepository.treatmentById(comparison.comparisonTreatmentId).take(1),
                        ) { originalLens, originalColoring, originalTreatment,
                            pickedLens, pickedColoring, pickedTreatment, ->

                            if (
                                originalLens == null
                                || originalColoring == null
                                || originalTreatment == null

                                || pickedLens == null
                                || pickedColoring == null
                                || pickedTreatment == null
                            ) {
                                error("One of the comparisons is null " +
                                        "originalLens: $originalLens \n" +
                                        "originalColoring: $originalColoring \n" +
                                        "originalTreatment: $originalTreatment \n" +
                                        "pickedLens: $pickedLens \n" +
                                        "pickedColoring: $pickedColoring \n" +
                                        "pickedTreatment: $pickedTreatment \n")
                            }

                            Timber.i("Loaded comparison " +
                                    "originalLens: $originalLens " +
                                    "originalColoring: $originalColoring " +
                                    "originalTreatment: $originalTreatment " +
                                    "pickedLens: $pickedLens " +
                                    "pickedColoring: $pickedColoring " +
                                    "pickedTreatment: $pickedTreatment ")

                            IndividualComparison(
                                id = comparison.id,

                                lensComparison = LensComparison(
                                    originalLens = originalLens,
                                    pickedLens = pickedLens,
                                ),

                                treatmentComparison = TreatmentComparison(
                                    originalTreatment = originalTreatment,
                                    pickedTreatment = pickedTreatment,
                                ),

                                coloringComparison = ColoringComparison(
                                    originalColoring = originalColoring,
                                    pickedColoring = pickedColoring,
                                )
                            )
                        }
                    )
                }

                if (comparisons.isEmpty()) {
                    flowOf(emptyList())
                } else {
                    combine(comparisons.map { it }) { it.asList() }
                }
            }
            .flowOn(Dispatchers.IO)
    }

    fun removeComparison(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            saleRepository.removeComparison(id)
        }
    }

    fun techsForComparison(lensComparison: IndividualComparison): Flow<List<FilterLensTechEntity>> {
        return productRepository.techsForLens(
            lensComparison.lensComparison.pickedLens.supplierId,
            lensComparison.lensComparison.pickedLens.brandId,
            lensComparison.lensComparison.pickedLens.designId,)
    }

    fun materialForComparison(lensComparison: IndividualComparison): Flow<List<FilterLensMaterialEntity>> {
        return productRepository.materialsForLens(
            lensComparison.lensComparison.pickedLens.supplierId,
            lensComparison.lensComparison.pickedLens.brandId,
            lensComparison.lensComparison.pickedLens.designId,
        )
    }

    fun treatmentsFor(lensComparison: IndividualComparison): Flow<List<LocalTreatmentEntity>> {
        return productRepository
            .treatmentsForLens(lensComparison.lensComparison.pickedLens.id)
    }

    fun coloringsFor(lensComparison: IndividualComparison): Flow<List<LocalColoringEntity>> {
        return productRepository
            .coloringsForLens(lensComparison.lensComparison.pickedLens.id)
    }

    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LensComparisonViewModel, LensComparisonState> {
        override fun create(state: LensComparisonState): LensComparisonViewModel
    }

    companion object:
        MavericksViewModelFactory<LensComparisonViewModel, LensComparisonState> by hiltMavericksViewModelFactory()
}