package com.peyess.salesapp.data.dao.management_picture_upload

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadEntity

@Dao
interface PictureUploadDao {
    @Insert(onConflict = REPLACE)
    suspend fun add(pictureUpload: PictureUploadEntity)

    @Update
    suspend fun update(pictureUpload: PictureUploadEntity)

    @Delete
    suspend fun delete(pictureUpload: PictureUploadEntity)

    @Query("""
        SELECT * FROM ${PictureUploadEntity.tableName}
        WHERE has_been_uploaded = 0
    """)
    suspend fun allWithPendingDownload(): List<PictureUploadEntity>

    @Query("""
        SELECT * FROM ${PictureUploadEntity.tableName}
        WHERE has_been_deleted = 1
    """)
    suspend fun allAlreadyDeleted(): List<PictureUploadEntity>

    @Query("""
        SELECT COUNT(id) FROM ${PictureUploadEntity.tableName}
        WHERE has_been_uploaded = 0
    """)
    suspend fun totalPending(): Int
}