package com.peyess.salesapp.data.repository.management_picture_upload

import arrow.core.Either
import com.peyess.salesapp.data.adapter.management_picture_upload.toPictureDocument
import com.peyess.salesapp.data.adapter.management_picture_upload.toPictureEntity
import com.peyess.salesapp.data.dao.management_picture_upload.PictureUploadDao
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.repository.management_picture_upload.error.PictureUploadFetchError
import com.peyess.salesapp.data.repository.management_picture_upload.error.PictureUploadUpdateError
import javax.inject.Inject

class PictureUploadRepositoryImpl @Inject constructor(
    private val uploadPictureDao: PictureUploadDao,
): PictureUploadRepository {
    override suspend fun pendingPictures(): PictureFetchListResponse = Either.catch {
        uploadPictureDao.allWithPendingDownload().map {
            it.toPictureDocument()
        }
    }.mapLeft {
        PictureUploadFetchError(
            description = "Error fetching pending pictures",
            error = it,
        )
    }

    override suspend fun updatePicture(
        picture: PictureUploadDocument,
    ): PictureUpdateResponse = Either.catch {
        val entity = picture.toPictureEntity()

        uploadPictureDao.update(entity)
    }.mapLeft {
        PictureUploadUpdateError(
            description = "Error updating picture",
            error = it,
        )
    }

    override suspend fun deletePicture(
        picture: PictureUploadDocument,
    ): PictureDeleteResponse = Either.catch {
        val entity = picture.toPictureEntity()

        uploadPictureDao.delete(entity)
    }.mapLeft {
        PictureUploadUpdateError(
            description = "Error deleting picture",
            error = it,
        )
    }

    override suspend fun addPicture(
        pictureUploadDocument: PictureUploadDocument,
    ): PictureAddResponse = Either.catch {
        val entity = pictureUploadDocument.toPictureEntity()

        uploadPictureDao.add(entity)
    }.mapLeft {
        PictureUploadUpdateError(
            description = "Error adding picture",
            error = it,
        )
    }
}
