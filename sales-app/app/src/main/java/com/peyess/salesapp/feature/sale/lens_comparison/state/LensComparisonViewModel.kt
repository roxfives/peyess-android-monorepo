package com.peyess.salesapp.feature.sale.lens_comparison.state

import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.peyess.salesapp.base.MavericksViewModel
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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class LensComparisonViewModel @AssistedInject constructor(
    @Assisted initialState: LensComparisonState,
    val productRepository: ProductRepository,
    val saleRepository: SaleRepository,
): MavericksViewModel<LensComparisonState>(initialState) {

    init {

    }

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

    fun comparisons(): Flow<List<IndividualComparison>> {
        return saleRepository
            .comparisons()
            .map { comparisonsIds ->
                val comparisons: MutableList<IndividualComparison> = mutableListOf()

                comparisonsIds.forEach {
                    combineAll(
                        productRepository.lensById(it.originalLensId),
                        productRepository.coloringById(it.originalColoringId),
                        productRepository.treatmentById(it.originalTreatmentId),

                        productRepository.lensById(it.originalLensId),
                        productRepository.coloringById(it.originalColoringId),
                        productRepository.treatmentById(it.originalTreatmentId),
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
                            error("One if the suggestions is null")
                        }


                        comparisons.add(
                            IndividualComparison(
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
                        )
                    }.collect()
                }

                comparisons
            }
            .flowOn(Dispatchers.IO)
    }


    // hilt
    @AssistedFactory
    interface Factory: AssistedViewModelFactory<LensComparisonViewModel, LensComparisonState> {
        override fun create(state: LensComparisonState): LensComparisonViewModel
    }

    companion object:
        MavericksViewModelFactory<LensComparisonViewModel, LensComparisonState> by hiltMavericksViewModelFactory()
}