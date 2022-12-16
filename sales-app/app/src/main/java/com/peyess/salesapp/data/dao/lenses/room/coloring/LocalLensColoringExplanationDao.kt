package com.peyess.salesapp.data.dao.lenses.room.coloring

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringEntity
import com.peyess.salesapp.data.model.lens.room.coloring.LocalLensColoringExplanationEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensCategoryEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensDescriptionEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensGroupEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSpecialtyEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensSupplierEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTechEntity
import com.peyess.salesapp.data.model.lens.room.dao.LocalLensTypeEntity

@Dao
interface LocalLensColoringExplanationDao {
    @Insert(onConflict = REPLACE)
    suspend fun add(entity: LocalLensColoringExplanationEntity)

    @Query("SELECT * FROM ${LocalLensColoringExplanationEntity.tableName} WHERE coloring_id = :id")
    suspend fun getAllForId(id: String): List<LocalLensColoringExplanationEntity>
}