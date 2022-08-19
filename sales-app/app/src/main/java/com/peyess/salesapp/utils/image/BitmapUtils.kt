package com.peyess.salesapp.utils.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.io.IOException

fun decodeAndRotateBitmapFrom(imageUri: Uri): Bitmap? {
    var bitmap: Bitmap? = null
    val matrix = Matrix()

    try {
        bitmap = BitmapFactory.decodeFile(imageUri.path)

        val ei: ExifInterface = ExifInterface(imageUri.path.toString())
        val orientation = ei.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )

        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                matrix.postRotate(90f)
                bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    false
                ) // rotateImage(imageBitmap, 90);
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                matrix.postRotate(180f)
                bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    false
                ) // rotateImage(imageBitmap, 180);
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                matrix.postRotate(270f)
                bitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    0,
                    bitmap.width,
                    bitmap.height,
                    matrix,
                    false
                ) // rotateImage(imageBitmap, 270);
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return bitmap
}
