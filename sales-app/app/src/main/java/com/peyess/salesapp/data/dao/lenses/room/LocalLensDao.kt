package com.peyess.salesapp.data.dao.lenses.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringEntity
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringExplanationEntity
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringPriceDBView
import com.peyess.salesapp.data.model.lens.room.coloring.embedded.LocalLensColoringWithExplanationsEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensAltHeightEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensBaseEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDisponibilityManufacturerEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensFamilyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensMaterialTypeEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeEntity
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensAltHeightCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensColoringCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDetailsCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensDisponibilityManufacturerCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensMaterialTypeCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensTreatmentCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.cross_ref.LocalLensTypeCategoryCrossRef
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensFullUnionWithHeightAndLensTypeDBView
import com.peyess.salesapp.data.model.lens.room.dao.embedded.LocalLensWithDetails
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensWithDetailsDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesGroupDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesDescriptionDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesFamilyDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesMaterialDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesSpecialtyDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesSupplierDBView
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensesTypeDBView
import com.peyess.salesapp.data.model.lens.room.dao.embedded.LocalLensCompleteWithAltHeight
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesGroupSimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesDescriptionSimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesFamilySimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesMaterialSimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesSpecialtySimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesSupplierSimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesTechSimplified
import com.peyess.salesapp.data.model.lens.room.dao.simplified.LocalLensesTypeSimplified
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentEntity
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentExplanationEntity
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentPriceDBView
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentWithExplanationsEntity

private const val coloringsTable = LocalLensColoringPriceDBView.viewName
private const val lensColoringJunction = LocalLensColoringCrossRef.tableName

private const val treatmentsTable = LocalLensTreatmentPriceDBView.viewName
private const val lensTreatmentJunction = LocalLensTreatmentCrossRef.tableName

