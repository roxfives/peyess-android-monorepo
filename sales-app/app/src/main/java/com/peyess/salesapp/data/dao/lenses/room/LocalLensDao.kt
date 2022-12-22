package com.peyess.salesapp.data.dao.lenses.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringEntity
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringExplanationEntity
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
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensWithDetails
import com.peyess.salesapp.data.model.lens.room.dao.database_view.LocalLensWithDetailsDBView
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentEntity
import com.peyess.salesapp.data.model.lens.room.treatment.LocalLensTreatmentExplanationEntity

private const val coloringsTable = LocalLensColoringEntity.tableName
private const val lensColoringJunction = LocalLensColoringCrossRef.tableName

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

    @Transaction
    @Query("SELECT * FROM ${LocalLensWithDetailsDBView.viewName}")
    suspend fun getAllLenses(): List<LocalLensWithDetails>

    @Query(
        """
            SELECT *
            FROM $coloringsTable AS colorings
            JOIN $lensColoringJunction AS _junction ON colorings.id = _junction.coloring_id
            WHERE _junction.lens_id = :lensId
        """
    )
    suspend fun getColoringsForLens(lensId: String): List<LocalLensColoringWithExplanationsEntity>
}
