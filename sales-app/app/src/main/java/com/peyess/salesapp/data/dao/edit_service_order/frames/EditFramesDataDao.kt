package com.peyess.salesapp.data.dao.edit_service_order.frames

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import com.peyess.salesapp.data.model.edit_service_order.frames.EditFramesDataEntity
import com.peyess.salesapp.typing.frames.FramesType
import kotlinx.coroutines.flow.Flow

@Dao
interface EditFramesDataDao {
    @Insert
    suspend fun addFrame(frame: EditFramesDataEntity)

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
        WHERE so_id = :so_id
    """)
    suspend fun updateIsNew(so_id: String, isNew: Boolean)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET description = :description
        WHERE so_id = :so_id
    """)
    suspend fun updateDescription(so_id: String, description: String)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET reference = :reference
        WHERE so_id = :so_id
    """)
    suspend fun updateReference(so_id: String, reference: String)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET value = :value
        WHERE so_id = :so_id
    """)
    suspend fun updateValue(so_id: String, value: Double)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET tag_code = :tagCode
        WHERE so_id = :so_id
    """)
    suspend fun updateTagCode(so_id: String, tagCode: String)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET type = :type
        WHERE so_id = :so_id
    """)
    suspend fun updateType(so_id: String, type: FramesType)

    @Query("""
        UPDATE ${EditFramesDataEntity.tableName}
        SET info = :info
        WHERE so_id = :so_id
    """)
    suspend fun updateInfo(so_id: String, info: String)
}