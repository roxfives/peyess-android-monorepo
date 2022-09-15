package com.peyess.salesapp.repository.products

import androidx.compose.runtime.mutableStateListOf
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import com.peyess.salesapp.R
import com.peyess.salesapp.app.SalesApplication
import com.peyess.salesapp.dao.products.firestore.lens_categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.products.firestore.lens_description.LensDescription
import com.peyess.salesapp.dao.products.firestore.lens_description.LensDescriptionDao
import com.peyess.salesapp.dao.products.firestore.lens_groups.LensGroupDao
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyDao
import com.peyess.salesapp.dao.products.room.filter_lens_family.FilterLensFamilyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_specialty.FilterLensSpecialtyEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensMaterialDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSpecialtyDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensTechDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensTypeDao
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringDao
import com.peyess.salesapp.dao.products.room.local_coloring.LocalColoringEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensDao
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispDao
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispEntity
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpDao
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentDao
import com.peyess.salesapp.dao.products.room.local_treatment.LocalTreatmentEntity
import com.peyess.salesapp.dao.sale.active_so.ActiveSOEntity
import com.peyess.salesapp.dao.sale.prescription_data.PrescriptionDataEntity
import com.peyess.salesapp.feature.sale.frames.state.Eye
import com.peyess.salesapp.feature.sale.lens_pick.model.LensSuggestionModel
import com.peyess.salesapp.feature.sale.lens_pick.model.Measuring
import com.peyess.salesapp.feature.sale.lens_pick.model.toMeasuring
import com.peyess.salesapp.feature.sale.lens_pick.model.toSuggestionModel
import com.peyess.salesapp.model.products.LensGroup
import com.peyess.salesapp.model.products.LensTypeCategory
import com.peyess.salesapp.repository.sale.SaleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class ProductRepositoryImpl @Inject constructor(
    private val salesApplication: SalesApplication,
    private val localLensesDao: LocalLensDao,
    private val treatmentDao: LocalTreatmentDao,
    private val coloringDao: LocalColoringDao,
    private val localLensDispDao: LocalLensDispDao,
    private val localProductExpDao: LocalProductExpDao,
    private val lensGroupDao: LensGroupDao,
    private val lensSpecialtiesDao: FilterLensSpecialtyDao,
    private val lensTypeDao: FilterLensTypeDao,
    private val lensSupplierDao: FilterLensSupplierDao,
    private val lensMaterialDao: FilterLensMaterialDao,
    private val lensTechDao: FilterLensTechDao,
    private val lensFamilyDao: FilterLensFamilyDao,
    private val lensDescriptionDao: LensDescriptionDao,
    private val saleRepository: SaleRepository,
): ProductRepository {

    private data class LensSupported(
        val isSupported: Boolean = true,
        val reasonNotSupported: String = "",
    )

    private fun <T1, T2, T3, T4, T5, T6, R> combine(
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

    private fun buildQuery(lensFilter: LensFilter): SimpleSQLiteQuery {
        var queryString = "SELECT * FROM ${LocalLensEntity.tableName} "
        val queryConditions = mutableListOf<String>()

        if (lensFilter.groupId.isNotEmpty()) {
            queryConditions.add(" group_id = \'${lensFilter.groupId}\' ")
        }

        if (lensFilter.specialtyId.isNotEmpty()) {
            queryConditions.add(" specialty_id = \'${lensFilter.specialtyId}\' ")
        }

        if (lensFilter.lensTypeId.isNotEmpty()) {
            queryConditions.add(" type_id = \'${lensFilter.lensTypeId}\' ")
        }

        if (lensFilter.supplierId.isNotEmpty()) {
            queryConditions.add(" supplier_id = \'${lensFilter.supplierId}\' ")
        }

        if (lensFilter.materialId.isNotEmpty()) {
            queryConditions.add(" material_id = \'${lensFilter.materialId}\' ")
        }

        if (lensFilter.familyId.isNotEmpty()) {
            queryConditions.add(" brand_id = \'${lensFilter.familyId}\' ")
        }

        if (lensFilter.descriptionId.isNotEmpty()) {
            queryConditions.add(" design_id = \'${lensFilter.descriptionId}\' ")
        }

        if (lensFilter.hasFilterUv) {
            queryConditions.add(" has_filter_uv = true ")
        }

        if (lensFilter.hasFilterBlue) {
            queryConditions.add(" has_filter_blue = true ")
        }

        var conditions = if (queryConditions.size > 0) {
            " WHERE ${queryConditions[0]} "
        } else {
            ""
        }

        if (queryConditions.size > 1) {
            for (i in 1 until queryConditions.size) {
                conditions += " AND ${queryConditions[i]}"
            }
        }


        queryString += "$conditions ORDER BY priority ASC"

        return SimpleSQLiteQuery(queryString)
    }

    private fun isLensSupported(
        lens: LocalLensEntity,
        activeSO: ActiveSOEntity,
        prescriptionData: PrescriptionDataEntity,
        disp: LocalLensDispEntity,
        mesureLeft: Measuring,
        measureRight: Measuring,
    ): LensSupported {
        // TODO: get from lensType conversion
        var fits = (activeSO.isLensTypeMono && lens.type.lowercase() == "monofocal")
                || (!activeSO.isLensTypeMono && lens.type.lowercase() != "monofocal")
        if (!fits) {
            return if (lens.type.lowercase() == "monofocal") {
                LensSupported(
                    isSupported = false,
                    reasonNotSupported = salesApplication
                        .stringResource(R.string.lens_incompatible_reason_required_mono),
                )
            } else {
                LensSupported(
                    isSupported = false,
                    reasonNotSupported = salesApplication
                        .stringResource(R.string.lens_incompatible_reason_required_multi),
                )
            }
        }

        val maxDiam = max(
            mesureLeft.diameter,
            measureRight.diameter,
        )

        val maxCyl = max(
            prescriptionData.cylindricalLeft,
            prescriptionData.cylindricalRight,
        )
        val minCyl = min(
            prescriptionData.cylindricalLeft,
            prescriptionData.cylindricalRight,
        )

        val maxSph = max(
            prescriptionData.sphericalLeft,
            prescriptionData.sphericalRight,
        )
        val minSph = min(
            prescriptionData.sphericalLeft,
            prescriptionData.sphericalRight,
        )

        val maxPrism = max(
            prescriptionData.prismDegreeLeft,
            prescriptionData.prismDegreeRight
        )

        val maxAddition = max(
            prescriptionData.additionLeft,
            prescriptionData.additionRight,
        )
        val minAddition = min(
            prescriptionData.additionLeft,
            prescriptionData.additionRight,
        )

        Timber.i("maxCyl: $maxCyl")
        Timber.i("minCyl: $minCyl")
        Timber.i("maxSph: $maxSph")
        Timber.i("minSph: $minSph")
        Timber.i("maxDiam: $maxDiam")

        Timber.i("disp: $disp")

        fits = maxCyl <= disp.maxCyl
        if (!fits) {
            return LensSupported(
                isSupported = false,
                reasonNotSupported = salesApplication
                    .stringResource(R.string.lens_incompatible_reason_cylindrical_too_big),
            )
        }

        fits = minCyl >= disp.minCyl
        if (!fits) {
            return LensSupported(
                isSupported = false,
                reasonNotSupported = salesApplication
                    .stringResource(R.string.lens_incompatible_reason_cylindrical_too_small),
            )
        }

        fits = maxSph <= disp.maxSph
        if (!fits) {
            return LensSupported(
                isSupported = false,
                reasonNotSupported = salesApplication
                    .stringResource(R.string.lens_incompatible_reason_spherical_too_big),
            )
        }

        fits = minSph >= disp.minSph
        if (!fits) {
            return LensSupported(
                isSupported = false,
                reasonNotSupported = salesApplication
                    .stringResource(R.string.lens_incompatible_reason_spherical_too_small),
            )
        }

        fits = maxDiam <= disp.diam
        if (!fits) {
            return LensSupported(
                isSupported = false,
                reasonNotSupported = salesApplication
                    .stringResource(R.string.lens_incompatible_reason_diameter_too_big),
            )
        }

        if (fits && prescriptionData.hasPrism) {
            fits = maxPrism <= disp.prism

            if (!fits) {
                return LensSupported(
                    isSupported = false,
                    reasonNotSupported = salesApplication
                        .stringResource(R.string.lens_incompatible_reason_prism_not_supported),
                )
            }
        }

        if (fits && prescriptionData.hasAddition) {
            fits = maxAddition <= disp.maxAdd
            if (!fits) {
                return LensSupported(
                    isSupported = false,
                    reasonNotSupported = salesApplication
                        .stringResource(R.string.lens_incompatible_reason_addition_too_big),
                )
            }

            fits = minAddition >= disp.minAdd
            if (!fits) {
                return LensSupported(
                    isSupported = false,
                    reasonNotSupported = salesApplication
                        .stringResource(R.string.lens_incompatible_reason_addition_too_small),
                )
            }
        }

        if (fits && disp.sumRule) {
            fits = prescriptionData.sphericalLeft + prescriptionData.cylindricalLeft - disp.maxCyl >= disp.minSph
                    && prescriptionData.sphericalRight + prescriptionData.cylindricalRight - disp.maxCyl >= disp.minSph

            if (!fits) {
                return LensSupported(
                    isSupported = false,
                    reasonNotSupported = salesApplication
                        .stringResource(R.string.lens_incompatible_reason_sum_rule_failed),
                )
            }
        }

        return LensSupported(
            isSupported = true,
            reasonNotSupported = salesApplication
                .stringResource(R.string.lens_incompatible_reason_compatible)
        )
    }

    override fun filteredLenses(lensFilter: LensFilter): Flow<PagingData<LensSuggestionModel>> {
        val query = buildQuery(lensFilter)
        val pagingSourceFactory =  { localLensesDao.getFilteredLenses(query) }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { lenses ->
            lenses.map { lens ->
                var lensSuggestionModel = LensSuggestionModel()

                Timber.i("Running mapping for lens ${lens.id}")
                runBlocking(Dispatchers.IO) {
                    combine(
                        localProductExpDao.getByProductId(lens.id).filterNotNull().take(1),
                        localLensDispDao.getLensDisp(lens.id).filterNotNull().take(1),
                        saleRepository.activeSO().filterNotNull().take(1),
                        saleRepository.currentPositioning(Eye.Left).filterNotNull().take(1),
                        saleRepository.currentPositioning(Eye.Right).filterNotNull().take(1),
                        saleRepository.currentPrescriptionData().filterNotNull().take(1),
                    ) { expList, lensDisp, so, posLeft, postRight, prescriptionData ->
                        val exp = expList.map { it.exp }
                        val measureLeft = posLeft.toMeasuring()
                        val measureRight = postRight.toMeasuring()

                        val reasonNotSupported = mutableListOf<String>()

                        var lensSupported: LensSupported
                        val supportedDisps = mutableStateListOf<LocalLensDispEntity>()
                        for (disp in lensDisp) {
                            lensSupported = isLensSupported(
                                lens, so, prescriptionData, disp, measureLeft, measureRight,
                            )

                            if (lensSupported.isSupported) {
                                supportedDisps.add(disp)
                            } else {
                                reasonNotSupported.add(lensSupported.reasonNotSupported)
                            }
                        }

                        var needsCheck = true
                        supportedDisps.forEach {
                            needsCheck = needsCheck && it.needsCheck
                        }

                        Timber.i("Updating lens ${lens.id} using exp $exp from $expList")
                        lensSuggestionModel = lens.toSuggestionModel(exp, reasonNotSupported)
                            .copy(
                                supportedDisponibilitites = supportedDisps,
                                needsCheck = needsCheck,
                            )
                    }.collect()
                }

                Timber.i("Returning final lens ${lens.id} as ${lensSuggestionModel}")
                lensSuggestionModel
            }
        }
    }

    override fun bestLensInGroup(groupId: String): Flow<LensSuggestionModel?> = flow {
        var lensSuggestionModel: LensSuggestionModel? = null
        val lenses = localLensesDao.getAllByGroupId(groupId)

        for (lens in lenses) {
            combine(
                localProductExpDao.getByProductId(lens.id).filterNotNull().take(1),
                localLensDispDao.getLensDisp(lens.id).filterNotNull().take(1),
                saleRepository.activeSO().filterNotNull().take(1),
                saleRepository.currentPositioning(Eye.Left).filterNotNull().take(1),
                saleRepository.currentPositioning(Eye.Right).filterNotNull().take(1),
                saleRepository.currentPrescriptionData().filterNotNull().take(1),
            ) { expList, lensDisp, so, posLeft, postRight, prescriptionData ->
                val exp = expList.map { it.exp }
                val measureLeft = posLeft.toMeasuring()
                val measureRight = postRight.toMeasuring()

                val reasonNotSupported = mutableListOf<String>()

                var lensSupported: LensSupported
                val supportedDisps = mutableStateListOf<LocalLensDispEntity>()
                for (disp in lensDisp) {
                    lensSupported = isLensSupported(
                        lens, so,prescriptionData, disp, measureLeft, measureRight,
                    )

                    if (lensSupported.isSupported) {
                        supportedDisps.add(disp)
                    } else {
                        reasonNotSupported.add(lensSupported.reasonNotSupported)
                    }
                }

                var needsCheck = true
                supportedDisps.forEach {
                    needsCheck = needsCheck && it.needsCheck
                }

                Timber.i("Updating lens ${lens.id} using exp $exp from $expList")
                lensSuggestionModel = lens.toSuggestionModel(exp, reasonNotSupported)
                    .copy(
                        supportedDisponibilitites = supportedDisps,
                        needsCheck = needsCheck,
                    )
            }.collect()

            if ((lensSuggestionModel?.supportedDisponibilitites?.size ?: 0) > 0) {
                emit(lensSuggestionModel)
                return@flow
            }
        }

        emit(null)
        return@flow
    }

    override fun lensGroups(): Flow<List<LensGroup>> {
        return lensGroupDao.groups()
    }

    override fun lensSpecialties(): Flow<List<FilterLensSpecialtyEntity>> {
        return lensSpecialtiesDao.getAll()
    }

    override fun lensTypes(): Flow<List<FilterLensTypeEntity>> {
        return lensTypeDao.getAll()
    }

    override fun lensSuppliers(): Flow<List<FilterLensSupplierEntity>> {
        return lensSupplierDao.getAll()
    }

    override fun lensMaterial(supplierId: String): Flow<List<FilterLensMaterialEntity>> {
        return lensMaterialDao.getAllWithSupplier(supplierId)
    }

    override fun lensFamily(supplierId: String): Flow<List<FilterLensFamilyEntity>> {
        return lensFamilyDao.getFamiliesWithSupplier(supplierId)
    }

    override fun lensDescription(familyId: String): Flow<List<LensDescription>> {
        return lensDescriptionDao.descriptionsFor(familyId)
    }

    override fun lensById(lensId: String): Flow<LocalLensEntity?> {
        return localLensesDao.getById(lensId)
    }

    override fun coloringById(coloringId: String): Flow<LocalColoringEntity?> {
        return coloringDao.getById(coloringId)
    }

    override fun treatmentById(treatmentId: String): Flow<LocalTreatmentEntity?> {
        return treatmentDao.getById(treatmentId)
    }

    override fun coloringsForLens(lensId: String): Flow<List<LocalColoringEntity>> {
        return coloringDao.coloringsForLens(lensId)
    }

    override fun treatmentsForLens(lensId: String): Flow<List<LocalTreatmentEntity>> {
        return treatmentDao.treatmentsForLens(lensId)
    }

    override fun materialsForLens(supplierId: String, brandId: String, designId: String):
            Flow<List<FilterLensMaterialEntity>> {

        return lensMaterialDao.materialsForLens(supplierId, brandId, designId)
            .onEach { Timber.i("Got materials $it") }
    }

    override fun lensWith(
        supplierId: String,
        brandId: String,
        designId: String,
        techId: String,
        materialId: String
    ): Flow<LocalLensEntity?> {
        return localLensesDao.searchForLens(supplierId, brandId, designId, techId, materialId)
    }

    override fun techsForLens(supplierId: String, brandId: String, designId: String):
            Flow<List<FilterLensTechEntity>> {
        return lensTechDao.techsForLens(supplierId, brandId, designId)
            .onEach { Timber.i("Got techs $it") }
    }
}