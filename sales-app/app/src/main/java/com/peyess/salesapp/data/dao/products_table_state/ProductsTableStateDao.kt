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

    @Query("SELECT * FROM ${ProductTableStateEntity.tableName} WHERE id = :id")
    fun observeProductTableState(id: Int): Flow<ProductTableStateEntity?>

    @Query("SELECT * FROM ${ProductTableStateEntity.tableName} WHERE id = :id")
    suspend fun getProductTableState(id: Int): ProductTableStateEntity?

    @Query("SELECT * FROM ${ProductTableStateEntity.tableName} WHERE ${true} = ${true}")
    fun getAll(): List<ProductTableStateEntity>
}