package com.peyess.salesapp.dao.products.room.filter_lens_supplier

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.dao.products.room.filter_lens_material.FilterLensMaterialEntity
import com.peyess.salesapp.dao.products.room.join_lens_material.JoinLensMaterialEntity
import com.peyess.salesapp.dao.products.room.local_lens.LocalLensEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterLensMaterialDao {

    @Insert(onConflict = REPLACE)
    fun add(filter: FilterLensMaterialEntity)

    @Query("SELECT * FROM ${FilterLensMaterialEntity.tableName} WHERE supplier_id = :supplierId")
    fun getAllWithSupplier(supplierId: String): Flow<List<FilterLensMaterialEntity>>

    @Query(
        "SELECT Materials.id, Materials.supplier_id, Materials.name " +
                "FROM ${FilterLensMaterialEntity.tableName} AS Materials " +
                "JOIN ${LocalLensEntity.tableName} AS Lenses " +
                "ON Materials.id = Lenses.material_id " +
                "WHERE Lenses.supplier_id = :supplierId " +
                "AND Lenses.brand_id = :brandId " +
                "AND Lenses.design_id = :designId " +
                "GROUP BY Materials.id " +
                "ORDER BY priority"
    )
    fun materialsForLens(
            supplierId: String,
            brandId: String,
            designId: String,
        ): Flow<List<FilterLensMaterialEntity>>
}