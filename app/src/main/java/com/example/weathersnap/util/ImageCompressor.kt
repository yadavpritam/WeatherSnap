package com.example.weathersnap.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.File
import java.io.FileOutputStream

object ImageCompressor {

    fun compressImage(context: Context, imagePath: String): CompressionResult {
        val originalFile = File(imagePath)
        val originalSize = originalFile.length()

        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(imagePath, options)

        val maxWidth = 1024
        val maxHeight = 1024
        var actualWidth = options.outWidth
        var actualHeight = options.outHeight

        var imgRatio = actualWidth.toFloat() / actualHeight.toFloat()
        val maxRatio = maxWidth.toFloat() / maxHeight.toFloat()

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight.toFloat() / actualHeight.toFloat()
                actualWidth = (imgRatio * actualWidth).toInt()
                actualHeight = maxHeight
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth.toFloat() / actualWidth.toFloat()
                actualHeight = (imgRatio * actualHeight).toInt()
                actualWidth = maxWidth
            } else {
                actualHeight = maxHeight
                actualWidth = maxWidth
            }
        }

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight)
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeFile(imagePath, options)

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, actualWidth, actualHeight, true)
        
        val compressedFile = File(context.cacheDir, "compressed_${originalFile.name}")
        val out = FileOutputStream(compressedFile)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
        out.flush()
        out.close()

        return CompressionResult(
            originalPath = imagePath,
            compressedPath = compressedFile.absolutePath,
            originalSize = originalSize,
            compressedSize = compressedFile.length()
        )
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}

data class CompressionResult(
    val originalPath: String,
    val compressedPath: String,
    val originalSize: Long,
    val compressedSize: Long
)
