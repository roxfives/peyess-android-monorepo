package com.peyess.salesapp.data.dao.edit_service_order.payment_discount

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.payment_discount.EditOverallDiscountEntity
import com.peyess.salesapp.typing.products.DiscountCalcMethod
import kotlinx.coroutines.flow.Flow

@Dao
interface EditOverallDiscountDao {
    @Insert(onConflict = REPLACE)
    suspend fun addPaymentDiscount(paymentDiscount: EditOverallDiscountEntity)

    @Query("""
        SELECT * FROM ${EditOverallDiscountEntity.tableName}
        WHERE sale_id = :saleId
    """)
    suspend fun discountForSale(saleId: String): EditOverallDiscountEntity?

    @Query("""
        SELECT * FROM ${EditOverallDiscountEntity.tableName}
        WHERE sale_id = :saleId
    """)
    fun streamDiscountForSale(saleId: String): Flow<EditOverallDiscountEntity?>

    @Query("""
        UPDATE ${EditOverallDiscountEntity.tableName}
        SET method = :method
        WHERE sale_id = :saleId
    """)
    suspend fun updateMethod(saleId: String, method: DiscountCalcMethod)

    @Query("""
        UPDATE ${EditOverallDiscountEntity.tableName}
        SET value = :value
        WHERE sale_id = :saleId
    """)
    suspend fun updateValue(saleId: String, value: Double)
}
