package com.peyess.salesapp.data.repository.management_picture_upload

import arrow.core.Either
import com.peyess.salesapp.data.model.management_picture_upload.PictureUploadDocument
import com.peyess.salesapp.data.repository.management_picture_upload.error.PictureUploadReadError
import com.peyess.salesapp.data.repository.management_picture_upload.error.PictureUploadWriteError

typealias PictureUpdateResponse = Either<PictureUploadWriteError, Unit>
typealias PictureDeleteResponse = Either<PictureUploadWriteError, Unit>
typealias PictureAddResponse = Either<PictureUploadWriteError, Unit>
typealias PictureSingleResponse = Either<PictureUploadReadError, PictureUploadDocument>
typealias PictureFetchListResponse =
        Either<PictureUploadReadError, List<PictureUploadDocument>>

interface PictureUploadRepository {
    suspend fun pendingPictures(): PictureFetchListResponse

    suspend fun pictureById(id: Long): PictureSingleResponse

    suspend fun updatePicture(picture: PictureUploadDocument): PictureUpdateResponse

    suspend fun deletePicture(picture: PictureUploadDocument): PictureDeleteResponse

    suspend fun addPicture(pictureUploadDocument: PictureUploadDocument): PictureAddResponse
}
