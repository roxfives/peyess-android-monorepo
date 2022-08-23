package com.peyess.salesapp.repository.products

import androidx.compose.runtime.mutableStateListOf
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.sqlite.db.SimpleSQLiteQuery
import com.peyess.salesapp.dao.products.firestore.lens_categories.LensTypeCategoryDao
import com.peyess.salesapp.dao.products.firestore.lens_groups.LensGroupDao
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensMaterialDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierDao
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensSupplierEntity
import com.peyess.salesapp.dao.products.room.filter_lens_supplier.FilterLensTypeDao
import com.peyess.salesapp.dao.products.room.filter_lens_type.FilterLensTypeEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensDao
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispDao
import com.peyess.salesapp.dao.products.room.local_lens_disp.LocalLensDispEntity
import com.peyess.salesapp.dao.products.room.local_product_exp.LocalProductExpDao
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.max
import kotlin.math.min

class ProductRepositoryImpl @Inject constructor(
    private val localLensesDao: LocalLensDao,
    private val localLensDispDao: LocalLensDispDao,
    private val localProductExpDao: LocalProductExpDao,
    private val lensGroupDao: LensGroupDao,
    private val lensTypeDao: FilterLensTypeDao,
    private val lensSupplierDao: FilterLensSupplierDao,
    private val lensMaterialDao: FilterLensMaterialDao,
    private val saleRepository: SaleRepository,
): ProductRepository {
    private fun buildQuery(lensFilter: LensFilter): SimpleSQLiteQuery {
        var queryString = "SELECT * FROM ${LocalLensEntity.tableName} "
        val queryConditions = mutableListOf<String>()


        if (lensFilter.groupId.isNotEmpty()) {
            queryConditions.add(" group_id = \'${lensFilter.groupId}\' ")
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
            queryConditions.add(" family_id = \'${lensFilter.familyId}\' ")
        }

        if (lensFilter.descriptionId.isNotEmpty()) {
            queryConditions.add(" description_id = \'${lensFilter.descriptionId}\' ")
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
        prescriptionData: PrescriptionDataEntity,
        disp: LocalLensDispEntity,
        mesureLeft: Measuring,
        measureRight: Measuring,
    ): Boolean {
        var fits: Boolean

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
                && minCyl >= disp.minCyl
                && maxSph <= disp.maxSph
                && minSph >= disp.minSph
                && maxDiam <= disp.diam

        if (fits && prescriptionData.hasPrism) {
            fits = maxPrism <= disp.prism
        }

        if (fits && prescriptionData.hasAddition) {
            fits = maxAddition <= disp.maxAdd
                    && minAddition >= disp.minAdd
        }

        if (fits && disp.sumRule) {
            fits = prescriptionData.sphericalLeft + prescriptionData.cylindricalLeft >= disp.minSph
                    && prescriptionData.sphericalRight + prescriptionData.cylindricalRight >= disp.minSph
        }

        return fits
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
                        saleRepository.currentPositioning(Eye.Left).filterNotNull().take(1),
                        saleRepository.currentPositioning(Eye.Right).filterNotNull().take(1),
                        saleRepository.currentPrescriptionData().filterNotNull().take(1)
                    ) { expList, lensDisp, posLeft, postRight, prescriptionData ->
                        val exp = expList.map { it.exp }
                        val measureLeft = posLeft.toMeasuring()
                        val measureRight = postRight.toMeasuring()

                        val supportedDisps = mutableStateListOf<LocalLensDispEntity>()
                        for (disp in lensDisp) {
                            if (isLensSupported(prescriptionData, disp, measureLeft, measureRight)) {
                                supportedDisps.add(disp)
                            }
                        }

                        var needsCheck = true
                        supportedDisps.forEach {
                            needsCheck = needsCheck && it.needsCheck
                        }

                        Timber.i("Updating lens ${lens.id} using exp $exp from $expList")
                        lensSuggestionModel = lens.toSuggestionModel(exp)
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

    override fun lensGroups(): Flow<List<LensGroup>> {
        return lensGroupDao.groups()
    }

    override fun lensTypes(): Flow<List<FilterLensTypeEntity>> {
        return lensTypeDao.getAll()
    }

    override fun lensSuppliers(): Flow<List<FilterLensSupplierEntity>> {
        return lensSupplierDao.getAll()
    }

    override fun lensMaterialDao(supplierId: String): Flow<List<FilterLensMaterialEntity>> {
        return lensMaterialDao.getAllWithSupplier(supplierId)
    }
}