@Dao
interface LocalLensDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensCategory(entity: LocalLensCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDescription(entity: LocalLensDescriptionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFamily(entity: LocalLensFamilyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGroup(entity: LocalLensGroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMaterialCategory(entity: LocalLensMaterialCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMaterialType(entity: LocalLensMaterialTypeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMaterialTypeCrossRef(entity: LocalLensMaterialTypeCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMaterial(entity: LocalLensMaterialEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpecialty(entity: LocalLensSpecialtyEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSupplier(entity: LocalLensSupplierEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTech(entity: LocalLensTechEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addType(entity: LocalLensTypeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTypeCategory(entity: LocalLensTypeCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTypeCategoryCrossRef(entity: LocalLensTypeCategoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensExplanation(entity: LocalLensExplanationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensBase(entity: LocalLensBaseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensDisponibility(entity: LocalLensDisponibilityEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensDisponibilityManufacturer(
        entity: LocalLensDisponibilityManufacturerEntity
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensDisponibilityManufacturerCrossRef(
        entity: LocalLensDisponibilityManufacturerCrossRef
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensTreatment(entity: LocalLensTreatmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensTreatmentExp(entity: LocalLensTreatmentExplanationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensTreatmentCrossRef(entity: LocalLensTreatmentCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensColoring(entity: LocalLensColoringEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensColoringExp(entity: LocalLensColoringExplanationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensColoringCrossRef(entity: LocalLensColoringCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensAltHeight(entity: LocalLensAltHeightEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensAltHeightCrossRef(entity: LocalLensAltHeightCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLens(entity: LocalLensEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLensDetails(entity: LocalLensDetailsCrossRef)

    @Query("""
        SELECT COUNT(id) FROM ${LocalLensEntity.tableName}
    """)
    suspend fun totalLenses(): Int

    @Query("""
        SELECT * FROM ${LocalLensTypeCategoryEntity.tableName}
    """)
    suspend fun lensTypeCategories(): List<LocalLensTypeCategoryEntity>

    @Query("""
        SELECT * FROM ${LocalLensTypeCategoryEntity.tableName}
        WHERE id = :id
    """)
    suspend fun lensTypeCategoryById(id: String): LocalLensTypeCategoryEntity?

    @Transaction
    @RawQuery(observedEntities = [LocalLensWithDetailsDBView::class])
    fun getFilteredLenses(query: SimpleSQLiteQuery): PagingSource<Int, LocalLensWithDetails>

    @Transaction
    @RawQuery(observedEntities = [LocalLensFullUnionWithHeightAndLensTypeDBView::class])
    suspend fun getLensFilteredWithDisponibilities(
        query: SimpleSQLiteQuery,
    ): LocalLensCompleteWithAltHeight?

    @Transaction
    @Query("SELECT * FROM ${LocalLensWithDetailsDBView.viewName} WHERE id = :id")
    suspend fun getLensById(id: String): LocalLensWithDetails?

    @Transaction
    @RawQuery(observedEntities = [LocalLensesTypeDBView::class])
    fun getFilteredTypes(query: SimpleSQLiteQuery): List<LocalLensesTypeSimplified>

    @Transaction
    @RawQuery(observedEntities = [LocalLensesSupplierDBView::class])
    fun getFilteredSuppliers(query: SimpleSQLiteQuery): List<LocalLensesSupplierSimplified>

    @Transaction
    @RawQuery(observedEntities = [LocalLensesFamilyDBView::class])
    fun getFilteredFamilies(query: SimpleSQLiteQuery): List<LocalLensesFamilySimplified>

    @Transaction
    @RawQuery(observedEntities = [LocalLensesDescriptionDBView::class])
    fun getFilteredDescriptions(query: SimpleSQLiteQuery): List<LocalLensesDescriptionSimplified>

    @Transaction
    @RawQuery(observedEntities = [LocalLensesMaterialDBView::class])
    fun getFilteredMaterials(query: SimpleSQLiteQuery): List<LocalLensesMaterialSimplified>

    @Transaction
    @RawQuery(observedEntities = [LocalLensesSpecialtyDBView::class])
    fun getFilteredSpecialties(query: SimpleSQLiteQuery): List<LocalLensesSpecialtySimplified>

    @Transaction
    @RawQuery(observedEntities = [LocalLensesGroupDBView::class])
    fun getFilteredGroups(query: SimpleSQLiteQuery): List<LocalLensesGroupSimplified>

    @Transaction
    @Query(
        """
            SELECT * FROM $coloringsTable AS colorings
            WHERE colorings.lensId = :lensId
            ORDER BY colorings.priority
        """
    )
    suspend fun getColoringsForLens(lensId: String): List<LocalLensColoringWithExplanationsEntity>


    @Transaction
    @Query("SELECT * FROM $coloringsTable WHERE lensId = :lensId AND id = :coloringId")
    suspend fun getColoringById(
        lensId: String,
        coloringId: String,
    ): LocalLensColoringWithExplanationsEntity?

    @Transaction
    @Query(
        """
            SELECT * FROM $treatmentsTable AS treatments
            WHERE treatments.lensId = :lensId
            ORDER BY treatments.priority
        """
    )
    suspend fun getTreatmentsForLens(lensId: String): List<LocalLensTreatmentWithExplanationsEntity>

    @Transaction
    @Query("SELECT * FROM $treatmentsTable WHERE lensId = :lensId AND id = :treatmentId")
    suspend fun getTreatmentById(
        lensId: String,
        treatmentId: String,
    ): LocalLensTreatmentWithExplanationsEntity?

    @Transaction
    @RawQuery(observedEntities = [LocalLensFullUnionWithHeightAndLensTypeDBView::class])
    suspend fun lensTechsFilteredByDisponibility(
        query: SimpleSQLiteQuery,
    ): List<LocalLensesTechSimplified>

    @Transaction
    @RawQuery(observedEntities = [LocalLensFullUnionWithHeightAndLensTypeDBView::class])
    suspend fun lensMaterialsFilteredByDisponibility(
        query: SimpleSQLiteQuery,
    ): List<LocalLensesMaterialSimplified>
}
