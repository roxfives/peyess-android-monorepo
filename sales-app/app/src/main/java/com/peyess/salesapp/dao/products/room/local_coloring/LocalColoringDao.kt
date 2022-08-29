package com.peyess.salesapp.dao.products.room.local_coloring

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.join_lens_coloring.JoinLensColoringEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalColoringDao {
    @Insert(onConflict = REPLACE)
    fun add(coloringEntity: LocalColoringEntity)

    @Query(
        "SELECT * FROM ${LocalColoringEntity.tableName} AS Colorings " +
                "JOIN ${JoinLensColoringEntity.tableName} AS JoinTable " +
                "ON Colorings.id = JoinTable.coloring_id " +
                "WHERE lens_id = :lensId " +
                "GROUP BY Colorings.id " +
                "ORDER BY Colorings.priority"

    )
    fun coloringsForLens(lensId: String): Flow<List<LocalColoringEntity>>

    @Query("SELECT * FROM ${LocalColoringEntity.tableName} WHERE id = :coloringId")
    fun getById(coloringId: String): Flow<LocalColoringEntity?>
}