package com.peyess.salesapp.dao.products.room.local_lens

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalLensDao {
    @Insert
    fun add(lensEntity: LocalLensEntity)

    @RawQuery(observedEntities = [LocalLensEntity::class])
    fun getFilteredLenses(query: SimpleSQLiteQuery): PagingSource<Int, LocalLensEntity>

    @Query("SELECT * FROM ${LocalLensEntity.tableName} WHERE id = :lensId")
    fun getById(lensId: String): Flow<LocalLensEntity?>

    @Query(
        "SELECT * FROM ${LocalLensEntity.tableName}" +
                " WHERE group_id = :groupId " +
                " ORDER BY priority"
    )
    fun getAllByGroupId(groupId: String): List<LocalLensEntity>

    @Query(
        "SELECT * FROM ${LocalLensEntity.tableName} " +
                "WHERE supplier_id = :supplierId " +
                "AND brand_id = :brandId " +
                "AND design_id = :designId " +
                "AND tech_id = :techId " +
                "AND material_id = :materialId " +
                "ORDER BY brand, design ASC " +
                "LIMIT 1"
    )
    fun searchForLens(
        supplierId: String,
        brandId: String,
        designId: String,
        techId: String,
        materialId: String,
    ): Flow<LocalLensEntity?>
}