package com.peyess.salesapp.data.dao.products_table_state

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductsTableStateDao {

    @Insert(onConflict = REPLACE)
    fun update(productState: ProductTableStateEntity)

    @Query("""
        UPDATE ${ProductTableStateEntity.tableName}
        SET is_updating = :isUpdating
        WHERE id = :id
    """)
    fun updateStatus(id: Int, isUpdating: Int)

    @Query("""
        UPDATE ${ProductTableStateEntity.tableName}
        SET has_updated = :hasUpdating
        WHERE id = :id
    """)
    fun updateHasUpdated(id: Int, hasUpdating: Int)

    @Query("""
        UPDATE ${ProductTableStateEntity.tableName}
        SET has_updated_failed = :hasUpdateFailed
        WHERE id = :id
    """)
    fun updateHasUpdateFailed(id: Int, hasUpdateFailed: Int)

    @Query("SELECT * FROM ${ProductTableStateEntity.tableName} WHERE id = :id")
    fun observeProductTableState(id: Int): Flow<ProductTableStateEntity?>

    @Query("SELECT * FROM ${ProductTableStateEntity.tableName} WHERE id = :id")
    suspend fun getProductTableState(id: Int): ProductTableStateEntity?

    @Query("SELECT * FROM ${ProductTableStateEntity.tableName}")
    fun getAll(): List<ProductTableStateEntity>
}