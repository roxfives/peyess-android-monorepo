package com.peyess.salesapp.data.dao.edit_service_order.frames

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.frames.EditFramesDataEntity
import com.peyess.salesapp.typing.frames.FramesType
import kotlinx.coroutines.flow.Flow

@Dao
interface EditFramesDataDao {
    @Insert
    suspend fun addFrames(frame: EditFramesDataEntity)

    @Query("""
        SELECT * FROM ${EditFramesDataEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    suspend fun findFramesForServiceOrder(serviceOrderId: String): EditFramesDataEntity?

    @Query("""
        SELECT * FROM ${EditFramesDataEntity.tableName}
        WHERE so_id = :serviceOrderId
    """)
    fun streamFramesForServiceOrder(serviceOrderId: String): Flow<EditFramesDataEntity?>

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET is_new = :isNew
        WHERE so_id = :soId
    """)
    suspend fun updateIsNew(soId: String, isNew: Boolean)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET description = :description
        WHERE so_id = :soId
    """)
    suspend fun updateDescription(soId: String, description: String)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET reference = :reference
        WHERE so_id = :soId
    """)
    suspend fun updateReference(soId: String, reference: String)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET value = :value
        WHERE so_id = :soId
    """)
    suspend fun updateValue(soId: String, value: Double)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET tag_code = :tagCode
        WHERE so_id = :soId
    """)
    suspend fun updateTagCode(soId: String, tagCode: String)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET type = :type
        WHERE so_id = :soId
    """)
    suspend fun updateType(soId: String, type: FramesType)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET info = :info
        WHERE so_id = :soId
    """)
    suspend fun updateInfo(soId: String, info: String)
}