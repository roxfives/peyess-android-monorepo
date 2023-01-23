package com.peyess.salesapp.utils.cloud.storage

import android.net.Uri
import arrow.core.Either
import com.google.firebase.storage.FirebaseStorage
import com.peyess.salesapp.utils.cloud.storage.error.StorageUploadError
import com.peyess.salesapp.utils.cloud.storage.error.uploadErrorAdapter
import kotlinx.coroutines.tasks.await

private const val storagePathDelimiter = "/"

private fun appendStoragePaths(paths: List<String>): String {
    return paths.joinToString(separator = storagePathDelimiter)
}

suspend fun uploadPicture(
    storage: FirebaseStorage,
    storagePath: String,
    filename: String,
    picture: Uri,
    onProgressUpdate: (Double) -> Unit,
): Either<StorageUploadError, Uri> = Either.catch {
    val storageRef = storage.reference

    val picturePath = appendStoragePaths(listOf(storagePath, filename))
    val pictureRef = storageRef.child(picturePath)

    val uploadTask = pictureRef.putFile(picture)
    uploadTask.addOnProgressListener {
        val progress = (it.bytesTransferred / it.totalByteCount).toDouble()

        onProgressUpdate(progress)
    }

    val taskSnapshot = uploadTask.await()
    val task = taskSnapshot.task

    if (task.isSuccessful) {
        pictureRef.downloadUrl.await()
    } else {
        error(task.exception ?: Throwable("Unknown error"))
    }
}.mapLeft {
    uploadErrorAdapter(it)
}
