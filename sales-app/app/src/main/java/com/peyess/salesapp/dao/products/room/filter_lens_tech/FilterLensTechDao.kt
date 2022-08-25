package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.filter_lens_tech.FilterLensTechEntity
import com.peyess.salesapp.dao.products.room.join_lens_tech.JoinLensTechEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterLensTechDao {

    @Insert(onConflict = REPLACE)
    fun add(filter: FilterLensTechEntity)

    @Query(
        "SELECT Techs.id, Techs.name FROM ${FilterLensTechEntity.tableName} AS Techs " +
                "JOIN ${LocalLensEntity.tableName} AS Lenses " +
                "ON Techs.id = Lenses.tech_id " +
                "WHERE Lenses.supplier_id = :supplierId " +
                "AND Lenses.brand_id = :brandId " +
                "AND Lenses.design_id = :designId " +
                "GROUP BY Techs.id " +
                "ORDER BY priority"
    )
    fun techsForLens(
        supplierId: String,
        brandId: String,
        designId: String,
    ): Flow<List<FilterLensTechEntity>>
}